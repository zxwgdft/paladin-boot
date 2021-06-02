package com.paladin.framework.service.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.styx.common.api.DeletedBaseModel;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author TontoZhou
 * @since 2021/4/9
 */
public class LogicDelete extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = String.format("UPDATE %s SET update_time = #{%s.updateTime}, update_by = #{%s.updateBy}, deleted = 1 WHERE id = #{%s} AND deleted = 0"
                , tableInfo.getTableName(), CommonMapper.PARAM_DBM, CommonMapper.PARAM_DBM, CommonMapper.PARAM_ID);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, DeletedBaseModel.class, CommonMapper.METHOD_LOGIC_DELETE_BY_ID, sqlSource);
    }
}
