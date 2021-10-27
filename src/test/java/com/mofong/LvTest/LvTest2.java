package com.mofong.LvTest;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mockito.internal.util.collections.ListUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class LvTest2 {
	static CloseableHttpClient httpclient;
	static HttpGet httpGet;

	static int imageNum = 1;

	static String prefix = "C";

	static String zero = "0000000";

	static String file_path = "c://Users/tt/Desktop/VALUE_004";

	static String image_folder = "VALUE_RIA";

	static String image_tyep = ".PNG";

	static String women_all_bag_url = "https://www.louisvuitton.cn/zhs-cn/women/handbags/all-handbags/_/N-1ouyuai"
			+ "?page=";
	static String men_all_bag_url = "https://www.louisvuitton.cn/zhs-cn/men/bags/all-bags/_/N-nstx58?page=";

	static ExecutorService executorService = Executors.newFixedThreadPool(40);

	static {
		httpclient = HttpClients.createDefault();
		httpGet = new HttpGet();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(20000).setConnectionRequestTimeout(20000)
				.setSocketTimeout(20000).build();
		// 模拟浏览器浏览（user-agent的值可以通过浏览器浏览，查看发出请求的头文件获取）
		httpGet.setHeader("User-Agent", "Mozilla/5.0");
		httpGet.addHeader("accept", "*/*");
		httpGet.setConfig(requestConfig);
	}

	public static void main(String[] args) throws Exception {
		Map<String, Product> products = new HashMap<>();
		for (int i = 1; i < 40; i++) {
			downProduct(women_all_bag_url, i, products);
		}
		for (int i = 1; i < 40; i++) {
			downProduct(men_all_bag_url, i, products);
		}

		executorService.shutdown();
		while (true) {
			if (executorService.isTerminated()) {
				System.out.println("异步线程下载图片结束了！");
				break;
			}
			Thread.sleep(200);
		}
		if (!CollectionUtils.isEmpty(products.values())) {
			String[] title = { "物品名称", "型号", "规格", "官网价格", "图片1", "图片2", "图片3", "图片4", "图片5", "图片6", "颜色", "材质", "描述",
					"商品链接" };
//			getHSSFWorkbook("prada商品信息", title, new ArrayList<>(products.values()));
		}
	}

	public static void downProduct(String url, int page, Map<String, Product> products) throws Exception {
		// 需要爬取商品信息的网站地址
		url = url + page;
		// 动态模拟请求数据
		httpGet.setURI(new URI(url));
		// 模拟浏览器浏览（user-agent的值可以通过浏览器浏览，查看发出请求的头文件获取）
		httpGet.setHeader("user-agent", "Mozilla/5.0");
		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			// 获取响应状态码
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (statusCode == 200) {
				String html = EntityUtils.toString(entity, Consts.UTF_8);
				Document doc = Jsoup.parse(html);
				Elements div = doc.select("div[class='lv-paginated-list lv-category__grid']");
				Elements liList = div.select("div[class='lv-paginated-list lv-category__grid']")
						.select("ul[class='lv" + "-list']").select("li");
				if (CollectionUtils.isEmpty(liList)) {
					return;
				}
				for (int i = 0; i < liList.size(); i++) {
					Element item = liList.get(i);
					Elements info = item.select("div[class='lv-product-card -compact-large']")
							.select("div" + "[class='lv-product-card__wrap']")
							.select("div[class='lv-product-card__info-wrapper']")
							.select("div[class='lv-product" + "-card__info']").select("span");
					Product product = new Product();
					String productNo = info.attr("id").replace("product-", "");
					if (StringUtils.isEmpty(productNo)) {
						continue;
					}
					product.setProductNo(productNo);

					String name = info.select("a").text();
					product.setName(name);

					String detailUrl = "https://www.louisvuitton.cn" + info.select("a").attr("href");
					product.setDetailUrl(detailUrl);
					products.put(productNo, product);
//					downProductDetail(detailUrl, product);
				}
				// 消耗掉实体
				EntityUtils.consume(response.getEntity());
			} else {
				// 消耗掉实体
				EntityUtils.consume(response.getEntity());
			}
			response.close();
		} catch (Exception e) {
			System.out.println("请求列表失败：" + e.getMessage());
		} finally {

		}
	}

	static class Product {

		private String name;
		private String price;
		private String color;
		private String specs;
		private String productNo;
		private String material;
		private String desc;
		private String detailUrl;
		private List<String> images;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getSpecs() {
			return specs;
		}

		public void setSpecs(String specs) {
			this.specs = specs;
		}

		public String getProductNo() {
			return productNo;
		}

		public void setProductNo(String productNo) {
			this.productNo = productNo;
		}

		public String getMaterial() {
			return material;
		}

		public void setMaterial(String material) {
			this.material = material;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public List<String> getImages() {
			return images;
		}

		public void setImages(List<String> images) {
			this.images = images;
		}

		public String getDetailUrl() {
			return detailUrl;
		}

		public void setDetailUrl(String detailUrl) {
			this.detailUrl = detailUrl;
		}
	}
}
