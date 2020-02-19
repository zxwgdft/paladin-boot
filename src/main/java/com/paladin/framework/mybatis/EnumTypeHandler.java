package com.paladin.framework.mybatis;

import com.paladin.framework.common.CodeEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 扩展默认枚举类型处理器，增加{CodeEnum}类型枚举特殊处理
 *
 * @author TontoZhou
 * @since 2020/1/2
 */
public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private Class<E> type;

    private boolean isCodeEnum;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }

        this.type = type;
        this.isCodeEnum = CodeEnum.class.isAssignableFrom(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (isCodeEnum) {
            ps.setInt(i, ((CodeEnum) parameter).getCode());
        } else {
            if (jdbcType == null) {
                ps.setString(i, parameter.name());
            } else {
                ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE); // see r3589
            }
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (isCodeEnum) {
            int code = rs.getInt(columnName);
            return code == 0 && rs.wasNull() ? null : codeOf(code);
        } else {
            String s = rs.getString(columnName);
            return s == null ? null : Enum.valueOf(type, s);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (isCodeEnum) {
            int code = rs.getInt(columnIndex);
            return code == 0 && rs.wasNull() ? null : codeOf(code);
        } else {
            String s = rs.getString(columnIndex);
            return s == null ? null : Enum.valueOf(type, s);
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (isCodeEnum) {
            int code = cs.getInt(columnIndex);
            return code == 0 && cs.wasNull() ? null : codeOf(code);
        } else {
            String s = cs.getString(columnIndex);
            return s == null ? null : Enum.valueOf(type, s);
        }
    }

    private E codeOf(int code) {
        try {
            return CodeEnum.codeOf(type, code);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot convert " + code + " to " + type.getSimpleName() + " by code value.", ex);
        }
    }


}
