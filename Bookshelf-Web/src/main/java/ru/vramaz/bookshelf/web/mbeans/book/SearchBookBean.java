package ru.vramaz.bookshelf.web.mbeans.book;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ru.vramaz.bookshelf.core.BookService;
import ru.vramaz.bookshelf.core.UserService;
import ru.vramaz.bookshelf.core.entity.Book;
import ru.vramaz.bookshelf.core.entity.User;
import ru.vramaz.bookshelf.web.AuthWebFilter;

@Named
@RequestScoped
public class SearchBookBean {

    @EJB
    BookService bService;
    @EJB
    UserService uService;

    private String name;
    private int isbn;
    private String author;
    private String publisher;
    private List<Book> books;

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("id")) {
            int id = Integer.parseInt(params.get("id"));
            link(id);
        }
    }

    public String search() {
        books = bService.findBooksLike(name, isbn, author, null, null, publisher, 0, 10);
        return "SearchBook";
    }

    public void link(int bookId) {
        HttpSession hSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        User user = uService.findUserByEmail(hSession.getAttribute(AuthWebFilter.ATTR_LOGIN_NAME).toString());
        Book book = bService.findBookById(bookId);
        book.getReaders().add(user);
        bService.updateBook(book);

        try {
            ((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse()).sendRedirect("/Bookshelf/Home.xhtml");
        } catch (IOException e) {
            // TODO
        }
    }

    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIsbn() {
        return isbn;
    }
    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

}
