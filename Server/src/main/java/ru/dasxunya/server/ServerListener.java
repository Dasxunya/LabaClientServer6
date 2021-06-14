package ru.dasxunya.server;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.other.Keeper;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@Slf4j
@Data
public
class ServerListener extends Thread implements Closeable {
    private final ServerSocketChannel serverSocketChannel;
    private final Keeper keeper = new Keeper();
    private final Selector selector;

    private int port = 6667;
    private int connectionCount = 0;

    public ServerListener(String[] args) throws IOException {
        selector = Selector.open();
        log.info("Селектор открыт.");

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        try {
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            log.info("Порт связан.");
        } catch (IOException e) {
            log.error("Невозможно прослушать порт. Выберите другой и перезапустите сервер.");
            close();
        }

        checkArguments(args);
    }

    public void checkArguments(String[] args) {
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

    @SneakyThrows
    @Override
    public void run() {
        log.info("Сервер запущен.");

        serverSocketChannel.register(selector, serverSocketChannel.validOps());

        while (true) {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();

                if (socketChannel != null) {
                    log.info("Новое подключение создано: " + socketChannel.getRemoteAddress());
                    socketChannel.configureBlocking(false);
                    connectionsIncrement();

                    ifFirstClient();

                    new ClientHandler(socketChannel, this).start();
                }

            } catch (IOException e) {
                log.error("Соединение с клиентом отсутствует.");
                close();

            }
        }
    }

    private void ifFirstClient() {
        if (connectionCount == 1) {
            try {
                Load.builder().keeper(keeper).build().run();
                log.info("Документ загружен.");
            } catch (Exception e) {
                log.error("Ошибка при загрузке коллекции из документа.");
            }
        }
    }

    private void connectionsIncrement() {
        connectionCount += 1;
    }

    @Override
    public void close() {
        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            log.error("Ошибка при закрытии сервера.");
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void connectionsDecrement() {
        connectionCount -= 1;
    }
}
