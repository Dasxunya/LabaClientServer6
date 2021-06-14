package ru.dasxunya.server;


import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.dasxunya.commands.*;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.Keeper;
import ru.dasxunya.other.Message;
import ru.dasxunya.other.ServerResponse;

import java.util.LinkedHashMap;
import java.util.List;

import static ru.dasxunya.commands.History.AddHistory;

/**
 * Класс - ответственный за маппинг комманд
 */
@SuperBuilder
@Data
public class CommandHandler {
    private final LinkedHashMap<String, Command> commands = new LinkedHashMap<>();
    private String commandName;
    private List<String> commandArgs;
    private Keeper keeper;

    public CommandHandler(String commandName, Keeper keeper, List<String> commandArgs) {
        settings(commandName, commandArgs, keeper, null, null);
    }

    public CommandHandler(String commandName, Keeper keeper, HumanBeing newHumanBeing) {
        settings(commandName, null, keeper, newHumanBeing, null);
    }

    public CommandHandler(String commandName, Keeper keeper, HumanBeing newHumanBeing, List<String> commandArgs) {
        settings(commandName, commandArgs, keeper, null, newHumanBeing);
    }

    private void settings(String commandName, List<String> commandArgs, Keeper keeper, HumanBeing humanBeingForAdd, HumanBeing humanBeingForUpdate) {
        this.commandName = commandName;
        this.commandArgs = commandArgs;
        this.keeper = keeper;

        commands.put("add", SimpleAdd.builder().keeper(this.keeper).humanBeing(humanBeingForAdd).build());
        commands.put("update", Update.builder().keeper(this.keeper).humanBeing(humanBeingForUpdate).build());

        createCommandCollection();
    }

    private void createCommandCollection() {
        commands.put("add_if_max", AddIfMax.builder().keeper(keeper).build());
        commands.put("add_if_min", AddIfMin.builder().keeper(keeper).build());
        commands.put("clear", Clear.builder().keeper(keeper).build());
        commands.put("execute_script", ExecuteScript.builder().keeper(keeper).commandHandler(this).build());
        commands.put("filter_starts_with_name", FilterName.builder().keeper(keeper).build());
        commands.put("help", Help.builder().keeper(keeper).commandHandler(this).build());
        commands.put("history", History.builder().keeper(keeper).build());
        commands.put("info", Info.builder().keeper(keeper).build());
        commands.put("max_by_soundtrack_name", MaxSoundtrack.builder().keeper(keeper).build());
        commands.put("remove_by_id", RemoveByID.builder().keeper(keeper).build());
        commands.put("show", Show.builder().keeper(keeper).build());
        commands.put("print_unique_weapon_type", UniqueWeapon.builder().keeper(keeper).build());
    }

    ServerResponse run() {
        Command command = commands.get(commandName);

        if (command != null) {
            AddHistory(Message.builder().commandName(command.getName()).build());
        } else {
            return ServerResponse.builder().error("Команды не существует.").build();
        }

        switch (commandName) {
            case "add":
                return commands.get("add").execute(null);
            default: {
                return command.execute(commandArgs);
            }
        }
    }

}