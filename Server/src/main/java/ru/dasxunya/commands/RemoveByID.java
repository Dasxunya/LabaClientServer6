package ru.dasxunya.commands;


import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.ServerResponse;

import java.util.LinkedList;
import java.util.List;



@SuperBuilder
@Slf4j
public class RemoveByID extends Command {


    @Override
    public ServerResponse execute(List<String> args) {
        if (args == null || args.size() != 1) {
            return ServerResponse.builder().error("У команды remove_by_id должен быть один аргумент - ID персоны. Введите команду снова.").command(getName()).build();
        } else {
            long id;
            try {
                id = Long.parseLong(args.get(0));
                if (id < 0)
                    return ServerResponse.builder().error("ID не может быть отрицательным числом. Введите команду снова.").command(getName()).build();
            } catch (Exception e) {
                return ServerResponse.builder().error("В качестве аргумента должна быть передана строка из цифр. Введите команду снова.").command(getName()).build();
            }

            LinkedList<HumanBeing> humanBeings = keeper.getHumanBeings();
            HumanBeing humanBeing = humanBeings.stream()
                    .filter(x -> x.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (humanBeing != null) {
                humanBeings.remove(humanBeing);
                return ServerResponse.builder().message("Объект с ID " + id + " успешно удален из коллекции.").command(getName()).build();
            }
            return ServerResponse.builder().message("Элемента с таким ID нет в коллекции.").command(getName()).build();
        }
    }


    @Override
    public String getName() {
        return "remove_by_id";
    }


    @Override
    public String getDescription() {
        return "remove_by_id id : удалить элемент из коллекции по его id";
    }
}
