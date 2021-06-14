package ru.dasxunya.core;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Data
@Jacksonized
public class Car {
    private String name;
    private boolean cool;
}
