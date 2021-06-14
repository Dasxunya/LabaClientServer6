package ru.dasxunya.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.core.HumanBeing;
import ru.dasxunya.other.Keeper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static ru.dasxunya.utils.Utils.SYSTEM_VARIABLE_START;
import static ru.dasxunya.utils.Utils.getObjectMapper;

/**
 * The type Load.
 */
@Slf4j
@SuperBuilder
public class Load {
    private final Keeper keeper;

    public void run() {

        String filename = System.getenv(SYSTEM_VARIABLE_START);

        // https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
        StringBuilder jsonStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                jsonStringBuilder.append(currentLine).append("\n");
            }

            // https://www.baeldung.com/jackson-object-mapper-tutorial
            ObjectMapper objectMapper = getObjectMapper();
            HumanBeing[] humanBeings = objectMapper.readValue(jsonStringBuilder.toString(), HumanBeing[].class);
            keeper.getHumanBeings().clear();
            keeper.getHumanBeings().addAll(Arrays.asList(humanBeings));
        } catch (IOException e) {
            log.info("Проблемы с файлом data6.txt!");
        }


    }
}
