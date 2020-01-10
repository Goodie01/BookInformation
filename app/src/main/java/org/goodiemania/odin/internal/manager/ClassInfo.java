package org.goodiemania.odin.internal.manager;

import java.beans.PropertyDescriptor;
import java.util.List;

public class ClassInfo<T> {
    private final Class<T> classInformation;
    private final String tableName;
    private final String searchTableName;
    private final PropertyDescriptor idField;
    private final List<PropertyDescriptor> indexedFields;

    /**
     * Creates a new ClassInfo object.
     *
     * @param classInformation Java Class representation
     * @param tableName        Table name to save all entities too
     * @param searchTableName  Search table name to save search fileds too
     * @param idField          Property descriptor for the ID field of the entity
     * @param indexedFields    All fields that will be searchable
     */
    public ClassInfo(
            final Class<T> classInformation,
            final String tableName,
            final String searchTableName,
            final PropertyDescriptor idField,
            final List<PropertyDescriptor> indexedFields) {
        this.classInformation = classInformation;
        this.tableName = tableName;
        this.searchTableName = searchTableName;
        this.idField = idField;
        this.indexedFields = indexedFields;
    }

    public Class<T> getClassInformation() {
        return classInformation;
    }

    public PropertyDescriptor getIdField() {
        return idField;
    }

    public List<PropertyDescriptor> getIndexedFields() {
        return indexedFields;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSearchTableName() {
        return searchTableName;
    }
}
