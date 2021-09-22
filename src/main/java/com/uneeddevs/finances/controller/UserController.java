package com.uneeddevs.finances.controller;

import com.uneeddevs.finances.controller.exception.StandardError;
import com.uneeddevs.finances.dto.UserInsertDTO;
import com.uneeddevs.finances.dto.UserResponseDTO;
import com.uneeddevs.finances.dto.UserUpdateDTO;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
@Tag(name = "User", description = "User endpoints")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/page")
    @Operation(summary = "Find users paginated",
            method = "GET",
            description = "Find users by page")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Found users"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Users not found",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = StandardError.class))}
        )})
    @PageableAsQueryParam
    public ResponseEntity<Iterable<UserResponseDTO>> findAll(Pageable pageable, HttpServletRequest request) {
        log.info("Receive get search users by ip: {}", request.getRemoteAddr());
        return ResponseEntity.ok(userService.findPage(pageable).map(User::toUserResponseDTO));
    }

    @GetMapping(value = "/{uuid}")
    @Operation(summary = "Find users ",
            method = "GET",
            description = "Find user by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found users",
                    content =  {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Users not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}
            )})
    public ResponseEntity<User> findById(@PathVariable(value = "uuid") UUID uuid, HttpServletRequest request){
        log.info("Receive get user by id[{}] users by ip: {}", uuid, request.getRemoteAddr());
        return ResponseEntity.ok(userService.findById(uuid));
    }

    @PostMapping
    @Operation(summary = "Create new user",
            method = "POST",
            description = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content =  {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}
            )})
    public ResponseEntity<User> insert(@RequestBody @Valid UserInsertDTO userInsertDTO, HttpServletRequest request){
        log.info("Receive post to create user by ip: {}", request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userInsertDTO.toModel()));
    }

    @PutMapping(value = "/{uuid}")
    @Operation(summary = "Update user",
            method = "PUT",
            description = "Update user name and password by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok",
                    content =  {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}
            )})
    public ResponseEntity<User> update(@RequestBody @Valid UserUpdateDTO userUpdate,
                                       @PathVariable(value = "uuid") UUID uuid,
                                       HttpServletRequest request){
        log.info("Receive put to update user with uuid {} by ip: {}", uuid,  request.getRemoteAddr());
        return ResponseEntity.ok(userService.update(userUpdate.toModel(uuid)));
    }

}
