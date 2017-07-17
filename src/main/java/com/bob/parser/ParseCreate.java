package com.bob.parser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLNotNullConstraint;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.parser.ParserException;
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
    public ParseCreate() {
        this.dbType = JdbcConstants.MYSQL;
    }
    public ParseCreate(String dbType) {
        this.dbType = dbType;
    }
    String dbType;
    private SQLTable parseCreateTableSQL(SQLStatement stmt) {
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
                    field.setAutoIncrement(col.isAutoIncrement());
                    field.setName(col.getName().toString());
                    if (col.getComment() != null) {
                        field.setComment(col.getComment().toString());
                    }
                    for (SQLColumnConstraint cons : col.getConstraints()) {
                        if (cons instanceof SQLNotNullConstraint) {
                            field.setNotNull(true);
                        }
                    }
                    if (col.getDefaultExpr() != null) {
                        SQLExpr expr = col.getDefaultExpr();
                        if (expr instanceof SQLIntegerExpr) {
                            SQLIntegerExpr intexpr = (SQLIntegerExpr)expr;
                            field.setDefValue(intexpr.getNumber().toString());
                        }
                        if (expr instanceof SQLCharExpr) {
                            SQLCharExpr charexpr = (SQLCharExpr)expr;
                            field.setDefValue(charexpr.getText());
                        }
                    }
                    field.setType(col.getDataType().getName());
                    fields.add(field);
                    table.setFields(fields);
                }
            }
            Map<String, String> options = new HashMap<>();
            for (String key : mysqlStmt.getTableOptions().keySet()) {
                String lowKey = key.toLowerCase();
                options.put(lowKey, mysqlStmt.getTableOptions().get(key).toString());
            }
            table.setOptions(options);
//            System.out.println(table);
            return table;
        }
        return null;
    }
    public List<SQLTable> doParse(String sql) throws ParserException {
        String result = SQLUtils.format(sql, dbType);
//        System.out.println(result); // 缺省大写格式
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        List<SQLTable> tables = new ArrayList<>();
        //解析出的独立语句的个数
//        System.out.println("size is:" + stmtList.size());
        for (SQLStatement stmt : stmtList) {
            SQLTable table = parseCreateTableSQL(stmt);
            if (table != null) {
                tables.add(table);
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
        return tables;
    }
}
