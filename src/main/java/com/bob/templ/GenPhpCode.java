package com.bob.templ;

import com.bob.model.SQLField;
import com.bob.model.SQLTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bob on 2017/7/14.
 */
public class GenPhpCode {
	private String genPostData(SQLTable table) {
		List<SQLField> fields = table.getFields();
//        $name = $_GPC['name'];
//        if (empty($name)) {
//            message("请填写店员姓名", "", "error");
//        }
//        $province = $_GPC['province'];
//        $data = array(
//            'uniacid'=>$uniacid,
//            'name'=>$name,
//            'province'=>$province,
//        );
		StringBuilder sb = new StringBuilder();
		List<String> data = new ArrayList<>();
		String tab = "\t";
		String newline = "\n";
		for (SQLField field : fields) {
			if (field.isAutoIncrement()) {
				continue;
			}
			if (field.getName().compareToIgnoreCase("uniacid") == 0) {
				data.add("'uniacid'=>$uniacid,");
				continue;
			}
			String phpName = "$" + field.getName();
			data.add("'" + field.getName() + "'=>" + phpName + ",");
			sb.append(phpName + " = $_GPC['" + field.getName() + "'];");
			sb.append(newline + tab + tab);
			if (field.isNotNull()) {
				sb.append("if (empty(" + phpName + ")) {");
				sb.append(newline + tab + tab + tab);
				sb.append("message(\"请填写" + field.getComment() + "\", \"\", \"error\");");
				sb.append(newline + tab + tab);
				sb.append("}");
				sb.append(newline + tab + tab);
			}
		}
		sb.append("$data = array(");
		sb.append(newline + tab + tab + tab);
		sb.append(String.join(newline + tab + tab + tab, data));
		sb.append(newline + tab + tab + tab);
		sb.append(");");
//        return sb.toString();
		return sb.toString().replaceAll("\\$", "RDS_CHAR_DOLLAR");
	}

	public String generate(SQLTable table) {
		String template = Utilities.readSourceFile("template.php");

		Map<String, String> data = new HashMap<String, String>();
		data.put("tablename", table.getName());
		data.put("defname", table.getShortName());
		data.put("postdata", genPostData(table));
		String out = StringTemplateUtils.render(template, data);
		return out.replaceAll("RDS_CHAR_DOLLAR", "\\$");
	}
}
