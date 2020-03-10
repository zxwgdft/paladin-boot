package com.paladin.demo.service.org;

import com.paladin.demo.model.org.OrgUnit;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.OrderType;
import com.paladin.framework.service.QueryOrderBy;
import com.paladin.framework.service.ServiceSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@QueryOrderBy(property = {OrgUnit.FIELD_ORDER_NO}, type = {OrderType.ASC})  // 查询列表时默认按照排序号排序
public class OrgUnitService extends ServiceSupport<OrgUnit> {

    @Transactional
    public boolean saveUnit(OrgUnit model) {
        save(model);
        OrgUnitContainer.updateData();
        return true;
    }

    @Transactional
    public boolean updateUnit(OrgUnit model) {
        update(model);
        OrgUnitContainer.updateData();
        return true;
    }

    @Transactional
    public boolean removeUnit(String id) {
        OrgUnitContainer.Unit unit = OrgUnitContainer.getUnit(id);
        if (unit == null) {
            throw new BusinessException("找不到对应删除的单位");
        }

        if (unit.getChildren().size() > 0) {
            throw new BusinessException("请先删除该单位下所有子单位");
        }

        removeByPrimaryKey(id);
        OrgUnitContainer.updateData();
        return true;
    }
}