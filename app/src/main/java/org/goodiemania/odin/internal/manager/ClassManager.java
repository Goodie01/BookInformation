package org.goodiemania.odin.internal.manager;

import java.util.function.Function;
import java.util.stream.Stream;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.internal.database.Database;
import org.reflections.Reflections;

public class ClassManager {
    private ClassInfoBuilder classInfoBuilder;
    private Database database;

    public ClassManager(final Database database, final ClassInfoBuilder classInfoBuilder) {
        this.database = database;
        this.classInfoBuilder = classInfoBuilder;
    }

    /**
     * Finds all classes in the package annotated with the Entity annotation.
     *
     * @param packageName Package name to search
     * @return Returns a stream of class info's
     */
    public Stream<ClassInfo<?>> find(final String packageName) {
        final Reflections reflections = new Reflections(packageName);

        //TODO should we really be using peek like this?
        return reflections.getTypesAnnotatedWith(Entity.class)
                .stream()
                .map((Function<Class<?>, ClassInfo<?>>) classInfoBuilder::build)
                .peek(this::buildDatabase);
    }

    private <T> void buildDatabase(final ClassInfo<T> classInfo) {
        database.createEntityTable(classInfo);

        if (!classInfo.getIndexedFields().isEmpty()) {
            database.createEntitySearchFieldTable(classInfo);
        }
    }
}
