package ru.dasxunya.starter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.server.ServerListener;
import ru.dasxunya.server.SpecialSave;

import java.io.IOException;

@Data
@Slf4j
public class Server {
    public static void main(String[] args) {
        try {
            ServerListener serverListener = new ServerListener(args);

            addShutdownHook(serverListener);

            serverListener.start();
        } catch (IOException e) {
            log.info("Ошибка при создании сервера.");
        }
    }

    private static void addShutdownHook(ServerListener serverListener) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Экстренное закрытие сервера.");
            SpecialSave save = SpecialSave.builder().keeper(serverListener.getKeeper()).build();
            save.execute();
        }));
    }
}

//            Тестовое добавление.
//            serverListener.getKeeper().getHumanBeings().add(
//                    HumanBeing.builder()
//                            .name("Вася")
//                            .coordinates(Coordinates.builder().x(2L).y(3).build())
//                            .car(Car.builder().cool(true).name("My Car").build())
//                            .creationDate(ZonedDateTime.now())
//                            .hasToothpick(true)
//                            .id(2)
//                            .impactSpeed(8.0)
//                            .mood(Mood.CALM)
//                            .realHero(true)
//                            .soundtrackName("testmusic")
//                            .weaponType(WeaponType.AXE)
//                            .build());