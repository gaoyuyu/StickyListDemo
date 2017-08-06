package com.gaoyy.stickylistdemo;

/**
 * Created by gaoyy on 2017/8/4 0004.
 */

public class Sample
{
    //item id
    private String id;
    //所属组id
    private String groupId;
    //item title
    private String title;
    //item desc
    private String desc;
    //组 title
    private String groupTitle;

    //数量
    private int count;

    public Sample(String id, String groupId, String title, String desc, String groupTitle,int count)
    {
        this.id = id;
        this.groupId = groupId;
        this.title = title;
        this.desc = desc;
        this.groupTitle = groupTitle;
        this.count= count;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getGroupTitle()
    {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle)
    {
        this.groupTitle = groupTitle;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    @Override
    public String toString()
    {
        return "Sample{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", groupTitle='" + groupTitle + '\'' +
                ", count=" + count +
                '}';
    }
}
