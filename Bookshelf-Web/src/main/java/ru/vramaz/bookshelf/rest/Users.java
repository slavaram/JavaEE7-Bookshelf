package ru.vramaz.bookshelf.rest;

import java.util.ArrayList;
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

import ru.vramaz.bookshelf.auth.AuthService;
import ru.vramaz.bookshelf.auth.InvalidPasswordException;
import ru.vramaz.bookshelf.core.BookService;
import ru.vramaz.bookshelf.core.UserService;
import ru.vramaz.bookshelf.core.entity.Book;
import ru.vramaz.bookshelf.core.entity.User;
import ru.vramaz.bookshelf.rest.jaxb.BookXml;
import ru.vramaz.bookshelf.rest.jaxb.Mapper;
import ru.vramaz.bookshelf.rest.jaxb.UserXml;

@Path("/v1.0/users/")
public class Users {

    @EJB
    BookService bService;
    @EJB
    UserService uService;
    @EJB
    AuthService authService;

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPlain(@QueryParam("from") int from, @QueryParam("number") int number) {
        if (from < 0) from = 0;
        if (number <= 0) number = 5;
        if (number - from > 20) number = from + 20;
        StringBuilder result = new StringBuilder();
        for (User u : uService.findAllUsers(from, number)) {
            result.append(u);
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
        List<UserXml> users = new ArrayList<>();
        for (User u : uService.findAllUsers(from, number)) {
            users.add(Mapper.processToXml(u));
        }
        return Response.ok(new GenericEntity<List<UserXml>>(users) {}).build();
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createPlain(@QueryParam("name") String name,
            @QueryParam("email") String email,
            @QueryParam("password") String password,
            @QueryParam("location") String location) {
        if (name == null || name.isEmpty()) return Response.status(Status.BAD_REQUEST).entity("name is emty").build();
        if (email == null || email.isEmpty()) return Response.status(Status.BAD_REQUEST).entity("email is empty").build();
        if (password == null || password.isEmpty()) return Response.status(Status.BAD_REQUEST).entity("password is empty").build();
        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPasswordHash(authService.passToHash(password));
            user.setLocation(location);
            uService.createUser(user);
            return Response.ok().build();
        } catch (InvalidPasswordException e) {
            return Response.status(Status.BAD_REQUEST).entity("invalid password").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response createXml(@QueryParam("password") String password, UserXml userXml) {
        if (password == null || password.isEmpty()) return Response.status(Status.BAD_REQUEST).entity("password is empty").build();
        try {
            User user = Mapper.processFromXml(userXml);
            user.setPasswordHash(authService.passToHash(password));
            uService.createUser(user);
            return Response.ok().build();
        } catch (InvalidPasswordException e) {
            return Response.status(Status.BAD_REQUEST).entity("invalid password").build();
        }
    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String getPlain(@PathParam("id") int id) {
        User user = uService.findUserById(id);
        return user == null ? "" : user.toString();
    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response getXml(@PathParam("id") int id) {
        User user = uService.findUserById(id);
        return user == null ? Response.status(Status.NO_CONTENT).build() : Response.ok(Mapper.processToXml(user)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updatePlain(@PathParam("id") int id,
            @QueryParam("name") String name,
            @QueryParam("email") String email,
            @QueryParam("password") String password,
            @QueryParam("location") String location) {
        try {
            User user = uService.findUserById(id);
            if (name != null && !name.isEmpty()) user.setName(name);
            if (email != null && !email.isEmpty()) user.setEmail(email);
            if (password != null && !password.isEmpty()) user.setPasswordHash(authService.passToHash(password));
            if (location != null && !location.isEmpty()) user.setLocation(location);
            uService.updateUser(user);
            return Response.ok().build();
        } catch (InvalidPasswordException e) {
            return Response.status(Status.BAD_REQUEST).entity("invalid password").build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response updateXml(@PathParam("id") int id, @QueryParam("password") String password, UserXml userXml) {
        User user = uService.findUserById(id);
        try {
            if (user.getId() == 0) return Response.status(Status.NO_CONTENT).build();
            if (userXml.getName() != null && !userXml.getName().isEmpty()) user.setName(userXml.getName());
            if (userXml.getEmail() != null && !userXml.getEmail().isEmpty()) user.setEmail(userXml.getEmail());
            if (password != null && !password.isEmpty()) user.setPasswordHash(authService.passToHash(password));
            if (userXml.getLocation() != null && !userXml.getLocation().isEmpty()) user.setLocation(userXml.getLocation());
            uService.updateUser(user);
            return Response.ok().build();
        } catch (InvalidPasswordException e) {
            return Response.status(Status.BAD_REQUEST).entity("invalid password").build();
        }
    }

    @DELETE
    @Path("{id}")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    public Response delete(@PathParam("id") int id) {
        User user = uService.findUserById(id);
        if (user == null) return Response.status(Status.NO_CONTENT).build();
        uService.removeUserLogo(user);
        uService.deleteUserById(id);
        return Response.ok().build();
    }

    @GET
    @Path("{id}/readed")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String getReadedBooksPlain(@PathParam("id") int id, @QueryParam("from") int from, @QueryParam("number") int number) {
        User user = uService.findUserById(id);
        if (user == null) return "";
        if (from < 0) from = 0;
        if (number <= 0) number = 5;
        if (number - from > 20) number = from + 20;
        if (user.getReadedBooks().size() <= from) return "";
        StringBuilder result = new StringBuilder();
        for (int i = from; i < from + number && i < user.getReadedBooks().size(); i++) {
            result.append(user.getReadedBooks().get(i));
            result.append("\n");
        }
        return result.toString();
    }

    @GET
    @Path("{id}/readed")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response getReadersXml(@PathParam("id") int id, @QueryParam("from") int from, @QueryParam("number") int number) {
        User user = uService.findUserById(id);
        if (user == null) return Response.status(Status.NO_CONTENT).build();
        if (from < 0) from = 0;
        if (number <= 0) number = 5;
        if (number - from > 20) number = from + 20;
        if (user.getReadedBooks().size() <= from) return Response.status(Status.NO_CONTENT).build();
        List<BookXml> readed = new ArrayList<>();
        for (int i = from; i < from + number && i < user.getReadedBooks().size(); i++) {
            readed.add(Mapper.processToXml(user.getReadedBooks().get(i)));
        }
        return Response.ok(new GenericEntity<List<BookXml>>(readed) {}).build();
    }

    @PUT
    @Path("{id}/readed")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    public Response addReaded(@PathParam("id") int userId, @QueryParam("id") int bookId) {
        Book book = bService.findBookById(bookId);
        User user = uService.findUserById(userId);
        if (book.getId() == 0 || user.getId() == 0) return Response.status(Status.NO_CONTENT).build();
        book.getReaders().add(user);
        bService.updateBook(book);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}/readed")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML})
    public Response deleteReaded(@PathParam("id") int bookId, @QueryParam("id") int userId) {
        Book book = bService.findBookById(bookId);
        User reader = uService.findUserById(userId);
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
    @Path("{id}/written")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String getWrittenBooksPlain(@PathParam("id") int id) {
        User user = uService.findUserById(id);
        return user == null ? "" : String.valueOf(user.isWriter());
    }

    @GET
    @Path("{id}/written")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response getAuthorsXml(@PathParam("id") int id) {
        return Response.ok("<bks:written xmlns:bks='http://ru.vramaz.bookshelf'>...</bks:written>").build();
    }


}
