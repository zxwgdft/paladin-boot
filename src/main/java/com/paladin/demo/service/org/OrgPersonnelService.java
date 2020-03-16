package com.paladin.demo.service.org;

import com.paladin.common.model.sys.SysAttachment;
import com.paladin.common.model.sys.SysUser;
import com.paladin.common.service.sys.SysAttachmentService;
import com.paladin.common.service.sys.SysUserService;
import com.paladin.demo.model.org.OrgPersonnel;
import com.paladin.demo.service.org.dto.OrgPersonnelDTO;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.ServiceSupport;
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
}