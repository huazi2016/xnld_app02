package com.zxqy.xunilaidian.entity;

import java.io.Serializable;

/**
 * 反馈类型
 */
public class FeedTypeEntity implements Serializable {
    private String type; // bug类型
    private boolean checked; // 是否选中

    public FeedTypeEntity(String type, boolean checked) {
        this.type = type;
        this.checked = checked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
