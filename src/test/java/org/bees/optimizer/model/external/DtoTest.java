package org.bees.optimizer.model.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class DtoTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void validateTokenJson() throws IOException {
        String json = "{ \"token\" : \"12321\", \"cars\": [\"sb1\", \"sb2\"], \"level\": 1 }";

        Token token = mapper.readValue(json, Token.class);
        log.info("Token values:\ntoken={}\ncars={}\nlevel={}", token.getToken(), token.getCars(), token.getLevel());
    }

    @Test
    public void validateRoutesJson() throws IOException {
        String json = "{ \"routes\": [\n" +
                      "{\"a\":1 , \"b\":7 , \"time\": 31 },\n" +
                      "{\"a\":6 , \"b\":30, \"time\": 1 },\n" +
                      "{\"a\":10, \"b\":17, \"time\": 12 }\n" +
                      "]}";

        Routes routes = mapper.readValue(json, Routes.class);
        log.info("Routes:\n{}", routes);
    }

}