package ru.dasxunya.client;

import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.Keeper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
@Slf4j
public class UpdateHuman {

    private final Keeper keeper;

    private boolean isFound = false;

    private HumanBeing updatedHumanBeing;

    public UpdateHuman(Keeper keeper) {
        this.keeper = keeper;
    }

    public HumanBeing updateHuman(List<String> args) {
        LinkedList<HumanBeing> humanBeings = keeper.getHumanBeings();

        if (args == null || args.size() != 2) {
            log.info("У команды update должно быть два аргумента - необходимое id и слово Human.");
            return null;
        } else {
            long id = Long.parseLong(args.get(0));

            for (HumanBeing humanBeing : humanBeings) {
                if (humanBeing.getId() == id) {
                    updatedHumanBeing = humanBeing;
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                log.info("Человека с таким id не существует");
                return null;
            } else {
                HumanBeing newHumanBeing = new CreateHumanBeing(keeper).createHumanBeing(Collections.singletonList("Human"));

//                keeper.getHumanBeings().remove(updatedHumanBeing);

                newHumanBeing.setId(updatedHumanBeing.getId());

//                keeper.getHumanBeings().add(newHumanBeing);

                return newHumanBeing;
            }
        }
    }
}
