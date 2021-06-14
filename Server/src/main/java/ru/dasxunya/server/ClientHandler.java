package ru.dasxunya.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.other.Message;
import ru.dasxunya.other.ServerResponse;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static ru.dasxunya.other.Utils.OBJECT_MAPPER;

@Slf4j
class ClientHandler extends Thread implements Closeable {
    private final SocketChannel socketChannel;
    private final ServerListener serverListener;

    public ClientHandler(SocketChannel socketChannel, ServerListener serverListener) {
        this.socketChannel = socketChannel;
        this.serverListener = serverListener;
    }

    @SneakyThrows
    @Override
    public void run() {
        socketChannel.configureBlocking(false);

        if (socketChannel.isOpen()) {
            socketChannel.write(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(serverListener.getKeeper())));
            log.info("Документ отправлен.");
        }

        while (socketChannel.isOpen()) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(5120);
                while (true) {
                    buffer.clear();
                    int read = socketChannel.read(buffer); // non-blocking

                    if (read < 0) {
                        break;
                    }

                    if (read > 0) {
                        Message message = OBJECT_MAPPER.readValue(buffer.array(), Message.class);
                        ServerResponse serverResponse = handleClientMessage(message);
                        log.info("Получено {}", message);
//                        AddHistory(message);
                        socketChannel.write(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(serverResponse)));
                        if (serverResponse.getMessage().equals("Отключение успешно.")) {
                            log.info("Клиент {} отключился.", socketChannel.getRemoteAddress());
                            if (serverResponse.getError() != null)
                                log.error(serverResponse.getError());
                            close();
                            break;
                        }
                    }
                    buffer.flip();
                }
            } catch (Exception e) {
                log.info("Соединение с клиентом" + socketChannel.getRemoteAddress() + " экстренно прекращено");
                e.printStackTrace();
                SpecialSave save = SpecialSave.builder().keeper(serverListener.getKeeper()).build();
                save.execute();
                close();

            }
        }
    }

    /**
     * Обработка сообщения от клиента
     *
     * @param message Данные о команде
     * @return ServerResponse
     */
    private ServerResponse handleClientMessage(Message message) {
        CommandHandler command;

        if (message == null) {
            return ServerResponse.builder().error("Клиент отправил некорректные данные").build();
        }

        if (message.getCommandName().equalsIgnoreCase("exit")) {
            serverListener.connectionsDecrement();
            SpecialSave save = SpecialSave.builder().keeper(serverListener.getKeeper()).build();
            save.execute();
            return ServerResponse.builder().message("Отключение успешно.").build();
        }

        switch (message.getCommandName()) {
            case "add":
                command = new CommandHandler(message.getCommandName(), serverListener.getKeeper(), message.getHumanBeing());
                break;
            case "update":
                command = new CommandHandler(message.getCommandName(), serverListener.getKeeper(), message.getHumanBeing(), message.getCommandArgs());
                break;
            default:
                command = new CommandHandler(message.getCommandName(), serverListener.getKeeper(), message.getCommandArgs());
                break;
        }

        return command.run();
    }

    @Override
    public void close() {
        try {
            socketChannel.close();
        } catch (IOException ioException) {
            log.error("Ошибка при закрытии клиента.");
        }
    }
}
