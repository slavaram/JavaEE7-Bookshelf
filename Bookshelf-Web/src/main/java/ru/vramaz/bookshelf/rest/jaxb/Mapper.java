package ru.vramaz.bookshelf.rest.jaxb;

import ru.vramaz.bookshelf.core.entity.Book;
import ru.vramaz.bookshelf.core.entity.User;

public class Mapper {

    public static BookXml processToXml(Book book) {
        BookXml result = new BookXml();
        if (book == null) return result;
        result.setId(book.getId());
        result.setName(book.getName());
        result.setIsbn(book.getIsbn());
        result.setAuthor(book.getAuthor());
        result.setPrinted(book.getPrinted());
        result.setPublished(book.getPublished());
        result.setPublisher(book.getPublisher());
        result.setCreated(book.getCreated());
        result.setLastUpdated(book.getLastUpdated());
        return result;
    }

    public static UserXml processToXml(User user) {
        UserXml result = new UserXml();
        if (user == null) return result;
        result.setId(user.getId());
        result.setName(user.getName());
        result.setEmail(user.getEmail());
        result.setLocation(user.getLocation());
        result.setIsWriter(user.isWriter());
        result.setCreated(user.getCreated());
        result.setLastUpdated(user.getLastUpdated());
        return result;
    }

    public static Book processFromXml(BookXml book) {
        Book result = new Book();
        if (book == null) return result;
        result.setId(book.getId());
        result.setName(book.getName());
        result.setIsbn(book.getIsbn());
        result.setAuthor(book.getAuthor());
        result.setPrinted(book.getPrinted());
        result.setPublished(book.getPublished());
        result.setPublisher(book.getPublisher());
        return result;
    }

    public static User processFromXml(UserXml user) {
        User result = new User();
        if (user == null) return result;
        result.setId(user.getId());
        result.setName(user.getName());
        result.setEmail(user.getEmail());
        result.setLocation(user.getLocation());
        result.setWriter(user.getIsWriter());
        return result;
    }

}
