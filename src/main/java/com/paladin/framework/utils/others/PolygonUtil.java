package com.paladin.framework.utils.others;

import com.paladin.framework.exception.BusinessException;

/**
 * 多边形工具类
 * <p>
 * 用于经纬度定位时，可做7位小数化为整数处理
 *
 * @author TontoZhou
 * @since 2020/7/22
 */
public class PolygonUtil {

    /**
     * 判断点是否在多边形内
     *
     * @param x       点X坐标（经度lng）
     * @param y       点Y左边（维度lat）
     * @param polygon 多边形
     * @return 在多边形内或边上返回true，否则为false
     */
    public static boolean isPointInPolygon(int x, int y, Polygon polygon) {

        // 如果在最大或最小X,Y值外，则肯定不在多边形内
        if (x > polygon.maxX || y > polygon.maxY || x < polygon.minX || y < polygon.minY) {
            return false;
        }

        // 由点作射线，计算多边形各边与射线交点个数，如果为奇数则在多边形内，否则为外面
        // 特殊情况需要处理（交点为多边行端点，射线与多边形的边重合），也可以不处理特殊情况，影响较小

        int size = polygon.size;
        int[] lngs = polygon.lngs;
        int[] lats = polygon.lats;

        int c = 0;

        for (int p1 = 0, p2 = 1; ; ) {

            int r = countOfIntersection(x, y, lngs[p1], lats[p1], lngs[p2], lats[p2]);

            if (r == -1) {
                return true;
            }

            if (r == 1) {
                c++;

                if (++p1 == size) {
                    break;
                }

                if (++p2 == size) {
                    p2 = 0;
                }

            } else if (r == 2 || r == 0) {
                // 只有第一条边才会出现等于2的情况，
                // 因为在后面的交点为端点的处理中会跳过2这种情况。
                // 我们在这里不处理，等到最后一条边时可以处理该端点，
                // 所以这里2与0情况相同处理

                if (++p1 == size) {
                    break;
                }

                if (++p2 == size) {
                    p2 = 0;
                }
            } else if (r == 3) {

                if (++p2 == size) {
                    p2 = 0;
                }

                int a = lats[p1];
                int b = lats[p2];

                // 重合情况
                if (b == y) {
                    // 点在重合线段上
                    if (lngs[p2] <= x) {
                        return true;
                    }

                    if (++p2 == size) {
                        p2 = 0;
                    }

                    b = lats[p2];


                    if ((a > y && y > b) || (a < y && y < b)) {
                        c++;
                    }

                    p1 += 3;

                    if (++p2 == size) {
                        p2 = 0;
                    }
                } else {
                    if ((a > y && y > b) || (a < y && y < b)) {
                        c++;
                    }

                    p1 += 2;

                    if (++p2 == size) {
                        p2 = 0;
                    }
                }

                if (p1 >= size) {
                    break;
                }

            } else {
                // r == 9
                // 如果为三角形，则重合情况下点不可能在三角形内

                if (size == 3) {
                    return false;
                }

                // 重合情况下会在r==3 情况下被处理，只有第一条边才会触发该情况
                p1 = 2;
                p2 = 3;
            }
        }

        return c % 2 != 0;
    }

    /**
     * 获取点作射线与多边形边的交点数
     * <p>
     * 返回值意义:
     * -1   :   点在多边形的边线上面，可直接判断点在多边形内或外
     * 0    :   没有交点
     * 1    :   存在一个交点
     * 2    :   存在一个交点，该交点为线段端点1
     * 3    :   存在一个交点，该交点为线段端点2
     * 9    :   线段和射线重合
     *
     * @param x   点X坐标
     * @param y   点Y坐标
     * @param px1 线段（多边形边）端点1的X坐标
     * @param py1 线段（多边形边）端点1的Y坐标
     * @param px2 线段（多边形边）端点2的X坐标
     * @param py2 线段（多边形边）端点2的Y坐标
     */
    private static int countOfIntersection(int x, int y, int px1, int py1, int px2, int py2) {

        // 先排除特殊值

        // 如果平行
        if (py1 == py2) {
            if (py1 != y) {
                return 0;
            }

            if (px1 > px2) {
                if (x < px2) {
                    return 9;
                } else if (x > px1) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (x < px1) {
                    return 9;
                } else if (x > px2) {
                    return 0;
                } else {
                    return -1;
                }
            }
        } else {
            // 如果线段端点在射线上
            if (y == py2) {
                if (px2 > x) {
                    return 3;
                } else if (px2 == x) {
                    return -1;
                } else {
                    return 0;
                }
            } else if (y == py1) {
                if (px1 > x) {
                    return 2;
                } else if (px1 == x) {
                    return -1;
                } else {
                    return 0;
                }
            }

            // 射线y值小于线段最小y值或大于线段最大值，则不可能相交
            if (py1 > py2) {
                if (y < py2 || py1 < y) {
                    return 0;
                }
            } else {
                if (y < py1 || py2 < y) {
                    return 0;
                }
            }


            long a = ((long) px1) * (y - py2) + ((long) px2) * (py1 - y);
            long b = ((long) x) * (py1 - py2);

            if (py1 > py2) {
                return a > b ? 1 : 0;
            } else {
                return a < b ? 1 : 0;
            }

        }
    }


    /**
     * 多边形
     */
    public static class Polygon {
        int[] lngs;
        int[] lats;

        int size;

        int maxX;
        int maxY;
        int minX;
        int minY;

        /**
         * @param content 例如：1/1,2/2,4/3
         */
        public Polygon(String content) {
            String[] points = content.split(",");

            int l = points.length;

            if (l < 3) {
                throw new BusinessException("points can't form a polygon");
            }

            int[] lngs = new int[l];
            int[] lats = new int[l];

            int px1 = 0, py1 = 0, px2 = 0, py2 = 0;
            // 是否包含第一个点
            boolean first = true;
            int k = 0;

            for (int i = 0; i <= l; i++) {
                if (i == l) {
                    // 处理开始的点1和点2

                    if (k < 3) {
                        throw new BusinessException("points can't form a polygon");
                    }
                    // k==3情况下，已经排除了同一直线情况
                    if (k > 3) {
                        int lng = lngs[0];
                        int lat = lats[0];

                        if (px2 != lng || py2 != lat) {
                            long a = ((long) (py1 - lat)) * (lng - px2);
                            long b = ((long) (lat - py2)) * (px1 - lng);
                            if (a == b) {
                                k--;
                            } else {
                                px1 = px2;
                                py1 = py2;
                            }
                            px2 = lng;
                            py2 = lat;
                        } else {
                            k--;
                        }

                        lng = lngs[1];
                        lat = lats[1];

                        long a = ((long) (py1 - lat)) * (lng - px2);
                        long b = ((long) (lat - py2)) * (px1 - lng);

                        if (a == b) {
                            // 应当舍去第一个点
                            first = false;
                        }
                    }
                    // 结束
                } else {
                    String[] lngLat = points[i].split("/");
                    int lng = Integer.valueOf(lngLat[0]);
                    int lat = Integer.valueOf(lngLat[1]);

                    if (k == 0) {
                        px1 = lng;
                        py1 = lat;
                        lngs[0] = lng;
                        lats[0] = lat;
                        k = 1;
                    } else if (k == 1) {
                        if (px1 != lng || py1 != lat) {
                            px2 = lng;
                            py2 = lat;
                            lngs[1] = lng;
                            lats[1] = lat;
                            k = 2;
                        }
                    } else {
                        if (px2 != lng || py2 != lat) {
                            long a = ((long) (py1 - lat)) * (lng - px2);
                            long b = ((long) (lat - py2)) * (px1 - lng);
                            if (a == b) {
                                lngs[k - 1] = lng;
                                lats[k - 1] = lat;
                                px2 = lng;
                                py2 = lat;
                            } else {
                                lngs[k] = lng;
                                lats[k++] = lat;
                                px1 = px2;
                                py1 = py2;
                                px2 = lng;
                                py2 = lat;
                            }
                        }
                    }
                }
            }

            size = first ? k : k - 1;

            if (size < 3) {
                throw new BusinessException("points can't form a polygon");
            }

            this.lngs = new int[size];
            this.lats = new int[size];

            System.arraycopy(lngs, first ? 0 : 1, this.lngs, 0, size);
            System.arraycopy(lats, first ? 0 : 1, this.lats, 0, size);

            for (int i = 0; i < size; i++) {
                int lng = this.lngs[i];
                int lat = this.lats[i];

                if (i == 0) {
                    maxX = lng;
                    minX = lng;

                    maxY = lat;
                    minY = lat;
                } else {
                    maxX = Math.max(maxX, lng);
                    minX = Math.min(minX, lng);

                    maxY = Math.max(maxY, lat);
                    minY = Math.min(minY, lat);
                }
            }
        }
    }

}
