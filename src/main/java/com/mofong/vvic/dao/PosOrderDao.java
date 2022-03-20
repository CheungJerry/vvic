package com.mofong.vvic.dao;

import com.mofong.vvic.bean.PosOrder;
import com.mofong.vvic.bean.PosOrderDetail;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

public interface PosOrderDao {
	int PosOrderInsertBatch(List<PosOrder> paramList);

	int PosOrderDetailInsertBatch(List<PosOrderDetail> paramList);

	List<Map<String, Object>> queryTest();

	List<PosOrder> queryPosOrderByDate(@Param("start") Date paramDate1, @Param("end") Date paramDate2,
			@Param("account") String account);

	List<PosOrderDetail> queryPosOrderDetailByDate(@Param("start") Date paramDate1, @Param("end") Date paramDate2,
			@Param("account") String account);

//	int updatePosOrder(PosOrder paramPosOrder);
//
//	int updatePosOrderDetail(PosOrderDetail paramPosOrderDetail);

	int deletePosOrderByYmd(@Param("ymd") String ymd, @Param("account") String account);
	
	int deletePosOrderDetailByYmd(@Param("ymd") String ymd, @Param("account") String account);
	
//	int deletePosOrderDetailByOrderNo(@Param("paramSet") Set<String> paramSet, @Param("account") String account);

//	int deletePosOrderByOrderNo(@Param("paramSet") Set<String> paramSet, @Param("account") String account);

//	String queryMaxDateInPosOrder();
//
//	String queryMaxDateInPosOrderDetail();
}
