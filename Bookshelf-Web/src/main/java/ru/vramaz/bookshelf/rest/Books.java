package ru.vramaz.bookshelf.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ru.vramaz.bookshelf.core.BookService;
import ru.vramaz.bookshelf.core.UserService;
import ru.vramaz.bookshelf.core.entity.Book;
import ru.vramaz.bookshelf.core.entity.User;
import ru.vramaz.bookshelf.rest.jaxb.BookXml;
import ru.vramaz.bookshelf.rest.jaxb.Mapper;
import ru.vramaz.bookshelf.rest.jaxb.UserXml;

@Path("/v1.0/books/")
public class Books {

    @EJB
    BookService bService;
    @EJB
    UserService uService;

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPlain(@QueryParam("from") int from, @QueryParam("number") int number) {
        if (from < 0) from = 0;
        if (number <= 0) number = 5;
        if (number - from > 20) number = from + 20;
        StringBuilder result = new StringBuilder();
        for (Book b : bService.findAllBooks(from, number)) {
            result.append(b);
            result.append("\n");
        }
        return Response.ok(result.toString()).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response getXml(@QueryParam("from") int from, @QueryParam("number") int number) {
        if (from < 0) from = 0;
        if (number <= 0) number = 5;
        if (number - from > 20) number = from + 20;
        List<BookXml> books = new ArrayList<>();
        for (Book b : bService.findAllBooks(from, number)) {
            books.add(Mapper.processToXml(b));
        }
        return Response.ok(new GenericEntity<List<BookXml>>(books) {}).build();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createPlain(@QueryParam("name") String name,
            @QueryParam("isbn") long isbn,
            @QueryParam("author") String author,
            @QueryParam("published") Date published,
            @QueryParam("publisher") String publisher,
            @QueryParam("printed") Date printed) {
        if (name == null || name.isEmpty()) return Response.status(Status.BAD_REQUEST).entity("name is emty").build();
        if (isbn == 0) return Response.status(Status.BAD_REQUEST).entity("isbn is empty").build();
        if (author == null || author.isEmpty()) return Response.status(Status.BAD_REQUEST).entity("author is empty").build();
        Book book = new Book();
        book.setName(name);
        book.setIsbn(isbn);
        book.setAuthor(author);
        book.setPublished(published);
        book.setPublisher(publisher);
        book.setPrinted(printed);
        bService.createBook(book);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response createXml(BookXml bookXml) {
        Book book = Mapper.processFromXml(bookXml);
        bService.createBook(book);
        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPlain(@PathParam("id") int id) {
        Book book = bService.findBookById(id);
        return book == null ? Response.status(Status.NO_CONTENT).build() : Response.ok(book.toString()).build();
    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response getXml(@PathParam("id") int id) {
        Book book = bService.findBookById(id);
        return book == null ? Response.status(Status.NO_CONTENT).build() : Response.ok(Mapper.processToXml(book)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updatePlain(@PathParam("id") int id,
            @QueryParam("name") String name,
            @QueryParam("isbn") long isbn,
            @QueryParam("author") String author,
            @QueryParam("published") Date published,
            @QueryParam("publisher") String publisher,
            @QueryParam("printed") Date printed) {
        Book book = bService.findBookById(id);
        if (name != null && !name.isEmpty()) book.setName(name);
        if (isbn != 0) book.setIsbn(isbn);
        if (author != null && !author.isEmpty()) book.setAuthor(author);
        if (published != null) book.setPublished(published);
        if (publisher != null && !publisher.isEmpty()) book.setPublisher(publisher);
        if (printed != null) book.setPrinted(printed);
        bService.updateBook(book);
        return Response.ok().build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response updateXml(@PathParam("id") int id, BookXml bookXml) {
        Book book = bService.findBookById(id);
        if (book.getId() == 0) return Response.status(Status.NO_CONTENT).build();
        if (bookXml.getName() != null && !bookXml.getName().isEmpty()) book.setName(bookXml.getName());
        if (bookXml.getIsbn() != 0) book.setIsbn(bookXml.getIsbn());
        if (bookXml.getAuthor() != null && !bookXml.getAuthor().isEmpty()) book.setAuthor(bookXml.getAuthor());
        if (bookXml.getPublished() != null) book.setPublished(bookXml.getPublished());
        if (bookXml.getPublisher() != null && !bookXml.getPublisher().isEmpty()) book.setPublisher(bookXml.getPublisher());
        if (bookXml.getPrinted() != null) book.setPrinted(bookXml.getPrinted());
        bService.updateBook(book);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    public Response delete(@PathParam("id") int id) {
        Book book = bService.findBookById(id);
        if (book == null) return Response.status(Status.NO_CONTENT).build();
        bService.removeBookCover(book);
        bService.deleteBookById(id);
        return Response.ok().build();
    }

    @GET
    @Path("{id}/readers")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getReadersPlain(@PathParam("id") int id, @QueryParam("from") int from, @QueryParam("number") int number) {
        Book book = bService.findBookById(id);
        if (book == null) return Response.status(Status.NO_CONTENT).build();
        if (from < 0) from = 0;
        if (number <= 0) number = 5;
        if (number - from > 20) number = from + 20;
        if (book.getReaders().size() <= from) return Response.status(Status.NO_CONTENT).build();
        StringBuilder result = new StringBuilder();
        for (int i = from; i < from + number && i < book.getReaders().size(); i++) {
            result.append(book.getReaders().get(i));
            result.append("\n");
        }
        return Response.ok(result.toString()).build();
    }

    @GET
    @Path("{id}/readers")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response getReadersXml(@PathParam("id") int id, @QueryParam("from") int from, @QueryParam("number") int number) {
        Book book = bService.findBookById(id);
        if (book == null) return Response.status(Status.NO_CONTENT).build();
        if (from < 0) from = 0;
        if (number <= 0) number = 5;
        if (number - from > 20) number = from + 20;
        if (book.getReaders().size() <= from) return Response.status(Status.NO_CONTENT).build();
        List<UserXml> readers = new ArrayList<>();
        for (int i = from; i < from + number && i < book.getReaders().size(); i++) {
            readers.add(Mapper.processToXml(book.getReaders().get(i)));
        }
        return Response.ok(new GenericEntity<List<UserXml>>(readers) {}).build();
    }

    @PUT
    @Path("{id}/readers")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    public Response addReader(@PathParam("id") int bookId, @QueryParam("id") int userId) {
        Book book = bService.findBookById(bookId);
        User user = uService.findUserById(userId);
        if (book.getId() == 0 || user.getId() == 0) return Response.status(Status.NO_CONTENT).build();
        book.getReaders().add(user);
        bService.updateBook(book);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}/readers")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    public Response deleteReader(@PathParam("id") int bookId, @QueryParam("id") int readerId) {
        Book book = bService.findBookById(bookId);
        User reader = uService.findUserById(readerId);
        if (book.getId() == 0 || reader.getId() == 0) return Response.status(Status.NO_CONTENT).build();
        Iterator<User> iter = book.getReaders().iterator();
        while (iter.hasNext()) {
            if (iter.next().getId() == reader.getId()) {
                iter.remove();
                break;
            }
        }
        bService.updateBook(book);
        return Response.ok().build();
    }

    @GET
    @Path("{id}/authors")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAuthorsPlain(@PathParam("id") int id) {
        Book book = bService.findBookById(id);
        return book == null ? Response.status(Status.NO_CONTENT).build() : Response.ok(book.getAuthor()).build();
    }

    @GET
    @Path("{id}/authors")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response getAuthorsXml(@PathParam("id") int id) {
        Book book = bService.findBookById(id);
        return book == null ? Response.status(Status.NO_CONTENT).build() :
            Response.ok("<bks:authors xmlns:bks='http://ru.vramaz.bookshelf'>" + book.getAuthor() + "</bks:authors>").build();
    }

}
