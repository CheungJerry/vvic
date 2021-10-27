package com.mofong.LvTest;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.mofong.App;
import com.mofong.util.GetRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class LvTest {

	private static Logger logger = LoggerFactory.getLogger(LvTest.class);

//	@Autowired(required = false)
//	private CloseableHttpClient httpClient;
//
//	@Autowired(required = false)
//	private RequestConfig requestConfig;

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

//	@Test
//	public void test() {
//		service.exportPosRecord("2021-09-01", "2021-09-13");
//	}

	@Test
	public void testInsertBatchPosOrders() {
	}

	public static void main(String[] args) {
		String html = "https://www.louisvuitton.cn/zhs-cn/women/ready-to-wear/all-ready-to-wear/_/N-1bayfv9";
//		String url = "https://api-www.louisvuitton.cn/api/zhs-cn/catalog/skuavailability/M45555,M58958,M58956,M59552,M58953,M59554,M58947,M59211,M58660,M59273,M58729,M80688,M45497,M58792,M45779,M44020,M41178,M45923,M45956,M44022,M61252,M41346,M80091,M69442,M61276,M80732,M57937,N63106,M80559,M58009,M80863,M80859,M68276,N58010,M57099,M44936,M68746,M69578,M44699,N60487,M71040,M76966";
		String url = "https://www.baidu.com";

//		getHtml("https://www.louisvuitton.cn/zhs-cn/homepage");

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Referer", "https://www.louisvuitton.cn/zhs-cn/women/ready-to-wear/all-ready-to-wear/_/N-1bayfv9");
		httpHeaders.set("Accept-Encoding", "gzip, deflate, br");
		httpHeaders.set("Accept", "*/*");
		httpHeaders.set("Accept-Language", "zh-CN,zh;q=0.9");
		httpHeaders.set("Host", "www.louisvuitton.cn");
		httpHeaders.set("Origin", "www.louisvuitton.cn");
		httpHeaders.set("Cookie", "ak_cc=CN; AKA_A2=A; bm_sz=532F32B1458392ADEF016A136E8D0619~YAAQdkInO+/Oh558AQAA9mmYuA1+i5ToERsuh6SA/rR5CMdHFDldkFHchN1dh91NKdhdI1mdKBw2Q9Xzc8gHyvwBwHuJK20UCKRJmNvLS4UqIS1ICIWxl3MuqkNWCXGNC7bAPuqoqRbvlXsRVYgtxrV8ZkXqPy1rZZX0dcZWapn14pkEJnpmiZ6hqvWJUhtV3h6cj/dY4/0aBk7phr3et/VB3IpqH0vxMLyxfeXRc1wsedL38cc0uNR/RCOYK2MD7+xjs4ZYSANjNZKICBdZtfvMMR2C3NvYj7Plqzlu0mLjAgbBwZQ2Nw==~3356725~3684678; lv-dispatch=zhs-cn; ak_bmsc=9D468BCF7245C1C2C500C75AAF12CD27~000000000000000000000000000000~YAAQdkInO2/Ph558AQAAGW+YuA0DDAsyzxJsNU3TABLgcooHCH+N6nDUTIetq2EMJbWXwUBjp5Y/vh6jARI6/O0YUSuASMLU4J+Td6s7N6VnM6M1tP4+FKc4LVQqoOU+kWgq6nskW5etpdLMnvJTeZc6D+NQ3QB4njFoq+kCyKEJiIhLXG+QJS4Be9Z6WVSCdu4zQpfsfbpP0EeIBKyfpMlxD7rNHndmoBB0gdI6RPajzHCN/9u88RLGUgkQPeEmkMG0omi7yssSPf/qf1orm+VyU60si/GrWutq6ZmQ0zid3wg9gfBYxNmYw0e+1CIq8+9neqEGRF5dGSp9J4/xM0soxZEFGWDdcC3ksA/LteTo3/0kJP5ADIZj4dmpXRkmZgEAX/egRQagN0es+FNxb9zgcXPMZAH7MsCdaxIiX/u8yp7vmAt26skRYB8JycD03uLAUJsIEgOExoIBFYGkLzrD35PJYBsRd6xnTc+J; _cs_mk=0.3109718629306826_1635184570573; _abck=E8FDC335ED578C93C120D39C3ED124C1~0~YAAQdkInO3HPh558AQAAY2+YuAZlVmG6Wvz49JD4IYymG3LVBAtOj9m29C6L/uglkym/qCmLQxCEaAzKN9qOc2nBOTVLt1m7GUmtFtOmE4DogJZQYJ2i3Cc8DZbcu+bhahhkGbgVVZp5rqhMoyKNz96FLT1hVDTvy/wiOzSb78Su7KnJMi8Q5tVoy63S4kQa64iK2MOk/NG9j0oz+3i4qLWaB0tCvIi0ln2QyAD/BTkehPPKEVVeilS/1GotDAhpBI9yI6gqOY7zFvjID51PDSvs8AOMg6H8iflX0SJaR7gK5t+bTv4gxd9ApKaBaCMwBG8iQB5+w1Tm3hz5VE4qJeVliwMfWRk0DyYDclyPal+ABx4qoVqcHyB7sPL3Vjf6mQbg4zEbMVLPTVMMYjRnBJPjp3PYwzg1rS+M3lwB~-1~-1~-1; _qubitTracker=nffdj3gn4sw-0kv6yo9th-ecu3ebc; qb_generic=:Xy4mHG0:.louisvuitton.cn; Qs_lvt_187854=1635184570; Qs_pv_187854=2975107124246124500; _ga=GA1.2.188104667.1635184571; _gid=GA1.2.893850450.1635184571; _gcl_au=1.1.265370910.1635184571; _cs_c=1; muteAudio=false; _cs_id=d94a698c-1ca5-af9a-a707-685f107cf86e.1635184571.1.1635184603.1635184571.1.1669348571549; _cs_s=2.1.0.1635186403930; qb_permanent=nffdj3gn4sw-0kv6yo9th-ecu3ebc:2:2:1:1:0::0:1:0:Bhdu+8:Bhdu/c:::::58.62.30.13:guangzhou:7392:china:CN:23.13:113.24:guangzhou:156196:guangdong%20sheng:35592:migrated|1635184585994:::Xy4mPM0:Xy4mHUk:0:0:0::0:0:.louisvuitton.cn:0; qb_session=2:1:7::0:Xy4mHUk:0:0:0:0:.louisvuitton.cn; bm_sv=345E5CA202B66DDA2A6FD5176E66E9B2~rfJJJMbxD10or25JGiguZOwyJyMdtHUpMACYTMxJJQ8p83aBMDSM55eXCxFAptYdmO85KxKsXr/9Ncrq7az1b1mBvj1cN6Z921a/083Ad5nIhAHXQ01aGquJjJ9WwIyrOUo730UplO1aLDP10dbRSQ4gYbDswoGQjX3/XUHkENc=; utag_main=v_id:017cb8986e9900185e9f855f1d9e0008b0046083004bb$_sn:1$_se:8$_ss:0$_st:1635186437571$ses_id:1635184570010%3Bexp-session$_pn:3%3Bexp-session$dc_visit:1$dc_event:6%3Bexp-session$dc_region:eu-central-1%3Bexp-session; OPTOUTMULTI=0:0%7Cc1:0%7Cc2:0%7Cc4:0%7Cc3:0; lv-dispatch-url=https://www.louisvuitton.cn/zhs-cn/women/ready-to-wear/all-ready-to-wear/_/N-1bayfv9");
		String result = GetRequest.requestByGetBackString(url, new HashMap<String, Object>(), restTemplate,
				httpHeaders);
		result = Jsoup.parse(result).toString();
		System.out.println(result);
	}

}
