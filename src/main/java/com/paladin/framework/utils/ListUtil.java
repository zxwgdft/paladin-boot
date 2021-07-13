package com.paladin.framework.utils;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2021/6/4
 */
public class ListUtil {

    public static <T> List<T> mergeList(List<T> list1, List<T> list2) {
        if (list1 != null && list1.size() > 0) {
            if (list2 != null && list2.size() > 0) {
                list1.addAll(list2);
            }
            return list1;
        } else {
            return list2;
        }
    }

}
