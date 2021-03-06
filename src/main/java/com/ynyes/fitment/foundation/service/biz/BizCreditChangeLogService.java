package com.ynyes.fitment.foundation.service.biz;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ynyes.fitment.foundation.entity.FitCompany;
import com.ynyes.fitment.foundation.entity.FitCreditChangeLog;
import com.ynyes.fitment.foundation.entity.FitEmployee;
import com.ynyes.fitment.foundation.entity.FitOrder;
import com.ynyes.fitment.foundation.entity.FitOrderCancel;
import com.ynyes.fitment.foundation.entity.FitOrderRefund;
import com.ynyes.lyz.entity.TdManager;

public interface BizCreditChangeLogService {
	
	void creditAction(TdManager manager, FitCompany company, Double inputCredit, String remark) throws Exception;

	FitCreditChangeLog consumeLog(FitCompany company, FitOrder order) throws Exception;
	
	FitCreditChangeLog cancelLog(FitCompany company, FitOrderCancel orderCancel) throws Exception;
	
	FitCreditChangeLog refundLog(FitCompany company, FitOrderRefund orderRefund) throws Exception;

	FitCreditChangeLog manageLog(TdManager manager, FitCompany company, Double inputCredit, String remark) throws Exception;

	List<FitCreditChangeLog> employeeGetLog(FitEmployee employee) throws Exception;

	Page<FitCreditChangeLog> employeeGetLog(FitEmployee employee, Integer page, Integer size) throws Exception;
}
