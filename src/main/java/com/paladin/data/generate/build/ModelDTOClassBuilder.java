package com.paladin.data.generate.build;

import org.springframework.stereotype.Component;

import com.paladin.data.generate.GenerateTableOption;


@Component
public class ModelDTOClassBuilder extends ModelVOClassBuilder {
	
	@Override
	public BuilderType getBuilderType() {
		return BuilderType.MODEL_DTO;
	}

	@Override
	public String getPackage(GenerateTableOption tableOption) {
		return "service.*.dto";
	}

	@Override
	public String getClassName(GenerateTableOption tableOption) {
		return tableOption.getModelName() + "DTO";
	}
}
