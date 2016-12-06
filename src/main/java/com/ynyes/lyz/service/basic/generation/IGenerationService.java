package com.ynyes.lyz.service.basic.generation;

import java.util.List;

import com.ynyes.lyz.entity.TdOrder;
import com.ynyes.lyz.interfaces.entity.TdOrderCouponInf;
import com.ynyes.lyz.interfaces.entity.TdOrderGoodsInf;
import com.ynyes.lyz.interfaces.entity.TdOrderInf;

public interface IGenerationService {

	void generateOrderData(List<TdOrder> orderList);
	
	TdOrderInf generateOrderInf(TdOrder order);
	
	List<TdOrderGoodsInf> generateOrderGoodsInf(TdOrder order);
	
	List<TdOrderCouponInf> generateOrderCouponInf(TdOrder order);
}
