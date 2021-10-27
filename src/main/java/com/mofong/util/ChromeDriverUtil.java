package com.mofong.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverUtil {

	private WebDriver webDriver;
	private ChromeOptions options;

	private static ChromeOptions getDefaultOptions() {
		ChromeOptions options = new ChromeOptions();
		// 无头模式
//		options.addArguments("--headless");
		// 沙箱
		options.addArguments("--no-sandbox");
		// 设置允许弹框
		options.addArguments("disable-infobars", "disable-web-security");
		// 设置无gui 开发时还是不要加，可以看到浏览器效果
		options.addArguments(
				"--user-agent='Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60'");
		options.addArguments("--disable-infobars", "disable-web-security", "--auto-open-devtools-for-tabs");
		// 设置允许cookie和javascript 测试
		options.addArguments("--enable-javascript");
		options.addArguments("--enable-cookie");
//		ArrayList<String> arg = new ArrayList<>();
//		arg.add("enable-automation");
//		options.setExperimentalOption("excludeSwitches", arg);
		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
		options.setExperimentalOption("useAutomationExtension", false);
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--disable-gpu");// 谷歌文档提到需要加上这个属性来规避bug
		// 反爬
		options.addArguments("--disable-blink-features");
		options.addArguments("--disable-blink-features=AutomationControlled");

		// 1026
		options.addArguments("lang=zh-CN.UTF-8");
		Map<String, Object> pref = new HashMap<String, Object>();
		pref.put("profile.managed_default_content_settings.images", 2);
		pref.put("profile.managed_default_content_settings.notifications", 2);
		pref.put("intl.accept_languages", "zh-CN,zh;q=0.9,en;q=0.5");
		options.setExperimentalOption("prefs", pref);
		return options;
	}

	public ChromeDriverUtil() {
		this.webDriver = new ChromeDriver(getDefaultOptions());
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public ChromeOptions getOptions() {
		return options;
	}

	public void setOptions(ChromeOptions options) {
		this.options = options;
	}

	public void driverOver() {
		this.webDriver.close();
		this.webDriver.quit();
	}

}
