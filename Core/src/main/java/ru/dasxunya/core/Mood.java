package ru.dasxunya.core;

public enum Mood {
    SADNESS,
    LONGING,
    CALM,
    RAGE;

    @Override
    public String toString() {
        return name();
    }

}
