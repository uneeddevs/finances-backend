package com.uneeddevs.finances.service.validation;

import com.uneeddevs.finances.controller.exception.FieldMessage;
import com.uneeddevs.finances.dto.UserInsertDTO;
import com.uneeddevs.finances.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserInsertValidator implements ConstraintValidator<UserInsert, UserInsertDTO> {

    private final UserService userService;

    @Override
    public boolean isValid(UserInsertDTO userInsertDTO, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        try {
            userService.findByEmail(userInsertDTO.getEmail());
            list.add(new FieldMessage("email", "Email already registered"));
        } catch (Exception e){
            //ignore exception because email is not registered
        }

        for(FieldMessage fieldMessage: list){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(fieldMessage.message())
                    .addPropertyNode(fieldMessage.fieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }
}
