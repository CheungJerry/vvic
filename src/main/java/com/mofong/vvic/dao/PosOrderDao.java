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

	List<PosOrder> queryPosOrderByDate(@Param("start") Date paramDate1, @Param("end") Date paramDate2);

	List<PosOrderDetail> queryPosOrderDetailByDate(@Param("start") Date paramDate1, @Param("end") Date paramDate2);

	int updatePosOrder(PosOrder paramPosOrder);

	int updatePosOrderDetail(PosOrderDetail paramPosOrderDetail);

	int deletePosOrderDetailByOrderNo(Set<String> paramSet);

	int deletePosOrderByOrderNo(Set<String> paramSet);

	String queryMaxDateInPosOrder();

	String queryMaxDateInPosOrderDetail();
}
