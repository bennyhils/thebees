package org.bees.optimizer;

import org.bees.optimizer.service.WebSocketHandler;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RouteOptimizerConfig.class)
public class SmokeTestConfig {
    @MockBean
    public WebSocketHandler webSocketHandler;
}
