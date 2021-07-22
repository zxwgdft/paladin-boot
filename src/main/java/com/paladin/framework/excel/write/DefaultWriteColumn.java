package com.paladin.framework.excel.write;

import com.paladin.framework.excel.EnumContainer;
import com.paladin.framework.utils.reflect.EntityField;

/**
 * 默认写Excel的列
 *
 * @author TontZhou
 */
public class DefaultWriteColumn extends WriteColumn {

    private EntityField[] parentFields;
    private EntityField entityField;

    private EnumContainer enumContainer;

    DefaultWriteColumn(EntityField entityField, EntityField[] parentFields) {
        this.entityField = entityField;
        this.parentFields = parentFields;
    }

    @Override
    public Object peelData(Object data) {
        if (parentFields != null) {
            for (EntityField field : parentFields) {
                data = field.getValue(data);
            }
        }
        return entityField == null ? data : entityField.getValue(data);
    }

    @Override
    public String getEnumName(Object value) {
        if (value == null) {
            return getDefaultEmptyValue();
        }
        return enumContainer != null ? enumContainer.getEnumName(getEnumType(), value.toString()) : value.toString();
    }

    public EnumContainer getEnumContainer() {
        return enumContainer;
    }

    public void setEnumContainer(EnumContainer enumContainer) {
        this.enumContainer = enumContainer;
    }
}
