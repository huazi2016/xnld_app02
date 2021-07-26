package com.zxqy.xunilaidian.bean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class FeedMsgBean extends LitePalSupport {
    String id;
    List<String> feedIdList;

    public List<String> getFeedIdList() {
        return feedIdList;
    }

    public void setFeedIdList(List<String> feedIdList) {
        this.feedIdList = feedIdList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
