package org.bees.optimizer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TrafficDto;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ModelConverterTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void moneyConverter() throws IOException {
        String json = "{ \"points\":[{\"p\":0,\"money\":0},{\"p\":1,\"money\":0},{\"p\":3,\"money\":53708},{\"p\":2,\"money\":25482},{\"p\":4,\"money\":27185}] }";
        long[] expected = new long[] {0, 0, 25482, 53708, 27185};

        long[] money = ModelConverter.convertPoints(mapper.readValue(json, PointsDto.class));

        Assert.assertArrayEquals(expected, money);
    }

    @Test
    public void routesConverter() throws IOException {
        String json = "{ \"routes\":[{\"a\":0,\"b\":1,\"time\":15},{\"a\":0,\"b\":2,\"time\":17},{\"a\":0,\"b\":3,\"time\":3},{\"a\":0,\"b\":4,\"time\":1},{\"a\":2,\"b\":1,\"time\":24},{\"a\":1,\"b\":3,\"time\":72},{\"a\":2,\"b\":3,\"time\":9},{\"a\":4,\"b\":3,\"time\":14},{\"a\":1,\"b\":4,\"time\":11},{\"a\":2,\"b\":4,\"time\":33}] }";
        int[][] expected = new int[][] {
                {-1, 15, 17, 3, 1},
                {-1, -1, 24, 72, 11},
                {-1, 24, -1, 9, 33},
                {-1, 72, 9, -1, 14},
                {-1, 11, 33, 14, -1}
        };

        int[][] routes = ModelConverter.convertRoutes(mapper.readValue(json, RoutesDto.class));

        Assert.assertArrayEquals(expected, routes);
    }

    @Test
    public void trafficConverter() throws IOException {
        String json = "{ \"traffic\":[{\"a\":0,\"b\":1,\"jam\":1.54},{\"a\":0,\"b\":2,\"jam\":1.02},{\"a\":0,\"b\":3,\"jam\":2.0},{\"a\":0,\"b\":4,\"jam\":1},{\"a\":2,\"b\":1,\"jam\":24},{\"a\":1,\"b\":3,\"jam\":72},{\"a\":2,\"b\":3,\"jam\":9},{\"a\":4,\"b\":3,\"jam\":14},{\"a\":1,\"b\":4,\"jam\":11},{\"a\":2,\"b\":4,\"jam\":33}] }";
        double[][] expected = new double[][] {
                {1, 1.54, 1.02, 2.0, 1},
                {1.54, 1, 24, 72, 11},
                {1.02, 24, 1, 9, 33},
                {2.0, 72, 9, 1, 14},
                {1, 11, 33, 14, 1}
        };

        double[][] jams = ModelConverter.convertTraffic(mapper.readValue(json, TrafficDto.class));

        Assert.assertArrayEquals(expected, jams);
    }

}