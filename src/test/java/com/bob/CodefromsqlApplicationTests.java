package com.bob;

import com.bob.templ.StringTemplateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodefromsqlApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void StringTemplateTest() {
		String template="您提现{{borrowAmount}}元至尾号{{tailNo}}的请求失败，您可以重新提交提款申请。";
		Map<String, String> data = new HashMap<String, String>();
		data.put("borrowAmount", "1000.00");
		data.put("tailNo", "1234");
		System.out.println(StringTemplateUtils.render(template,data));
	}
}
