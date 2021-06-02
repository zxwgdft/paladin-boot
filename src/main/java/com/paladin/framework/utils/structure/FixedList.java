package com.paladin.framework.utils.structure;

import java.util.*;

/**
 * 固定列表
 * <p>
 * 主要用于固定元素为满足方法参数要求以List形式传递参数，例如单个元素的List
 *
 * @author TontoZhou
 * @since 2020/7/15
 */
public class FixedList<E> implements List<E> {

    private int size;
    private List<E> list;

    public FixedList(E... elements) {
        if (elements == null || elements.length == 0) {
            throw new IllegalArgumentException("参数不能为空");
        }
        size = elements.length;
        list = Arrays.asList(elements);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(E e) {
        throw new RuntimeException("Not Support Method");
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("Not Support Method");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new RuntimeException("Not Support Method");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new RuntimeException("Not Support Method");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new RuntimeException("Not Support Method");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new RuntimeException("Not Support Method");
    }

    @Override
    public void clear() {
        throw new RuntimeException("Not Support Method");
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        throw new RuntimeException("Not Support Method");
    }

    @Override
    public void add(int index, E element) {
        throw new RuntimeException("Not Support Method");
    }

    @Override
    public E remove(int index) {
        throw new RuntimeException("Not Support Method");
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
