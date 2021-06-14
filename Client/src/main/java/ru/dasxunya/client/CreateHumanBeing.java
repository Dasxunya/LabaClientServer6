package ru.dasxunya.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.dasxunya.core.*;
import ru.dasxunya.other.Keeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

@Data
@Slf4j
@AllArgsConstructor
public class CreateHumanBeing {
    private final Keeper keeper;

    public HumanBeing createHumanBeing(List<String> args) {
        if (args == null) {
            log.info("У команды add должен быть один аргумент - слово 'Human' или строка формата json. Введите команду снова.");
            return null;
        } else if (args.size() == 1 && args.get(0).equalsIgnoreCase("Human")) {
            return getHumanBeing();
        } else {
            log.info("У команды add должен быть только один аргумент - слово 'Human' или строка формата json. Введите команду снова.");
            return null;
        }
    }

    private HumanBeing getHumanBeing() {
        HumanBeing humanBeing;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            log.info("Вы выбрали добавление элемента вручную через консоль.");

            humanBeing = run(bufferedReader);

            log.info("Элемент " + humanBeing.getName() + " добавлен в коллекцию");

            return humanBeing;
        } catch (Exception e) {
            log.info("Ошибка при чтении данных");
            return null;
        }
    }


    /**
     * Run.
     *
     * @param bufferedReader the bufferedReader
     */
    public  HumanBeing run(BufferedReader bufferedReader) {
        log.info("Добавление элемента в коллекцию: ");

        return getParameterHumanBeing(bufferedReader, getParameterId());
    }


    public  HumanBeing getParameterHumanBeing(BufferedReader bufferedReader, Integer ID) {
        return HumanBeing.builder().
                id(ID).
                name(getName(bufferedReader)).
                coordinates(getCoordinates(bufferedReader)).
                creationDate(ZonedDateTime.now()).
                realHero(getRealHero(bufferedReader)).
                hasToothpick(getHasToothpick(bufferedReader)).
                impactSpeed(getImpactSpeed(bufferedReader)).
                soundtrackName(getSoundtrackName(bufferedReader)).
                weaponType(getWeaponType(bufferedReader)).
                mood(getMood(bufferedReader)).
                car(getCar(bufferedReader)).
                build();
    }

    public  Car getCar(BufferedReader bufferedReader) {
        log.info("Характеристики машины: ");
        String name = getParameterNameCar(bufferedReader);

        if (name == null) {
            return null;
        }

        boolean cool = getParameterCool(bufferedReader);

        return Car.builder().name(name).cool(cool).build();
    }

    public  boolean getParameterCool(BufferedReader bufferedReader) {
        boolean cool;
        while (true) {
            log.info("Машина хорошая?");
            String text = null;
            try {
                text = bufferedReader.readLine().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (text.equals("true")) {
                cool = true;
                break;
            }
            if (text.equals("false")) {
                cool = false;
                break;
            }

            log.info("Некорректный аргумент для Cool (Необходимо: true или false)!");
        }
        return cool;
    }

    public  String getParameterNameCar(BufferedReader bufferedReader) {
        String nameCar;
        while (true) {
            log.info("Введите название автомобиля: ");

            try {
                nameCar = bufferedReader.readLine().trim();
                if (nameCar.equals("")) {
                    nameCar = null;
                    break;
                }
                break;
            } catch (Exception exception) {
                log.info(exception.getMessage());
            }
        }
        return nameCar;
    }


    public  Mood getMood(BufferedReader bufferedReader)  {
        Mood mood = null;

        while (true) {
            log.info("Введите настроение: ");
            log.info("Возможные типы настроения: (SADNESS, LONGING, CALM, RAGE)");
            try {
                String text = bufferedReader.readLine().trim();
                if (!text.equals("")) {
                    mood = Mood.valueOf(text.toUpperCase());
                }
                break;

            } catch (IllegalArgumentException | IOException exception) {
                log.error("Некорректный аргумент! Он должен соответствовать представленному набору (SADNESS, LONGING, CALM, RAGE)!");
            }
        }
        return mood;
    }

    public  WeaponType getWeaponType(BufferedReader bufferedReader) {
        log.info("Тип оружия: ");
        WeaponType weaponType = null;

        while (true) {
            log.info("Введите тип оружия: ");
            log.info("Возможные типы оружия: (AXE, RIFLE, PISTOL, KNIFE, BAT)");
            try {
                String text = bufferedReader.readLine().trim();
                if (!text.equals("")) {
                    weaponType = WeaponType.valueOf(text.toUpperCase());
                }
                break;

            } catch (IllegalArgumentException | IOException exception) {
                log.error("Некорректный аргумент! Он должен соответствовать представленному набору (AXE, RIFLE, PISTOL, KNIFE, BAT)!");
            }
        }
        return weaponType;
    }


    public  String getSoundtrackName(BufferedReader bufferedReader) {
        log.info("Саундтрек: ");
        String soundtrackName;
        while (true) {
            log.info("Введите имя саундтрека: ");

            try {
                soundtrackName = bufferedReader.readLine().trim();
                if (soundtrackName.equals("")) {
                    throw new IllegalArgumentException("soundtrackName не может быть пустым!");
                }
                break;
            } catch (Exception exception) {
                log.error(exception.getMessage());
            }
        }
        return soundtrackName;
    }


    public  Double getImpactSpeed(BufferedReader bufferedReader) {
        log.info("Скорость удара существа: ");
        Double impactSpeed;
        while (true) {
            log.info("Введите скорость удара: ");

            try {
                String text = bufferedReader.readLine().trim();
                if (text.equals("")) {
                    impactSpeed = null;
                    break;
                }

                impactSpeed = Double.parseDouble(text);
                if (impactSpeed < 0) {
                    throw new IllegalArgumentException("impactSpeed не может быть отрицательным!");
                }
                if (impactSpeed > 10) {
                    throw new IllegalArgumentException("impactSpeed не может быть больше 10!");
                }
                break;
            } catch (NumberFormatException exception) {
                log.error("Некорректный аргумент для переменной impactSpeed (Необходимо: [0;10])!");
            } catch (IllegalArgumentException exception) {
                log.error(exception.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return impactSpeed;
    }


    public  Boolean getHasToothpick(BufferedReader bufferedReader) {
        log.info("Имеется ли у существа зубочистка: ");
        String command = null;
        Boolean hasToothpick;
        while (true) {
            log.info("Человеческое существо с зубочисткой?");
            try {
                command = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (command.equals("true")) {
                hasToothpick = true;
                break;
            }
            if (command.equals("false")) {
                hasToothpick = false;
                break;
            }
            log.info("Некорректный аргумент для hasToothpick (Необходимо: true или false)!");
        }
        return hasToothpick;
    }


    public  Boolean getRealHero(BufferedReader bufferedReader) {
        String command = null;
        log.info("Является ли человек реальным: ");

        Boolean realHero;
        while (true) {
            log.info("Человеческое существо реально?");
            try {
                command = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (command.equals("true")) {
                realHero = true;
                break;
            }
            if (command.equals("false")) {
                realHero = false;
                break;
            }
            log.error("Некорректный аргумент для realHero (Необходимо: true или false)!");
        }
        return realHero;
    }


    public  ru.dasxunya.core.Coordinates getCoordinates(BufferedReader bufferedReader) {
        log.info("Создание координат: ");

        Long x = getParameterX(bufferedReader);
        int y = getParameterY(bufferedReader);

        return Coordinates.builder().x(x).y(y).build();
    }


    public  int getParameterY(BufferedReader bufferedReader) {
        int y;

        while (true) {
            log.info("Введите y: ");

            try {
                y = Integer.parseInt(bufferedReader.readLine());
                if (y > 270) {
                    log.info("Y должен быть меньше 270!");
                    continue;
                }
                break;
            } catch (NumberFormatException | IOException exception) {
                log.error("Некорректный аргумент для переменной y!");
            }
        }
        return y;
    }


    public  Long getParameterX(BufferedReader bufferedReader) {
        Long x;
        while (true) {
            log.info("Введите x: ");

            try {
                x = Long.valueOf(bufferedReader.readLine().trim());
                if (x <= -146) {
                    log.info("X должен быть больше -146!");
                    continue;
                }
                break;
            } catch (NumberFormatException | IOException exception) {
                log.error("Некорректный аргумент для переменной х!");
            }
        }
        return x;
    }


    public  String getName(BufferedReader bufferedReader) {
        String name;

        while (true) {
            log.info("Введите имя: ");

            try {
                name = bufferedReader.readLine().trim();
                if (name.equals("")) {
                    throw new IllegalArgumentException("Введите значение для имени!");
                }
                break;
            } catch (Exception exception) {
                log.info(exception.getMessage());
            }
        }
        return name;
    }


    public  Integer getParameterId() {
        HumanBeing humanBeingWithMaxID = keeper.getHumanBeings().stream().max(Comparator.comparing(human -> human.getId())).orElse(null);

        Integer id;
        if (humanBeingWithMaxID == null) {
            id = 1;
        } else {
            id = humanBeingWithMaxID.getId() + 1;
        }

        if (id <= 0) {
            throw new IllegalArgumentException("id не может быть отрицательным или равным нулю!");
        }

        return id;
    }
}
