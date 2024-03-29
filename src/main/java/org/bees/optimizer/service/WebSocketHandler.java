package org.bees.optimizer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.EndDto;
import org.bees.optimizer.model.external.GotoDto;
import org.bees.optimizer.model.external.LoginDto;
import org.bees.optimizer.model.external.ReconnectDto;
import org.bees.optimizer.model.external.TokenDto;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
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
public class WebSocketHandler implements
        InitializingBean,
        ApplicationContextAware,
        ApplicationListener<ApplicationReadyEvent>
{

    private MessageDispatcher messageDispatcher;
    private static final int RECONNECT_COUNT = 3;
    @Value("${org.bees.teamname:The Bees}")
    private String teamName;

    @Autowired
    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    private String endpoint;
    private Session userSession = null;
    private String token;

    private ApplicationContext context;
    private final ObjectMapper mapper = new ObjectMapper();

    private void startConnection() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(endpoint));
            Thread.sleep(500);
            registerOnServer(teamName);
        } catch (Exception e) {
            log.error("Got some error while trying to connect: ", e);
        }
    }

    private void tryReconnect() throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, new URI(endpoint));
        Thread.sleep(500);
        registerReconnect(token);
    }

    private void startPolling() {
        while (true) {
            try {
                Thread.sleep(2000);
                log.debug("I'm ok");
            } catch (InterruptedException e) {
                log.error("Interrupted by: ", e);
                break;
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("Connected to server with session: {}", session);
        this.userSession = session;
        this.userSession.setMaxTextMessageBufferSize(1000000);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.warn("Session closed: {}", reason);
        this.userSession = null;

        log.warn("Trying to reconnect...");
        for (int i = 0; i < RECONNECT_COUNT; ++i) {
            try {
                tryReconnect();
            } catch (Exception e) {
                log.warn("Reconnect {} failed: ", i, e);
            }
        }
    }

    @OnMessage
    public void onMessage(String message) {
        String[] split = message.split("\n");
        for (String line : split) {
            messageDispatcher.dispatchMessage(line);
        }
    }

    private void registerOnServer(String teamName) throws IOException {
        log.info("Registering as {}", teamName);
        LoginDto loginDto = new LoginDto(teamName);

        this.userSession.getAsyncRemote().sendText(mapper.writeValueAsString(loginDto));
        log.info("send login DTO to server {}", loginDto);
    }

    private void registerReconnect(String token) throws JsonProcessingException {
        ReconnectDto reconnectDto = new ReconnectDto(token);

        this.userSession.getAsyncRemote().sendText(mapper.writeValueAsString(reconnectDto));
    }

    @SneakyThrows
    public void sendCar(GotoDto gotoDto) {
        log.info("Car sent: {}", gotoDto);
        this.userSession.getAsyncRemote().sendText(mapper.writeValueAsString(gotoDto));
    }

    @Override
    public void afterPropertiesSet() {
        Environment env = this.context.getEnvironment();
        this.endpoint = env.getProperty("server.socket.endpoint", String.class, "ws://localhost:8080/race");
        log.info("Got endpoint value: {}", this.endpoint);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public void saveToken(TokenDto tokenDto) {
        this.token = tokenDto.getToken();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.startConnection();
        new Thread(() -> this.startPolling()).start();
    }

    public void processEnd(EndDto endDto) {
        if (endDto.isEnd()) {
            log.info("Session closed by server.");
            Thread curThread = Thread.currentThread();
            Thread.getAllStackTraces().forEach((t, s) -> {if (!curThread.equals(t)) t.interrupt();});
            this.userSession = null;
        }
    }
}
