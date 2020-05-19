package com.paladin.framework.service;

import com.paladin.framework.spring.SpringBeanHelper;
import com.paladin.framework.spring.SpringContainer;

import java.util.*;
import java.util.Map.Entry;

public class DataContainerManager implements SpringContainer {

    private static DataContainerManager manager;

    private static Map<String, DataContainer> containerMap;
    private static List<DataContainer> containers;

    @Override
    public boolean initialize() {
        manager = this;

        Map<String, DataContainer> versionContainerMap = SpringBeanHelper.getBeansByType(DataContainer.class);

        Map<String, DataContainer> containerMap = new HashMap<>();
        List<DataContainer> containers = new ArrayList<>();

        for (Entry<String, DataContainer> entry : versionContainerMap.entrySet()) {
            DataContainer container = entry.getValue();
            String containerId = container.getId();
            containerMap.put(containerId, container);
            containers.add(container);
        }

        Collections.sort(containers, (DataContainer o1, DataContainer o2) -> o1.order() - o2.order());

        for (DataContainer container : containers) {
            container.load();
        }

        DataContainerManager.containerMap = Collections.unmodifiableMap(containerMap);
        DataContainerManager.containers = Collections.unmodifiableList(containers);
        return true;
    }

    public static void reloadContainer(String id) {
        DataContainer container = containerMap.get(id);
        if (container != null) {
            container.reload();
        }
    }

    public static void reloadContainer(Class clazz) {
        reloadContainer(clazz.getName());
    }

    public static void reloadAllContainer() {
        for (DataContainer container : containers) {
            container.reload();
        }
    }

    public static List<DataContainer> getDataContainers() {
        return containers;
    }

}
