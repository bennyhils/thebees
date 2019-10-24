package org.bees.optimizer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bees.optimizer.RouteOptimizerConfig;
import org.bees.optimizer.model.external.LoginDto;
import org.bees.optimizer.model.external.TokenDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.test.web.client.SimpleRequestExpectationManager;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RouteOptimizerConfig.class)
public class ServerConnectorTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServerConnector serverConnector;

    private MockRestServiceServer solutionServer;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() {
        solutionServer = MockRestServiceServer.bindTo(restTemplate)
                                              .build(new SimpleRequestExpectationManager());
    }

    @Test
    public void validate() throws JsonProcessingException {
        LoginDto loginDto = new LoginDto("The Bees");

        TokenDto tokenDto = new TokenDto("12321", Arrays.asList("sb1", "sb2"), 1);

        solutionServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo("http://localhost:8080/login"))
                      .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                      .andExpect(MockRestRequestMatchers.content().bytes(mapper.writeValueAsString(loginDto).getBytes()))
                      .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).body(mapper.writeValueAsString(tokenDto)));

        serverConnector.sendTeam(loginDto);

        solutionServer.verify();
    }

}
