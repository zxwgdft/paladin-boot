package com.paladin.framework.service;

import com.paladin.framework.spring.SpringBeanHelper;
import com.paladin.framework.spring.SpringContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;

@Component
public class VersionContainerManager implements SpringContainer {

    private static Logger logger = LoggerFactory.getLogger(VersionContainerManager.class);

    private Map<String, VersionObject> versionObjectMap = new HashMap<>();
    private List<VersionObject> versionObjects = new ArrayList<>();

    @Value("${paladin.cluster:false}")
    private boolean isCluster;

    private VersionContainerDAO versionContainerDAO;

    private static VersionContainerManager manager;

    @Override
    public boolean initialize() {
        manager = this;

        Map<String, VersionContainer> versionContainerMap = SpringBeanHelper.getBeansByType(VersionContainer.class);

        // 尝试去spring寻找bean
        if (versionContainerDAO == null) {
            versionContainerDAO = SpringBeanHelper.getFirstBeanByType(VersionContainerDAO.class);
        }

        for (Entry<String, VersionContainer> entry : versionContainerMap.entrySet()) {
            VersionContainer container = entry.getValue();
            String containerId = container.getId();

            if (versionObjectMap.containsKey(containerId)) {
                logger.warn("===>已经存在版本容器[ID:" + containerId + "]，该容器会被忽略");
                continue;
            }

            VersionObject versionObject = new VersionObject(container);
            versionObjectMap.put(containerId, versionObject);
            versionObjects.add(versionObject);
        }

        if (versionObjects.size() > 0) {
            Collections.sort(versionObjects, new Comparator<VersionObject>() {

                @Override
                public int compare(VersionObject o1, VersionObject o2) {
                    return o1.container.order() - o2.container.order();
                }
            });

            checkVersion();

            if (isCluster) {
                logger.info("===>启动版本容器管理定时任务<===");
                startTimer();
            }
        }

        versionObjects = Collections.unmodifiableList(versionObjects);

        if (versionContainerDAO != null) {
            for (VersionObject vo : versionObjects) {
                String id = vo.getId();
                Long version = versionContainerDAO.getVersion(id);
                if (version == null) {
                    try {
                        versionContainerDAO.saveContainer(id, 0L, new Date());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return true;
    }

    /**
     * 检测版本
     */
    private void checkVersion() {

        for (VersionObject versionObject : versionObjects) {
            Long version = 0L;

            // 尝试去数据库中获取版本号
            if (versionContainerDAO != null) {
                String containerId = versionObject.id;
                version = versionContainerDAO.getVersion(containerId);
                if (version == null) {
                    version = -1L;
                }
            }

            if (versionObject.version == null || versionObject.version < version) {
                if (versionObject.container.versionChangedHandle(version)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("版本容器[" + versionObject.id + "]更新版本号为：" + version);
                    }

                    versionObject.version = version;
                    versionObject.updateTime = new Date();
                }
            } else if (versionObject.version > version) {
                if (versionContainerDAO != null) {
                    versionContainerDAO.updateVersion(versionObject.id, version, versionObject.version, versionObject.updateTime);
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("===>完成一次容器版本检测<===");
        }
    }

    private Timer timer;

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkVersion();
            }
        }, 60 * 1000, 10 * 1000);
    }

    public void allVersionChangedHandle() {
        for (String id : versionObjectMap.keySet()) {
            versionChangedHandle(id);
        }
    }

    public void versionChangedHandle(String containerId) {
        versionChangedHandle(containerId, 1);
    }

    private void versionChangedHandle(String containerId, int times) {
        if (times > 5) {
            return;
        }

        VersionObject versionObject = versionObjectMap.get(containerId);

        if (versionObject != null) {
            long newVersion = 0;
            Date now = new Date();

            if (versionObject.version != null) {
                newVersion = versionObject.version + 1;
            }

            versionObject.container.versionChangedHandle(newVersion);

            // 持久化version到数据库
            if (versionContainerDAO != null) {
                if (versionContainerDAO.updateVersion(containerId, versionObject.version, newVersion, now) > 0) {
                    versionObject.version = newVersion;
                    versionObject.updateTime = now;
                } else {
                    versionChangedHandle(containerId, times + 1);
                }
            } else {
                versionObject.version = newVersion;
                versionObject.updateTime = now;
            }

        } else {
            logger.error("无法找到对应容器[ID:" + containerId + "]");
        }

    }

    public void setVersionContainerDAO(VersionContainerDAO versionContainerDAO) {
        this.versionContainerDAO = versionContainerDAO;
    }

    @Override
    public boolean afterInitialize() {
        return true;
    }

    @Override
    public int order() {
        return 0;
    }

    public static class VersionObject {

        private String id;
        private Long version;
        private Date updateTime;
        private VersionContainer container;

        private VersionObject(VersionContainer container) {
            this.id = container.getId();
            this.container = container;
        }

        public String getId() {
            return id;
        }

        public Long getVersion() {
            return version;
        }

        public VersionContainer getContainer() {
            return container;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

    }

    public static void versionChanged(String containerId) {
        manager.versionChangedHandle(containerId);
    }

    public static void versionChanged() {
        manager.allVersionChangedHandle();
    }

    public static List<VersionObject> getVersionObjects() {
        return manager.versionObjects;
    }

    public static VersionObject getVersionObject(String containerId) {
        return manager.versionObjectMap.get(containerId);
    }

}
