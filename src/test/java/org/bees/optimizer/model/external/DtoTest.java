package org.bees.optimizer.model.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DtoTest {

    private final ObjectMapper mapper = new ObjectMapper();

    //--------------
    // Input models
    //--------------

    @Test
    public void validateTokenJson() throws IOException {
        String json = "{ \"token\" : \"12321\", \"cars\": [\"sb1\", \"sb2\"], \"level\": 1 }";

        TokenDto token = mapper.readValue(json, TokenDto.class);
        log.info("TokenDto:\n{}", token);
        Assert.assertEquals("12321", token.getToken());
        Assert.assertEquals(Arrays.asList("sb1", "sb2"), token.getCars());
        Assert.assertEquals(1, token.getLevel());
    }

    @Test
    public void validateRoutesJson() throws IOException {
        String json = "{ \"routes\": [\n" +
                      "{\"a\":1 , \"b\":7 , \"time\": 31 },\n" +
                      "{\"a\":6 , \"b\":30, \"time\": 1 },\n" +
                      "{\"a\":10, \"b\":17, \"time\": 12 }\n" +
                      "]}";

        RoutesDto routes = mapper.readValue(json, RoutesDto.class);
        log.info("RoutesDto:\n{}", routes);

        List<RouteDto> routesList = routes.getRoutes();
        Assert.assertEquals(3, routesList.size());

        RouteDto routeDto0 = routesList.get(0);
        Assert.assertEquals(1, routeDto0.getFrom());
        Assert.assertEquals(7, routeDto0.getTo());
        Assert.assertEquals(31, routeDto0.getTime());

        RouteDto routeDto1 = routesList.get(1);
        Assert.assertEquals(6, routeDto1.getFrom());
        Assert.assertEquals(30, routeDto1.getTo());
        Assert.assertEquals(1, routeDto1.getTime());

        RouteDto routeDto2 = routesList.get(2);
        Assert.assertEquals(10, routeDto2.getFrom());
        Assert.assertEquals(17, routeDto2.getTo());
        Assert.assertEquals(12, routeDto2.getTime());

    }

    @Test
    public void validateTrafficJson() throws IOException {
        String json = "{ \"traffic\": [\n" +
                      "{\"a\":1 , \"b\":7 , \"jam\": 1.0 },\n" +
                      "{\"a\":6 , \"b\":30, \"jam\": 1.5 },\n" +
                      "{\"a\":10, \"b\":17, \"jam\": 1.9 }\n" +
                      "]}";

        TrafficDto traffic = mapper.readValue(json, TrafficDto.class);
        log.info("TrafficDto:\n{}", traffic);

        List<TrafficJamDto> trafficList = traffic.getTraffic();

        TrafficJamDto trafficJamDto0 = trafficList.get(0);
        Assert.assertEquals(1, trafficJamDto0.getFrom());
        Assert.assertEquals(7, trafficJamDto0.getTo());
        Assert.assertEquals(1.0, trafficJamDto0.getJam(), 1e-15);

        TrafficJamDto trafficJamDto1 = trafficList.get(1);
        Assert.assertEquals(6, trafficJamDto1.getFrom());
        Assert.assertEquals(30, trafficJamDto1.getTo());
        Assert.assertEquals(1.5, trafficJamDto1.getJam(), 1e-15);

        TrafficJamDto trafficJamDto2 = trafficList.get(2);
        Assert.assertEquals(10, trafficJamDto2.getFrom());
        Assert.assertEquals(17, trafficJamDto2.getTo());
        Assert.assertEquals(1.9, trafficJamDto2.getJam(), 1e-15);
    }

    @Test
    public void validateTrafficJamJson() throws IOException {
        String json = "{ \"trafficjam\": [\n" +
                      "{\"a\":1 , \"b\":7 , \"jam\": 1.0 },\n" +
                      "{\"a\":6 , \"b\":30, \"jam\": 1.5 },\n" +
                      "{\"a\":10, \"b\":17, \"jam\": 1.9 }\n" +
                      "]}";

        TrafficDto traffic = mapper.readValue(json, TrafficDto.class);
        log.info("TrafficDto:\n{}", traffic);

        List<TrafficJamDto> trafficList = traffic.getTraffic();
        Assert.assertEquals(3, trafficList.size());

        TrafficJamDto trafficJamDto0 = trafficList.get(0);
        Assert.assertEquals(1, trafficJamDto0.getFrom());
        Assert.assertEquals(7, trafficJamDto0.getTo());
        Assert.assertEquals(1.0, trafficJamDto0.getJam(), 1e-15);

        TrafficJamDto trafficJamDto1 = trafficList.get(1);
        Assert.assertEquals(6, trafficJamDto1.getFrom());
        Assert.assertEquals(30, trafficJamDto1.getTo());
        Assert.assertEquals(1.5, trafficJamDto1.getJam(), 1e-15);

        TrafficJamDto trafficJamDto2 = trafficList.get(2);
        Assert.assertEquals(10, trafficJamDto2.getFrom());
        Assert.assertEquals(17, trafficJamDto2.getTo());
        Assert.assertEquals(1.9, trafficJamDto2.getJam(), 1e-15);
    }

    @Test
    public void validatePointsJson() throws IOException {
        String json = "{ \"points\": [\n" +
                      "{\"p\": 0, \"money\": 1000200},\n" +
                      "{\"p\": 1, \"money\": 1000234},\n" +
                      "{\"p\": 2, \"money\": 1323200},\n" +
                      "{\"p\": 3, \"money\": 1434545}\n" +
                      "]}";

        PointsDto points = mapper.readValue(json, PointsDto.class);
        log.info("PointsDto:\n{}", points);

        List<PointDto> pointsList = points.getPoints();
        Assert.assertEquals(4, pointsList.size());

        PointDto pointDto0 = pointsList.get(0);
        Assert.assertEquals(0, pointDto0.getIndex());
        Assert.assertEquals(1000200, pointDto0.getMoney());

        PointDto pointDto1 = pointsList.get(1);
        Assert.assertEquals(1, pointDto1.getIndex());
        Assert.assertEquals(1000234, pointDto1.getMoney());

        PointDto pointDto2 = pointsList.get(2);
        Assert.assertEquals(2, pointDto2.getIndex());
        Assert.assertEquals(1323200, pointDto2.getMoney());

        PointDto pointDto3 = pointsList.get(3);
        Assert.assertEquals(3, pointDto3.getIndex());
        Assert.assertEquals(1434545, pointDto3.getMoney());
    }

    @Test
    public void validateArriveJson() throws IOException {
        String json = "{ \"point\": 2, \"car\": \"sb4\" }";

        ArriveDto arriveDto = mapper.readValue(json, ArriveDto.class);
        log.info("ArriveDto:\n{}", arriveDto);

        Assert.assertEquals(2, arriveDto.getPoint());
        Assert.assertEquals("sb4", arriveDto.getCarName());
    }

    //---------------
    // Output models
    //---------------

    @Test
    public void validateLoginJson() throws JsonProcessingException {
        LoginDto dto = new LoginDto("The Bees");
        String expectedJson = "{\"team\":\"The Bees\"}";
        String json = mapper.writeValueAsString(dto);

        Assert.assertEquals(expectedJson, json);
    }

    @Test
    public void validateGotoJson() throws JsonProcessingException {
        GotoDto dto = new GotoDto(2, "sb4");
        String expectedJson = "{\"goto\":2,\"car\":\"sb4\"}";
        String json = mapper.writeValueAsString(dto);

        Assert.assertEquals(expectedJson, json);
    }

    @Test
    public void validateGotoNomoneyJson() throws JsonProcessingException {
        GotoDto dto = new GotoDto(45, "sb1", true);
        String expectedJson = "{\"goto\":45,\"car\":\"sb1\",\"nomoney\":true}";
        String json = mapper.writeValueAsString(dto);

        Assert.assertEquals(expectedJson, json);
    }

    @Test
    public void validateReconnectJson() throws JsonProcessingException {
        ReconnectDto dto = new ReconnectDto("12321");
        String expectedJson = "{\"reconnect\":\"12321\"}";
        String json = mapper.writeValueAsString(dto);

        Assert.assertEquals(expectedJson, json);
    }

}