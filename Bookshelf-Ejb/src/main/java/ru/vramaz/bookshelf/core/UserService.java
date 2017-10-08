package ru.vramaz.bookshelf.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ru.vramaz.bookshelf.core.entity.User;

@Stateless
public class UserService {

    @EJB
    public ImageService imgService;

    @PersistenceContext(unitName="em_bookshelf")
    public EntityManager em;

    private final static Logger LOGGER = Logger.getLogger(BookService.class.getName());

    public User createUser(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        try {
            em.persist(user);
            return user;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while creating a user", e);
            throw new PersistenceException("Exception occurred while creating a user", e);
        }
    }

    public void updateUser(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        try {
            em.merge(user);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while updating a user", e);
            throw new PersistenceException("Exception occurred while updating a user", e);
        }
    }

    public boolean deleteUser(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user.getId() <= 0) throw new IllegalArgumentException("Invalid user Id: " + user.getId());
        // Locked
        return false;
    }

    public boolean deleteUserById(int id) {
        return deleteUser(findUserById(id));
    }

    public int getUsersCount() {
        return ((Number) em.createNamedQuery("user.getCount", Integer.class).getSingleResult()).intValue();
    }

    public User findUserById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Invalid user Id: " + id);
        try {
            return em.find(User.class, id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while searching a user", e);
            throw new PersistenceException("Exception occurred while searching a user", e);
        }
    }

    @SuppressWarnings("unchecked")
    public User findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("Invalid user email: " + email);
        List<User> result = em.createNamedQuery("user.findByEmail").setParameter("e", email).getResultList();
        return result.size() == 0 ? null : result.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<User> findAllUsers(int fromRecord, int records) {
        int count = getUsersCount();
        if (fromRecord < 0) fromRecord = 0;
        if (records < 0) records = 5;
        if (fromRecord > count) {
            return Arrays.asList();
        }
        if (fromRecord + records > count) {
            records = count - fromRecord;
        }
        return em.createNamedQuery("user.findAll").setFirstResult(fromRecord).setMaxResults(records).getResultList();
    }

    public InputStream getUserLogo(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user.getId() <= 0) throw new IllegalArgumentException("Invalid user Id: " + user.getId());
        try {
            return imgService.findImg("books/" + user.getId() + ".jpg");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while loading user logo image", e);
            throw new PersistenceException("Exception occurred while loading user logo image", e);
        }
    }

    public void setUserLogo(User user, InputStream in) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user.getId() <= 0) throw new IllegalArgumentException("Invalid user Id: " + user.getId());
        if (in == null) throw new IllegalArgumentException("User logo stream cannot be null");
        try {
            if (in.available() > 0) imgService.saveImg("users/" + user.getId() + ".jpg", in);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while saving user logo image", e);
            throw new PersistenceException("Exception occurred while saving user logo image", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {}
        }
    }

    public void removeUserLogo(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user.getId() <= 0) throw new IllegalArgumentException("Invalid user Id: " + user.getId());
        try {
            imgService.deleteImg("users/" + user.getId() + ".jpg");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while deleting user logo image", e);
            throw new PersistenceException("Exception occurred while deleting user logo image", e);
        }
    }

}
