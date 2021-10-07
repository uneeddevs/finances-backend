package com.uneeddevs.finances.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uneeddevs.finances.dto.BankAccountInsertDTO;
import com.uneeddevs.finances.dto.BankAccountResponseDTO;
import com.uneeddevs.finances.dto.BankAccountUpdateDTO;
import com.uneeddevs.finances.mocks.BankAccountInsertDTOMock;
import com.uneeddevs.finances.mocks.BankAccountMock;
import com.uneeddevs.finances.mocks.BankAccountUpdateDTOMock;
import com.uneeddevs.finances.mocks.UserMock;
import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.service.BankAccountService;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankAccountController.class)
class BankAccountControllerTest {

    private final String BASE_PATH = "/bank-accounts";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankAccountService bankAccountService;

    @Test
    void testInsertNewBankAccountExpectedSuccess() throws Exception{

        BankAccount bankAccountMock = BankAccountMock.mock();
        BankAccountInsertDTO bankAccountInsertDTOMock = BankAccountInsertDTOMock.mock();
        when(bankAccountService.save(any(BankAccount.class))).thenReturn(bankAccountMock);

        mockMvc.perform(post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankAccountInsertDTOMock)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(bankAccountMock.toBankAccountResponseDTO()), false));

        verify(bankAccountService).save(any(BankAccount.class));

    }

    @ParameterizedTest
    @MethodSource(value = "badRequestCreateBankAccountProvider")
    void testInsertNewBankAccountExpectedBadRequest(String name, UUID userId, BigDecimal initialBalance) throws Exception {
        BankAccountInsertDTO bankAccountInsertDTOMock = BankAccountInsertDTOMock.mock(name, userId, initialBalance);
        mockMvc.perform(post(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankAccountInsertDTOMock)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(bankAccountService, never()).save(any(BankAccount.class));
    }

    private static Stream<Arguments> badRequestCreateBankAccountProvider() {
        return Stream.of(
                Arguments.of("", UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), BigDecimal.valueOf(0d)),
                Arguments.of(null, UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), BigDecimal.valueOf(0d)),
                Arguments.of("Pretty Name", UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), BigDecimal.valueOf(-1d)),
                Arguments.of("Pretty Name", null, BigDecimal.valueOf(0d)),
                Arguments.of("Pretty Name", UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), null)
        );
    }

    @Test
    void testInsertNewBankAccountWithNonexistentUserExpectedNotFound() throws Exception{

        BankAccountInsertDTO bankAccountInsertDTOMock = BankAccountInsertDTOMock.mock();
        when(bankAccountService.save(any(BankAccount.class))).thenThrow(new NoResultException("No user founded"));
        mockMvc.perform(post(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankAccountInsertDTOMock)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bankAccountService).save(any(BankAccount.class));
    }

    @Test
    void testInsertNewBankAccountExpectedInternalServerError() throws Exception{

        BankAccountInsertDTO bankAccountInsertDTOMock = BankAccountInsertDTOMock.mock();
        when(bankAccountService.save(any(BankAccount.class))).thenThrow(new RuntimeException("error"));
        mockMvc.perform(post(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankAccountInsertDTOMock)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(bankAccountService).save(any(BankAccount.class));
    }

    @Test
    void testTestFindByIdExpectedSuccess() throws Exception {
        BankAccount bankAccountMock = BankAccountMock.mock();
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        UUID uuid = UUID.fromString(id);
        when(bankAccountService.findById(uuid)).thenReturn(bankAccountMock);
        mockMvc.perform(get(BASE_PATH + "/{uuid}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(bankAccountMock.toBankAccountResponseDTO()), false));

        verify(bankAccountService).findById(uuid);
    }

    @Test
    void testTestFindByIdExpectedNotFound() throws Exception {
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        UUID uuid = UUID.fromString(id);
        when(bankAccountService.findById(uuid)).thenThrow(new NoResultException("No bank account founded"));
        mockMvc.perform(get(BASE_PATH + "/{uuid}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(bankAccountService).findById(uuid);
    }

    @Test
    void testTestFindByIdWithInvalidUUIDExpectedBadRequest() throws Exception {
        String id = "3fa85f64-5717-3f66afa6";
        mockMvc.perform(get(BASE_PATH + "/{uuid}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateBankAccountExpectedSuccess() throws Exception {
        BankAccountUpdateDTO bankAccountUpdateDTO = BankAccountUpdateDTOMock.mock();
        BankAccount bankAccountMock = BankAccountMock.mock();
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        when(bankAccountService.update(any(BankAccount.class))).thenReturn(bankAccountMock);

        mockMvc.perform(put(BASE_PATH + "/{uuid}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankAccountUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(bankAccountMock.toBankAccountResponseDTO()), false));

        verify(bankAccountService).update(any(BankAccount.class));
    }

    @Test
    void testUpdateBankAccountInvalidUUIDExpectedBadRequest() throws Exception {
        BankAccountUpdateDTO bankAccountUpdateDTO = BankAccountUpdateDTOMock.mock();
        String id = "3fa85f64-5717-45f66afa6";

        mockMvc.perform(put(BASE_PATH + "/{uuid}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankAccountUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateBankAccountExpectedBadRequest() throws Exception {
        BankAccountUpdateDTO bankAccountUpdateDTO = BankAccountUpdateDTOMock.mock(null);
        BankAccount bankAccountMock = BankAccountMock.mock();
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        when(bankAccountService.update(any(BankAccount.class))).thenReturn(bankAccountMock);

        mockMvc.perform(put(BASE_PATH + "/{uuid}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankAccountUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(bankAccountService, never()).update(any(BankAccount.class));
    }

    @Test
    void testUpdateBankAccountExpectedNotFound() throws Exception {
        BankAccountUpdateDTO bankAccountUpdateDTO = BankAccountUpdateDTOMock.mock();
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        when(bankAccountService.update(any(BankAccount.class))).thenThrow(new NoResultException("No bank account with id"));

        mockMvc.perform(put(BASE_PATH + "/{uuid}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankAccountUpdateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(bankAccountService).update(any(BankAccount.class));
    }

    @Test
    void testDeleteByIdExpectedSuccess() throws Exception {
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        mockMvc.perform(delete(BASE_PATH + "/{uuid}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteByIdExpectedNotFound() throws Exception {
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        when(bankAccountService.deleteById(UUID.fromString(id))).thenThrow(new NoResultException("Not found"));
        mockMvc.perform(delete(BASE_PATH + "/{uuid}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteByIdExpectedInternalServerError() throws Exception {
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        when(bankAccountService.deleteById(UUID.fromString(id))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(delete(BASE_PATH + "/{uuid}", id))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testTestFindByUserExpectedSuccess() throws Exception {
        BankAccount bankAccountMock = BankAccountMock.mock();
        List<BankAccount> bankAccountList = Collections.singletonList(bankAccountMock);
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        User user = UserMock.mock(false);
        when(bankAccountService.findByUser(user)).thenReturn(bankAccountList);
        final List<BankAccountResponseDTO> response = Collections.singletonList(bankAccountMock.toBankAccountResponseDTO());
        mockMvc.perform(get(BASE_PATH + "/search?user={userid}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response), false));

        verify(bankAccountService).findByUser(user);
    }

    @Test
    void testTestFindByUserExpectedNotFound() throws Exception {
        String id = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        User user = UserMock.mock(false);
        when(bankAccountService.findByUser(user)).thenThrow(new NoResultException("No bank account founded"));
        mockMvc.perform(get(BASE_PATH + "/search?user={userid}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(bankAccountService).findByUser(user);
    }

    @Test
    void testTestFindByUserExpectedBadRequest() throws Exception {
        String id = "3fa85f64c-2c963f66afa6";
        User user = UserMock.mock(false);
        when(bankAccountService.findByUser(user)).thenThrow(new NoResultException("No bank account founded"));
        mockMvc.perform(get(BASE_PATH + "/search?user={userid}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(bankAccountService, never()).findByUser(user);
    }

}
