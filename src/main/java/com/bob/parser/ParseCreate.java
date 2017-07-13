package com.bob.parser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.bob.model.SQLField;
import com.bob.model.SQLTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bob on 2017/7/13.
 */
public class ParseCreate {
    String dbType = JdbcConstants.MYSQL;
    String sql = "CREATE TABLE IF NOT EXISTS `ims_mego_online_list_gift_in_bag` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `uniacid` int(10) unsigned NOT NULL,\n" +
            "  `bagid` int(10) NOT NULL COMMENT 'gift_bag的id',\n" +
            "  `giftid` int(10) NOT NULL COMMENT 'gift的id',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";

    public void doParse() {
        String result = SQLUtils.format(sql, dbType);
//        System.out.println(result); // 缺省大写格式
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        //解析出的独立语句的个数
//        System.out.println("size is:" + stmtList.size());
        for (SQLStatement stmt : stmtList) {
            if (stmt instanceof MySqlCreateTableStatement) {
                MySqlCreateTableStatement mysqlStmt = (MySqlCreateTableStatement)stmt;
//                mysqlStmt.getTableSource(); //表名
//                mysqlStmt.getTableElementList(); //fields
//                mysqlStmt.getTableOptions(); //获取设置，如ENGINE=InnoDB等
//                mysqlStmt.isIfNotExiists();
                SQLTable table = new SQLTable();
                table.setName(mysqlStmt.getTableSource().toString());
                List<SQLField> fields = new ArrayList<>(mysqlStmt.getTableElementList().size());
                for (SQLTableElement element : mysqlStmt.getTableElementList()) {
                    if (element instanceof SQLColumnDefinition) {
                        SQLColumnDefinition col = (SQLColumnDefinition)element;
                        SQLField field = new SQLField();
                        field.setName(col.getName().toString());
                        if (col.getComment() != null) {
                            field.setComment(col.getComment().toString());
                        }
                        field.setType(col.getDataType().getName());
                        fields.add(field);
                        table.setFields(fields);
                    }
                }
                Map<String, String> options = new HashMap<>();
                for (String key : mysqlStmt.getTableOptions().keySet()) {
                    options.put(key, mysqlStmt.getTableOptions().get(key).toString());
                }
                table.setOptions(options);
                System.out.println(table);
            }
//            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
//            stmt.accept(visitor);
//            //获取表名称
//            System.out.println("Tables : " + visitor.getCurrentTable());
//            //获取操作方法名称,依赖于表名称
//            System.out.println("Manipulation : " + visitor.getTables());
//            //获取字段名称
//            System.out.println("fields : " + visitor.getColumns());
        }

    }
}
