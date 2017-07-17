package com.bob.templ;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bob on 2017/7/14.
 */
public class StringTemplateUtils {
    public static final String DEF_REGEX="\\{\\{(.+?)\\}\\}";

    public static String render(String template, Map<String, String> data) {
        return render(template,data,DEF_REGEX);
    }

    public static String render(String template, Map<String, String> data,String regex) {
        if(template == null || template.length() == 0){
            return "";
        }
        if(regex == null || regex.length() == 0){
            return template;
        }
        if(data == null || data.size() == 0){
            return template;
        }
        try {
            StringBuffer sb = new StringBuffer();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(template);
            while (matcher.find()) {
                String name = matcher.group(1);// 键名
                String value = data.get(name);// 键值
                if (value == null) {value = "";}
                matcher.appendReplacement(sb, value);
            }
            matcher.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;

    }
}
