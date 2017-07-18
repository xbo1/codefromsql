package com.bob.templ;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 通用工具方法
 *
 * @author admin on 2017/7/18.
 */
public abstract class Utilities {
	private static final Logger logger = LoggerFactory.getLogger(Utilities.class);
	/*默认资源所在文件夹位置*/
	private static final String DEFAULT_SOURCE_FOLDER = "/static/phpwe7/";
	public static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * 读取默认文件夹下的指定文件内容。
	 *
	 * @param fileName
	 * @return
	 */
	public static final String readSourceFile(String fileName) {
		return readSourceFile(DEFAULT_SOURCE_FOLDER, fileName);
	}

	/**
	 * 读取文件内容
	 *
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static final String readSourceFile(String path, String fileName) {
		return readSourceFile(path, fileName, DEFAULT_CHARSET);
	}


	/**
	 * 读取文件内容
	 *
	 * @param path
	 * @param fileName
	 * @param charset
	 * @return
	 */
	public static final String readSourceFile(String path, String fileName, String charset) {
		String fullFile = path + fileName;
		try (InputStream in = Utilities.class.getResourceAsStream(fullFile)) {
			Assert.notNull(in, "文件" + fullFile + "所在的资源文件信息为空！");
			logger.info("读取到的文件{}内流数据信息为{}...", fullFile, in);

			BufferedInputStream bis = new BufferedInputStream(in);
			return IOUtils.toString(bis, DEFAULT_CHARSET);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
