package ru.vramaz.bookshelf.web.mbeans;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ru.vramaz.bookshelf.auth.AuthService;
import ru.vramaz.bookshelf.auth.InvalidPasswordException;
import ru.vramaz.bookshelf.auth.LoginNotFoundException;
import ru.vramaz.bookshelf.web.AuthWebFilter;

@Named
@RequestScoped
public class LoginBean {

    @EJB
    AuthService authService;

    private String login;
    private String password;

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("logout")) {
            FacesContext fContext = FacesContext.getCurrentInstance();
            HttpSession hSession = (HttpSession) fContext.getExternalContext().getSession(true);
            hSession.removeAttribute(AuthWebFilter.ATTR_AUTHORIZED_NAME);
            hSession.removeAttribute(AuthWebFilter.ATTR_LOGIN_NAME);
        }
    }

    public String submit() {
        try {
            authService.login(login, password);

            FacesContext fContext = FacesContext.getCurrentInstance();
            HttpSession hSession = (HttpSession) fContext.getExternalContext().getSession(true);
            hSession.setAttribute(AuthWebFilter.ATTR_AUTHORIZED_NAME, AuthWebFilter.ATTR_AUTHORIZED_VAL);
            hSession.setAttribute(AuthWebFilter.ATTR_LOGIN_NAME, login);

            Map<String, String> params = fContext.getExternalContext().getRequestParameterMap();
            if (params.containsKey(AuthWebFilter.PARAM_REDIRECTED_URI)) {
                try {
                    HttpServletResponse hResponse = (HttpServletResponse) fContext.getExternalContext().getResponse();
                    String redirectUri = params.get(AuthWebFilter.PARAM_REDIRECTED_URI);
                    hResponse.sendRedirect(redirectUri == null || redirectUri.isEmpty() ? AuthWebFilter.URI_HOME : redirectUri);
                } catch (IOException e) {
                    return "Home";
                }
            }

            return "Home";
        } catch (InvalidPasswordException e) {
            return "Login";
        } catch (LoginNotFoundException e) {
            return "Login";
        }
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
