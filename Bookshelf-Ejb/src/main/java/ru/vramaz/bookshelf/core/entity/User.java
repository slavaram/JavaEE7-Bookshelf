package ru.vramaz.bookshelf.core.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="Users")
@NamedQueries({
    @NamedQuery(name="user.findAll", query="select u from User u"),
    @NamedQuery(name="user.getCount", query="select count(u) from User u"),
    @NamedQuery(name="user.findByEmail", query="select u from User u where u.email = :e")
})
public class User {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="pwd_hash")
    private String passwordHash;

    @Column(name="location")
    private String location;

    @Column(name="is_writer")
    private boolean isWriter;

    @Temporal(TemporalType.DATE)
    @Column(name="created")
    private Date created;

    @Temporal(TemporalType.DATE)
    @Column(name="last_updated")
    private Date lastUpdated;

    @ManyToMany(mappedBy="readers")
    private List<Book> readedBooks;

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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public boolean isWriter() {
        return isWriter;
    }
    public void setWriter(boolean isWriter) {
        this.isWriter = isWriter;
    }
    public Date getCreated() {
        return created;
    }
    public Date getLastUpdated() {
        return lastUpdated;
    }
    public List<Book> getReadedBooks() {
        return readedBooks;
    }
    public void setReadedBooks(List<Book> readedBooks) {
        this.readedBooks = readedBooks;
    }

    @Override
    public String toString() {
        return "User [" +
                "id: " + id + ", " +
                "name: " + name + ", " +
                "email: " + email + ", " +
                "location: " + location + ", " +
                "isWriter: " + isWriter + ", " +
                "created: " + created + ", " +
                "lastUpdated: " + lastUpdated + "]";
    }

}
