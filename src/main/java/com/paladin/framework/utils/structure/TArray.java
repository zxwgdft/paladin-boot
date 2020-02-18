package com.paladin.framework.utils.structure;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TArray<T> {

	T[] values;
	int size;
	int capacity;

	public TArray(T[] values) {
		if (values == null)
			throw new IllegalArgumentException("values can't be null");

		this.values = values;
		this.size = values.length;
		this.capacity = size;
	}

	@SuppressWarnings("unchecked")
	public TArray(Class<T> componentType, int capacity) {
		this.values = (T[]) Array.newInstance(componentType, capacity);
		this.size = 0;
		this.capacity = capacity;
	}

	public void add(T[] arr) {

		if (arr != null) {
			int len = arr.length;

			if (len > 0) {
				int toSize = size + len;
				if (toSize > capacity) {
					capacity = capacity > len ? capacity * 2 : len * 2;
					values = Arrays.copyOf(values, capacity);
				}
				System.arraycopy(arr, 0, values, size, len);
				size = toSize;
			}
		}
	}
	
	/**
	 * 移除元素（源数组中的NULL会被清除）
	 * @param arr
	 */
	public void remove(T[] arr)
	{
		if(arr != null && arr.length>0)
		{
			int count = 0;
			for(int i = 0;i<arr.length;i++)
			{
				T t = arr[i];
				for(int j = 0;j<size;j++)
				{
					if(values[j] == t)
					{
						values[j] =null;
						count++;
					}
				}
			}
			
			if(count>0)
			{
				int s = 0;
				for(int i = 0;i<size;i++)
				{
					T t = values[i];
					if(t==null)
						s++;
					else if(s>0)
						values[i-s] = t;
				}	
				
				size -= count;
			}	
		}
	}
	
	

	public T[] toArray() {
		return Arrays.copyOf(values, size);
	}
	

}
