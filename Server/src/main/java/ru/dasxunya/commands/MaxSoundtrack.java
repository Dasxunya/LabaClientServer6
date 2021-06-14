package ru.dasxunya.commands;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.ServerResponse;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@SuperBuilder
public class MaxSoundtrack extends Command{

    @Override
    public ServerResponse execute(List<String> args) {

        LinkedList<HumanBeing> humanBeings = keeper.getHumanBeings();

        var response = new StringBuilder("SoundtrackName является максимальным: " );

        if (humanBeings.isEmpty()) {
            response.append("Такой SoundtrackName отсутствует!");
        } else {
            HumanBeing humanBeingWithMaxSoundtrack = humanBeings.stream().max(Comparator.comparing(HumanBeing::getSoundtrackName)).orElse(null);

            response.append(humanBeingWithMaxSoundtrack);
        }

        return ServerResponse.builder().message(response.toString()).command(getName()).build();
    }

    @Override
    public String getName() {
        return "max_by_soundtrack_name";
    }

    @Override
    public String getDescription() {
        return "max_by_soundtrack_name : вывести любой объект из коллекции, значение поля soundtrackName которого является максимальным.";
    }
}
