package com.mofong.lv.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mofong.bean.Result;
import com.mofong.util.GetRequest;

@Service("lvService")
public class LvService {

	private static Logger logger = LoggerFactory.getLogger(LvService.class);

	private RestTemplate restTemplate;

	@Value("${selenium.chrome-driver-path}")
	private String driverPath;

	/**
	 * 通过selenium调用chrome driver打开页面进行爬虫，主运行程序
	 * 
	 * @throws InterruptedException
	 */
	public void run() throws InterruptedException {
		ChromeOptions options = new ChromeOptions();
		// 无头模式
//		options.addArguments("--headless");
		// 设置允许弹框
		options.addArguments("disable-infobars", "disable-web-security");
		// 设置无gui 开发时还是不要加，可以看到浏览器效果
		options.addArguments(
				"--user-agent='Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60'");
		// options.addArguments(
		// "--user-agent=Mozilla/5.0 (Windows NT 5.1; U; en; rv:1.8.1) Gecko/20061208
		// Firefox/2.0.0 Opera 9.50");
		options.addArguments("--disable-infobars", "disable-web-security", "--auto-open-devtools-for-tabs");
		// 设置允许cookie和javascript 测试
		options.addArguments("--enable-javascript");
		options.addArguments("--enable-cookie");
		ArrayList<String> arg = new ArrayList<>();
		arg.add("enable-automation");
		options.setExperimentalOption("excludeSwitches", arg);
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--disable-gpu");// 谷歌文档提到需要加上这个属性来规避bug

		System.setProperty("webdriver.chrome.driver", driverPath);
		WebDriver driver = new ChromeDriver(options);
		Integer spiderCount = 0;
		Integer insertCount = 0;
		try {
			driver.get(driverPath);
			driver.get("https://www.louisvuitton.cn/zhs-cn/homepage");
			Document doc = Jsoup.parse(driver.getPageSource());
			System.out.println(doc.toString());
			Thread.sleep(60000);
		} finally {
			driver.close();
			driver.quit();
		}
	}

//	/**
//	 * 页面html数据格式化为list<Map>
//	 * 
//	 * @param html
//	 */
//	public static void getSixData(String html, List<SixData> sdObjectList, Integer spiderCount) {
//		Document doc = Jsoup.parse(html);
//		Elements pre = doc.select("pre");
//		String preStr = pre.text();
//		if (StringUtils.isEmpty(preStr)) {
//			logger.info("无pre元素");
//			return;
//		}
////		System.out.println(preStr);
//		List<Map<String, Object>> sixDataListMap = new ArrayList<>();
//		JSONArray jsonArray = JSON.parseArray(preStr);
//		List sixDataList = jsonArray.toJavaObject(List.class);
//		logger.info("本次爬虫{}条", sixDataList.size());
//		spiderCount = spiderCount.intValue() + sixDataList.size();
//		for (Object obj : sixDataList) {
//			Map<String, Object> map = JsonUtil.str2Map(JsonUtil.obj2Str(obj));
//			sixDataListMap.add(map);
//		}
//		sixDataMapping(sixDataListMap, sdObjectList);
//	}

	public Result getLvProdList() {

		String url = "https://www.louisvuitton.cn/content/dam/lv/online/stories/Technical/modules-women.json";
		String result = GetRequest.requestByGetBackString(url, new HashMap<String, Object>(), restTemplate,
				new HttpHeaders());
		System.out.println(result);

		return Result.success();
	}

}
