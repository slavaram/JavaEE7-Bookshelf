package ru.vramaz.bookshelf.rest.jaxb;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="book", namespace="http://ru.vramaz.bookshelf")
public class BookXml {

    private int id;
    private String name;
    private long isbn;
    private String author;
    private Date printed;
    private Date published;
    private String publisher;
    private Date created;
    private Date lastUpdated;

    @XmlElement(name="id")
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @XmlElement(name="name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @XmlElement(name="isbn")
    public long getIsbn() {
        return isbn;
    }
    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }
    @XmlElement(name="author")
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    @XmlElement(name="printed")
    public Date getPrinted() {
        return printed;
    }
    public void setPrinted(Date printed) {
        this.printed = printed;
    }
    @XmlElement(name="published")
    public Date getPublished() {
        return published;
    }
    public void setPublished(Date published) {
        this.published = published;
    }
    @XmlElement(name="publisher")
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    @XmlElement(name="created")
    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
    @XmlElement(name="lastUpdated")
    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
