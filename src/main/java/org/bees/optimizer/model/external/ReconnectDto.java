package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReconnectDto {
    @JsonProperty("reconnect")
    private final String token;
}
