package com.paladin.data.generate.build;

import com.paladin.data.generate.GenerateTableOption;

public interface FileBuilder {

    String buildContent(GenerateTableOption tableOption);

    BuilderType getBuilderType();

    void buildFile(GenerateTableOption tableOption, String projectPath);


}
