package ru.dasxunya.commands;


import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.ServerResponse;

import java.util.LinkedList;
import java.util.List;


@SuperBuilder
@Slf4j
public class Info extends Command {

    @Override
    public ServerResponse execute(List<String> args) {
        if (args == null) {
            LinkedList<HumanBeing> humanBeings = keeper.getHumanBeings();
            String response = "Тип коллекции: " + humanBeings.getClass() + "\nТип элементов: HumanBeing\nКоличество элементов: " + humanBeings.size();
            if (humanBeings.size() != 0) {
                response += "\nДата инициализации: " + humanBeings.get(0).getCreationDate();
            } else {
                response += "\nДата инициализации: -";
            }
            return ServerResponse.builder().message(response).command(getName()).build();
        } else {
            return ServerResponse.builder().error("У команды info нет аргументов. Введите команду снова.").command(getName()).build();
        }
    }

    @Override
    public String getName() {
        return "info";
    }


    @Override
    public String getDescription() {
        return "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}
