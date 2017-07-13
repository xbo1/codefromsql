package com.bob;

import com.bob.parser.ParseCreate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by bob on 2017/7/13.
 */
@Controller
public class ParseController {
    @RequestMapping("/")
    public String parse() {
        ParseCreate pc = new ParseCreate();
        pc.doParse();
        return "index";
    }
}
