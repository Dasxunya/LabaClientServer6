package ru.dasxunya.commands;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.other.Message;
import ru.dasxunya.other.ServerResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@SuperBuilder
public class History extends Command{

    public static LinkedList<Message> history = new LinkedList<>();

    public static void AddHistory(Message message) {
        if (history.size() == 11) {
            history.removeFirst();
        }
        history.add(message);
    }

    @Override
    public ServerResponse execute(List<String> args) {
        String response = history.stream().map(message -> message.getCommandName() + System.lineSeparator()).collect(Collectors.joining("", "Последние 11 команд:" + System.lineSeparator(), ""));
        return ServerResponse.builder().message(response).command(getName()).build();
    }

    @Override
    public String getName() {
        return "history";
    }

    @Override
    public String getDescription() {
        return "history : вывести последние 11 команд (без их аргументов)";
    }
}
