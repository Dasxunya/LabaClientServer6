package ru.dasxunya.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * The type Utils.
 */
public class Utils {
    /**
     * The constant originalOutput.
     */
    public static final PrintStream originalOutput = System.out;
    /**
     * The constant originalInput.
     */
    public static final InputStream originalInput = System.in;
    /**
     * The constant LINE_SEPARATOR.
     */
    public static final String LINE_SEPARATOR = System.lineSeparator();
    /**
     * The constant EXIT.
     */
    public static final String EXIT = "exit";
    /**
     * The constant outputContent.
     */
    public static ByteArrayOutputStream outputContent;
    /**
     * The constant inputContent.
     */
    public static ByteArrayInputStream inputContent;

    /**
     * The constant SYSTEM_VARIABLE.
     */
    public static String SYSTEM_VARIABLE_START = "start6";
    public static String SYSTEM_VARIABLE_OUTPUT = "output6";
    /**
     * The constant scanner.
     */
    public static Scanner scanner = new Scanner(System.in);

    /**
     * Gets object mapper.
     *
     * @return the object mapper
     */
    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}
