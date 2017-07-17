package com.bob.model;

/**
 * Created by bob on 2017/7/13.
 */
public class SQLField {
    private String name; //字段名
    private String comment;
    private String type;
    private boolean autoIncrement;
    private boolean notNull = false;
    private String defValue;

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.name = this.name.replaceAll("`", "");
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        this.comment = this.comment.replaceAll("\'", "");
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
