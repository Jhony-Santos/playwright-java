package org.example.demoqa.data;

import org.example.demoqa.models.TextBoxData;

public final class TextBoxDataFactory {
    private TextBoxDataFactory() {}

    public static TextBoxData valid() {
        return new TextBoxData(
                "Jhonatan Santos",
                "jhony.jpn@gmail.com",
                "Rua das Flores, 123",
                "Curitiba - PR"
        );
    }

    public static TextBoxData anotherValid() {
        return new TextBoxData(
                "Rafael Santos",
                "rafael.santos@gmail.com",
                "Av. Brasil, 456",
                "São Paulo - SP"
        );
    }

    public static TextBoxData invalidEmail() {
        return new TextBoxData(
                "Fulano da Silva",
                "email-invalido",
                "Endereço Teste",
                "Cidade Teste"
        );
    }
}
