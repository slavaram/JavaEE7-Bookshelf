package ru.vramaz.bookshelf.rest.jaxb;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user", namespace="http://ru.vramaz.bookshelf")
public class UserXml {

    private int id;
    private String name;
    private String email;
    private String location;
    private boolean isWriter;
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
    @XmlElement(name="email")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @XmlElement(name="location")
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    @XmlElement(name="isWriter")
    public boolean getIsWriter() {
        return isWriter;
    }
    public void setIsWriter(boolean isWriter) {
        this.isWriter = isWriter;
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
