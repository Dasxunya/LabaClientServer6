package ru.dasxunya.commands;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.ServerResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * The type Unique weapon.
 */
@Slf4j
@SuperBuilder
public class UniqueWeapon extends Command {

    public static void run() {


//        if (App.humanBeings.isEmpty()) {
//            log.info("Значения поля weaponType отсутствуют!");
//        } else {
//            SortedSet<String> weaponTypeSet = new TreeSet<>();
//
//            for (HumanBeing humanBeing : App.humanBeings) {
//                weaponTypeSet.add(humanBeing.getWeaponType().toString());
//            }
//
//            for (String weaponType : weaponTypeSet) {
//                log.info(weaponType);
//            }
//        }
    }

    @Override
    public ServerResponse execute(List<String> args) {
        LinkedList<HumanBeing> humanBeings = keeper.getHumanBeings();

        var response = new StringBuilder("Уникальные значения поля weaponType: ");

        if (humanBeings.isEmpty()) {
            response.append("Значения поля weaponType отсутствуют!");
        } else {
            SortedSet<String> weaponTypeSet = humanBeings.stream().map(humanBeing -> humanBeing.getWeaponType().toString()).collect(Collectors.toCollection(TreeSet::new));

            weaponTypeSet.forEach(response::append);
        }

        return ServerResponse.builder().message(response.toString()).command(getName()).build();
    }

    @Override
    public String getName() {
        return "print_unique_weapon_type";
    }

    @Override
    public String getDescription() {
        return "print_unique_weapon_type : вывести уникальные значения поля weaponType всех элементов в коллекции";
    }
}
