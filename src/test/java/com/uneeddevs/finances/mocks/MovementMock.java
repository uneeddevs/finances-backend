package com.uneeddevs.finances.mocks;

import com.uneeddevs.finances.enums.MovementType;
import com.uneeddevs.finances.model.Movement;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.UUID;

public class MovementMock {

    private static final UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

    private MovementMock() {}

    public static Movement mock(boolean isInput) throws Exception {
        Class<Movement> clazz = Movement.class;
        Field idField = clazz.getDeclaredField("id");
        idField.setAccessible(true);
        MovementType movementType = isInput ? MovementType.INPUT : MovementType.OUTPUT;
        Movement movement = new Movement(movementType, BigDecimal.TEN, BankAccountMock.mock());
        idField.set(movement, uuid);
        return  movement;
    }
}
