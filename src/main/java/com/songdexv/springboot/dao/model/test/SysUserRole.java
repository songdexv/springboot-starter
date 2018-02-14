package com.songdexv.springboot.dao.model.test;

import javax.persistence.*;

@Table(name = "sys_user_role")
public class SysUserRole {
    @Column(name = "sys_user_id")
    private Long sysUserId;

    @Column(name = "role_name")
    private String roleName;

    /**
     * @return sys_user_id
     */
    public Long getSysUserId() {
        return sysUserId;
    }

    /**
     * @param sysUserId
     */
    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    /**
     * @return role_name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}