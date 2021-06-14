package ru.dasxunya.commands;


import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.ServerResponse;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Data
@SuperBuilder
@Slf4j
public class Show extends Command {

    @Override
    public ServerResponse execute(List<String> args) {
        if (args == null) {
            LinkedList<HumanBeing> humanBeings = keeper.getHumanBeings();

            StringBuilder response = new StringBuilder("Коллекция Human:");

            if (humanBeings.size() == 0)
                return ServerResponse.builder().message("Коллекция HumanBeings пуста.").command(getName()).build();
            else {
                humanBeings.stream()
                        .sorted(Comparator.comparing(human -> human.getId()))
                        .forEach(humanBeing -> response.append("\n" + humanBeing.toString()));
            }

            return ServerResponse.builder().message(response.toString()).command(getName()).build();
        } else {
            return ServerResponse.builder().error("У команды show нет аргументов. Введите команду снова.").command(getName()).build();
        }
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
