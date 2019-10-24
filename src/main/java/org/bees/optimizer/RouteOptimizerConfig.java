package org.bees.optimizer;

import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.ArriveDto;
import org.bees.optimizer.model.external.OverallSum;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TokenDto;
import org.bees.optimizer.model.external.TrafficDto;
import org.bees.optimizer.service.Solver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RouteOptimizerConfig {

    @Bean
    public Solver solver() {
        return new Solver() {

            @Override
            public void processPoints(PointsDto pointsDto) { }

            @Override
            public void processTraffic(TrafficDto trafficDto) { }

            @Override
            public void processRoutes(RoutesDto routesDto) { }

            @Override
            public void processArrive(ArriveDto arriveDto) { }

            @Override
            public void processOverallSum(OverallSum overallSum) { }

            @Override
            public void processToken(TokenDto tokenDto) { }
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(RouteOptimizerConfig.class, args);
    }
}
