package com.paladin.demo.service.org;

import com.paladin.common.model.sys.SysAttachment;
import com.paladin.common.model.sys.SysUser;
import com.paladin.common.service.sys.SysAttachmentService;
import com.paladin.common.service.sys.SysUserService;
import com.paladin.demo.core.DemoUserSession;
import com.paladin.demo.model.org.OrgPersonnel;
import com.paladin.demo.service.org.dto.OrgPersonnelDTO;
import com.paladin.demo.service.org.dto.OrgPersonnelQuery;
import com.paladin.demo.service.org.dto.PersonnelPermissionQuery;
import com.paladin.demo.service.org.vo.OrgPersonnelVO;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.PageResult;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrgPersonnelService extends ServiceSupport<OrgPersonnel> {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysAttachmentService attachmentService;

    @Transactional
    public boolean savePersonnel(OrgPersonnelDTO orgPersonnelDTO) {
        OrgPersonnel orgPersonnel = SimpleBeanCopyUtil.simpleCopy(orgPersonnelDTO, new OrgPersonnel());
        String account = orgPersonnel.getAccount();
        String id = orgPersonnel.getId();

        List<SysAttachment> attachments = attachmentService.mergeAttachments(
                orgPersonnelDTO.getAttachment(), orgPersonnelDTO.getAttachmentFiles());

        if (attachments != null && attachments.size() > 5) {
            throw new BusinessException("附件数量不能超过5个");
        }

        orgPersonnel.setAttachment(attachmentService.splicingAttachmentId(attachments));

        if (sysUserService.createUserAccount(account, id, SysUser.TYPE_USER)) {
            return save(orgPersonnel);
        }

        return false;
    }

    @Transactional
    public boolean updatePersonnel(OrgPersonnelDTO orgPersonnelDTO) {
        String id = orgPersonnelDTO.getId();
        OrgPersonnel orgPersonnel = get(id);
        if (orgPersonnel == null) {
            throw new BusinessException("找不到需要更新的人员[ID:" + id + "]");
        }

        List<SysAttachment> attachments = attachmentService.replaceAndMergeAttachments(
                orgPersonnel.getAttachment(), orgPersonnelDTO.getAttachment(), orgPersonnelDTO.getAttachmentFiles());

        if (attachments != null && attachments.size() > 5) {
            throw new BusinessException("附件数量不能超过5个");
        }

        orgPersonnelDTO.setAttachment(attachmentService.splicingAttachmentId(attachments));

        String originAccount = orgPersonnel.getAccount();
        String account = orgPersonnelDTO.getAccount();

        if (!account.equals(originAccount)) {
            // 账号不一样需要更新账号
            sysUserService.updateUserAccount(id, originAccount, account);
        }

        SimpleBeanCopyUtil.simpleCopy(orgPersonnelDTO, orgPersonnel);
        return update(orgPersonnel);
    }

    /**
     * 删除人员
     *
     * @param id
     * @return
     */
    public boolean removePersonnel(String id) {
        OrgPersonnel orgPersonnel = get(id);
        if (orgPersonnel != null) {
            // 是否要考虑删除附件
            String atts = orgPersonnel.getAttachment();
            if (StringUtil.isNotEmpty(atts)) {
                attachmentService.deleteAttachments(atts.split(","));
            }
        }
        removeByPrimaryKey(id);
        return true;
    }

    public PageResult<OrgPersonnelVO> findPersonnel(OrgPersonnelQuery query) {

        // 增加数据权限过滤
        // 应用管理员级别可以查看所有人员数据
        // 机构管理员可以查看所管辖机构及以下单位人员数据
        // 个人只能查看自己的数据
        DemoUserSession userSession = DemoUserSession.getCurrentUserSession();
        int roleLevel = userSession.getRoleLevel();

        PersonnelPermissionQuery permissionQuery = null;
        if (roleLevel >= DemoUserSession.ROLE_LEVEL_APP_ADMIN) {
            // 可以查询所有，不设置过滤条件
        } else {
            permissionQuery = new PersonnelPermissionQuery();
            if (roleLevel >= DemoUserSession.ROLE_LEVEL_UNIT_ADMIN) {
                String unitId = userSession.getUnitId();
                if (unitId != null && unitId.length() > 0) {
                    OrgUnitContainer.Unit unit = OrgUnitContainer.getUnit(unitId);
                    if (unit != null) {
                        List<String> ids = unit.getSelfAndChildrenIds();
                        if (ids.size() == 1) {
                            permissionQuery.setUnitId(ids.get(0));
                        } else {
                            permissionQuery.setUnitIds(ids);
                        }
                    } else {
                        permissionQuery.setId(userSession.getUserId());
                    }
                } else {
                    permissionQuery.setId(userSession.getUserId());
                }
            } else {
                permissionQuery.setId(userSession.getUserId());
            }
        }

        // 多个查询条件可以数组或列表组合，他们会在同一个标准下（Example.Criteria）
        return searchPage(query.getOffset(), query.getLimit(), OrgPersonnelVO.class,
                permissionQuery == null ? query : new Object[]{query, permissionQuery});
    }


}