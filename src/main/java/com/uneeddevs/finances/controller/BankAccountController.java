package com.uneeddevs.finances.controller;

import com.uneeddevs.finances.controller.exception.StandardError;
import com.uneeddevs.finances.controller.exception.ValidationError;
import com.uneeddevs.finances.dto.BankAccountInsertDTO;
import com.uneeddevs.finances.dto.BankAccountResponseDTO;
import com.uneeddevs.finances.dto.BankAccountUpdateDTO;
import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/bank-accounts")
@Tag(name = "Bank Account", description = "Bank account operations")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping
    @Operation(summary = "Create new account for user",
            method = "POST",
            description = "Create new account for user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content =  {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BankAccountResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationError.class))}
            )})
    public ResponseEntity<BankAccount> insert(@RequestBody @Valid BankAccountInsertDTO bankAccountInsert, HttpServletRequest request) {
        log.info("Receive POST create bank account by ip: {}", request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(bankAccountService.save(bankAccountInsert.toModel()));
    }

    @GetMapping(value = "/{uuid}")
    @Operation(summary = "Find bank account ",
            method = "GET",
            description = "Find bank account by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found bank account",
                    content =  {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BankAccountResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bank account not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}
            )})
    public ResponseEntity<BankAccount> findById(@PathVariable(value = "uuid") UUID uuid, HttpServletRequest request){
        log.info("Receive get bank account by id[{}] by ip: {}", uuid, request.getRemoteAddr());
        return ResponseEntity.ok(bankAccountService.findById(uuid));
    }

    @GetMapping(value = "/search")
    @Operation(summary = "Find bank accounts ",
            method = "GET",
            description = "Find bank accounts by user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found bank account",
                    content =  {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BankAccountResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bank account not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}
            )})
    public ResponseEntity<List<BankAccount>> findByUser(@RequestParam(value = "user") UUID uuid, HttpServletRequest request){
        log.info("Receive get bank account by user[{}] by ip: {}", uuid, request.getRemoteAddr());
        final User user = new User(uuid);
        return ResponseEntity.ok(bankAccountService.findByUser(user));
    }

    @DeleteMapping(value = "/{uuid}")
    @Operation(summary = "Delete bank account ",
            method = "DELETE",
            description = "Delete bank account by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Bank Account deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bank account not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}
            )})
    public ResponseEntity<Void> deleteById(@PathVariable(value = "uuid") UUID uuid, HttpServletRequest request) {
        log.info("Receive DELETE bank account by id[{}] by ip: {}", uuid, request.getRemoteAddr());
        bankAccountService.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{uuid}")
    @Operation(summary = "Update bank account",
            method = "PUT",
            description = "Update bank account name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok",
                    content =  {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BankAccountResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationError.class))}
            )})
    public ResponseEntity<BankAccount> update(@RequestBody @Valid BankAccountUpdateDTO bankAccountUpdateDTO,
                                       @PathVariable(value = "uuid") UUID uuid,
                                       HttpServletRequest request){
        log.info("Receive put to update user with uuid {} by ip: {}", uuid,  request.getRemoteAddr());
        return ResponseEntity.ok(bankAccountService.update(bankAccountUpdateDTO.toModel(uuid)));
    }

}
