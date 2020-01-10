package org.goodiemania.books.model;

import java.time.ZonedDateTime;

public class Audit {
    private DataSource source;
    private ZonedDateTime dateTime;
    private String field;
    private String value;

    private Audit(final DataSource source,
                  final ZonedDateTime dateTime,
                  final String field,
                  final String value) {
        this.source = source;
        this.dateTime = dateTime;
        this.field = field;
        this.value = value;
    }

    public static Audit build(final DataSource source, final String field, final String value) {
        return new Audit(source, ZonedDateTime.now(), field, value);
    }

    public DataSource getSource() {
        return source;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
