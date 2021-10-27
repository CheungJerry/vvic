package com.mofong.LvTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.builder.ParameterExpression;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.mofong.App;
import com.mofong.util.ChromeDriverUtil;
import com.mofong.util.GetRequest;
import com.mofong.util.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class LvTestSelenium {

	private static String driverPath = "D:\\project\\spider\\lib\\chromedriver.exe";

	private static String url = "https://www.louisvuitton.cn/zhs-cn/homepage";

	public static void main(String[] args) throws InterruptedException {

		System.setProperty("webdriver.chrome.driver", driverPath);
		ChromeDriverUtil driverUtil = new ChromeDriverUtil();
//		driverUtil.getOptions().addArguments("--no-sandbox");
		WebDriver driver = driverUtil.getWebDriver();
		try {
			driver.get(url);
			Thread.sleep(3000L);
			String pageSource = driver.getPageSource();
			pageSource = driver.getPageSource();

			WebDriver.Options manage = driver.manage();
			Set<Cookie> cookies = manage.getCookies();
//			manage.deleteAllCookies();
//			StringBuilder sb = new StringBuilder();
//			for (Cookie c : cookies) {
//				sb.append(c.getName()+"="+c.getValue()+" ");
//			}
//
//			RestTemplate restTemplate = new RestTemplate();
//			HttpHeaders httpHeaders = new HttpHeaders();
//			httpHeaders.add("cookie", sb.toString());
//			String rtnString = GetRequest.requestByGetBackString(
//					"https://api-www.louisvuitton.cn/api/zhs-cn/catalog/skuavailability/M44020",
//					new HashMap<String, Object>(), restTemplate, httpHeaders);
//			System.out.println(rtnString);
			driver.get(url);
			pageSource = driver.getPageSource();
			Document doc = Jsoup.parse(pageSource);
//			System.out.println(doc.toString());
//			driver.get(driver.getCurrentUrl());

			System.out.println("--------------------");
			Thread.sleep(3000L);
//			manage = driver.manage();
//			cookies = manage.getCookies();
//			for (Cookie c : cookies) {
//				System.out.println(c.getName() + " = " + c.getValue());
//			}
			for (int i = 0; i < 20; i++) {
//				driver.get("https://api-www.louisvuitton.cn/api/zhs-cn/catalog/skuavailability/M58788");
				driver.get("https://api-cn.louisvuitton.cn/api/zhs-cn/catalog/availability/nvprod3190089v");
				pageSource = driver.getPageSource();
				doc = Jsoup.parse(pageSource);
				String pre = doc.getElementsByTag("pre").text();
				if (StringUtils.isEmpty(pre)) {
					driver.get(url);
					continue;
				}
				List<Map<String, Object>> preListMap = (List<Map<String, Object>>) JsonUtil.str2Map(pre)
						.get("skuAvailability");
				boolean inStock = (boolean) preListMap.get(0).get("inStock");
				System.out.println("instock:" + inStock);
				Thread.sleep(3000L);
			}
//			driver.get(url);
//			driver.get("https://api-cn.louisvuitton.cn/api/zhs-cn/catalog/availability/nvprod3190089v");
			doc = Jsoup.parse(pageSource);
			Elements pre = doc.getElementsByTag("pre");
			System.out.println(pre.text());
//			Thread.sleep(90000L);
		} finally {
			driverUtil.driverOver();
		}

//		getReq();
	}

	public static String getHtml(String url) {
		// 1.生成httpclient，相当于该打开一个浏览器
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		// 2.创建get请求，相当于在浏览器地址栏输入 网址
		// HttpGet request = new
		// HttpGet("http://www.shicimingju.com/chaxun/list/4059.html");
		HttpGet request = new HttpGet(url);
		try {
			// 3.执行get请求，相当于在输入地址栏后敲回车键
			response = httpClient.execute(request);
			// 4.判断响应状态为200，进行处理
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 5.获取响应内容
				HttpEntity httpEntity = response.getEntity();
				String html = EntityUtils.toString(httpEntity, "utf-8");
				// Jsoup 解析网页数据
				Document document = Jsoup.parse(html);
				System.out.println(document.toString());
				// 获取目标内容
				Elements item_content = document.getElementsByClass("item_content");
				// String text = item_content.text();
				return item_content.toString();
			} else {
				// 如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
				System.out.println("返回状态不是200");
				System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
				return "不是200";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 6.关闭
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpClient);
		}
		return "请输入正确url地址";
	}

	public static void getReq() {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 创建Get请求
		HttpGet httpGet = new HttpGet(
				"https://www.louisvuitton.cn/content/dam/lv/online/stories/Technical/modules-women.json");

		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 由客户端执行(发送)Get请求
			response = httpClient.execute(httpGet);
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				System.out.println("响应内容长度为:" + responseEntity.getContentLength());
				System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testInsertBatchPosOrders() {
	}

}
