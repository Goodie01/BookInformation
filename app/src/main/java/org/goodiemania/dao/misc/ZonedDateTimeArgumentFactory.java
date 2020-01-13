package org.goodiemania.dao.misc;

import java.sql.Types;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

public class ZonedDateTimeArgumentFactory extends AbstractArgumentFactory<ZonedDateTime> {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss[.SSS]X");

    public ZonedDateTimeArgumentFactory() {
        super(Types.VARCHAR);
    }

    @Override
    protected Argument build(ZonedDateTime value, ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, value.format(DATE_FORMAT));
    }
}
