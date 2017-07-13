package com.bob.model;

import java.util.List;
import java.util.Map;

/**
 * Created by bob on 2017/7/13.
 */
public class SQLTable {
    private boolean ifNotExist; //无用
    private String name; //tablename
    private List<SQLField> fields;
    private List<Map<String, String>> options;

    public boolean isIfNotExist() {
        return ifNotExist;
    }

    public void setIfNotExist(boolean ifNotExist) {
        this.ifNotExist = ifNotExist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SQLField> getFields() {
        return fields;
    }

    public void setFields(List<SQLField> fields) {
        this.fields = fields;
    }

    public List<Map<String, String>> getOptions() {
        return options;
    }

    public void setOptions(List<Map<String, String>> options) {
        this.options = options;
    }
}
