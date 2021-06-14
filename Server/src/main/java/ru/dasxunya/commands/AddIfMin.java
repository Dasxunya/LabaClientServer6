package ru.dasxunya.commands;


import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.client.CreateHumanBeing;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.ServerResponse;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@SuperBuilder
@Slf4j
public class AddIfMin extends Command {

    @Override
    public ServerResponse execute(List<String> args) {
        return isArgumentWithHuman(args) ? addIfMin(args) : ServerResponse.builder().error("У команды add_if_min должен быть аргумент - слово 'Human'. Введите команду снова.").command(getName()).build();
    }

    private boolean isArgumentWithHuman(List<String> args) {
        return args != null  && args.size() == 1 && args.get(0).equalsIgnoreCase("Human");
    }

    private ServerResponse addIfMin(List<String> args) {
        log.info("Если элемент меньше минимального, добавление в коллекцию: ");

        LinkedList<HumanBeing> humanBeings = keeper.getHumanBeings();
        HumanBeing maybeHumanBeing = new CreateHumanBeing(keeper).createHumanBeing(args);
        HumanBeing humanBeingWithMinImpactSpeed = humanBeings.stream().min(Comparator.comparing(HumanBeing::getImpactSpeed)).orElse(null);

        if (humanBeings.isEmpty() || humanBeingWithMinImpactSpeed.getImpactSpeed() > maybeHumanBeing.getImpactSpeed()) {
            keeper.getHumanBeings().add(maybeHumanBeing);
            return ServerResponse.builder().message("Скорость удара существа оказалась меньше минимальных значений в коллекции. Добавляем существо в коллекцию!").command(getName()).build();
        } else {
            return ServerResponse.builder().error("Скорость удара существа оказалась больше минимальных значений в коллекции. Существо не добавляем!").command(getName()).build();
        }
    }


    @Override
    public String getName() {
        return "add_if_min";
    }

    @Override
    public String getDescription() {
        return "add_if_min {element} :добавить новый элемент в коллекцию, если его id меньше, чем у наименьшего id  этой коллекции";
    }
}
