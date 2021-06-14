package ru.dasxunya.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@SuperBuilder
@Data
public class HumanBeing implements Comparable<HumanBeing> {
    private Integer id;
    private String name;
    private Coordinates coordinates;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private java.time.ZonedDateTime creationDate;
    private Boolean realHero;
    private boolean hasToothpick;
    private Double impactSpeed;
    private String soundtrackName;
    private WeaponType weaponType;
    private Mood mood;
    private Car car;

    @Override
    public int compareTo(HumanBeing other) {
        return this.id - other.id;
    }
}
