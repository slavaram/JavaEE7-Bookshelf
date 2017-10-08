package ru.vramaz.bookshelf.web.mbeans.user;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import ru.vramaz.bookshelf.auth.AuthService;
import ru.vramaz.bookshelf.auth.InvalidPasswordException;
import ru.vramaz.bookshelf.core.BookService;
import ru.vramaz.bookshelf.core.UserService;
import ru.vramaz.bookshelf.core.entity.User;
import ru.vramaz.bookshelf.web.AuthWebFilter;

@Named
@RequestScoped
public class UserBean {

    @EJB
    UserService uService;
    @EJB
    AuthService authService;

    private final static Logger LOGGER = Logger.getLogger(BookService.class.getName());

    private User user;
    private String password;
    private Part img;

    @PostConstruct
    private void init() {
        HttpSession hSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        String login = hSession.getAttribute(AuthWebFilter.ATTR_LOGIN_NAME).toString();
        if (login != null && !login.isEmpty()) {
            user = uService.findUserByEmail(login);
        } else {
            user = new User();
        }
    }

    public String submit() {
        if (user.getId() == 0) {
            return createUser();
        } else {
            return updateUser();
        }
    }

    private String createUser() {
        try {
            String pHash = authService.passToHash(password);
            user.setPasswordHash(pHash);
            uService.createUser(user);
            return "Login";
        } catch (InvalidPasswordException e) {
            LOGGER.log(Level.SEVERE, "Exception while registration", e);
            return "Registration";
        }
    }

    private String updateUser() {
        uService.updateUser(user);
        if (img != null) {
            try {
                InputStream in = img.getInputStream();
                uService.setUserLogo(user, in);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception while saving user logo image", e);
            }
        }
        return "Home";
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Part getImg() {
        return img;
    }
    public void setImg(Part img) {
        this.img = img;
    }

}
