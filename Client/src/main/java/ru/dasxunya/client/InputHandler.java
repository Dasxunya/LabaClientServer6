package ru.dasxunya.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import ru.dasxunya.other.Keeper;
import ru.dasxunya.other.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class InputHandler {

    private final HashSet<String> names = new HashSet<>();
    private final Keeper collections; // передать ее с сервера

    public InputHandler(Keeper keeper) {
        collections = keeper;
    }

    private Message run() {
        do {
            try {
                val input = getDataFromInput();
                val commandName = getCommandName(input);
                val args = getArguments(input);

                switch (commandName) {
                    case "add":
                        return createHuman(args);
                    case "update":
                        return updateHuman(args);
                }

                return Message.builder().commandName(commandName).commandArgs(args).build();
            } catch (NullPointerException ne) {
                log.error("Экстренное закрытие клиента");
            } catch (Exception e) {
                log.error("Неверный формат ввода команды. Введите команду еще раз.");
            }
        } while (true);
    }

    @SneakyThrows
    private String getDataFromInput() {
        System.out.print(">");
        return new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
    }

    private Message createHuman(List<String> args) {
        var creation = new CreateHumanBeing(collections);
        var newHumanBeing = creation.createHumanBeing(args);

        if (newHumanBeing == null)
        {
            return null;
        }

        return Message.builder().commandName("add").humanBeing(newHumanBeing).build();
    }

    private Message updateHuman(List<String> args) {
        var update = new UpdateHuman(collections);
        var updatedHumanBeing = update.updateHuman(args);
        if (updatedHumanBeing == null) return null;
        else return Message.builder().commandName("update").humanBeing(updatedHumanBeing).commandArgs(args).build();
    }

    public String getCommandName(String input) {
        var elements = input.split(" +");
        return elements[0].toLowerCase(); //только название команды
    }

    public List<String> getArguments(String input) {
        var elements = Arrays.stream(input.split(" +")).collect(Collectors.toList());
        return elements.size() > 1 ? elements.stream().skip(1).collect(Collectors.toList()) : null;
    }

    public Message setStart() {
        return run();
    }
}
