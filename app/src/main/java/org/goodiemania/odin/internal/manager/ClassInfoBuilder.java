package org.goodiemania.odin.internal.manager;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.odin.external.annotations.Id;
import org.goodiemania.odin.external.annotations.Index;
import org.goodiemania.odin.external.exceptions.EntityException;


public class ClassInfoBuilder {
    /**
     * Takes a Java Class definition and builds a Odin internal ClassInfo object from it.
     *
     * @param classInformation Java Class object
     * @param <T>              Type of the passed Java Class object
     * @return Returns ClassInfo object containing the passed Java Class object
     */
    public <T> ClassInfo<T> build(final Class<T> classInformation) {

        String tableName = classInformation.getSimpleName();
        String searchTableName = "__" + tableName + "_SearchField";
        PropertyDescriptor idField;
        List<PropertyDescriptor> indexedFields;

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(classInformation);

            idField = findIdAnnotatedField(classInformation, beanInfo);

            if (classInformation.isAnnotationPresent(Index.class)) {
                indexedFields = findFields(beanInfo);
            } else {
                indexedFields = findFields(beanInfo, classInformation, Index.class);
            }
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }

        return new ClassInfo<>(classInformation, tableName, searchTableName, idField, indexedFields);
    }


    private static PropertyDescriptor findIdAnnotatedField(
            final Class<?> classInformation,
            final BeanInfo beanInfo) {
        List<PropertyDescriptor> annotatedFields = findFields(beanInfo, classInformation, Id.class);

        if (annotatedFields.size() != 1) {
            throw new EntityException(
                    String.format(
                            "Require exactly one, and only one, field annotated with @Id, on class: %s",
                            classInformation.getCanonicalName()));
        }

        return annotatedFields.get(0);
    }

    private static List<PropertyDescriptor> findFields(final BeanInfo beanInfo) {
        return Arrays.stream(beanInfo.getPropertyDescriptors())
                .filter(propertyDescriptor ->
                        !StringUtils.equals("class", propertyDescriptor.getName()))
                .collect(Collectors.toList());
    }

    private static List<PropertyDescriptor> findFields(
            final BeanInfo beanInfo,
            final Class<?> classInformation,
            final Class<? extends Annotation> annotationClass) {
        return findFields(beanInfo)
                .stream()
                .filter(propertyDescriptor -> {
                    try {
                        return classInformation.getDeclaredField(propertyDescriptor.getName())
                                .isAnnotationPresent(annotationClass);
                    } catch (NoSuchFieldException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
