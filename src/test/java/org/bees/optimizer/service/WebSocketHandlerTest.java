package org.bees.optimizer.service;

import org.bees.optimizer.RouteOptimizerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = RouteOptimizerConfig.class,
        properties = {
                "server.socket.endpoint=ws://localhost:8080/race",
                "spring.profiles.active=local"
        }
)
public class WebSocketHandlerTest {

    @Test
    public void testExchange() throws InterruptedException {
        Thread.sleep(30000);
    }

}