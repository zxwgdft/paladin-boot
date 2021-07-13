/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paladin.framework.service.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Objects;

import static java.util.stream.Collectors.joining;

/**
 * 根据 ID 更新整个对象
 * <p>
 * mybatis plus默认更新策略为NOT_NULL，所以新增该方法用于弥补需要整个对象update的情况（更新null值需要）
 *
 * @author hubin
 * @since 2018-04-06
 */
public class UpdateWholeById extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String formatSql = "<script>\nUPDATE %s %s WHERE %s=#{%s} %s\n</script>";
        final String additional = optlockVersion(tableInfo) + tableInfo.getLogicDeleteSql(true, true);
        String sql = String.format(formatSql, tableInfo.getTableName(),
                sqlSet2(tableInfo.isWithLogicDelete(), false, tableInfo, false, ENTITY, ENTITY_DOT),
                tableInfo.getKeyColumn(), ENTITY_DOT + tableInfo.getKeyProperty(), additional);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, CommonMapper.METHOD_UPDATE_WHOLE_BY_ID, sqlSource);
    }


    protected String sqlSet2(boolean logic, boolean ew, TableInfo table, boolean judgeAliasNull, final String alias,
                             final String prefix) {

        /*
         * 从原updateById方法复制而来的代码
         */

        final String newPrefix = prefix == null ? EMPTY : prefix;
        String sqlScript = table.getFieldList().stream()
                .filter(i -> {
                    if (logic) {
                        return !(table.isWithLogicDelete() && i.isLogicDelete());
                    }
                    return true;
                    // 修改 getSqlSet方法，忽略if判断null值的逻辑
                }).map(i -> i.getSqlSet(true, newPrefix)).filter(Objects::nonNull).collect(joining(NEWLINE));


        if (judgeAliasNull) {
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", alias), true);
        }
        if (ew) {
            sqlScript += NEWLINE;
            sqlScript += SqlScriptUtils.convertIf(SqlScriptUtils.unSafeParam(U_WRAPPER_SQL_SET),
                    String.format("%s != null and %s != null", WRAPPER, U_WRAPPER_SQL_SET), false);
        }
        sqlScript = SqlScriptUtils.convertSet(sqlScript);
        return sqlScript;
    }


}
