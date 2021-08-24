/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.service;

import com.cv.accountswing.entity.helper.BalanceSheetRetObj;
import com.cv.accountswing.entity.helper.ProfitAndLostRetObj;
import java.util.Map;

/**
 *
 * @author winswe
 */
public interface ReportService {

    public void genReport(String reportPath, String filePath, String fontPath,
            Map<String, Object> parameters) throws Exception;

    public void getProfitLost(String plProcess, String from, String to, String dept,
            String currency, String comp, String userCode, String macId, String invCOA) throws Exception;

    public ProfitAndLostRetObj getPLCalculateValue(String compCode, String macId);

    public BalanceSheetRetObj getBSCalculateValue(String compCode, String macId) throws Exception;

    public void genBalanceSheet(String toDate, String compCode, String curr, String macId, String process, String inventory) throws Exception;

    public Object getAggResult(String sql);

    public void genIncomeAndExpense(String process, String compCode, String macId);

    public ProfitAndLostRetObj calculateIncomeExpense(String compCode, String macId);

    public void deleteOpTemp(String macId) throws Exception;

    public double genOpBalance(String process, String opDate, String clDate,
            String endDate, String curr, String compCode, String dept, String macId) throws Exception;

}
