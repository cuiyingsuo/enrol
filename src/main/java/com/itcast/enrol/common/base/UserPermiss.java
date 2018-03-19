package com.itcast.enrol.common.base;

import java.io.Serializable;
import java.util.List;

/**
 * 用户权限：导航菜单权限（type=1,2）；页面操作权限（type=3）；业务数据权限（由业务功能来实现）；
 */
public class UserPermiss implements Serializable{

    private String currentName;

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    /**
     * 用户菜单权限；（type=1,2）;
     */
    List<BaseMenu> userMenus;
    /**
     * 用户操作权限；（type=3）
     */
    List<BaseMenu> userPerms;

    public List<BaseMenu> getUserMenus() {
        return userMenus;
    }

    public void setUserMenus(List<BaseMenu> userMenus) {
        this.userMenus = userMenus;
    }

    public List<BaseMenu> getUserPerms() {
        return userPerms;
    }

    public void setUserPerms(List<BaseMenu> userPerms) {
        this.userPerms = userPerms;
    }
}