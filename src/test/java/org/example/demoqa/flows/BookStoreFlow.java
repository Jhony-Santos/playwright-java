package org.example.demoqa.flows;

import com.microsoft.playwright.Page;
import org.example.demoqa.pages.BookStoreApplicationPage;
import org.example.demoqa.pages.HomePage;

public final class BookStoreFlow {

    private BookStoreFlow() {}

    public static void addBookToUser(
            Page page,
            String user,
            String pass,
            String bookName
    ) {
        BookStoreApplicationPage app = new HomePage(page)
                .gotoHome()
                .openBookStoreApplication();

        app.assertLoaded();

        app.openLogin()
                .login(user, pass);

        app.openBookStore()
                .searchBook(bookName)
                .openBook(bookName)
                .addToCollection();

        app.openProfile()
                .assertBookPresent(bookName);
    }
}