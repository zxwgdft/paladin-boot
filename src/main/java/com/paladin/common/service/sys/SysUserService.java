package com.paladin.common.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paladin.common.mapper.sys.SysUserMapper;
import com.paladin.common.model.sys.SysUser;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.service.UserSession;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.framework.utils.secure.SecureUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SysUserService extends ServiceSupport<SysUser, SysUserMapper> {

    @Value("${paladin.default-password:1}")
    private String defaultPassword;

    @Value("${paladin.default-password-random:false}")
    private boolean randomPassword;

    private Pattern accountPattern = Pattern.compile("^\\w{6,30}$");
    private Pattern passwordPattern = Pattern.compile("^\\w{6,20}$");

    // 获取默认密码（随机或固定）
    private String getDefaultPassword() {
        if (randomPassword) {
            return UUIDUtil.createUUID().substring(0, 10);
        }
        return defaultPassword;
    }

    /**
     * 创建一个账号，并返回密码
     */
    public String createUserAccount(String account, String userId, Integer type) {
        if (account == null || !validateAccount(account)) {
            throw new BusinessException("账号不符合规则或者已经存在该账号");
        }

        String salt = SecureUtil.createSalt();

        String originPassword = getDefaultPassword();
        String password = SecureUtil.hashByMD5(originPassword, salt);

        SysUser user = new SysUser();
        user.setAccount(account);
        user.setPassword(password);
        user.setSalt(salt);
        user.setUserId(userId);
        user.setState(SysUser.STATE_ENABLED);
        user.setUserType(type);
        user.setCreateTime(new Date());
        save(user);

        return originPassword;
    }


    /**
     * 验证账号
     */
    public boolean validateAccount(String account) {
        if (!accountPattern.matcher(account).matches()) {
            return false;
        }
        return searchCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, account)
        ) == 0;
    }

    /**
     * 通过账号查找用户
     *
     * @param account
     * @return
     */
    public SysUser getUserByAccount(String account) {
        List<SysUser> users = findList(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, account));
        return (users != null && users.size() > 0) ? users.get(0) : null;
    }

    /**
     * 更新登录人密码
     *
     * @param newPassword
     * @param oldPassword
     * @return
     */
    public void updateSelfPassword(String newPassword, String oldPassword) {

        if (newPassword == null || !passwordPattern.matcher(newPassword).matches()) {
            throw new BusinessException("密码不符合规则");
        }

        UserSession session = UserSession.getCurrentUserSession();
        String account = session.getAccount();
        SysUser user = getUserByAccount(account);
        if (user == null) {
            throw new BusinessException("账号异常");
        }

        oldPassword = SecureUtil.hashByMD5(oldPassword, user.getSalt());
        if (!oldPassword.equals(user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }

        String salt = SecureUtil.createSalt();
        newPassword = SecureUtil.hashByMD5(newPassword, salt);

        SysUser updateUser = new SysUser();
        updateUser.setId(user.getId());
        updateUser.setSalt(salt);
        updateUser.setPassword(newPassword);
        updateUser.setUpdateTime(new Date());

        if (updateSelection(updateUser)) {
            SecurityUtils.getSubject().logout();
        }

        throw new BusinessException("密码修改失败");
    }


    /**
     * 更新个人用户账号
     */
    public void updateUserAccount(String userId, String newAccount) {
        getSqlMapper().updateAccount(userId, newAccount);
    }


    public void updateLastLoginTime(String userId) {
        getSqlMapper().updateLastLoginTime(userId);
    }

}
