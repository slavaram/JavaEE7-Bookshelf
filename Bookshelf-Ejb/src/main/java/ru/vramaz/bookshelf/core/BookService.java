package ru.vramaz.bookshelf.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ru.vramaz.bookshelf.core.entity.Book;

@Stateless
public class BookService {

    @EJB
    public ImageService imgService;

    @PersistenceContext(unitName="em_bookshelf")
    public EntityManager em;

    private final static Logger LOGGER = Logger.getLogger(BookService.class.getName());

    public Book createBook(String name, int isbn, String author, Date printed, Date published, String publisher) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Book name is mandatory and cannot be empty");
        Book b = new Book();
        b.setName(name);
        b.setIsbn(isbn);
        b.setAuthor(author);
        b.setPrinted(printed);
        b.setPublished(published);
        b.setPublisher(publisher);
        return createBook(b);
    }

    public Book createBook(Book book) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null");
        try {
            em.persist(book);
            return book;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while creating a book", e);
            throw new PersistenceException("Exception occurred while creating a book", e);
        }
    }

    public void updateBook(Book book) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null");
        try {
            em.merge(book);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while updating a book", e);
            throw new PersistenceException("Exception occurred while updating a book", e);
        }
    }

    public void deleteBook(Book book) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null");
        if (book.getId() <= 0) throw new IllegalArgumentException("Invalid book Id: " + book.getId());
        try {
            em.remove(findBookById(book.getId()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while deleting a book", e);
            throw new PersistenceException("Exception occurred while deleting a book", e);
        }
    }

    public void deleteBookById(int id) {
        deleteBook(findBookById(id));
    }

    public int getBooksCount() {
        return ((Number) em.createNamedQuery("book.getCount", Integer.class).getSingleResult()).intValue();
    }

    public Book findBookById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Invalid book Id: " + id);
        try {
            return em.find(Book.class, id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while searching a book", e);
            throw new PersistenceException("Exception occurred while searching a book", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Book> findAllBooks(int fromRecord, int records) {
        int count = getBooksCount();
        if (fromRecord < 0) fromRecord = 0;
        if (records < 0) records = 5;
        if (fromRecord > count) {
            return Arrays.asList();
        }
        if (fromRecord + records > count) {
            records = count - fromRecord;
        }
        return em.createNamedQuery("book.findAll").setFirstResult(fromRecord).setMaxResults(records).getResultList();
    }

    public List<Book> findBooksLike(String name, Integer isbn, String author, Date printed,
            Date published, String publisher, int fromRecord, int records) {
        String qName, qIsbn, qAuthor, qPublisher; qName = qIsbn = qAuthor = qPublisher = null;
        if (name != null && !name.trim().isEmpty()) qName = "%" + name + "%";
        if (isbn != 0) qIsbn = "%" + isbn.toString() + "%";
        if (author != null && !author.trim().isEmpty()) qAuthor = "%" + author + "%";
        if (publisher != null && !publisher.trim().isEmpty()) qPublisher = "%" + publisher + "%";
        return findBooks(qName, qIsbn, qAuthor, null, null, qPublisher, fromRecord, records, "like");
    }

    public List<Book> findBooksExect(String name, Integer isbn, String author, Date printed,
            Date published, String publisher, int fromRecord, int records) {
        return findBooks(name, isbn.toString(), author, printed, published, publisher, fromRecord, records, "=");
    }

    @SuppressWarnings("unchecked")
    private List<Book> findBooks(String name, String isbn, String author, Date printed,
            Date published, String publisher, int fromRecord, int records, String how) {
        try {
            if ((name == null || name.trim().isEmpty()) &&
                    (isbn == null || isbn.trim().isEmpty()) &&
                    (author == null || author.trim().isEmpty()) &&
                    (printed == null || printed.compareTo(new Date()) == 0) &&
                    (published == null || published.compareTo(new Date()) == 0) &&
                    (publisher == null || publisher.trim().isEmpty())) {
                return Arrays.asList();
            }

            StringBuilder queryBuilder = new StringBuilder("select b from Book b where ");
            if (name != null && !name.trim().isEmpty()) queryBuilder.append("b.name " + how + " :name ");
            if (isbn != null && !isbn.trim().isEmpty()) queryBuilder.append("b.isbn " + how + " :isbn ");
            if (author != null && !author.trim().isEmpty()) queryBuilder.append("b.author " + how + " :author ");
            if (published != null && published.compareTo(new Date()) != 0) queryBuilder.append("b.published " + how + " :published ");
            if (publisher != null && !publisher.trim().isEmpty()) queryBuilder.append("b.publisher " + how + " :publisher ");
            if (printed != null && printed.compareTo(new Date()) != 0) queryBuilder.append("b.printed " + how + " :printed ");
            Query query = em.createQuery(queryBuilder.toString());
            if (name != null && !name.trim().isEmpty()) query.setParameter("name", name);
            if (isbn != null && !isbn.trim().isEmpty()) query.setParameter("isbn", isbn);
            if (author != null && !author.trim().isEmpty()) query.setParameter("author", author);
            if (published != null && published.compareTo(new Date()) != 0) query.setParameter("published", published);
            if (publisher != null && !publisher.trim().isEmpty()) query.setParameter("publisher", publisher);
            if (printed != null && printed.compareTo(new Date()) != 0) query.setParameter("printed", printed);
            return query.setFirstResult(fromRecord).setMaxResults(records).getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while searching a book", e);
            throw new PersistenceException("Exception occurred while searching a book", e);
        }
    }

    public InputStream getBookCover(Book book) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null");
        if (book.getId() <= 0) throw new IllegalArgumentException("Invalid book Id: " + book.getId());
        try {
            return imgService.findImg("books/" + book.getId() + ".jpg");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while loading book cover image", e);
            throw new PersistenceException("Exception occurred while loading book cover image", e);
        }
    }

    public void setBookCover(Book book, InputStream in) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null");
        if (book.getId() <= 0) throw new IllegalArgumentException("Invalid book Id: " + book.getId());
        if (in == null) throw new IllegalArgumentException("Book cover stream cannot be null");
        try {
            if (in.available() > 0)	imgService.saveImg("books/" + book.getId() + ".jpg", in);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while saving book cover image", e);
            throw new PersistenceException("Exception occurred while saving book cover image", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {}
        }
    }

    public void removeBookCover(Book book) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null");
        if (book.getId() <= 0) throw new IllegalArgumentException("Invalid book Id: " + book.getId());
        try {
            imgService.deleteImg("books/" + book.getId() + ".jpg");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while deleting book cover image", e);
            throw new PersistenceException("Exception occurred while deleting book cover image", e);
        }
    }

}
