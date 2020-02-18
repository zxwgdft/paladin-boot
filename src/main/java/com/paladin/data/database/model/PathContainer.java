package com.paladin.data.database.model;

import com.paladin.data.struct.ObjectContainer;

public class PathContainer<T> extends ObjectContainer<String, T> {

	public Object getByPath(String path) {
		return getByPath(path.split("\\."), 0);
	}

	public Object getByPath(String[] pathKeys) {
		return getByPath(pathKeys, 0);
	}

	public Object getByPath(String[] pathKeys, int start) {

		if (pathKeys != null && start < pathKeys.length) {
			String key = pathKeys[start];
			T t = getChild(key);

			if (t != null && t instanceof PathContainer && ++start < pathKeys.length)
				return ((PathContainer<?>) t).getByPath(pathKeys, start);
			else
				return t;
		}

		return null;
	}

}
