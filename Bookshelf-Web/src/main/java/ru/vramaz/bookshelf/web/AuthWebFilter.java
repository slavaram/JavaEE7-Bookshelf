package ru.vramaz.bookshelf.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.vramaz.bookshelf.auth.AuthService;
import ru.vramaz.bookshelf.core.BookService;

@WebFilter(urlPatterns = { "/*" })
public class AuthWebFilter implements Filter {

    @EJB
    private AuthService authService;

    private final static Logger LOGGER = Logger.getLogger(BookService.class.getName());

    public static final String URI_LOGIN = "/Bookshelf/Login.xhtml";
    public static final String URI_REGISTER = "/Bookshelf/Registration.xhtml";
    public static final String URI_HOME = "/Bookshelf/Home.xhtml";
    public static final String URI_REST = "/Bookshelf/rest/";

    public static final String ATTR_AUTHORIZED_NAME = "authoruzed";
    public static final String ATTR_AUTHORIZED_VAL = "true";
    public static final String ATTR_LOGIN_NAME = "login";

    public static final String PARAM_REDIRECTED_URI = "redirect";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fChain) {
        HttpServletRequest hReq = (HttpServletRequest) req;
        HttpServletResponse hRes = (HttpServletResponse) res;


        String uri = hReq.getRequestURI();

        try {
            hReq.setCharacterEncoding("UTF-8");
            hRes.setCharacterEncoding("UTF-8");

            if (uri.endsWith(URI_LOGIN) || uri.endsWith(URI_REGISTER) || uri.startsWith(URI_REST)) {
                fChain.doFilter(req, res);
            } else {
                String authorized = (String) hReq.getSession().getAttribute(ATTR_AUTHORIZED_NAME);
                if (authorized != null && ATTR_AUTHORIZED_VAL.equals(authorized)) {
                    fChain.doFilter(req, res);
                } else {
                    hRes.sendRedirect(URI_LOGIN + "?" + PARAM_REDIRECTED_URI + "=" + hReq.getRequestURI());
                }
            }
        } catch (IOException | ServletException e) {
            LOGGER.log(Level.SEVERE, "Exception while filtering http request in Authentication Filter", e);
        }

        return;
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        // TODO
    }

    @Override
    public void destroy() {
        // TODO
    }

}
