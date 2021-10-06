package com.uneeddevs.finances.controller;

import com.uneeddevs.finances.dto.MovementInsertDTO;
import com.uneeddevs.finances.enums.MovementType;
import com.uneeddevs.finances.model.Movement;
import com.uneeddevs.finances.service.MovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

}
