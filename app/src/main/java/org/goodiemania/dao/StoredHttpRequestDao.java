package org.goodiemania.dao;

import java.util.Optional;
import javax.sql.DataSource;
import org.goodiemania.dao.misc.ZonedDateTimeArgumentFactory;
import org.goodiemania.dao.misc.ZonedDateTimeColumnMapper;
import org.goodiemania.models.http.StoredHttpRequest;
import org.jdbi.v3.core.Jdbi;

/**
 * StoredHttpRequestDao needs a javadoc.
 */
public class StoredHttpRequestDao {
    private final Jdbi jdbi;

    /**
     * Creates a instance of this DAO given a JDBC connection URL.
     *
     * @param jdbcConnectUrl JDBC connection URL to use
     */
    public StoredHttpRequestDao(final String jdbcConnectUrl) {
        jdbi = Jdbi.create(jdbcConnectUrl);
        jdbi.registerColumnMapper(new ZonedDateTimeColumnMapper());
        jdbi.registerArgument(new ZonedDateTimeArgumentFactory());
    }

    /**
     * Creates a instance of this DAO given a datasource.
     *
     * @param datasource JDBC connection URL to use
     */
    public StoredHttpRequestDao(final DataSource datasource) {
        jdbi = Jdbi.create(datasource);
        jdbi.registerColumnMapper(new ZonedDateTimeColumnMapper());
        jdbi.registerArgument(new ZonedDateTimeArgumentFactory());
    }

    /**
     * Creates the tables needed to use this DAO.
     */
    public void createTables() {
        jdbi.useHandle(handle ->
                handle.execute(
                        "CREATE TABLE IF NOT EXISTS stored_Http_Request ("
                                + "url varchar(255) UNIQUE, "
                                + "body text, "
                                + "status varchar(3), "
                                + "responseType varchar(40),"
                                + "requestTime varchar(40)"
                                + ")"));
    }

    /**
     * Attempts to find a StoredHttpRequest matching the given URL.
     *
     * @param url The given URL.
     * @return Potential cached StoredHttpRequest
     */
    public Optional<StoredHttpRequest> getByUrl(final String url) {
        return jdbi.withHandle(handle -> handle.createQuery(
                "select * from stored_Http_Request where url = :url")
                .bind("url", url)
                .mapToBean(StoredHttpRequest.class)
                .stream()
                .findFirst());
    }

    /**
     * Saves the given StoredHttpRequest in the database.
     *
     * @param storedHttpRequest the request to be saved
     */
    public void save(final StoredHttpRequest storedHttpRequest) {
        jdbi.useHandle(handle -> handle.createUpdate(
                "insert or replace into stored_Http_Request"
                        + "(url, body, status, responseType, requestTime)"
                        + "values"
                        + "(:url, :response.body, :response.status, :response.responseType, :response.requestTime)")
                .bindBean(storedHttpRequest)
                .execute());
    }
}
