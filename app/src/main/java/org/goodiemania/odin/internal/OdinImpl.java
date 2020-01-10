package org.goodiemania.odin.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.goodiemania.odin.external.EntityManager;
import org.goodiemania.odin.external.Odin;
import org.goodiemania.odin.internal.database.Database;
import org.goodiemania.odin.internal.manager.ClassInfo;
import org.goodiemania.odin.internal.manager.EntityManagerImpl;
import org.goodiemania.odin.internal.manager.classinfo.Holder;
import org.goodiemania.odin.internal.manager.search.SearchFieldGenerator;

public class OdinImpl implements Odin {
    private final Holder entityClasses;
    private final ObjectMapper objectMapper;
    private final Database database;
    private final SearchFieldGenerator searchFieldGenerator;

    /**
     * Creates a new instance of OdinImpl.
     *
     * @param objectMapper         Object mapper, from jackson, preconfigured
     * @param entityClasses        Classes found in the package
     * @param database             Databese object
     * @param searchFieldGenerator Extracts serch fields from a givenentity object
     */
    public OdinImpl(
            final ObjectMapper objectMapper,
            final Holder entityClasses,
            final Database database,
            final SearchFieldGenerator searchFieldGenerator) {
        this.objectMapper = objectMapper;
        this.entityClasses = entityClasses;
        this.database = database;
        this.searchFieldGenerator = searchFieldGenerator;
    }

    @Override
    public <T> EntityManager<T> createFor(final Class<T> entityClass) {
        ClassInfo<T> classInfo = entityClasses.find(entityClass)
                .orElseThrow(() -> new IllegalStateException("Unable to find given class in any packages"));

        return new EntityManagerImpl<T>(database, objectMapper, searchFieldGenerator, classInfo);
    }
}
