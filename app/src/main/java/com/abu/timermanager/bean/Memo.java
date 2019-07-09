package com.abu.timermanager.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 备忘录
 */
public class Memo extends LitePalSupport {

    private String content;             //内容
    private String title;               //标题
    private String createTime;          //创建时间
    private String remindTime;          //提醒时间

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", createTime='" + createTime + '\'' +
                ", remindTime='" + remindTime + '\'' +
                '}';
    }
}
