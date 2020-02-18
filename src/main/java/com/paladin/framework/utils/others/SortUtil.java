package com.paladin.framework.utils.others;

/**
 * 均为倒序，自学研究所得，实用性不高
 *
 * @author TontZhou
 */
public class SortUtil {

    /**
     * 简单选择排序
     *
     * @param arr
     * @return
     */
    @Deprecated
    public static void oldSelectSort(int[] arr) {

        if (arr == null)
            return;

        int size = arr.length;

        for (int i = 0; i < size; i++) {
            int max = i;
            for (int j = i + 1; j < size; j++) {
                if (arr[j] > arr[max])
                    max = j;
            }

            int v = arr[i];
            arr[i] = arr[max];
            arr[max] = v;

        }

    }

    /**
     * 选择排序改进版，每次循环找出最大和最小的插入最前和最后
     * <p>
     * 比没改进前快了10%-20%
     *
     * @param arr
     * @return
     */
    public static void selectSort(int[] arr) {
        if (arr == null)
            return;

        int size = arr.length;
        int temp, max, min;

        for (int i = 0, j = size - 1; i < j; i++, j--) {
            max = i;
            min = i;
            for (int k = i + 1; k <= j; k++) {
                if (arr[k] > arr[max]) {
                    max = k;
                    continue;
                }

                if (arr[k] < arr[min])
                    min = k;

            }

            if (max == min)
                return;
            if (min == i) {
                temp = arr[j];
                arr[j] = arr[i];

                if (max == j)
                    arr[i] = temp;
                else {
                    arr[i] = arr[max];
                    arr[max] = temp;
                }
            } else {
                temp = arr[i];
                arr[i] = arr[max];
                arr[max] = temp;
                temp = arr[j];
                arr[j] = arr[min];
                arr[min] = temp;

            }
        }

        return;
    }

    /**
     * 冒泡排序 ，相对较慢
     *
     * @param arr
     */
    public static void bubbleSort(int[] arr) {
        if (arr == null)
            return;

        int size = arr.length;

        for (int i = 0; i < size - 1; ) {
            i++;
            for (int j = 0; j < size - i; j++) {
                if (arr[j] < arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }

            }
        }
    }

    /**
     * 快速排序，平均时间比上面都快
     *
     * @param arr
     */
    public static void quickSort(int[] arr) {
        if (arr != null)
            quickSort(arr, 0, arr.length - 1);
    }

    /**
     * 快速排序，平均时间比上面都快
     *
     * @param arr
     * @param start 起始位置
     * @param end   结束位置
     */
    public static void quickSort(int[] arr, int start, int end) {

        if (start < end) {
            int temp, i, j, base;

            i = start;
            j = end;

            base = arr[start];

            do {

                while (arr[i] >= base && i < end)
                    i++;
                while (arr[j] <= base && j > start)
                    j--;

                if (i < j) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }

                if (i == j) {
                    arr[i] = arr[j];
                    arr[j] = base;
                }

            } while (i <= j);

            quickSort(arr, start, j);
            quickSort(arr, i, end);
        }
    }


}
