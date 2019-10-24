package org.bees.optimizer.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.ArriveDto;
import org.bees.optimizer.model.external.GotoDto;
import org.bees.optimizer.model.external.LoginDto;
import org.bees.optimizer.model.external.OverallSum;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TrafficDto;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

@Slf4j
@Service
@ClientEndpoint
public class WebSocketHanler implements InitializingBean, ApplicationContextAware {

    private MessageDispatcher messageDispatcher;

    @Autowired
    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    private Session userSession = null;

    private ApplicationContext context;
    private final ObjectMapper mapper = new ObjectMapper();

    private void startConnection(String endpoint) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(endpoint));
            Thread.sleep(500);
            registerOnServer("The Bees");
        } catch (Exception e) {
            log.error("Got some error while trying to connect: ", e);
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("Connected to server with session: {}", session);
        this.userSession = session;
        this.userSession.setMaxTextMessageBufferSize(100000);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.warn("Session closed: {}", reason);
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        String[] split = message.split("\n");
        for (String line : split) {
            messageDispatcher.dispatchMessage(line);
        }
    }

    private void registerOnServer(String teamName) throws IOException {
        LoginDto loginDto = new LoginDto(teamName);

        this.userSession.getAsyncRemote().sendText(mapper.writeValueAsString(loginDto));
    }

    public void sendCar(GotoDto gotoDto) throws IOException {
        this.userSession.getAsyncRemote().sendText(mapper.writeValueAsString(gotoDto));
    }

    @Override
    public void afterPropertiesSet() {
        Environment env = this.context.getEnvironment();
        String endpoint = env.getProperty("server.socket.endpoint", String.class, "ws://localhost:8080/race");
        this.startConnection(endpoint);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
