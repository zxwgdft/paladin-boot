package com.paladin.common.service.sys;

import com.paladin.common.PaladinProperties;
import com.paladin.common.mapper.sys.SysUserMapper;
import com.paladin.common.model.sys.SysUser;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.*;
import com.paladin.framework.utils.ValidateUtil;
import com.paladin.framework.utils.secure.SecureUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SysUserService extends ServiceSupport<SysUser> {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Resource
    private PaladinProperties paladinProperties;

    /**
     * 创建一个账号
     *
     * @param account
     * @param userId
     * @param type
     * @return
     */
    public int createUserAccount(String account, String userId, Integer type) {

        if (account == null || !validateAccount(account))
            throw new BusinessException("账号不符合规则或者已经存在该账号");

        String salt = SecureUtil.createSalte();
        String password = paladinProperties.getDefaultPassword();
        password = SecureUtil.createPassword(password, salt);

        SysUser user = new SysUser();
        user.setAccount(account);
        user.setPassword(password);
        user.setSalt(salt);
        user.setUserId(userId);
        user.setState(SysUser.STATE_ENABLED);
        user.setType(type);

        return save(user);

    }

    private final static Pattern accountPattern = Pattern.compile("^\\w{6,30}$");
    private final static Pattern passwordPattern = Pattern.compile("^\\w{6,20}$");

    /**
     * 验证账号
     *
     * @param account
     * @return true 可用/false 不可用
     */
    public boolean validateAccount(String account) {
        if (!accountPattern.matcher(account).matches())
            return false;

        if (ValidateUtil.isValidatedAllIdcard(account)) {
            return false;
        }

        Example example = GeneralCriteriaBuilder.buildAnd(SysUser.class, new Condition(SysUser.FIELD_ACCOUNT, QueryType.EQUAL, account));

        // example.and()

        return sysUserMapper.selectCountByExample(example) == 0;
    }

    /**
     * 通过账号查找用户
     *
     * @param account
     * @return
     */
    public SysUser getUserByAccount(String account) {
        List<SysUser> users = searchAll(new Condition(SysUser.FIELD_ACCOUNT, QueryType.EQUAL, account));
        return (users != null && users.size() > 0) ? users.get(0) : null;
    }

    /**
     * 更新登录人密码
     *
     * @param newPassword
     * @param oldPassword
     * @return
     */
    public boolean updateSelfPassword(String newPassword, String oldPassword) {

        if (newPassword == null || !passwordPattern.matcher(newPassword).matches()) {
            throw new BusinessException("密码不符合规则");
        }

        UserSession session = UserSession.getCurrentUserSession();
        String account = session.getAccount();
        SysUser user = getUserByAccount(account);
        if (user == null) {
            throw new BusinessException("账号异常");
        }

        oldPassword = SecureUtil.createPassword(oldPassword, user.getSalt());
        if (!oldPassword.equals(user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }

        String salt = SecureUtil.createSalte();

        SysUser updateUser = new SysUser();
        updateUser.setId(user.getId());
        updateUser.setSalt(salt);
        updateUser.setPassword(newPassword);
        updateUser.setIsFirstLogin(0);// 密码强制修改后该状态值设为0
        updateUser.setUpdateTime(new Date());

        int effect = updateSelective(updateUser);

        if (effect > 0) {
            SecurityUtils.getSubject().logout();
        }

        return effect > 0;
    }

    public void updateLastTime(String account) {
        // 不更新update_time
        SysUser sysUser = getUserByAccount(account);
        SysUser user = new SysUser();
        user.setId(sysUser.getId());
        user.setLastLoginTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(user);
    }

}
