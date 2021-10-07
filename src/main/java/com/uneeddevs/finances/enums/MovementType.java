package com.uneeddevs.finances.enums;

import lombok.Getter;

import static com.uneeddevs.finances.util.CheckUtils.requirePositive;

@Getter
public enum MovementType {

    INPUT(1),
    OUTPUT(2);

    private static final String INVALID_VALUE_MESSAGE = "Invalid value to enum";

    private final Integer value;

    MovementType(Integer value) {
        this.value = value;
    }

    public static MovementType valueOf(int value) {
        requirePositive(value, INVALID_VALUE_MESSAGE);
        return switch (value) {
            case 1 -> INPUT;
            case 2 -> OUTPUT;
            default -> throw new IllegalArgumentException(INVALID_VALUE_MESSAGE);
        };
    }
}
