package com.paladin.demo.mapper.org;

import com.paladin.demo.core.DataPermissionParam;
import com.paladin.demo.model.org.OrgPersonnel;
import com.paladin.demo.service.org.dto.OrgPersonnelQuery;
import com.paladin.demo.service.org.vo.OrgPersonnelVO;
import com.paladin.framework.service.mybatis.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrgPersonnelMapper extends CommonMapper<OrgPersonnel> {
    List<OrgPersonnelVO> findPersonnel(@Param("query") OrgPersonnelQuery query, @Param("permission") DataPermissionParam permission);
}