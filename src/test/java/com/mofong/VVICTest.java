package com.mofong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mofong.vvic.service.VvicService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class VVICTest {

	private static Logger logger = LoggerFactory.getLogger(VVICTest.class);
	private static String api = "https://bet.hkjc.com/marksix/getJSON.aspx?sd=20190501&ed=20190731&sb=0";

	@Autowired
	private VvicService service;

//	@Test
//	public void test() {
//		service.exportPosRecord("2021-09-01", "2021-09-13");
//	}

	@Test
	public void testInsertBatchPosOrders() {
		service.savePosOrder("2021-10-01", "2021-10-02");
	}

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
