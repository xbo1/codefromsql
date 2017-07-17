package com.bob.model;

import java.util.List;
import java.util.Map;

/**
 * Created by bob on 2017/7/13.
 */
public class SQLTable {
    private boolean ifNotExist; //无用
    private String name; //tablename
    private String comment;
    private List<SQLField> fields;
    private Map<String, String> options;

    private String shortName;
    public String getShortName() {
        return shortName;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        this.comment = this.comment.replaceAll("\'", "");
    }

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
        this.name = name.replaceAll("`", "");
        if (this.name.startsWith("ims_")) {
            this.name = this.name.substring("ims_".length());
        }
        String[] ss = this.name.split("_");
        shortName = ss[ss.length-1];
    }

    public List<SQLField> getFields() {
        return fields;
    }

    public void setFields(List<SQLField> fields) {
        this.fields = fields;
    }

    public  Map<String, String> getOptions() {
        return options;
    }

    public void setOptions( Map<String, String> options) {
        this.options = options;
        if (options.get("comment") != null) {
            setComment(options.get("comment"));
        }
    }

    @Override
    public String toString() {
        String ret = "Table: "+name+"\n";
        String strFields = "Fields:\n";
        if (fields != null) {
            for(SQLField field : fields) {
                strFields = strFields+field.toString()+"\n";
            }
        }
        String strOpts = "Options:\n ";
        if (options != null) {
            for (String key : options.keySet()) {
                strOpts = strOpts+key+": "+options.get(key)+"\n";
            }
        }
        return ret+strFields+strOpts;
    }
}
