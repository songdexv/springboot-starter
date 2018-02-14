package com.songdexv.springboot.config.security;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songdexv on 2017/8/7.
 */
public final class CustomizedFiltersInjector {
    final List<CustomizedFilter> list = new ArrayList<CustomizedFilter>();

    public List<CustomizedFilter> add(CustomizedFilter customizedFilter) {
        list.add(customizedFilter);
        return list;
    }

    public List<CustomizedFilter> get() {
        return list;
    }
}
