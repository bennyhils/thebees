package org.bees.optimizer.model.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class TokenTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void validateJackson() throws IOException {
        String str = "{ \"token\" : \"12321\", \"cars\": [\"sb1\", \"sb2\"], \"level\": 1 }";

        Token token = mapper.readValue(str, Token.class);
        log.info("Token values:\ntoken={}\ncars={}\nlevel={}", token.getToken(), token.getCars(), token.getLevel());
    }


}