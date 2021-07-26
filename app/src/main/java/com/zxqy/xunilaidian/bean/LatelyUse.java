package com.zxqy.xunilaidian.bean;

import org.litepal.crud.LitePalSupport;

public class LatelyUse  extends LitePalSupport {
    String path;
    String name;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
