package com.paladin.data.generate.build;

import com.paladin.data.generate.GenerateTableOption;

public interface FileBuilder {
	
	public String buildContent(GenerateTableOption tableOption);
	
	public BuilderType getBuilderType();
	
	public void buildFile(GenerateTableOption tableOption, String projectPath);
	
	
}
