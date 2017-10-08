package ru.vramaz.bookshelf.core;

import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ru.vramaz.bookshelf.core.entity.Book;

public class BookServiceTest {

    private BookService bService;

    @Before
    public void init() {
        bService = new BookService();
        bService.em = Mockito.mock(EntityManager.class);
    }

    @After
    public void destroy() {
        // nothing to do
    }

    @Test(expected=IllegalArgumentException.class)
    public void createBookNullName() {
        bService.createBook(null, 1234567890, "J. K. Rowling", new Date(), new Date(), "Bloomsbury Publishing");
    }

    @Test(expected=IllegalArgumentException.class)
    public void createBookEmptyName() {
        bService.createBook("  ", 1234567890, "J. K. Rowling", new Date(), new Date(), "Bloomsbury Publishing");
    }

    @Test
    public void createBook() {
        Book book = bService.createBook("Harry Potter", 1234567890, "J. K. Rowling", new Date(), new Date(), "Bloomsbury Publishing");
        Assert.assertNotNull(book);
        Mockito.verify(bService.em).persist(book);
    }

    @Test(expected=IllegalArgumentException.class)
    public void updateBookNullId() {
        bService.updateBook(null);
    }

    @Test
    public void updateBook() {
        Book book = new Book();
        book.setId(1);
        book.setName("Harry Potter");
        bService.updateBook(book);
        Mockito.verify(bService.em).merge(book);
    }

    @Test(expected=IllegalArgumentException.class)
    public void deleteBookNull() {
        Book book = new Book();
        bService.deleteBook(book);
    }

    @Test
    public void deleteBook() {
        Book book = new Book();
        book.setId(1);
        book.setName("Harry Potter");
        bService.deleteBook(book);
        Mockito.verify(bService.em).remove(null);
    }

    /**
     * Another tests
     */

}
