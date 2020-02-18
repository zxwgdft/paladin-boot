package com.paladin.common.core.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paladin.common.model.syst.SysConstant;
import com.paladin.common.service.syst.SysConstantService;
import com.paladin.framework.core.VersionContainer;
import com.paladin.framework.core.VersionContainerManager;

@Component
public class ConstantsContainer implements VersionContainer {

	@Autowired
	private SysConstantService constantService;

	private static Map<String, List<KeyValue>> constantMap = new HashMap<>();
	private static Map<String, Map<String, String>> constantValueMap = new HashMap<>();
	private static Map<String, Map<String, String>> constantKeyMap = new HashMap<>();

	private static Map<String, List<KeyValue>> otherConstantMap = new HashMap<>();

	public synchronized boolean initialize(boolean needReadTable) {

		if (needReadTable) {
			Map<String, List<KeyValue>> enumConstantMap = new HashMap<>();
			Map<String, Map<String, String>> constantValueMap = new HashMap<>();
			Map<String, Map<String, String>> constantKeyMap = new HashMap<>();

			List<SysConstant> constants = constantService.findAll();
			for (SysConstant constant : constants) {
				String type = constant.getType();
				List<KeyValue> kvList = enumConstantMap.get(type);
				if (kvList == null) {
					kvList = new ArrayList<>();
					enumConstantMap.put(type, kvList);
				}

				String code = constant.getCode();
				String name = constant.getName();

				kvList.add(new KeyValue(code, name));

				Map<String, String> valueMap = constantValueMap.get(type);
				if (valueMap == null) {
					valueMap = new HashMap<>();
					constantValueMap.put(type, valueMap);
				}

				valueMap.put(name, code);

				Map<String, String> keyMap = constantKeyMap.get(type);
				if (keyMap == null) {
					keyMap = new HashMap<>();
					constantKeyMap.put(type, keyMap);
				}

				keyMap.put(code, name);
			}

			ConstantsContainer.constantMap = enumConstantMap;
			ConstantsContainer.constantValueMap = constantValueMap;
			ConstantsContainer.constantKeyMap = constantKeyMap;
		}

		for (Entry<String, List<KeyValue>> entry : otherConstantMap.entrySet()) {
			String type = entry.getKey();
			List<KeyValue> kvs = entry.getValue();

			Map<String, String> valueMap = ConstantsContainer.constantValueMap.get(type);
			if (valueMap == null) {
				valueMap = new HashMap<>();
				ConstantsContainer.constantValueMap.put(type, valueMap);
			} else {
				valueMap.clear();
			}

			Map<String, String> keyMap = ConstantsContainer.constantKeyMap.get(type);
			if (keyMap == null) {
				keyMap = new HashMap<>();
				ConstantsContainer.constantKeyMap.put(type, keyMap);
			} else {
				keyMap.clear();
			}

			for (KeyValue kv : kvs) {
				String code = kv.getKey();
				String name = kv.getValue();

				valueMap.put(name, code);
				keyMap.put(code, name);
			}

			ConstantsContainer.constantMap.put(type, kvs);
		}

		return true;
	}

	@Override
	public String getId() {
		return "constants_container";
	}

	@Override
	public boolean versionChangedHandle(long version) {
		initialize(true);
		container = this;
		return true;
	}

	public void putConstant(String typeCode, List<KeyValue> list) {
		otherConstantMap.put(typeCode, list);
		initialize(false);
	}

	private static ConstantsContainer container;

	public static void updateData() {
		VersionContainerManager.versionChanged(container.getId());
	}

	public static Map<String, List<KeyValue>> getTypeChildren(String... typeCodes) {
		Map<String, List<KeyValue>> result = new HashMap<>();
		for (String typeCode : typeCodes) {
			result.put(typeCode, constantMap.get(typeCode));
		}
		return result;
	}

	public static List<KeyValue> getType(String typeCode) {
		return constantMap.get(typeCode);
	}

	public static List<KeyValue> getTypes(String typeCode, String... keys) {
		if (keys != null && keys.length > 0) {
			List<KeyValue> typeList = constantMap.get(typeCode);
			if (typeList != null) {
				List<KeyValue> result = new ArrayList<>(keys.length);
				for (String key : keys) {
					for (KeyValue kv : typeList) {
						if (kv.getKey().equals(key)) {
							result.add(kv);
							break;
						}
					}
				}
				return result;
			}
		}
		return null;
	}

	/**
	 * 根据类型和名称得到常量KEY
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	public static String getTypeKey(String type, String name) {
		if (name == null || name.length() == 0) {
			return null;
		}
		Map<String, String> valueMap = constantValueMap.get(type);
		if (valueMap != null) {
			return valueMap.get(name);
		}
		return null;
	}

	/**
	 * 获取所有key集合
	 * 
	 * @return
	 */
	public static Collection<String> getAllKey() {
		return (Collection<String>) constantMap.keySet();
	}

	/**
	 * 根据类型和编码得到常量值
	 * 
	 * @param type
	 * @param key
	 * @return
	 */
	public static String getTypeValue(String type, String key) {
		if (key == null) {
			return null;
		}
		Map<String, String> keyMap = constantKeyMap.get(type);
		if (keyMap != null) {
			return keyMap.get(key);
		}
		return null;
	}

	public final static class KeyValue {
		private String key;
		private String value;

		public KeyValue(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public KeyValue(int key, String value) {
			this.key = String.valueOf(key);
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}

}
