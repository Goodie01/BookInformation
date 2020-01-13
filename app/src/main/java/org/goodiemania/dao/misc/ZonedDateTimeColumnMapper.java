package org.goodiemania.dao.misc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class ZonedDateTimeColumnMapper implements ColumnMapper<ZonedDateTime> {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss[.SSS]X");

    @Override
    public ZonedDateTime map(final ResultSet r, final int columnNumber, final StatementContext ctx) throws SQLException {
        return ZonedDateTime.parse(r.getString(columnNumber), DATE_FORMAT);
    }
}
