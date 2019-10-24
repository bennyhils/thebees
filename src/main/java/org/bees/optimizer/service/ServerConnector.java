package org.bees.optimizer.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.LoginDto;
import org.bees.optimizer.model.external.ReconnectDto;
import org.bees.optimizer.model.external.RouteDto;
import org.bees.optimizer.model.external.TokenDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerConnector {

    @NonNull
    private RestTemplate restTemplate;

    public void sendTeam(LoginDto team) {
        try {
//            ResponseEntity<Response> rs = restTemplate.postForEntity(exportUrl(), campaignDTOBy(campaign),
//                    Response.class);
//            processResponse(rs);
        } catch (HttpClientErrorException ex) {
            log.error("Team name sending error: " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            log.error("Team name sending error: ", ex);
        }
    }

    public void sendCarsRoute (TokenDto tokenDto, RouteDto routeDto) {
        try {

        } catch (HttpClientErrorException ex) {
            log.error("Cars route sending error: " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            log.error("Cars route sending error: ", ex);
        }
    }

    public void reconnect (ReconnectDto reconnectDto) {
        try {

        } catch (HttpClientErrorException ex) {
            log.error("Cars route sending error: " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            log.error("Cars route sending error: ", ex);
        }

    }
}
