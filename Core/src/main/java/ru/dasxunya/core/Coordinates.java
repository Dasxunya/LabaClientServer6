package ru.dasxunya.core;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Data
@Jacksonized
public class Coordinates {
    private Long x;
    private int y;
}
