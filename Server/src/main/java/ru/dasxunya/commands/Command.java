package ru.dasxunya.commands;


import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.other.Keeper;
import ru.dasxunya.other.ServerResponse;

import java.util.List;

@SuperBuilder
@Slf4j
public abstract class Command {

    public Keeper keeper;

    public abstract ServerResponse execute(List<String> args);

    public abstract String getName();

    public abstract String getDescription();
}
