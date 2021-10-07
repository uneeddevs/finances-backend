package com.uneeddevs.finances.controller;

import com.uneeddevs.finances.dto.MovementInsertDTO;
import com.uneeddevs.finances.enums.MovementType;
import com.uneeddevs.finances.model.Movement;
import com.uneeddevs.finances.service.MovementService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/movements")
public class MovementController {

    private final MovementService movementService;

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<Movement> findById(@PathVariable(value = "uuid") UUID uuid, HttpServletRequest request) {
        log.info("Receive GET to search movement with uuid {} by ip: {}", uuid,  request.getRemoteAddr());
        return ResponseEntity.ok(movementService.findById(uuid));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Movement>> findByPeriod(@RequestParam(value = "bankAccount") @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID bankAccount,
                                                       @RequestParam(value = "start") @Schema(example = "2021-10-06T20:43:03Z") String startString,
                                                       @RequestParam(value = "end") @Schema(example = "2021-10-05T20:43:03Z") String endString) {
        final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_DATE_TIME;
        try {
            final LocalDateTime start = dateTimeFormat.parse(startString, LocalDateTime::from);
            final LocalDateTime end = dateTimeFormat.parse(endString, LocalDateTime::from);
            return ResponseEntity.ok(movementService.findByPeriodAndBankAccount(start, end, bankAccount));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @PostMapping(value = "/input")
    public ResponseEntity<Movement> inputMovement(@Valid @RequestBody MovementInsertDTO movementInsertDTO, HttpServletRequest request) {
        log.info("Receive POST to create input movement {} by ip: {}", movementInsertDTO, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(movementService.save(movementInsertDTO.toModel(MovementType.INPUT)));
    }

    @PostMapping(value = "/output")
    public ResponseEntity<Movement> outputMovement(@Valid @RequestBody MovementInsertDTO movementInsertDTO, HttpServletRequest request) {
        log.info("Receive POST to create input movement {} by ip: {}", movementInsertDTO, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(movementService.save(movementInsertDTO.toModel(MovementType.OUTPUT)));
    }

    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID uuid, HttpServletRequest request) {
        log.info("Receive DELETE to delete movement by id {} by ip: {}", uuid, request.getRemoteAddr());
        movementService.deleteMovementById(uuid);
        return ResponseEntity.noContent().build();
    }


}
