package com.paladin.demo.service.org;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.paladin.common.model.sys.SysAttachment;
import com.paladin.common.model.sys.SysUser;
import com.paladin.common.service.sys.SysAttachmentService;
import com.paladin.common.service.sys.SysUserService;
import com.paladin.demo.core.DataPermissionParam;
import com.paladin.demo.core.DataPermissionUtil;
import com.paladin.demo.mapper.org.OrgPersonnelMapper;
import com.paladin.demo.model.org.OrgPersonnel;
import com.paladin.demo.service.org.dto.OrgPersonnelDTO;
import com.paladin.demo.service.org.dto.OrgPersonnelQuery;
import com.paladin.demo.service.org.vo.OrgPersonnelVO;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.PageResult;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class OrgPersonnelService extends ServiceSupport<OrgPersonnel, OrgPersonnelMapper> {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysAttachmentService attachmentService;

    @Transactional
    public String savePersonnel(OrgPersonnelDTO orgPersonnelDTO) {
        OrgPersonnel orgPersonnel = SimpleBeanCopyUtil.simpleCopy(orgPersonnelDTO, new OrgPersonnel());

        String account = orgPersonnel.getAccount();
        String id = UUIDUtil.createUUID();
        orgPersonnel.setId(id);

        List<SysAttachment> attachments = attachmentService.createAndGetAttachment1(
                orgPersonnelDTO.getAttachment(), orgPersonnelDTO.getAttachmentFiles());
        if (attachments != null && attachments.size() > 5) {
            throw new BusinessException("附件数量不能超过5个");
        }
        attachmentService.persistAttachments(id, attachments);

        orgPersonnel.setAttachment(attachmentService.joinAttachmentId(attachments));

        String password = sysUserService.createUserAccount(account, id, SysUser.USER_TYPE_PERSONNEL);
        save(orgPersonnel);
        return password;
    }

    @Transactional
    public void updatePersonnel(OrgPersonnelDTO orgPersonnelDTO) {
        String id = orgPersonnelDTO.getId();
        OrgPersonnel orgPersonnel = getWhole(id);
        if (orgPersonnel == null) {
            throw new BusinessException("找不到需要更新的人员");
        }

        List<SysAttachment> attachments = attachmentService.createAndGetAttachment1(
                orgPersonnelDTO.getAttachment(), orgPersonnelDTO.getAttachmentFiles());

        if (attachments != null && attachments.size() > 5) {
            throw new BusinessException("附件数量不能超过5个");
        }

        String originAttIds = orgPersonnel.getAttachment();
        if (StringUtil.isNotEmpty(originAttIds)) {
            attachmentService.deleteAttachments(originAttIds.split(","));
        }

        attachmentService.persistAttachments(id, attachments);
        orgPersonnel.setAttachment(attachmentService.joinAttachmentId(attachments));

        String originAccount = orgPersonnel.getAccount();
        String account = orgPersonnelDTO.getAccount();

        if (!account.equals(originAccount)) {
            // 账号不一样需要更新账号
            sysUserService.updateUserAccount(id, account);
        }

        SimpleBeanCopyUtil.simpleCopy(orgPersonnelDTO, orgPersonnel);

        updateWhole(orgPersonnel);
    }

    /**
     * 删除人员
     *
     * @param id
     */
    @Transactional
    public void removePersonnel(String id) {
        OrgPersonnel orgPersonnel = get(id);
        if (orgPersonnel != null) {
            // 删除用户所有附件
            attachmentService.deleteAttachmentsByUser(id);
        }
        deleteById(id);
    }

    public PageResult<OrgPersonnelVO> findPersonnelPage(OrgPersonnelQuery query) {
        DataPermissionParam dataPermissionParam = DataPermissionUtil.getDataPermissionParam();
        if (dataPermissionParam.isHasPermission()) {
            Page<OrgPersonnelVO> page = PageHelper.offsetPage(query.getOffset(), query.getLimit());
            List<OrgPersonnelVO> result = getSqlMapper().findPersonnel(query, dataPermissionParam);
            return new PageResult<>(page, result);
        }
        return PageResult.getEmptyPageResult(query.getLimit());
    }

    public List<OrgPersonnelVO> findPersonnelList(OrgPersonnelQuery query) {
        DataPermissionParam dataPermissionParam = DataPermissionUtil.getDataPermissionParam();
        if (dataPermissionParam.isHasPermission()) {
            return getSqlMapper().findPersonnel(query, dataPermissionParam);
        }
        return Collections.EMPTY_LIST;
    }
}