package ru.vramaz.bookshelf.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.vramaz.bookshelf.core.BookService;

public class ApplicationConfig {

    private final static Logger LOGGER = Logger.getLogger(BookService.class.getName());

    private Properties props;

    private ApplicationConfig() {}

    private static class LazyHolder {
        static final ApplicationConfig INSTANCE = new ApplicationConfig();
    }

    public static ApplicationConfig instance() {
        return LazyHolder.INSTANCE;
    }

    public String getParameter(String name) {
        if (props == null) loadProperties();
        return props.getProperty(name);
    }

    private void loadProperties() {
        try {
            props = new Properties();
            InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties");
            props.load(in);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "cannot load config.properties", e);
        }
    }

}
