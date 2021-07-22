package com.paladin.framework.excel.write;

import com.paladin.framework.excel.EnumContainer;
import com.paladin.framework.utils.reflect.Entity;
import com.paladin.framework.utils.reflect.EntityField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TontZhou
 */
public class DefaultWriteRow extends WriteRow {

    private EntityField[] parentFields;
    private EntityField entityField;

    DefaultWriteRow(EntityField entityField, EntityField[] parentFields) {
        this.entityField = entityField;
        this.parentFields = parentFields;
    }

    @Override
    public Object peelData(Object data) {
        if (parentFields != null) {
            for (EntityField field : parentFields) {
                data = field.getValue(data);
                if (data == null) {
                    return null;
                }
            }
        }
        return entityField == null ? data : entityField.getValue(data);
    }

    public static WriteRow createWriteRow(Class<?> clazz, EnumContainer enumContainer) {
        return createWriteRow(clazz, enumContainer, null, null);
    }

    private static WriteRow createWriteRow(Class<?> clazz, EnumContainer enumContainer, EntityField rowField, EntityField... parentField) {

        WriteRow row = new DefaultWriteRow(rowField, parentField);
        List<WriteRow> subRows = new ArrayList<>();

        Entity entity = Entity.getEntity(clazz);
        List<EntityField> fields = entity.getEntityFields();
        List<WriteColumn> columns = new ArrayList<>(fields.size());

        for (EntityField field : fields) {
            WriteBean writeBean = field.getAnnotation(WriteBean.class);
            if (writeBean != null) {
                // 不考虑集合和数组中嵌套集合和数组的情况
                if (field.isCollection()) {
                    WriteRow subRow = createWriteRow(field.getCollectionType(), enumContainer, field);
                    if (subRow != null) {
                        subRows.add(subRow);
                    }
                } else if (field.isArray()) {
                    WriteRow subRow = createWriteRow(field.getArrayType(), enumContainer, field);
                    if (subRow != null) {
                        subRows.add(subRow);
                    }
                } else if (field.isMap()) {
                    throw new RuntimeException("can't support <Map> type");
                } else {
                    EntityField[] parents;
                    if (parentField != null) {
                        parents = Arrays.copyOf(parentField, parentField.length + 1);
                        parents[parentField.length] = field;
                    } else {
                        parents = new EntityField[]{field};
                    }

                    WriteRow subRow = createWriteRow(field.getType(), enumContainer, field, parents);
                    columns.addAll(subRow.getColumns());
                    subRows.addAll(subRow.getSubRows());
                }
                continue;
            }

            WriteProperty writeProp = field.getAnnotation(WriteProperty.class);
            if (writeProp != null) {
                DefaultWriteColumn defaultColumn = new DefaultWriteColumn(field, parentField);

                defaultColumn.setCellIndex(writeProp.cellIndex());
                defaultColumn.setEnumType(writeProp.enumType().length() == 0 ? null : writeProp.enumType());
                defaultColumn.setName(writeProp.name().length() == 0 ? field.getName() : writeProp.name());
                defaultColumn.setWidth(writeProp.width());
                defaultColumn.setAlignment(writeProp.alignment());
                defaultColumn.setAutoWidth(writeProp.autoWidth());
                defaultColumn.setDefaultEmptyValue(writeProp.defaultValue());
                defaultColumn.setFormat(writeProp.format());
                defaultColumn.setWrapText(writeProp.wrapText());
                defaultColumn.setDateFormat(writeProp.dateFormat());
                defaultColumn.setEnumContainer(enumContainer);
                defaultColumn.setBooleanTrueValue(writeProp.booleanTrue());
                defaultColumn.setBooleanFalseValue(writeProp.booleanFalse());

                ValueFormat cellFormat = field.getAnnotation(ValueFormat.class);
                if (cellFormat != null) {
                    Class<? extends ValueFormatter> formatClass = cellFormat.format();
                    ValueFormatter formatter;
                    if (cellFormat.cache()) {
                        formatter = formatterCache.get(formatClass);
                        if (formatter == null) {
                            synchronized (formatter) {
                                formatter = formatterCache.get(formatClass);
                                if (formatter == null) {
                                    try {
                                        formatter = formatClass.newInstance();
                                        formatterCache.put(formatClass, formatter);
                                    } catch (InstantiationException | IllegalAccessException e) {
                                        throw new RuntimeException("无法发射生成类：" + formatClass + "的实例对象", e);
                                    }
                                }
                            }
                        }
                    } else {
                        try {
                            formatter = formatClass.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new RuntimeException("无法发射生成类：" + formatClass + "的实例对象", e);
                        }
                    }
                    defaultColumn.setValueFormatter(formatter);
                }
                columns.add(defaultColumn);
            }
        }

        row.setColumns(columns);
        row.setSubRows(subRows);

        return row;
    }

    private static Map<Class<? extends ValueFormatter>, ValueFormatter> formatterCache = new ConcurrentHashMap<>();

}
