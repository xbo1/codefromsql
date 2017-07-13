package com.bob.model;

/**
 * Created by bob on 2017/7/13.
 */
public class SQLField {
    private String name; //字段名
    private String comment;
    private String type;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if (comment != null && comment.length() > 0) {
            return name+": "+type+" comment: "+comment;
        }
        return name+": "+type;
    }
}
