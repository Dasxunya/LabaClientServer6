package ru.dasxunya.commands;


import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.ServerResponse;

import java.util.LinkedList;
import java.util.List;


@SuperBuilder
@Slf4j
public class Update extends Command {

    private final HumanBeing humanBeing;
    private final LinkedList<HumanBeing> humanBeings = keeper.getHumanBeings();


    @Override
    public ServerResponse execute(List<String> args) {
        int id = Integer.parseInt(args.get(0));

        HumanBeing humanBeingForUpdate = null;

        for (HumanBeing being : humanBeings)
            if (being.getId().equals(id)) {
                humanBeingForUpdate = being;
            }

        humanBeing.setId(id);
        humanBeings.remove(humanBeingForUpdate);
        humanBeings.add(humanBeing);

        return ServerResponse.builder().message("Объект успешно обновлен").command(getName()).build();
    }


    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
}
