package com.uneeddevs.finances.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uneeddevs.finances.dto.MovementInsertDTO;
import com.uneeddevs.finances.dto.MovementResponseDTO;
import com.uneeddevs.finances.enums.MovementType;
import com.uneeddevs.finances.mocks.MovementMock;
import com.uneeddevs.finances.model.Movement;
import com.uneeddevs.finances.service.MovementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovementController.class)
class MovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovementService movementService;

    private static final String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
    private static final String BASE_PATH = "/movements";

    @Test
    void testFindMovementByIdExpectedOkStatus() throws Exception {

        Movement movement = MovementMock.mock(false);
        UUID uuid = UUID.fromString(id);
        when(movementService.findById(uuid)).thenReturn(movement);

        mockMvc.perform(get(BASE_PATH + "/{uuid}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(movement.toMovementResponseDTO())));

        verify(movementService).findById(uuid);
    }

    @Test
    void testFindMovementByIdExpectedNotFoundStatus() throws Exception {
        UUID uuid = UUID.fromString(id);

        when(movementService.findById(uuid)).thenThrow(new NoResultException("No movement"));

        mockMvc.perform(get(BASE_PATH + "/{uuid}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(movementService).findById(uuid);
    }

    @ParameterizedTest
    @MethodSource(value = "createMovementExpectedSuccess")
    void createInputMovementExpectedIsCreatedStatus(MovementInsertDTO movementInsertDTO) throws Exception {

        final Movement movement = movementInsertDTO.toModel(MovementType.INPUT);
        when(movementService.save(movement)).thenReturn(movement);

        mockMvc.perform(post(BASE_PATH + "/input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movementInsertDTO)))
                .andExpect(status().isCreated());
        verify(movementService).save(any(Movement.class));
    }

    @ParameterizedTest
    @MethodSource(value = "createMovementExpectedSuccess")
    void createOutputMovementExpectedIsCreatedStatus(MovementInsertDTO movementInsertDTO) throws Exception {

        final Movement movement = movementInsertDTO.toModel(MovementType.INPUT);
        when(movementService.save(movement)).thenReturn(movement);
        mockMvc.perform(post(BASE_PATH + "/output")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movementInsertDTO)))
                .andExpect(status().isCreated());
        verify(movementService).save(any(Movement.class));

    }

    private static Stream<Arguments> createMovementExpectedSuccess() {
        return Stream.of(
                Arguments.of(new MovementInsertDTO(BigDecimal.TEN, UUID.randomUUID())),
                Arguments.of(new MovementInsertDTO(BigDecimal.ONE, UUID.randomUUID())),
                Arguments.of(new MovementInsertDTO(BigDecimal.valueOf(25d), UUID.randomUUID()))
        );
    }

    @ParameterizedTest
    @MethodSource(value = "createMovementExpectedBadRequest")
    void createOutputMovementExpectedBadRequestStatus(MovementInsertDTO movementInsertDTO) throws Exception {

        mockMvc.perform(post(BASE_PATH + "/output")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movementInsertDTO)))
                .andExpect(status().isBadRequest());

        verify(movementService, never()).save(any(Movement.class));

    }

    private static Stream<Arguments> createMovementExpectedBadRequest() {
        return Stream.of(
                Arguments.of(new MovementInsertDTO(null, UUID.randomUUID())),
                Arguments.of(new MovementInsertDTO(BigDecimal.valueOf(-1), UUID.randomUUID())),
                Arguments.of(new MovementInsertDTO(BigDecimal.valueOf(0), UUID.randomUUID())),
                Arguments.of(new MovementInsertDTO(BigDecimal.valueOf(10), null))
        );
    }

    @Test
    void testDeleteByIdExpectedNoContentStatus() throws Exception {

        UUID uuid = UUID.fromString(id);
        doNothing().when(movementService).deleteMovementById(uuid);

        mockMvc.perform(delete(BASE_PATH + "/{uuid}", id))
                .andExpect(status().isNoContent());

        verify(movementService).deleteMovementById(uuid);
    }

    @Test
    void testDeleteByIdExpectedNotFoundStatus() throws Exception {

        UUID uuid = UUID.fromString(id);
        doThrow(new NoResultException("No movement")).when(movementService).deleteMovementById(uuid);

        mockMvc.perform(delete(BASE_PATH + "/{uuid}", id))
                .andExpect(status().isNotFound());

        verify(movementService).deleteMovementById(uuid);
    }

    @Test
    void testDeleteByIdExpectedBadRequestStatus() throws Exception {

        String id = "3fa85f64-6afa6";
        doThrow(new NoResultException("No movement")).when(movementService).deleteMovementById(any(UUID.class));

        mockMvc.perform(delete(BASE_PATH + "/{uuid}", id))
                .andExpect(status().isBadRequest());

        verify(movementService, never()).deleteMovementById(any(UUID.class));
    }

    @Test
    void testFindByPeriodExpectedIsOkStatus() throws Exception {
        String start = "2021-10-06T20:43:03Z";
        String end = "2021-10-05T20:43:03Z";
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        final List<Movement> movements = Collections
                .singletonList(MovementMock.mock(false));

        final List<MovementResponseDTO> movementResponseDTOS = movements
                .stream()
                .map(Movement::toMovementResponseDTO).collect(Collectors.toList());

        when(movementService
                .findByPeriodAndBankAccount(any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        any(UUID.class)))
                .thenReturn(movements);

        mockMvc.perform(get(BASE_PATH + "/search?start={start}&end={end}&bankAccount={id}", start, end, id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(movementResponseDTOS)));

        verify(movementService).findByPeriodAndBankAccount(any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(UUID.class));
    }

    @Test
    void testFindByPeriodExpectedNotFoundStatus() throws Exception {
        String start = "2021-10-06T20:43:03Z";
        String end = "2021-10-05T20:43:03Z";
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";

        when(movementService
                .findByPeriodAndBankAccount(any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        any(UUID.class)))
                .thenThrow(new NoResultException("No Movements"));

        mockMvc.perform(get(BASE_PATH + "/search?start={start}&end={end}&bankAccount={id}", start, end, id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @ParameterizedTest
    @MethodSource(value = "badRequestGetSource")
    void testFindByPeriodExpectedBadRequestStatus(String start, String end, String id) throws Exception {

        mockMvc.perform(get(BASE_PATH + "/search?start={start}&end={end}&bankAccount={id}", start, end, id))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(movementService, never()).findByPeriodAndBankAccount(any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(UUID.class));
    }

    private static Stream<Arguments> badRequestGetSource() {
        return Stream.of(
                Arguments.of("2021-10-0620:43:03Z", "2021-10-05T20:43:03Z", "3fa85f64-5717-4562-b3fc-2c963f66afa6"),
                Arguments.of("2021-10-06T20:43:03Z", "2021-:43:03Z", "3fa85f64-5717-4562-b3fc-2c963f66afa6"),
                Arguments.of("2021-10-06T20:43:03Z", "2021-10-05T20:43:03Z", "3fa85f64-3f66afa6")
        );
    }

}
