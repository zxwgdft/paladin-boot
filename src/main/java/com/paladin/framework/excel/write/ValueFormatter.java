package com.paladin.framework.excel.write;

/**
 * 格式化接口
 *
 * @author TontZhou
 */
public interface ValueFormatter {
    /**
     * 格式化方法
     *
     * @param obj
     * @return
     */
    String format(Object obj);
}
