package org.bees.optimizer.model.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private long capacity = 1000000L;
    private String name;

    public Car(String name) {
        this.name = name;
    }
}
