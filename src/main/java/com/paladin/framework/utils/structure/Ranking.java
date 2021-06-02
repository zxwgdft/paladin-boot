package com.paladin.framework.utils.structure;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排行工具类
 * <p>
 * 线程安全
 *
 * @author TontoZhou
 * @since 2020/12/31
 */
public class Ranking {

    public final static int ORDER_TYPE_ASC = 1;
    public final static int ORDER_TYPE_DESC = 2;


    private int orderType;

    private RankingNode first;

    private Map<Integer, RankingNode> nodeMap;

    private Object lock = new Object();

    public Ranking(int orderType) {
        this.orderType = orderType;
        this.nodeMap = new HashMap<>();
    }

    public Ranking(int initialCapacity, int orderType) {
        this.orderType = orderType;
        this.nodeMap = new HashMap<>(initialCapacity);
    }

    /**
     * 获取对应值
     */
    public Float getValueById(int id) {
        RankingNode node = nodeMap.get(id);
        return node == null ? null : node.getValue();
    }

    /**
     * 获取排行榜所有数据总和
     */
    public float getTotalValue() {
        synchronized (lock) {
            float total = 0;
            RankingNode node = first;
            while (node != null) {
                total += node.value;
                node = node.next;
            }
            return total;
        }
    }

    /**
     * 获取排行榜
     *
     * @return
     */
    public List<RankingNode> getRanking() {
        return getRanking(-1);
    }

    /**
     * 获取排行榜前多少位
     */
    public List<RankingNode> getRanking(int size) {
        synchronized (lock) {
            List<RankingNode> list = new ArrayList<>(size > 0 ? Math.min(size, nodeMap.size()) : nodeMap.size());
            RankingNode node = first;
            if (size > 0) {
                while (node != null) {
                    list.add(node);
                    if (--size == 0) {
                        break;
                    }
                    node = node.next;
                }
            } else {
                while (node != null) {
                    list.add(node);
                    node = node.next;
                }
            }

            return list;
        }
    }


    /**
     * 如果值缺失则放入值，并排序
     */
    public void putIfAbsent(int id, float value) {
        put(id, value, true);
    }

    /**
     * 放入值，并排序
     */
    public void put(int id, float value) {
        put(id, value, false);
    }

    private void put(int id, float value, boolean absent) {
        synchronized (lock) {
            RankingNode node = nodeMap.get(id);

            if (node == null) {
                node = new RankingNode(id, value);
                nodeMap.put(id, node);

                if (first == null) {
                    first = node;
                } else {
                    if (orderType == ORDER_TYPE_DESC) {
                        // 降序，从头开始查找第一个大于的值
                        backDesc(first, node);
                    } else {
                        // 升序，从头开始查找第一个小于的值
                        backAsc(first, node);
                    }
                }
            } else {

                // 开启缺失才放入值，所以这里直接返回
                if (absent) {
                    return;
                }

                float d = node.value - value;
                if (d == 0) {
                    return;
                } else {
                    node.value = value;

                    if (d > 0) {
                        // 变小
                        if (orderType == ORDER_TYPE_DESC) {
                            // 降序，往后排
                            RankingNode next = node.next;
                            if (next == null || next.value <= value) {
                                // 大多数情况可能并不变化，所以需要单另出这个判断节省效率
                                return;
                            } else {
                                if (node.prev != null) {
                                    node.prev.next = next;
                                    next.prev = node.prev;
                                } else {
                                    first = next;
                                    next.prev = null;
                                }

                                if (next.next == null) {
                                    next.next = node;
                                    node.prev = next;
                                    node.next = null;
                                } else {
                                    backDesc(next.next, node);
                                }
                            }
                        } else {
                            // 升序，往前排
                            RankingNode prev = node.prev;
                            if (prev == null || prev.value <= value) {
                                // 大多数情况可能并不变化，所以需要单另出这个判断节省效率
                                return;
                            } else {
                                if (node.next != null) {
                                    node.next.prev = prev;
                                }
                                prev.next = node.next;

                                if (prev.prev == null) {
                                    prev.prev = node;
                                    node.prev = null;
                                    node.next = prev;
                                    first = node;
                                } else {
                                    forwardAsc(prev.prev, node);
                                }
                            }
                        }
                    } else {
                        // 变大
                        if (orderType == ORDER_TYPE_DESC) {
                            // 降序，往前排
                            RankingNode prev = node.prev;
                            if (prev == null || prev.value >= value) {
                                // 大多数情况可能并不变化，所以需要单另出这个判断节省效率
                                return;
                            } else {
                                if (node.next != null) {
                                    node.next.prev = prev;
                                }
                                prev.next = node.next;

                                if (prev.prev == null) {
                                    prev.prev = node;
                                    node.prev = null;
                                    node.next = prev;
                                    first = node;
                                } else {
                                    forwardDesc(prev.prev, node);
                                }
                            }
                        } else {
                            // 升序，往后排
                            RankingNode next = node.next;
                            if (next == null || next.value >= value) {
                                // 大多数情况可能并不变化，所以需要单另出这个判断节省效率
                                return;
                            } else {
                                if (node.prev != null) {
                                    node.prev.next = next;
                                    next.prev = node.prev;
                                } else {
                                    first = next;
                                    next.prev = null;
                                }

                                if (next.next == null) {
                                    next.next = node;
                                    node.prev = next;
                                    node.next = null;
                                } else {
                                    backAsc(next.next, node);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 倒序往后查找第一个大于的值节点，并插入到节点前
    private void backDesc(RankingNode next, RankingNode node) {
        float value = node.value;
        while (true) {
            if (value > next.value) {
                if (next.prev != null) {
                    next.prev.next = node;
                } else {
                    first = node;
                }

                node.prev = next.prev;
                node.next = next;
                next.prev = node;
                break;
            }

            if (next.next != null) {
                next = next.next;
            } else {
                next.next = node;
                node.prev = next;
                node.next = null;
                break;
            }
        }
    }

    // 倒序往后查找第一个小于的值节点，并插入到节点前
    private void backAsc(RankingNode next, RankingNode node) {
        float value = node.value;
        while (true) {
            if (value < next.value) {
                if (next.prev != null) {
                    next.prev.next = node;
                } else {
                    first = node;
                }

                node.prev = next.prev;
                node.next = next;
                next.prev = node;
                break;
            }

            if (next.next != null) {
                next = next.next;
            } else {
                next.next = node;
                node.prev = next;
                node.next = null;
                break;
            }
        }
    }

    // 倒序向前查找第一个小于的值节点，并插入到节点后
    private void forwardDesc(RankingNode prev, RankingNode node) {
        float value = node.value;
        while (true) {
            if (value < prev.value) {
                if (prev.next != null) {
                    prev.next.prev = node;
                }

                node.prev = prev;
                node.next = prev.next;
                prev.next = node;
                break;
            }

            if (prev.prev != null) {
                prev = prev.prev;
            } else {
                prev.prev = node;
                node.prev = null;
                node.next = prev;
                first = node;
                break;
            }
        }
    }

    // 倒序向前查找第一个大于的值节点，并插入到节点后
    private void forwardAsc(RankingNode prev, RankingNode node) {
        float value = node.value;
        while (true) {
            if (value > prev.value) {
                if (prev.next != null) {
                    prev.next.prev = node;
                }

                node.prev = prev;
                node.next = prev.next;
                prev.next = node;
                break;
            }

            if (prev.prev != null) {
                prev = prev.prev;
            } else {
                prev.prev = node;
                node.prev = null;
                node.next = prev;
                first = node;
                break;
            }
        }
    }


    public static class RankingNode {

        @JsonIgnore
        private RankingNode prev;
        @JsonIgnore
        private RankingNode next;

        private int id;
        private float value;

        public RankingNode(int id, float value) {
            this.id = id;
            this.value = value;
        }

        public float getValue() {
            return value;
        }

        public int getId() {
            return id;
        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        RankingNode node = first;
        while (true) {
            sb.append(node.id).append("(").append(node.value).append(")");
            node = node.next;
            if (node == null) {
                break;
            } else {
                sb.append("->");
            }
        }
        sb.append("]");
        return sb.toString();
    }


}
