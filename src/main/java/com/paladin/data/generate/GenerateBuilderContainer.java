package com.paladin.data.generate;

import com.paladin.data.generate.build.BuilderType;
import com.paladin.data.generate.build.FileBuilder;
import com.paladin.data.generate.build.SpringBootClassBuilder;
import com.paladin.data.generate.build.SpringBootPageBuilder;
import com.paladin.framework.spring.SpringBeanHelper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GenerateBuilderContainer implements ApplicationRunner {

    static Map<BuilderType, FileBuilder> builderMap = new HashMap<>();


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, FileBuilder> builders = SpringBeanHelper.getBeansByType(FileBuilder.class);

        for (FileBuilder builder : builders.values()) {
            builderMap.put(builder.getBuilderType(), builder);
        }
    }


    public static FileBuilder getFileContentBuilder(BuilderType type) {
        return builderMap.get(type);
    }

    public static String getClassImportPackage(BuilderType type, GenerateTableOption tableOption) {
        SpringBootClassBuilder classbuilder = (SpringBootClassBuilder) builderMap.get(type);
        if (classbuilder != null) {
            return classbuilder.getImportPackage(tableOption);
        }
        return null;
    }

    public static String getClassName(BuilderType type, GenerateTableOption tableOption) {
        SpringBootClassBuilder classbuilder = (SpringBootClassBuilder) builderMap.get(type);
        if (classbuilder != null) {
            return classbuilder.getClassName(tableOption);
        }
        return null;
    }

    public static String getViewPath(BuilderType type, GenerateTableOption tableOption) {
        SpringBootPageBuilder classbuilder = (SpringBootPageBuilder) builderMap.get(type);
        if (classbuilder != null) {
            return classbuilder.getViewPath(tableOption);
        }
        return null;
    }


}
