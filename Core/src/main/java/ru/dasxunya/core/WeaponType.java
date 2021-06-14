package ru.dasxunya.core;

public enum WeaponType implements Comparable<WeaponType> {
    AXE,
    PISTOL,
    RIFLE,
    KNIFE,
    BAT;

    @Override
    public String toString() {
        return name();
    }

}
