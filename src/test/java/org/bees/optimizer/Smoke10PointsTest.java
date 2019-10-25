package org.bees.optimizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.ArriveDto;
import org.bees.optimizer.model.external.GotoDto;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TokenDto;
import org.bees.optimizer.model.external.TrafficDto;
import org.bees.optimizer.service.Solver;
import org.bees.optimizer.service.WebSocketHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmokeTestConfig.class)
public class Smoke10PointsTest {

    @Autowired
    private Solver solver;

    @Autowired
    private WebSocketHandler mockWebSocketHandler;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void smoke() throws IOException, InterruptedException {
        String pointsJson = "{\"points\":[{\"money\":0,\"index\":0},{\"money\":0,\"index\":1},{\"money\":25482,\"index\":2},{\"money\":53708,\"index\":3},{\"money\":27185,\"index\":4},{\"money\":56552,\"index\":5},{\"money\":64320,\"index\":6},{\"money\":30363,\"index\":7},{\"money\":80370,\"index\":8},{\"money\":90231,\"index\":9}]}";
        String routesJson = "{\"routes\":[{\"time\":15,\"from\":0,\"to\":1},{\"time\":9,\"from\":0,\"to\":3},{\"time\":12,\"from\":0,\"to\":5},{\"time\":7,\"from\":0,\"to\":6},{\"time\":7,\"from\":0,\"to\":4},{\"time\":13,\"from\":0,\"to\":8},{\"time\":13,\"from\":0,\"to\":9},{\"time\":10,\"from\":0,\"to\":2},{\"time\":7,\"from\":0,\"to\":7},{\"time\":10,\"from\":1,\"to\":2},{\"time\":9,\"from\":1,\"to\":3},{\"time\":12,\"from\":1,\"to\":5},{\"time\":13,\"from\":1,\"to\":6},{\"time\":13,\"from\":1,\"to\":7},{\"time\":8,\"from\":1,\"to\":4},{\"time\":12,\"from\":1,\"to\":8},{\"time\":12,\"from\":1,\"to\":9},{\"time\":6,\"from\":2,\"to\":5},{\"time\":4,\"from\":2,\"to\":4},{\"time\":3,\"from\":2,\"to\":3},{\"time\":15,\"from\":2,\"to\":6},{\"time\":7,\"from\":2,\"to\":9},{\"time\":7,\"from\":2,\"to\":7},{\"time\":8,\"from\":2,\"to\":8},{\"time\":8,\"from\":3,\"to\":4},{\"time\":7,\"from\":3,\"to\":5},{\"time\":14,\"from\":3,\"to\":6},{\"time\":8,\"from\":3,\"to\":7},{\"time\":6,\"from\":3,\"to\":9},{\"time\":10,\"from\":3,\"to\":8},{\"time\":7,\"from\":4,\"to\":7},{\"time\":14,\"from\":4,\"to\":6},{\"time\":8,\"from\":4,\"to\":8},{\"time\":5,\"from\":4,\"to\":5},{\"time\":6,\"from\":4,\"to\":9},{\"time\":12,\"from\":5,\"to\":6},{\"time\":7,\"from\":5,\"to\":7},{\"time\":6,\"from\":5,\"to\":8},{\"time\":6,\"from\":5,\"to\":9},{\"time\":6,\"from\":6,\"to\":7},{\"time\":12,\"from\":6,\"to\":9},{\"time\":12,\"from\":6,\"to\":8},{\"time\":11,\"from\":7,\"to\":9},{\"time\":13,\"from\":7,\"to\":8},{\"time\":7,\"from\":8,\"to\":9}]}";
        String trafficJson = "{\"traffic\":[{\"jam\":1.46,\"from\":0,\"to\":1},{\"jam\":1.34,\"from\":0,\"to\":3},{\"jam\":1.45,\"from\":0,\"to\":5},{\"jam\":1.24,\"from\":0,\"to\":6},{\"jam\":1.33,\"from\":0,\"to\":4},{\"jam\":1.99,\"from\":0,\"to\":8},{\"jam\":1.2,\"from\":0,\"to\":9},{\"jam\":1.64,\"from\":0,\"to\":2},{\"jam\":1.67,\"from\":0,\"to\":7},{\"jam\":1.37,\"from\":1,\"to\":2},{\"jam\":1.99,\"from\":1,\"to\":3},{\"jam\":1.29,\"from\":1,\"to\":5},{\"jam\":1.73,\"from\":1,\"to\":6},{\"jam\":1.53,\"from\":1,\"to\":7},{\"jam\":1.23,\"from\":1,\"to\":4},{\"jam\":1.26,\"from\":1,\"to\":8},{\"jam\":1.6,\"from\":1,\"to\":9},{\"jam\":1.29,\"from\":2,\"to\":5},{\"jam\":1.95,\"from\":2,\"to\":4},{\"jam\":1.93,\"from\":2,\"to\":3},{\"jam\":1.65,\"from\":2,\"to\":6},{\"jam\":1.48,\"from\":2,\"to\":9},{\"jam\":1.47,\"from\":2,\"to\":7},{\"jam\":1.7,\"from\":2,\"to\":8},{\"jam\":1.91,\"from\":3,\"to\":4},{\"jam\":1.48,\"from\":3,\"to\":5},{\"jam\":1.95,\"from\":3,\"to\":6},{\"jam\":1.48,\"from\":3,\"to\":7},{\"jam\":1.13,\"from\":3,\"to\":9},{\"jam\":1.03,\"from\":3,\"to\":8},{\"jam\":1.33,\"from\":4,\"to\":7},{\"jam\":1.08,\"from\":4,\"to\":6},{\"jam\":1.91,\"from\":4,\"to\":8},{\"jam\":1.33,\"from\":4,\"to\":5},{\"jam\":1.25,\"from\":4,\"to\":9},{\"jam\":1.22,\"from\":5,\"to\":6},{\"jam\":1.94,\"from\":5,\"to\":7},{\"jam\":1.96,\"from\":5,\"to\":8},{\"jam\":1.71,\"from\":5,\"to\":9},{\"jam\":1.24,\"from\":6,\"to\":7},{\"jam\":1.15,\"from\":6,\"to\":9},{\"jam\":2.0,\"from\":6,\"to\":8},{\"jam\":1.94,\"from\":7,\"to\":9},{\"jam\":1.35,\"from\":7,\"to\":8},{\"jam\":1.26,\"from\":8,\"to\":9}]}";
        String tokenJson = "{ \"token\" : \"dd76b4f8191893288054f74385a07e5f\", \"cars\": [\"sb0\"], \"level\": 1 }";

        List<Long> collectedMoney = new ArrayList<>();

        RoutesDto routes = mapper.readValue(routesJson, RoutesDto.class);
        TrafficDto traffic = mapper.readValue(trafficJson, TrafficDto.class);
        PointsDto points = mapper.readValue(pointsJson, PointsDto.class);
        TokenDto token = mapper.readValue(tokenJson, TokenDto.class);

        AtomicInteger tickCount = new AtomicInteger(5);

        Mockito.doAnswer(invocation -> {
            GotoDto gotoDto = invocation.getArgument(0);

            collectedMoney.add(points.getPoints()
                                     .stream()
                                     .filter(r -> r.getIndex() == gotoDto.getPoint())
                                     .findAny()
                                     .get()
                                     .getMoney()
            );

            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // do nothing
                }

                solver.processArrive(new ArriveDto(
                        gotoDto.getPoint(),
                        "sb0",
                        collectedMoney.stream().reduce(0L, (a, b) -> a + b),
                        0.0
                ));
                if (tickCount.decrementAndGet() != 0L) {
                    solver.processTraffic(traffic);
                }
            }).start();

            return null;
        }).when(mockWebSocketHandler).sendCar(Mockito.any());


        solver.processToken(token);
        solver.processRoutes(routes);
        solver.processPoints(points);
        solver.processTraffic(traffic);

        Thread.sleep(20000);
        log.info("Money: {}", collectedMoney.stream().reduce(0L, (a, b) -> a + b));
    }

}
