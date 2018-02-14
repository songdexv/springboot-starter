package com.songdexv.springboot.config.security;

import java.util.List;

/**
 * security获取用户和url对应的角色
 * <p>
 * Created by songdexv on 2017/8/7.
 */
public interface RoleService {
    /**
     * 查询user对应的所有的role，如果不存在，且存在defaultroleName，则表示需要添加默认
     *
     * @param userName
     * @param defaultRoleName
     *
     * @return
     */
    List<String> getAllRolesOfUserAndAddDefaultIfNull(String userName, String defaultRoleName);

    /**
     * 查询url对应的所有的url
     *
     * @param url
     *
     * @return
     */
    List<String> getAllRolesOfURL(String url);
}
