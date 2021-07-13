package com.paladin.framework.service.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2021/4/9
 */
public class CommonSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> list = super.getMethodList(mapperClass);
        list.add(new LogicDelete());
        list.add(new UpdateWholeById());
        list.add(new SelectWholeById());
        return list;
    }
}
