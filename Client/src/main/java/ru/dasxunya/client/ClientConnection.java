package ru.dasxunya.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.other.Keeper;
import ru.dasxunya.other.Message;
import ru.dasxunya.other.ServerResponse;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import static ru.dasxunya.other.Utils.OBJECT_MAPPER;

/**
 * Главный клиентский класс, создает потоки ввода-вывода и устанавливает соединение
 */
@Data
@Slf4j
public class ClientConnection implements Closeable {
    private Socket clientSocket;
    private InputHandler inputHandler;
    private OutputStream output;
    private InputStream input;
    private ByteBuffer buffer = ByteBuffer.allocate(5120);

    private int port = 6667;

    public static void main(String[] args) {

        try (ClientConnection client = new ClientConnection()) {
            client.checkArguments(args);
            client.createSocket();
            client.run();
            client.serverAnswer();
            client.WhileClientSocketConnected();
        } catch (PortUnreachableException e) {
            log.error("Не удалось получить данные по указанному порту/сервер не доступен");
        } catch (UnknownHostException e) {
            log.error("Неизвестный хост");
        } catch (IOException e) {
            log.error("Ошибка при подключении к серверу. Выберите другой порт.");
        }
    }

    private void checkArguments(String[] args) {
        if (args.length == 0) {
            return;
        }

        try {
            port = Integer.parseInt(args[0]);

            if (port <= 0 || port > 65535) {
                log.error("Порт должен лежать в пределах 1-65535");
                System.exit(-1);
            }
        } catch (Exception e) {
            log.error("Порт должен быть числом");
            System.exit(-1);
        }

    }

    private void run() throws IOException {
        output = clientSocket.getOutputStream();
        input = clientSocket.getInputStream();

        log.info("Клиент запущен");
    }

    private void createSocket() throws IOException {
        clientSocket = new Socket("localhost", port);

        log.info("Создан сокет");
    }

    private void serverAnswer() {
        try {
            if (clientSocket.isConnected()) {
                try {
                    buffer.clear();
                    if (isExistResponse()) {
                        inputHandler = new InputHandler(OBJECT_MAPPER.readValue(buffer.array(), Keeper.class));
                    }
                    buffer.flip();
                } catch (Exception e) {
                    log.error("Ошибка при получении сообщения от сервера");
                }
            }
        } catch (Exception e) {
            log.error("Отсутствует подключение к серверу.");
            close();
        }
    }

    private void WhileClientSocketConnected() {
        while (clientSocket.isConnected()) {
            try {
                Message message = inputHandler.setStart();

                if (message == null)
                    continue;

                output.write(OBJECT_MAPPER.writeValueAsBytes(message));
                output.flush();

                if (message.getCommandName().equalsIgnoreCase("exit")) {
                    log.info("Клиент закрыл соединение.");
                    Thread.sleep(1000);
                    handleRequest();
                    close();
                }

                Thread.sleep(500);

                buffer.clear();
                handleRequest();
                buffer.flip();

            } catch (IOException e) {
                log.error("Отсутствует подключение к серверу. Клиент отключается.");
                close();
            } catch (InterruptedException e) {
                log.error("Ожидающий поток был прерван");
            } catch (NullPointerException e) {
                log.error("Команда не может быть обработана.");
            }
        }
    }

    private void handleRequest() throws IOException {
        if (isExistResponse()) {
            ServerResponse serverResponse = OBJECT_MAPPER.readValue(buffer.array(), ServerResponse.class);

            if (serverResponse.getError().isEmpty())
                log.info(serverResponse.getMessage());
            else
                log.error(serverResponse.getError());
        }

    }

    private boolean isExistResponse() throws IOException {
        int serverAnswer = input.read(buffer.array());
        return serverAnswer > 0;
    }

    @Override
    public void close() {
        try {
            input.close();
            output.close();
            clientSocket.close();
            log.info("Клиент закрыт.");
        } catch (NullPointerException e) {
            log.error("Клиентский сокет не был создан.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}