package com.paladin.common.model.syst;

import com.paladin.framework.common.UnDeleteBaseModel;
import com.paladin.framework.mybatis.GenIdImpl;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class SysUser extends UnDeleteBaseModel implements Serializable {

    private static final long serialVersionUID = -1534400185542562200L;

    /**
     * 启用状态
     */
    public final static Integer STATE_ENABLED = 1;
    /**
     * 停用状态
     */
    public final static Integer STATE_DISABLED = 0;

    /**
     * 管理员账号
     */
    public final static Integer TYPE_ADMIN = 1;
    /**
     * 应用管理员账号
     */
    public final static Integer TYPE_APP_ADMIN = 3;


    public final static String COLUMN_FIELD_ACCOUNT = "account";

    @Id
    @KeySql(genId = GenIdImpl.class)
    private String id;
    private String account;
    private String password;
    private String salt;
    private String userId;
    private Integer state;
    private Integer type;
    private Date lastLoginTime;
    private Integer isFirstLogin = 1;


}
