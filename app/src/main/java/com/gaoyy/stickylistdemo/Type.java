package com.gaoyy.stickylistdemo;

/**
 * Created by gaoyy on 2017/8/4 0004.
 */

public class Type
{
    private int status;
    private String type;

    public Type(int status, String type)
    {
        this.status = status;
        this.type = type;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "Type{" +
                "status=" + status +
                ", type='" + type + '\'' +
                '}';
    }
}
