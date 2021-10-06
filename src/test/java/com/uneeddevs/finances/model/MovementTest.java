package com.uneeddevs.finances.model;

import com.uneeddevs.finances.enums.MovementType;
import com.uneeddevs.finances.mocks.BankAccountMock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovementTest {

    @ParameterizedTest
    @MethodSource(value = "creationMovementSuccess")
    void testCreateInstanceExpectedSuccess(MovementType movementType, BigDecimal value, BankAccount bankAccount) throws Exception {
        assertDoesNotThrow(() -> new Movement(movementType, value, bankAccount),
                "Cannot throws nothing");
    }

    private static Stream<Arguments> creationMovementSuccess() throws Exception {
        return Stream.of(
                Arguments.of(MovementType.INPUT, BigDecimal.valueOf(1.0), BankAccountMock.mock()),
                Arguments.of(MovementType.INPUT, BigDecimal.valueOf(10), BankAccountMock.mock()),
                Arguments.of(MovementType.OUTPUT, BigDecimal.valueOf(1.0), BankAccountMock.mock()),
                Arguments.of(MovementType.OUTPUT, BigDecimal.valueOf(10.0), BankAccountMock.mock())
        );
    }

    @ParameterizedTest
    @MethodSource(value = "creationMovementIllegalArgumentException")
    void testCreateInstanceExpectedIllegalArgumentException(MovementType movementType, BigDecimal value, BankAccount bankAccount) throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new Movement(movementType, value, bankAccount),
                "Cannot throws nothing");
    }

    private static Stream<Arguments> creationMovementIllegalArgumentException() throws Exception {
        return Stream.of(
                Arguments.of(null, BigDecimal.valueOf(1.0), BankAccountMock.mock()),
                Arguments.of(MovementType.INPUT, null, BankAccountMock.mock()),
                Arguments.of(MovementType.OUTPUT, BigDecimal.valueOf(1.0), null),
                Arguments.of(MovementType.OUTPUT, BigDecimal.valueOf(-1), BankAccountMock.mock()),
                Arguments.of(MovementType.OUTPUT, BigDecimal.valueOf(-1), null)
        );
    }
}
