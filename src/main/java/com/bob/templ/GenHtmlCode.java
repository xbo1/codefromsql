package com.bob.templ;

import com.bob.model.SQLField;
import com.bob.model.SQLTable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bob on 2017/7/14.
 */
public class GenHtmlCode {
    private String toHtmlInputType(String type) {
        if (type == null || type.length() == 0){
            return "text";
        }
        if (type.compareToIgnoreCase("int") == 0) {
            return "number";
        }
        if (type.compareToIgnoreCase("float") == 0) {
            return "number";
        }
        return "text";
    }
    private String genPostHtml(SQLTable table) {
        ClassLoader classLoader = getClass().getClassLoader();
        BufferedInputStream bufferedInputStream = null;
        StringBuilder sbtemp = new StringBuilder();
        try {
            bufferedInputStream = (BufferedInputStream) classLoader.getResource("static/phpwe7/postitem.html").getContent();
            byte[] bs = new byte[1024*4];
            int len = 0;
            while ((len= bufferedInputStream.read(bs)) != -1) {
                sbtemp.append(new String(bs,0,len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String template = sbtemp.toString();
        List<SQLField> fields = table.getFields();
        List<String> strFields = new ArrayList<>();
        String tab = "\t";
        String newline = "\n";
        for (SQLField field : fields) {
            if (field.isAutoIncrement()) {
                continue;
            }
            if (field.getName().compareToIgnoreCase("uniacid") == 0) {
                continue;
            }
            Map<String, String> data = new HashMap<String, String>();
            data.put("fieldname", field.getComment());
            data.put("field", field.getName());
            data.put("type", toHtmlInputType(field.getType()));
            strFields.add(StringTemplateUtils.render(template,data));
        }
        String out = String.join(newline,strFields);
        return out.replaceAll("\\$", "RDS_CHAR_DOLLAR");
    }
    public String generate(SQLTable table) {
        ClassLoader classLoader = getClass().getClassLoader();
        BufferedInputStream bufferedInputStream = null;
        StringBuilder sbtemp = new StringBuilder();
        try {
            bufferedInputStream = (BufferedInputStream) classLoader.getResource("static/phpwe7/template.html").getContent();
            byte[] bs = new byte[1024*4];
            int len = 0;
            while ((len= bufferedInputStream.read(bs)) != -1) {
                sbtemp.append(new String(bs,0,len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String template = sbtemp.toString();
        Map<String, String> data = new HashMap<String, String>();
        data.put("tablename", table.getName());
        data.put("title", table.getComment());
        data.put("defname", table.getShortName());
        data.put("posthtml", genPostHtml(table));
        data.put("tbtitle", genListTitleHtml(table));
        data.put("tbtds", genTdsHtml(table));
        String out = StringTemplateUtils.render(template,data);
        return out.replaceAll("RDS_CHAR_DOLLAR","\\$");
    }

    private String genListTitleHtml(SQLTable table) {
        List<SQLField> fields = table.getFields();
        List<String> strFields = new ArrayList<>();
        String tab = "\t";
        String newline = "\n";
        for (SQLField field : fields) {
            if (field.isAutoIncrement()) {
                continue;
            }
            if (field.getName().compareToIgnoreCase("uniacid") == 0) {
                continue;
            }
            String str = "<th width=\"100\">"+field.getComment()+"</th>";
            strFields.add(str);
        }
        String out = String.join(newline+tab+tab+tab+tab+tab+tab,strFields);
        return out;
    }

    private String genTdsHtml(SQLTable table) {
        List<SQLField> fields = table.getFields();
        List<String> strFields = new ArrayList<>();
        String tab = "\t";
        String newline = "\n";
        for (SQLField field : fields) {
            if (field.isAutoIncrement()) {
                continue;
            }
            if (field.getName().compareToIgnoreCase("uniacid") == 0) {
                continue;
            }

            String str = "<td>{$li['"+field.getName()+"']}</td>";
            strFields.add(str);
        }
        String out = String.join(newline+tab+tab+tab+tab+tab+tab,strFields);
        return out.replaceAll("\\$", "RDS_CHAR_DOLLAR");
    }
}
