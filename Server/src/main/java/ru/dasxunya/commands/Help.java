package ru.dasxunya.commands;


import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ru.dasxunya.other.ServerResponse;
import ru.dasxunya.server.CommandHandler;

import java.util.List;


@SuperBuilder
@Slf4j
public class Help extends Command {

    private final CommandHandler commandHandler;

    public ServerResponse execute(List<String> args) {
        if (args == null) {
            var commands = commandHandler.getCommands();
            var response = new StringBuilder("Доступные вам команды:" + System.lineSeparator());
            commands.values().forEach(command -> response.append(command.getDescription() + System.lineSeparator()));
            return ServerResponse.builder().message(response.toString()).command(getName()).build();
        } else {
            return ServerResponse.builder().error("У команды help нет аргументов. Введите команду снова.").command(getName()).build();
        }
    }

    public String getName() {
        return "help";
    }

    public String getDescription() {
        return "help : вывести справку по доступным командам";
    }
}
