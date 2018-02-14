package com.songdexv.springboot.config.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songdexv.springboot.constant.Constant;
import com.songdexv.springboot.dao.mapper.test.SysUrlRoleMapper;
import com.songdexv.springboot.dao.mapper.test.SysUserMapper;
import com.songdexv.springboot.dao.mapper.test.SysUserRoleMapper;
import com.songdexv.springboot.dao.model.test.SysUrlRole;
import com.songdexv.springboot.dao.model.test.SysUser;
import com.songdexv.springboot.dao.model.test.SysUserRole;
import com.songdexv.springboot.dao.model.test.UrlRole;

import tk.mybatis.mapper.entity.Example;

/**
 * Created by songdexv on 2017/8/4.
 */
@Service
public class SecurityRoleService implements RoleService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUrlRoleMapper sysUrlRoleMapper;

    @Override
    public List<String> getAllRolesOfUserAndAddDefaultIfNull(String userName, String defaultRoleName) {
        List<String> roleList = new ArrayList<>();
        roleList.add(Constant.DEFAULT_ROLE_NAME);
        Example example = new Example(SysUser.class);
        example.createCriteria().andEqualTo("name", userName);
        List<SysUser> userList = sysUserMapper.selectByExample(example);

        if (userList.size() > 0) {
            SysUser sysUser = userList.get(0);
            example = new Example(SysUserRole.class);
            example.createCriteria().andEqualTo("sys_user_id", sysUser.getId());
            List<SysUserRole> roleIds = sysUserRoleMapper.selectByExample(example);
            if (roleIds != null && !roleIds.isEmpty()) {
                Set<String> roleSet = new HashSet<>();
                for (SysUserRole role : roleIds) {
                    roleSet.add(role.getRoleName());
                }
                roleList.addAll(new ArrayList<>(roleSet));
            }

        }
        return roleList;
    }

    @Override
    public List<String> getAllRolesOfURL(String url) {
        List<String> roleList = new ArrayList<>();
        roleList.add(Constant.DEFAULT_ROLE_NAME);
        Example example = new Example(UrlRole.class);
        example.createCriteria().andEqualTo("url", url);
        List<SysUrlRole> urlRoles = sysUrlRoleMapper.selectByExample(example);
        if (urlRoles.size() > 0) {
            Set<String> roleSet = new HashSet<>();
            for (SysUrlRole role : urlRoles) {
                roleSet.add(role.getRoleName());
            }
            roleList.addAll(new ArrayList<>(roleSet));
        }
        return roleList;
    }
}
