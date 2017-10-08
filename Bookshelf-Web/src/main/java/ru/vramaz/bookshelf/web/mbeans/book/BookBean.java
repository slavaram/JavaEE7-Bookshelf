package ru.vramaz.bookshelf.web.mbeans.book;

import java.io.InputStream;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.inject.Named;
import javax.servlet.http.Part;

import ru.vramaz.bookshelf.core.BookService;
import ru.vramaz.bookshelf.core.entity.Book;

@Named
@RequestScoped
public class BookBean {

    @EJB
    BookService bService;

    private Book book;
    private Part img;

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        FaceletContext fContext = (FaceletContext) FacesContext.getCurrentInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
        if (params.containsKey("id") && !params.get("id").isEmpty()) {
            int id = Integer.parseInt(params.get("id"));
            book = bService.findBookById(id);
        } else if (fContext.getAttribute("id") != null) {
            int id = Integer.parseInt(fContext.getAttribute("id").toString());
            book = bService.findBookById(id);
        } else {
            book = new Book();
        }
    }

    public String submit() {
        if (book.getId() == 0) {
            book = bService.createBook(this.book);
        } else {
            bService.updateBook(book);
        }
        if (img != null) {
            try {
                InputStream in = img.getInputStream();
                bService.setBookCover(book, in);
            } catch (Exception e) {
                // TODO
            }
        }
        return "Home";
    }

    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public Part getImg() {
        return img;
    }
    public void setImg(Part img) {
        this.img = img;
    }

}
