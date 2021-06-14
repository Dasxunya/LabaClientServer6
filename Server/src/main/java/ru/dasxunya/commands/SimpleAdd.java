package ru.dasxunya.commands;

import lombok.experimental.SuperBuilder;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.ServerResponse;

import java.util.List;

@SuperBuilder
public class SimpleAdd extends Command {

    private final HumanBeing humanBeing;

    /**
     * Главный метод класса, запускает команду
     *
     * @param args Параметры командной строки
     * @return true/false Успешно ли завершилась команда
     */
    public ServerResponse execute(List<String> args) {
        keeper.getHumanBeings().add(humanBeing);
        return ServerResponse.builder().message("Объект успешно добавлен").command("add").build();
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "add human: добавить новый элемент в коллекцию, ввод вручную";
    }
}

