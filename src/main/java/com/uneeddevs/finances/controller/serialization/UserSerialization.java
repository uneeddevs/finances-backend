package com.uneeddevs.finances.controller.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.uneeddevs.finances.model.User;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class UserSerialization extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(user.toUserResponseDTO());
    }
}
