package com.mofong;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mofong.vvic.bean.Cookie;
import com.mofong.vvic.service.CookieService;
import com.mofong.vvic.service.VvicService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class VVICTest {

	private static Logger logger = LoggerFactory.getLogger(VVICTest.class);
	private static String api = "https://bet.hkjc.com/marksix/getJSON.aspx?sd=20190501&ed=20190731&sb=0";

	@Autowired
	private VvicService service;

//	@Autowired
//	private CookieService cookieService;

	@Test
	public void test() {
//		service.exportPosRecord("2021-09-01", "2021-09-13");
//		service.savePosOrderDetail("2018-03-01", "2022-03-19", CookieService.cookieMap.get("lvx"));
//		service.savePosOrder("2018-03-01", "2022-03-19", CookieService.cookieMap.get("lvx"));
		
		service.updateVvicDataByDate(2022,2,CookieService.cookieMap.get("lvx"));
	}

//	@Test
//	public void testInsertBatchPosOrders() {
//		LocalDateTime lastDay = LocalDateTime.now().plusDays(-1);
////		if (lastDay.getDayOfMonth() % 10 == 0 || LocalDateTime.now().getDayOfMonth() == 1) {
//			System.out.println(lastDay.getYear()+","+ lastDay.getMonthValue());
////		}
////		service.updateVvicDataByDate(2022, 1);
//	}

	public static void main(String[] args) {
		String exportPosRecordUrl = "https://www.vvic.com/apic/trade/pos/record?currentPage=1&pageSize=1&startDate=%s&endDate=%s&pos_resource_id=19&keywords=";
		String format = String.format(exportPosRecordUrl, "2021-09-13", "2021-09-13");
		System.out.println(format);
//		String date = "01/01/2020";
//		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//		LocalDate formatDate = LocalDate.parse(date, fmt);
//		System.out.println(formatDate.toString() + "," + formatDate.getDayOfMonth() + "," + formatDate.getMonthValue()
//				+ "," + formatDate.getYear());
//		String text = "1+2+3+4";
//		String[] split = text.split("\\+");
//		for (String str : split) {
//			System.out.println(str);
//		}
	}

}
