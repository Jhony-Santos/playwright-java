package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.BookStoreApplicationPage;
import org.junit.jupiter.api.Test;

public class BookStoreFlowTest extends BaseTest {

    @Test
    void shouldLoginAndAddBookToCollection() {

        String user = "SEU_USUARIO_REAL";
        String password = "SUA_SENHA_REAL";
        String bookName = "Git Pocket Guide";

        BookStoreApplicationPage app = new org.example.demoqa.pages.HomePage(page)
                .gotoHome()
                .openBookStoreApplication();

        app.assertLoaded();

        // Login
        app.openLogin()
                .login(user, password);

        // Ir para Book Store e adicionar livro
        app.openBookStore()
                .searchBook(bookName)
                .openBook(bookName)
                .addToCollection();

        // Validar no Profile
        app.openProfile()
                .assertBookPresent(bookName);
    }
}