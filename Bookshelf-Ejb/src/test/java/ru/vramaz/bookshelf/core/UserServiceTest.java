package ru.vramaz.bookshelf.core;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ru.vramaz.bookshelf.core.entity.User;

public class UserServiceTest {

    private UserService uService;

    @Before
    public void init() {
        uService = new UserService();
        uService.em = Mockito.mock(EntityManager.class);
    }

    @After
    public void destroy() {
        // nothing to do
    }

    @Test(expected=IllegalArgumentException.class)
    public void createUserNullName() {
        uService.createUser(null);
    }

    @Test
    public void createUser() {
        User user = new User();
        user.setName("Maggie");
        user.setEmail("mag@gmail.ru");
        Assert.assertNotNull(uService.createUser(user));
        Mockito.verify(uService.em).persist(user);
    }

    @Test(expected=IllegalArgumentException.class)
    public void updateBookNullId() {
        uService.updateUser(null);
    }

    @Test
    public void updateBook() {
        User user = new User();
        user.setName("Maggie");
        user.setEmail("mag@gmail.ru");
        uService.updateUser(user);
        Mockito.verify(uService.em).merge(user);
    }

    @Test(expected=IllegalArgumentException.class)
    public void deleteUserNull() {
        uService.deleteUser(null);
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setId(1);
        user.setName("Maggie");
        uService.deleteUser(user);
    }

    /**
     * Another tests
     */

}
