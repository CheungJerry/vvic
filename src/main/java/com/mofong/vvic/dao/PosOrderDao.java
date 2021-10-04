package com.mofong.vvic.dao;

import java.util.List;
import java.util.Map;

import com.mofong.vvic.bean.PosOrder;
import com.mofong.vvic.bean.PosOrderDetail;

public interface PosOrderDao {

	int PosOrderInsertBatch(List<PosOrder> list);

	int PosOrderDetailInsertBatch(List<PosOrderDetail> list);

	List<Map<String, Object>> queryTest();
}
