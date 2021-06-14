package ru.dasxunya.commands;


import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.ServerResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Filter name.
 */
@Slf4j
@SuperBuilder
public class FilterName extends Command {

    @Override
    public ServerResponse execute(List<String> args) {
        return isOneArgument(args) ? filterName(args) : ServerResponse.builder().error("У команды filter_starts_with_name должен быть аргумент. Введите команду снова.").command(getName()).build();
    }

    private boolean isOneArgument(List<String> args) {
        return args != null && args.size() == 1;
    }

    public ServerResponse filterName(List<String> args) {
        LinkedList<HumanBeing> humanBeings = keeper.getHumanBeings();

        var response = new StringBuilder("Значение поля name начинается с заданной подстроки: ");

        if (humanBeings.isEmpty()) {
            response.append("Коллекция пустая! Поиск невозможен!");
        } else {

            // https://metanit.com/java/tutorial/10.6.php
            List<HumanBeing> humanBeingsWithSubstringName = humanBeings.stream().filter(humanBeing -> humanBeing.getName().indexOf(args.get(0)) == 0).sorted().collect(Collectors.toList());

            humanBeingsWithSubstringName.forEach(response::append);
        }

        return ServerResponse.builder().message(response.toString()).command(getName()).build();
    }

    @Override
    public String getName() {
        return "filter_starts_with_name";
    }

    @Override
    public String getDescription() {
        return "filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки.";
    }
}
