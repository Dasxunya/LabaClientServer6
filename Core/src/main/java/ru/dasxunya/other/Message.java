package ru.dasxunya.other;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import ru.dasxunya.core.HumanBeing;

import java.io.Serializable;
import java.util.List;

@Data
@SuperBuilder
@Jacksonized
public class Message implements Serializable {
    private String commandName;
    private List<String> commandArgs;
    private HumanBeing humanBeing;
}

