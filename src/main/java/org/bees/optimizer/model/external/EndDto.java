package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class EndDto {
    final boolean end;

    @JsonCreator
    public EndDto(@JsonProperty("end") final boolean end) {
        this.end = end;
    }
}
