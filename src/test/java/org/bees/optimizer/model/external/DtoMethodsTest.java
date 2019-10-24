package org.bees.optimizer.model.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DtoMethodsTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getDestinationsFromRoutesTest() throws IOException {
        String json = "{ \"routes\":[{\"a\":0,\"b\":1,\"time\":15},{\"a\":0,\"b\":2,\"time\":17},{\"a\":0,\"b\":3,\"time\":3},{\"a\":0,\"b\":4,\"time\":1},{\"a\":2,\"b\":1,\"time\":24},{\"a\":1,\"b\":3,\"time\":72},{\"a\":2,\"b\":3,\"time\":9},{\"a\":4,\"b\":3,\"time\":14},{\"a\":1,\"b\":4,\"time\":11},{\"a\":2,\"b\":4,\"time\":33}] }";

        RoutesDto routesDto = mapper.readValue(json, RoutesDto.class);
        List<RoutesDto.DestinationDto> destinationsFrom0 = routesDto.getDestinationsFrom(0);

        List<RoutesDto.DestinationDto> expected = new ArrayList<>();
        expected.add(new RoutesDto.DestinationDto(1, 15));
        expected.add(new RoutesDto.DestinationDto(2, 17));
        expected.add(new RoutesDto.DestinationDto(3, 3));
        expected.add(new RoutesDto.DestinationDto(4, 1));

        Assert.assertEquals(4, destinationsFrom0.size());

        for (RoutesDto.DestinationDto dto : expected) {
            Assert.assertTrue(destinationsFrom0.contains(dto));
        }
    }

}
