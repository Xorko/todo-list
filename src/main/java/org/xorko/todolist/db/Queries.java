package org.xorko.todolist.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Queries {
    private static final String propFileName = "queries.properties";
    private static Properties props;

    private static Properties getQueries() throws IOException {
        // Avoid reloading the file if it is already loaded
        if (props == null) {
            // https://stackoverflow.com/a/18699561
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName);
            if (is == null) {
                throw new IOException("Unable to load property file: " + propFileName);
            }
            props = new Properties();
            try {
                props.load(is);
            } catch (IOException e) {
                System.out.println("Unable to load property file: " + propFileName);
            }
        }
        return props;
    }

    public static String getQuery(String query) throws IOException {
        return getQueries().getProperty(query);
    }
}
