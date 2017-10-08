package ru.vramaz.bookshelf.core.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="Book")
@NamedQueries({
    @NamedQuery(name="book.findAll", query="select b from Book b order by b.lastUpdated"),
    @NamedQuery(name="book.getCount", query="select count(b) from Book b")
})
public class Book {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="isbn")
    private long isbn;

    @Column(name="author")
    private String author;

    @Temporal(TemporalType.DATE)
    @Column(name="printed")
    private Date printed;

    @Temporal(TemporalType.DATE)
    @Column(name="published")
    private Date published;

    @Column(name="publisher")
    private String publisher;

    @Temporal(TemporalType.DATE)
    @Column(name="created")
    private Date created;

    @Temporal(TemporalType.DATE)
    @Column(name="last_updated")
    private Date lastUpdated;

    @ManyToMany
    @JoinTable(
            name="ReadM2M",
            joinColumns=@JoinColumn(name="book_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="user_id", referencedColumnName="id"))
    private List<User> readers;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getIsbn() {
        return isbn;
    }
    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public Date getPrinted() {
        return printed;
    }
    public void setPrinted(Date printed) {
        this.printed = printed;
    }
    public Date getPublished() {
        return published;
    }
    public void setPublished(Date published) {
        this.published = published;
    }
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public Date getCreated() {
        return created;
    }
    public Date getLastUpdated() {
        return lastUpdated;
    }
    public List<User> getReaders() {
        return readers;
    }
    public void setReaders(List<User> readers) {
        this.readers = readers;
    }

    @Override
    public String toString() {
        return "Book [" +
                "id: " + id + ", " +
                "name: " + name + ", " +
                "isbn: " + isbn + ", " +
                "author: " + author + ", " +
                "published: " + published + ", " +
                "publisher: " + publisher + ", " +
                "printed: " + printed + ", " +
                "created: " + created + ", " +
                "lastUpdated: " + lastUpdated + "]";
    }

}
