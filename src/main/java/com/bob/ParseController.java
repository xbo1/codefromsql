package com.bob;

import com.bob.model.SQLTable;
import com.bob.parser.ParseCreate;
import com.bob.templ.GenHtmlCode;
import com.bob.templ.GenPhpCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by bob on 2017/7/13.
 */
@Controller
public class ParseController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView indexPost(String sqlcode, HttpServletResponse resp) throws IOException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");

        if (sqlcode == null || sqlcode.length() == 0) {
            mav.addObject("errMsg", "SQL语句不能为空");
            return mav;
        }
        ParseCreate pc = new ParseCreate();
        List<SQLTable> tables = new ArrayList<>();

        try {
            tables = pc.doParse(sqlcode);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (tables.size() <= 0){
            mav.addObject("errMsg", "SQL语句不合法");
            return mav;
        }
        Map<String, String> files = new HashMap<>();
        for (SQLTable table : tables) {
            String fname = table.getShortName();
            GenPhpCode genPhp = new GenPhpCode();
            files.put(fname+".inc.php", genPhp.generate(table));
            GenHtmlCode genHtml = new GenHtmlCode();
            files.put(fname+".html", genHtml.generate(table));
        }
        downloadResp("phpcodes.zip", files, resp);
        return mav;
    }

    @RequestMapping("/testcode")
    public void download(HttpServletResponse resp) throws IOException {
        String sql = "CREATE TABLE IF NOT EXISTS `ims_mego_fishbs_player` (\n" +
                "  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,\n" +
                "  `uniacid` int(10) unsigned NOT NULL,\n" +
                "  `fishbs_id` int(10) unsigned NOT NULL COMMENT '比赛id',\n" +
                "  `name` varchar(64) NOT NULL COMMENT '姓名',\n" +
                "  `phone` varchar(20) NOT NULL COMMENT '电话',\n" +
                "  `height` varchar(32) NOT NULL COMMENT '身高',\n" +
                "  `weight` varchar(32) NOT NULL COMMENT '体重',\n" +
                "  `age` INT(4) UNSIGNED NOT NULL COMMENT '年龄',\n" +
                "  `fish_age` INT(4) UNSIGNED NOT NULL COMMENT '钓龄',\n" +
                "  `location` varchar(100) NOT NULL COMMENT '所在地',\n" +
                "  `introduce` varchar(500) NOT NULL COMMENT '个人介绍',\n" +
                "  `image_url` varchar(255) NOT NULL COMMENT '照片',\n" +
                "  `code` varchar(50) NOT NULL default '' COMMENT '参赛码',\n" +
                "  `store_id` int(10) unsigned NOT NULL COMMENT '参赛店铺',\n" +
                "  `create_time` int(11) NOT NULL COMMENT '报名时间',\n" +
                "  `goods` int(10) unsigned NOT NULL COMMENT '鱼获量',\n" +
                "  `likes` int(10) unsigned NOT NULL COMMENT '得票数',\n" +
                "  `openid` varchar(50) NOT NULL COMMENT '微信号',\n" +
                "  `status` int(3) unsigned DEFAULT 0 COMMENT '状态, 0未支付, 1已报名,',\n" +
                "  `orderid` int(10) unsigned DEFAULT 0 COMMENT '订单号',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `uniacid` (`uniacid`),\n" +
                "  KEY `openid` (`openid`)\n" +
                ") ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户';";
        ParseCreate pc = new ParseCreate();
        List<SQLTable> tables = pc.doParse(sql);
        Map<String, String> files = new HashMap<>();
        for (SQLTable table : tables) {
            String fname = table.getShortName();
            GenPhpCode genPhp = new GenPhpCode();
            files.put(fname+".inc.php", genPhp.generate(table));
            GenHtmlCode genHtml = new GenHtmlCode();
            files.put(fname+".html", genHtml.generate(table));
        }
        downloadResp("phpcodes.zip", files, resp);
    }

    private void downloadResp(String zipName, Map<String, String> files,
                              HttpServletResponse resp) throws IOException{
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition",
                "attachment; filename=" + zipName);
        ZipOutputStream zos = new ZipOutputStream(resp.getOutputStream());
        for (String key : files.keySet()) {
            zos.putNextEntry(new ZipEntry(key));
            zos.write(files.get(key).getBytes());
        }
        zos.flush();
        zos.close();
    }

}
