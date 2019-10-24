package org.bees.optimizer.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.knapsack.SackPoint;
import org.bees.optimizer.knapsack.TimeKnapsack;
import org.bees.optimizer.knapsack.TimeKnapsack.Find;
import org.bees.optimizer.model.external.*;
import org.bees.optimizer.knapsack.Cacher;
import org.bees.optimizer.knapsack.PointExtractor;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

import static org.bees.optimizer.server.DataProvider.*;

@Slf4j
public class TestingServer {
    private final static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testServer() throws IOException {
        start();
    }

    @Test
    public void go() throws IOException {
        ArriveDto arriveDto = mapper.readValue(getCarsum(), ArriveDto.class);
        System.out.println(arriveDto);
    }

    @Test
    public void go_first() throws IOException {
        OverallSum overallSum = mapper.readValue(getTeamSum(), OverallSum.class);
        System.out.println(overallSum);
    }

    private void start() throws IOException {

    }

}