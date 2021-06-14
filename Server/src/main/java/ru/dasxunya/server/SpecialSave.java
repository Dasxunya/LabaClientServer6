package ru.dasxunya.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.other.Keeper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static ru.dasxunya.utils.Utils.*;

@SuperBuilder
@Slf4j
public class SpecialSave {

    private final Keeper keeper;

    public String execute() {
        // https://www.baeldung.com/jackson-object-mapper-tutorial
        // https://www.baeldung.com/jackson-jsonmappingexception
        // https://www.baeldung.com/jackson-serialize-dates

        log.info("Сохранение коллекции в файл: ");

        ObjectMapper objectMapper = getObjectMapper();

        try {
            // https://attacomsian.com/blog/jackson-read-write-json
            ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
            String output = writer.writeValueAsString(keeper.getHumanBeings());
            String filename = System.getenv(SYSTEM_VARIABLE_OUTPUT);
            try (FileOutputStream fos = new FileOutputStream(filename); PrintStream printStream = new PrintStream(fos)) {
                printStream.println(output);
                log.info("Коллекция сохранена в файл!");
            } catch (IOException ex) {
                return ex.getMessage();
            }
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }

        return "";
    }
}