package com.uneeddevs.finances.controller.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.uneeddevs.finances.model.BankAccount;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class BankAccountSerialization extends JsonSerializer<BankAccount> {

    @Override
    public void serialize(BankAccount bankAccount, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(bankAccount.toBankAccountResponseDTO());
    }
}
