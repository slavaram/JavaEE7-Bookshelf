package ru.vramaz.bookshelf.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import ru.vramaz.bookshelf.core.BookService;
import ru.vramaz.bookshelf.core.UserService;
import ru.vramaz.bookshelf.core.entity.User;

@Singleton
public class AuthService {

    @EJB
    UserService uService;

    private final static Logger LOGGER = Logger.getLogger(BookService.class.getName());

    private final String ENCRYPTION_ALGORITHM = "MD5";

    public boolean login(String login, String password) throws InvalidPasswordException, LoginNotFoundException {
        if (login == null || login.trim().isEmpty()) throw new LoginNotFoundException("Login is empty or null");
        if (password == null || password.trim().isEmpty()) throw new InvalidPasswordException("Password is empty or null");

        String passHash = passToHash(password);

        User user = uService.findUserByEmail(login);
        if (user.getId() == 0) throw new LoginNotFoundException("Login not found");
        if (!passHash.equals(user.getPasswordHash())) throw new InvalidPasswordException("Wrong password");

        return true;
    }

    public String passToHash(String password) throws InvalidPasswordException {
        if (password == null || password.trim().isEmpty()) throw new InvalidPasswordException("Password is empty or null");
        try {
            MessageDigest mDigest = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
            mDigest.reset();
            byte[] digestB = mDigest.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digestB.length; i++) {
                sb.append(Integer.toHexString(0xff & digestB[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred when generating hash for password", e);
            throw new InvalidPasswordException("Cannot generate hash");
        }
    }

}
