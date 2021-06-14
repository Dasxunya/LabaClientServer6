package ru.dasxunya.commands;


import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.other.ServerResponse;
import ru.dasxunya.server.CommandHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SuperBuilder
@Slf4j
public class ExecuteScript extends Command {

    public static boolean execute_script_completed = false;

    private final CommandHandler commandHandler;


    @Override
    public ServerResponse execute(List<String> args) {
        if (args.size() == 1) {
            try {
                Path path = Paths.get(args.get(0));
                if (Files.exists(path) && !Files.isRegularFile(path)) {
                    return ServerResponse.builder().error("Нельзя передать специальный файл в качестве скрипта. Введите команду снова с новым аргументом.").command(getName()).build();
                } else {
                    if (keeper.getScriptNames().size() == 0) keeper.addScriptName(args.get(0));
                    String dir = path.toString();
                    try (BufferedReader br = new BufferedReader(new FileReader(dir))) {
                        Map<String, Command> commands = commandHandler.getCommands();
                        String line;
                        while ((line = br.readLine()) != null) {
                            line = line.trim();
                            if (line.equals("execute_script " + args.get(0))) {
                                return ServerResponse.builder().error("В скрипте обнаружена рекурсия, удалите строку execute_script " + args.get(0)).command(getName()).build();
                            } else if (line.startsWith(getName())) {
                                String cmd = getCommandName(line);
                                List<String> arguments = getArguments(line);
                                if (keeper.addScriptName(arguments.get(0))) {
                                    Command command = commands.get(cmd);
                                    command.execute(arguments);
                                } else
                                    return ServerResponse.builder().error("В одном из вызовов команды execute_script в файле обнаружена рекурсия, команда пропущена\nУдалите строку " + arguments.get(0) + "\n").command(getName()).build();
                            } else {
                                String cmd = getCommandName(line);
                                List<String> arguments = getArguments(line);
                                Command command = commands.get(cmd);
                                command.execute(arguments);
                                log.info(System.lineSeparator());
                            }
                        }
                        keeper.clearScriptNames();
                    } catch (FileNotFoundException e) {
                        return ServerResponse.builder().error("Файл не найден. Убедитесь, что вы правильно указали путь к файлу и введите команду снова.").command(getName()).build();
                    } catch (Exception e) {
                        return ServerResponse.builder().error("Произошла ошибка при чтении команды скрипта.").command(getName()).build();
                    }
                }
            } catch (Exception e) {
                return ServerResponse.builder().error("Ошибка при обработке файла. Проверьте, что команде не был передан специальный файл.").command(getName()).build();
            }
        } else {
            return ServerResponse.builder().error("У команды execute_script должен быть один аргумент - путь к файлу. Введите команду снова.").command(getName()).build();
        }
        return ServerResponse.builder().message("Скрипт выполнен").command(getName()).build();
    }
    
    public String getCommandName(String input) {
        String[] elements = input.split(" +");
        return elements[0].toLowerCase(); //только название команды
    }


    public List<String> getArguments(String input) {
        List<String> elements = Arrays.stream(input.split(" +")).collect(Collectors.toList());
        return elements.size() > 1 ? elements.stream().skip(1).collect(Collectors.toList()) : null;
    }


    @Override
    public String getName() {
        return "execute_script";
    }


    @Override
    public String getDescription() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла (вместо file_name укажите путь к файлу). В скрипте содержатся команды в таком же виде, в котором они вводятся в интерактивном режиме.";
    }
}
