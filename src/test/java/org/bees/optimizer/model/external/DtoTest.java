package org.bees.optimizer.model.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

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
        log.info("TokenDto values:\ntoken={}\ncars={}\nlevel={}", token.getToken(), token.getCars(), token.getLevel());
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
    }

    @Test
    public void validateArriveJson() throws IOException {
        String json = "{ \"point\": 2, \"car\": \"sb4\" }";

        ArriveDto arriveDto = mapper.readValue(json, ArriveDto.class);
        log.info("ArriveDto:\n{}", arriveDto);
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