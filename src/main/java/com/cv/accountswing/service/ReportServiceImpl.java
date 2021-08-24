/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.service;

import com.cv.accountswing.dao.COADao;
import com.cv.accountswing.dao.ReportDao;
import com.cv.accountswing.entity.ChartOfAccount;
import com.cv.accountswing.entity.helper.BalanceSheetRetObj;
import com.cv.accountswing.entity.helper.ProfitAndLostRetObj;
import com.cv.accountswing.util.Util1;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author winswe
 */
@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private ReportDao dao;
    @Autowired
    private COADao coaDao;

    @Override
    public void genReport(String reportPath, String filePath, String fontPath,
            Map<String, Object> parameters) throws Exception {
        dao.genReport(reportPath, filePath, fontPath, parameters);
    }

    @Override
    public void getProfitLost(String plProcess, String from, String to, String dept,
            String currency, String comp, String userCode, String macId, String inventory) {
        try {
            dao.execSQLRpt("delete from tmp_profit_lost where mac_id = " + macId + "");
        } catch (Exception ex) {
            logger.error("delete tmp_profit_lost : " + ex.getMessage());
        }
        String strInsert = "insert into tmp_profit_lost(acc_code,curr_code,dept_code,\n "
                + "acc_total,comp_code, sort_order,mac_id)";
        String[] process = plProcess.split(",");
        int sortOrder = 1;

        from = Util1.toDateStrMYSQL(from, "dd/MM/yyyy");
        to = Util1.toDateStrMYSQL(to, "dd/MM/yyyy");
        String invCode = getCOACode(inventory, comp);
        inventory = invCode.equals("-") ? "'" + inventory + "'" : invCode;
        //Sales Income 
        for (String tmp : process) {
            switch (tmp) {
                case "os" -> {
                    try {
                        String sql = "select coa_code,curr_id,ifnull(dept_code,'-'),if(dr_amt>0,dr_amt*-1,cr_amt) acc_toal,\n"
                                + "'" + comp + "'," + sortOrder + "," + macId + "\n"
                                + "from tmp_tri\n"
                                + "where mac_id = " + macId + " \n"
                                + "and coa_code in (" + inventory + ")";
                        dao.execSQLRpt(strInsert + "\n" + sql);
                    } catch (Exception e) {
                        logger.error("OS :" + e.getMessage());
                    }
                }
                case "cs" -> {
                    try {
                        String strCS = "select coa_code,curr_code,dept_code,amount,\n"
                                + "'" + comp + "'," + sortOrder + "," + macId + "\n"
                                + "from stock_op_value\n"
                                + "where  coa_code in (" + inventory + ")\n"
                                + "and dept_code = '" + dept + "' or '-' ='" + dept + "'\n"
                                + "and comp_code = '" + comp + "'\n"
                                + "and (curr_code = '" + currency + "' or '-' = '" + currency + "')\n"
                                + "and date(tran_date) = '" + to + "'";
                        dao.execSQLRpt(strInsert + "\n" + strCS);
                    } catch (Exception e) {
                        logger.error("CS : " + e.getMessage());
                    }
                }
                default -> {
                    String coaCode = getCOACode(tmp, comp);
                    coaCode = coaCode.equals("-") ? "" : coaCode;
                    try {
                        String sql = "select coa_code,curr_id,ifnull(dept_code,'-'),if(dr_amt>0,dr_amt*-1,cr_amt) acc_toal,\n"
                                + "'" + comp + "'," + sortOrder + "," + macId + "\n"
                                + "from tmp_tri\n"
                                + "where mac_id = " + macId + " \n"
                                + "and coa_code in (" + coaCode + ")";
                        dao.execSQLRpt(strInsert + "\n" + sql);
                    } catch (Exception e) {
                        logger.error("Default : " + e.getMessage());
                    }
                }
            }
            sortOrder++;
        }
    }

    public String getCOACode(String code, String compCode) {
        String tmp = "-";
        List<ChartOfAccount> listCoA = coaDao.getAllChild(code, compCode);
        if (!listCoA.isEmpty()) {
            tmp = "";
            tmp = listCoA.stream().map(coa -> String.format("'%s',", coa.getCode())).reduce(tmp, String::concat);
        }
        tmp = tmp.substring(0, tmp.length() - 1);
        return Util1.isNull(tmp, "-");
    }

    @Override
    public ProfitAndLostRetObj getPLCalculateValue(String compCode, String macId) {
        ProfitAndLostRetObj obj = new ProfitAndLostRetObj();
        String sql = "select abs(sum(acc_total)) acc_total,sort_order\n"
                + "from tmp_profit_lost\n"
                + "where mac_id = " + macId + " and comp_code ='" + compCode + "'\n"
                + "group by sort_order";
        ResultSet rs;
        try {
            rs = dao.executeSql(sql);
            if (rs != null) {
                while (rs.next()) {
                    double ttl = rs.getDouble("acc_total");
                    int order = rs.getInt("sort_order");
                    switch (order) {
                        case 1 -> //Sale Income
                            obj.addSaleIncome(ttl);
                        case 2 -> //Opening Stock
                            obj.addOPStock(ttl);
                        case 3 -> //Purchase
                            obj.addPurchase(ttl);
                        case 4 -> //Closiing Stock
                            obj.addCLStock(ttl);
                        case 5 -> //Other Income
                            obj.addOtherIncome(ttl);
                        case 6 -> //Other Expense
                            obj.addOtherExpense(ttl);
                    }
                }

            }
        } catch (Exception ex) {
            logger.error("getPLCalculateValue : " + ex.getMessage());
        }

        return obj;
    }

    @Override
    public void genBalanceSheet(String toDate, String compCode, String curr, String macId, String blProcess, String inventory) throws Exception {
        String invCode = getCOACode(inventory, compCode);
        inventory = invCode.equals("-") ? "'" + inventory + "'" : invCode;
        String strSqlDelete = "delete from tmp_balance_sheet where mac_id = '" + macId + "'";
        dao.execSQLRpt(strSqlDelete);
        String strInsert = "insert into tmp_balance_sheet(acc_code,curr_code,dept_code,\n "
                + "acc_total, comp_code, sort_order,mac_id)";
        String[] process = blProcess.split(",");
        int sortOrder = 1;
        for (String tmp : process) {
            String coaCode = getCOACode(tmp, compCode);
            String sql = "select coa_code,curr_id,ifnull(dept_code,'-'),if(dr_amt>0,dr_amt*-1,cr_amt) acc_toal,\n"
                    + "'" + compCode + "'," + sortOrder + "," + macId + "\n"
                    + "from tmp_tri\n"
                    + "where mac_id = " + macId + " \n"
                    + "and coa_code in (" + coaCode + ")";
            dao.execSQLRpt(strInsert + "\n" + sql);
            sortOrder++;
        }
        String updateSql = "update tmp_balance_sheet tmp,\n"
                + "(select amount*(-1) acc_total,coa_code from stock_op_value where date(tran_date) = '" + toDate + "') op\n"
                + "set tmp.acc_total = op.acc_total\n"
                + "where tmp.acc_code = op.coa_code ";
        dao.execSQLRpt(updateSql);
    }

    @Override
    public Object getAggResult(String sql
    ) {
        return dao.getAggResult(sql);
    }

    @Override
    public BalanceSheetRetObj getBSCalculateValue(String compCode, String macId) throws Exception {
        ProfitAndLostRetObj pl = getPLCalculateValue(compCode, macId);
        double profit = Util1.getDouble(pl.getNetProfit());
        String delSql = "delete from tmp_balance_sheet where mac_id = " + macId + " and sort_order= 10";
        String insertPL = "insert into tmp_balance_sheet(acc_code,curr_code,dept_code,acc_total,comp_code, sort_order,mac_id)\n"
                + "select '-','-','-'," + profit + ",'" + compCode + "',10," + macId + "";
        dao.execSQLRpt(delSql, insertPL);
        //
        BalanceSheetRetObj bs = new BalanceSheetRetObj();
        bs.setProfit(profit);
        String sql = "select abs(sum(acc_total)) acc_total,sort_order\n"
                + "from tmp_balance_sheet\n"
                + "where mac_id = " + macId + " and comp_code ='" + compCode + "'\n"
                + "group by sort_order";
        ResultSet rs;
        try {
            rs = dao.executeSql(sql);
            if (rs != null) {
                while (rs.next()) {
                    double ttl = rs.getDouble("acc_total");
                    int order = rs.getInt("sort_order");
                    switch (order) {
                        case 1 ->
                            bs.setFixedAss(ttl);
                        case 2 ->
                            bs.setCurrentAss(ttl);
                        case 3 ->
                            bs.setLiabilitie(ttl);
                        case 4 ->
                            bs.setCapital(ttl);
                    }
                    //curr
                    //lia
                    //capital
                }
            }
        } catch (Exception ex) {
            logger.error("getPLCalculateValue : " + ex.getMessage());
        }
        return bs;
    }

    @Override
    public void genIncomeAndExpense(String process, String compCode, String macId) {
        try {
            int sortOrder = 1;
            String[] tmp = process.split(",");
            String strSqlDelete = "delete from tmp_in_ex where mac_id = '" + macId + "'";
            dao.execSQLRpt(strSqlDelete);
            String strInsert = "insert into tmp_in_ex(coa_code,cur_code,dept_code,\n "
                    + "acc_total, comp_code, sort_order,mac_id)";
            for (String code : tmp) {
                String coaCode = getCOACode(code, compCode);
                String sql = "select coa_code,curr_id,ifnull(dept_code,'-'),if(dr_amt>0,dr_amt*-1,cr_amt) acc_toal,\n"
                        + "'" + compCode + "'," + sortOrder + "," + macId + "\n"
                        + "from tmp_tri\n"
                        + "where mac_id = " + macId + " \n"
                        + "and coa_code in (" + coaCode + ")";
                dao.execSQLRpt(strInsert + "\n" + sql);
                sortOrder++;
            }
        } catch (Exception e) {
            logger.error("genIncomeAndExpense : " + e.getMessage());
        }
    }

    @Override
    public ProfitAndLostRetObj calculateIncomeExpense(String compCode, String macId) {
        ProfitAndLostRetObj pl = new ProfitAndLostRetObj();
        String sql = "select sum(acc_total) acc_total,sort_order\n"
                + "	from tmp_in_ex\n"
                + "	where mac_id = " + macId + "\n"
                + "		and comp_code = '" + compCode + "'\n"
                + "group by sort_order";
        ResultSet rs;
        try {
            rs = dao.executeSql(sql);
            if (rs != null) {
                while (rs.next()) {
                    double ttl = rs.getDouble("acc_total");
                    int order = rs.getInt("sort_order");
                    switch (order) {
                        case 1 ->
                            pl.addSaleIncome(ttl);
                        case 2 ->
                            pl.addOtherIncome(ttl);
                        case 3 ->
                            pl.addPurchase(ttl);
                        case 4 ->
                            pl.addOtherExpense(ttl);
                    }
                    //curr
                    //lia
                    //capital
                }
            }
        } catch (Exception ex) {
            logger.error("calculateIncomeExpense : " + ex.getMessage());
        }
        return pl;
    }

    @Override
    public void deleteOpTemp(String macId) throws Exception {
        String delSql = "delete from tmp_op_cl where mac_id = " + macId;
        String delSql1 = "delete from tmp_tri where mac_id = " + macId;
        dao.execSQLRpt(delSql, delSql1);
    }

    @Override
    public double genOpBalance(String process, String opDate, String clDate,
            String endDate, String curr, String compCode, String dept, String macId) throws Exception {
        String[] coaCodes = process.split(",");
        double ttlOP = 0;
        if (coaCodes.length > 0) {
            for (String coaCode : coaCodes) {
                String opSql = "insert into tmp_op_cl(coa_code, curr_id,opening,mac_id) \n"
                        + "select a.acc_code, a.cur_code, sum(a.balance) balance, " + macId + "\n"
                        + "from (\n"
                        + "select source_acc_id acc_code,cur_code,sum(ifnull(dr_amt,0))-sum(ifnull(cr_amt,0)) balance,\n"
                        + "		sum(ifnull(dr_amt,0)) dr_amt, sum(ifnull(cr_amt,0)) cr_amt,trader_code\n"
                        + "	from coa_opening \n"
                        + "	where source_acc_id = '" + coaCode + "'\n"
                        + "        and (dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
                        + "        and comp_code = '" + compCode + "'\n"
                        + "        and date(op_date) = '" + opDate + "'\n"
                        + "        and (cur_code = '" + curr + "' or '-' ='" + curr + "')\n"
                        + "      group by acc_code,cur_code\n"
                        + "             union all\n"
                        + "select '" + coaCode + "' acc_code ,cur_code cur_code, sum(get_dr_cr_amt(source_ac_id, account_id, '" + coaCode + "', \n"
                        + "		ifnull(dr_amt,0), ifnull(cr_amt,0), 'DR')-get_dr_cr_amt(source_ac_id, \n"
                        + "             account_id, '" + coaCode + "', ifnull(dr_amt,0), ifnull(cr_amt,0), 'CR')) balance, \n"
                        + "		sum(ifnull(dr_amt,0)) dr_amt, sum(ifnull(cr_amt,0)) cr_amt,trader_code \n"
                        + "     from gl\n"
                        + "	where  (source_ac_id = '" + coaCode + "' or account_id = '" + coaCode + "') \n"
                        + "		and date(gl_date)>= '" + opDate + "'\n"
                        + "        and date(gl_date) < '" + clDate + "' \n"
                        + "        and (dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
                        + "        and comp_code = '" + compCode + "'\n"
                        + "        and (cur_code = '" + curr + "' or '-' ='" + curr + "')\n"
                        + "	group by acc_code,cur_code) a \n"
                        + "group by a.acc_code, a.cur_code";
                dao.execSQLRpt(opSql);
            }

            String strSql = "insert into tmp_tri(coa_code, curr_id, dept_code, mac_id, dr_amt, cr_amt)\n"
                    + "select toc.coa_code, toc.curr_id, gl.dept_code, " + macId + " as mac_id,\n"
                    + "sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, toc.coa_code, gl.dr_amt, gl.cr_amt, 'DR')) dr_amt,\n"
                    + "sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, toc.coa_code, gl.dr_amt, gl.cr_amt, 'CR')) cr_amt\n"
                    + "from tmp_op_cl toc\n"
                    + "join gl on (toc.coa_code = gl.source_ac_id or toc.coa_code = gl.account_id) and toc.curr_id = gl.cur_code\n"
                    + "where gl.gl_date between '" + clDate + "' and '" + endDate + "' and toc.mac_id = " + macId + " \n"
                    + "group by toc.coa_code, toc.curr_id, gl.dept_code";
            dao.execSQLRpt(strSql);

            strSql = "select sum(ifnull(opening,0)) as ttl_op\n"
                    + "from tmp_op_cl\n"
                    + "where mac_id = " + macId;
            ResultSet rs = dao.executeSql(strSql);

            if (rs != null) {
                if (rs.next()) {
                    ttlOP = rs.getDouble("ttl_op");
                }
            }
        }
        return ttlOP;
    }

}
