package org.goodiemania.odin.internal.manager.classinfo;

import java.util.Optional;
import java.util.Set;
import org.goodiemania.odin.internal.manager.ClassInfo;

public class Holder {
    private final Set<ClassInfo<?>> entityClasses;

    public Holder(final Set<ClassInfo<?>> entityClasses) {
        this.entityClasses = entityClasses;
    }

    /**
     * Finds the appropriate ClassInfo for the passed in class.
     *
     * @param entityClass Class that you wish to find information for
     * @param <T>         Generic type of the class your asking for
     * @return Optional potentially containing ClassInfo for the class
     */
    public <T> Optional<ClassInfo<T>> find(final Class<T> entityClass) {
        ClassInfo<T> foundClassInfo = null;
        for (ClassInfo<?> classInfo : entityClasses) {
            if (classInfo.getClassInformation().isAssignableFrom(entityClass)) {
                foundClassInfo = (ClassInfo<T>) classInfo;
                break;
            }
        }

        if (foundClassInfo == null) {
            return Optional.empty();
        }

        return Optional.of(foundClassInfo);
    }
}
