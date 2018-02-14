package com.songdexv.springboot.config.security;

import javax.servlet.Filter;

/**
 * 用户自定义的filter的属性
 *
 * Created by songdexv on 2017/8/7.
 */
public class CustomizedFilter {
    private Filter filter; // 插入的filter

    private boolean before; // 在positionFlag的前面

    private Class positionFlag; // 插入的 下标index

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public boolean isBefore() {
        return before;
    }

    public void setBefore(boolean before) {
        this.before = before;
    }

    public Class getPositionFlag() {
        return positionFlag;
    }

    public void setPositionFlag(Class positionFlag) {
        this.positionFlag = positionFlag;
    }
}
