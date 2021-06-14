package ru.dasxunya.commands;


import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.other.ServerResponse;

import java.util.List;

@SuperBuilder
@Slf4j
public class Clear extends Command {

    @Override
    public ServerResponse execute(List<String> args) {
        if (args == null) {
            keeper.getHumanBeings().clear();
            return ServerResponse.builder().message("Коллекция успешно очищена.").command(getName()).build();
        } else {
            return ServerResponse.builder().error("У команды clear нет аргументов. Введите команду снова.").build();
        }
    }


    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "clear : очистить коллекцию";
    }
}
