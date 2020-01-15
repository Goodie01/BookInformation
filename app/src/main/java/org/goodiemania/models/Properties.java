package org.goodiemania.models;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public enum Properties {
    API_KEY_GOOD_READS,
    API_KEY_LIBRARY_THING,
    API_KEY_GOOGLE_BOOKS,
    DB_HOST,
    DB_PORT,
    DB_DATABASE,
    DB_PASSWORD,
    DB_USER;

    /**
     * Get the specified parameter from the specified enum.
     * pulls from the properties file that is specified on the command line.
     * TODO this method shouldn't also throw an exception? Should it?
     *
     * @return Returns a Optional, optionally containing the value
     */
    public Optional<String> get() {
        java.util.Properties properties = new java.util.Properties();
        String propertyFileName = System.getProperty("prop", "default.properties");

        try (FileReader propertiesFileReader = new FileReader(propertyFileName)) {
            properties.load(propertiesFileReader);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load properties file", e);
        }
        return Optional.ofNullable((String) properties.get(this.toString()));
    }
}
