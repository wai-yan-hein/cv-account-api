
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.util.Util1;
import com.cv.accountswing.entity.temp.TmpOpeningClosing;
import com.cv.accountswing.entity.view.VAccOpeningD;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author winswe
 */
@Repository
public class COAOpeningDaoDImpl extends AbstractDao<Long, VAccOpeningD> implements COAOpeningDaoD {

    private static final Logger logger = LoggerFactory.getLogger(COAOpeningDaoDImpl.class);

    @Override
    public List<VAccOpeningD> search(String tranIdH) {
        String strSql = "select o from VAccOpeningD o where o.tranIdH = " + tranIdH;
        List<VAccOpeningD> listVOAD = findHSQLList(strSql);
        return listVOAD;
    }

    @Override
    public int delete(String tranId) {
        String strSql = "delete from AccOpeningD o where o.tranId = " + tranId;
        int cnt = execUpdateOrDelete(strSql);
        return cnt;
    }

    @Override
    public void insertFilter(String coaCode, int level, String opDate,
            String curr, String userCode) throws Exception {
        String strFilter = "";

        if (!coaCode.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "coa.coa_code = '" + coaCode + "'";
            } else {
                strFilter = strFilter + " and coa.coa_code = '" + coaCode + "'";
            }
        }

        if (!opDate.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "ifnull(op.op_date, '1900-01-01') <= '"
                        + Util1.toDateStr(opDate, "dd/MM/yyyy", "yyyy-MM-dd") + "'";
            } else {
                strFilter = strFilter + " and ifnull(op.op_date, '1900-01-01') <= '"
                        + Util1.toDateStr(opDate, "dd/MM/yyyy", "yyyy-MM-dd") + "'";
            }
        }

        if (!curr.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "coa.cur_code = '" + curr + "'";
            } else {
                strFilter = strFilter + " and coa.cur_code = '" + curr + "'";
            }
        }

        String strSql = "insert into tmp_op_filter(comp_code, coa_code, op_tran_id_d, "
                + "curr_id, op_date, user_code) "
                + "select coa.comp_code, coa.coa_code, op.tran_id_d, coa.cur_code, "
                + "ifnull(op.op_date, '1900-01-01') op_date,'" + userCode + "'"
                + " from (select comp_code, coa_code, cur_code, level\n"
                + "from chart_of_account, currency) coa left join (select m.tran_id_d, m.curr_id, m.op_date, m.coa_code, m.comp_code\n"
                + " from v_coa_opening_by_date m, ("
                + "select a.curr_id, a.coa_code, max(ifnull(a.op_date, '1900-01-01')) op_date "
                + "from v_coa_opening_by_date a group by a.curr_id, a.coa_code) f "
                + "where m.curr_id = f.curr_id and m.coa_code = f.coa_code and m.op_date = f.op_date) op on "
                + "coa.comp_code = op.comp_code and coa.coa_code = op.coa_code and coa.cur_code = op.curr_id "
                + "where coa.comp_code is not null and coa.level >= " + level;

        if (!strFilter.isEmpty()) {
            strSql = strSql + " and " + strFilter;
        }

        //strSql = strSql + " group by coa.comp_code, coa.coa_code, op.tran_id_d, op.curr_id";
        execSQL(strSql);
    }

    @Override
    public List<TmpOpeningClosing> getOpBalance(String coaCode, int level, String opDate,
            String curr, String userCode) throws Exception {
        insertFilter(coaCode, level, opDate, curr, userCode);
        String strSql = "insert into tmp_op_cl(coa_code, curr_id, user_code, opening, dr_amt, cr_amt) "
                + "select a.coa_code, a.curr_id, '" + userCode + "', sum(ifnull(a.balance,0)), 0, 0 "
                + "from (select tof.comp_code, tof.coa_code, tof.curr_id, aod.ex_rate, aod.dr_amt, "
                + "aod.cr_amt,(ifnull(aod.dr_amt,0)-ifnull(aod.cr_amt,0)) balance,"
                + "ifnull(tof.op_date, date('1900-01-01')) tran_date, 'Opening' tran_option, tof.user_code "
                + "from tmp_op_filter tof, acc_opening_d aod "
                + "where tof.coa_code = aod.coa_code and tof.op_tran_id_d = aod.tran_id "
                + "and tof.curr_id = aod.curr_code and tof.user_code = '" + userCode + "' "
                + "union all "
                + "select tof.comp_code, tof.coa_code, tof.curr_id, gl.ex_rate, "
                + "gl.dr_amt, gl.cr_amt, (ifnull(gl.dr_amt,0)-ifnull(gl.cr_amt,0)) balance, "
                + "gl.gl_date tran_date, 'GL' tran_option, tof.user_code "
                + "from tmp_op_filter tof, gl "
                + "where tof.coa_code = source_ac_id and tof.curr_id = from_cur_id "
                + "and gl_date >= tof.op_date and gl_date < '"
                + Util1.toDateStr(opDate, "dd/MM/yyyy", "yyyy-MM-dd")
                + "' and tof.user_code = '" + userCode + "') a group by a.coa_code, a.curr_id";

        //logger.info((strSql));
        execSQL(strSql);

        String strHSql = "select o from TmpOpeningClosing o where o.key.userCode = '" + userCode + "'";
        List<TmpOpeningClosing> listTOC = findHSQLList(strHSql);

        return listTOC;
    }

    @Override
    public void insertFilterGL(String coaCode, String opDate, int level,
            String curr, String userCode) throws Exception {
        String strFilter = "";

        if (!coaCode.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "gl.source_ac_id = '" + coaCode + "'";
            } else {
                strFilter = strFilter + " and gl.source_ac_id = '" + coaCode + "'";
            }
        }

        if (!opDate.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "ifnull(gl_date, '1900-01-01') = '"
                        + Util1.toDateStr(opDate, "dd/MM/yyyy", "yyyy-MM-dd") + "'";
            } else {
                strFilter = strFilter + " and ifnull(gl_date, '1900-01-01') = '"
                        + Util1.toDateStr(opDate, "dd/MM/yyyy", "yyyy-MM-dd") + "'";
            }
        }

        if (!curr.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "from_cur_id = '" + curr + "'";
            } else {
                strFilter = strFilter + " and from_cur_id = '" + curr + "'";
            }
        }

        if (strFilter.isEmpty()) {
            strFilter = "tran_source = 'OPENING'";
        } else {
            strFilter = strFilter + " and tran_source = 'OPENING'";
        }

        String strSql = "insert into tmp_op_filter(comp_code, coa_code, op_tran_id_d, curr_id, op_date, user_code) "
                + "select gl.comp_code, source_ac_id, gl_id, from_cur_id, gl_date, '" + userCode + "' "
                + "from gl, chart_of_account coa where gl.comp_code = coa.comp_code "
                + "and gl.source_ac_id = coa.coa_code and coa.level >= " + level;

        if (!strFilter.isEmpty()) {
            strSql = strSql + " and " + strFilter;
        }

        execSQL(strSql);
    }

    @Override
    public void genOpBalanceGL(String coaCode, String opDate,
            String clDate, int level, String curr, String userCode, String dept) throws Exception {
    }

    @Override
    public List<TmpOpeningClosing> getOpBalanceGL(String userCode, String coaCode, String macId) {
        String strHSql = "select o from TmpOpeningClosing o where o.key.userCode = "
                + "'" + userCode + "' and o.key.coaId ='" + coaCode + "' and o.key.macId = " + macId + "";
        List<TmpOpeningClosing> listTOC = findHSQLList(strHSql);
        return listTOC;
    }

    @Override
    public void genTriBalance(String compCode, String fromDate, String opDate, String tranDate,
            String coaCode, String currency, String dept, String cvId, String userCode) throws Exception {
    }

    @Override
    public void genArAp(String compCode, String fromDate, String opDate, String tranDate,
            String coaCode, String currency, String dept, String cvId, String userCode) throws Exception {
    }

    @Override
    public void genArAp1(String compCode, String fromDate, String opDate,
            String tranDate, String coaCode, String currency, String dept,
            String traderCode, String macId) throws Exception {
        String delSql = "delete from tmp_op_cl_apar where mac_id = '" + macId + "'";
        execSQL(delSql);
        String strSql = "insert into tmp_op_cl_apar(coa_code, curr_id, mac_id, trader_code, dept_code, dr_amt, cr_amt)\n"
                + "select vcc.account_code, vcc.cur_code," + macId + ", vcc.id trader_code ,ifnull(a.dept_code, '-') dept_code,\n"
                + "       sum(a.dr_amt) dr,\n"
                + "       sum(a.cr_amt) cr\n"
                + "  from v_trader_curr_dept vcc \n"
                + "  left join (\n"
                + "  select coa.comp_code,coa.op_date,coa.source_acc_id, coa.cur_code, coa.trader_code, coa.dept_code, \n"
                + "			        sum(ifnull(coa.dr_amt,0)) dr_amt, sum(ifnull(coa.cr_amt,0)) cr_amt\n"
                + "			   from coa_opening coa, trader t\n"
                + "               where coa.trader_code = t.code \n"
                + "               group by coa.trader_code \n"
                + "			  union all\n"
                + "			 select g.comp_code,g.gl_date,t.account_code, g.from_cur_id, g.trader_code, g.dept_code,\n"
                + "					sum(get_dr_cr_amt(g.source_ac_id, g.account_id, t.account_code, \n"
                + "					ifnull(g.dr_amt,0), ifnull(g.cr_amt,0), 'DR')) dr_amt,\n"
                + "					sum(get_dr_cr_amt(g.source_ac_id, g.account_id, t.account_code, ifnull(g.dr_amt,0), \n"
                + "                      ifnull(g.cr_amt,0), 'CR')) cr_amt\n"
                + "			   from gl g, trader t\n"
                + "			  where g.trader_code = t.code and g.gl_date <= '" + tranDate + "'\n"
                + "				and (g.comp_code = '" + compCode + "' or '-' = '" + compCode + "') "
                + "and (g.from_cur_id = '" + currency + "' or '-' = '" + currency + "') and (g.dept_code = '" + dept + "' or '-' = '" + dept + "') \n"
                + "			  group by g.comp_code,g.gl_date,g.comp_code, g.source_ac_id, g.account_id, g.dept_code, g.from_cur_id, \n"
                + "              g.tran_source, g.trader_code, t.code, t.account_code) a\n"
                + "  on vcc.comp_code = a.comp_code and vcc.account_code = a.source_acc_id and vcc.cur_code = a.cur_code\n"
                + "and vcc.id = a.trader_code  and vcc.dept_code = a.dept_code\n"
                + "where (vcc.account_code = '-' or '-' = '-' or vcc.coa_parent = '-') \n"
                + "and (vcc.cur_code = '" + currency + "' or '-' = '" + currency + "') and (a.cr_amt > 0 or a.dr_amt >0) \n"
                + "and (vcc.dept_code = '" + dept + "' or '-' = '" + dept + "') and (vcc.id ='" + traderCode + "' or '-' = '" + traderCode + "')\n"
                + "group by vcc.account_code,vcc.cur_code,vcc.comp_code,vcc.id;";
        execSQL(strSql);
    }

    @Override
    public void genTriBalance1(String compCode, String stDate,
            String enDate, String coaCode, String currency, String dept,
            String cvId, String userCode, String macId) throws Exception {
        deleteTmp(Integer.parseInt(macId));
        logger.info("inserting fileter");
        String strSqlFilter = "insert into tmp_gl_filter(comp_code, coa_code, dept_code, curr_id, tran_source, op_date, user_code,mac_id)"
                + "select vcc.comp_code, vcc.coa_code, ifnull(vcc.dept_code, '-') dept_code, vcc.cur_code, 'OPENING' as tran_source, ifnull(a.op_date, '1900-01-01') op_date,\n"
                + "'" + userCode + "','" + macId + "'  from v_coa_curr_dept vcc left join (\n"
                + "select comp_code, source_acc_id, ifnull(dept_code, '-') as dept_code, cur_code, \n"
                + "ifnull(coa.tran_source, '-') tran_source, max(op_date) as op_date\n"
                + "from coa_opening coa\n"
                + "where coa.op_date = '" + stDate + "' and ifnull(coa.tran_source, '-') = 'OPENING'\n"
                + "and (coa.comp_code = '" + compCode + "' or '-' = '" + compCode + "') and "
                + "(coa.cur_code = '" + currency + "' or '-' = '" + currency + "') "
                + "and (coa.dept_code = '" + dept + "' or '-' = '" + dept + "') \n"
                + "and coa.trader_code ='" + cvId + "' or '-' = '" + cvId + "' \n"
                + "group by coa.comp_code, coa.source_acc_id, coa.dept_code, coa.cur_code, coa.tran_source) a\n"
                + "on vcc.dept_code = a.dept_code and vcc.comp_code = a.comp_code and vcc.coa_code = a.source_acc_id\n"
                + "and vcc.cur_code = a.cur_code\n"
                + "where vcc.coa_level >= 3 and (vcc.coa_code = '" + coaCode + "' or '-' = '" + coaCode + "' or vcc.coa_parent = '-') "
                + "and (vcc.cur_code = '" + currency + "' or '-' = '" + currency + "')";
        execSQL(strSqlFilter);
        logger.info("inserting fileter end.");
        logger.info("inserting opeinging.");
        String strSql1 = "insert into tmp_op_cl(coa_code, curr_id, user_code, dr_amt, cr_amt,mac_id) \n"
                + "select coa_code, curr_id, '" + userCode + "', if(sum(dr_amt-cr_amt)>0, sum(dr_amt-cr_amt),0), if(sum(dr_amt-cr_amt)<0, sum(dr_amt-cr_amt)*-1,0), " + macId + "\n"
                + "from (\n"
                + "	select op.source_acc_id as coa_code, op.cur_code as curr_id,\n"
                + "		   sum(ifnull(op.dr_amt,0)) dr_amt, sum(ifnull(op.cr_amt,0)) cr_amt\n"
                + "	from  coa_opening op\n"
                + "	where date(op.op_date) = '" + stDate + "' \n"
                + "		and (op.dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
                + "	group by op.source_acc_id, op.cur_code\n"
                + "			union all\n"
                + "	select gl.account_id, gl.from_cur_id,sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, \n"
                + "			gl.account_id, ifnull(gl.dr_amt,0), ifnull(gl.cr_amt,0), 'DR')) dr_amt,\n"
                + "            sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, gl.account_id, ifnull(gl.dr_amt,0), \n"
                + "			ifnull(gl.cr_amt,0), 'CR')) cr_amt\n"
                + "     from gl \n"
                + "     where gl.account_id in (select coa_code from tmp_gl_filter where mac_id =" + macId + ")\n"
                + "     and gl.dept_code = gl.dept_code and date(gl.gl_date) between '" + stDate + "' \n"
                + "        and '" + enDate + "' and (gl.dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
                + "        and gl.comp_code = '" + compCode + "'\n"
                + "     group by gl.account_id, gl.from_cur_id, gl.source_ac_id, gl.account_id"
                + "                     union all \n"
                + "     select gl.source_ac_id, gl.from_cur_id,sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, \n"
                + "			gl.source_ac_id, ifnull(gl.dr_amt,0), ifnull(gl.cr_amt,0), 'DR')) dr_amt,\n"
                + "            sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, gl.source_ac_id, ifnull(gl.dr_amt,0), \n"
                + "			ifnull(gl.cr_amt,0), 'CR')) cr_amt\n"
                + "     from gl \n"
                + "     where gl.source_ac_id in (select coa_code from tmp_gl_filter where mac_id =" + macId + ")\n"
                + "     and gl.dept_code = gl.dept_code and date(gl.gl_date) between '" + stDate + "' \n"
                + "        and '" + enDate + "' and (gl.dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
                + "        and gl.comp_code = '" + compCode + "'\n"
                + "     group by gl.account_id, gl.from_cur_id, gl.source_ac_id, gl.account_id) a \n"
                + "     group by coa_code, curr_id";
        //logger.info(strSql1);
        execSQL(strSql1);

        logger.info("inserting opening end.");
        //updatePreviousClosing(opDate, tranDate, userCode, dept);
    }

    @Override
    public void genOpBalanceGL1(String coaCode, String opDate, String clDate,
            int level, String curr, String userCode, String dept, String macId) throws Exception {
        deleteTmp(Integer.parseInt(macId));
        String insertSql = "insert into tmp_op_filter(comp_code, coa_code, op_tran_id_d, curr_id, op_date, user_code,mac_id,dept_code) \n"
                + "select coa.comp_code,coa.coa_code,op.coa_op_id,ifnull(op.cur_code,'" + curr + "'),op.op_date,'" + userCode + "'," + macId + "\n"
                + ",op.dept_code "
                + "from chart_of_account coa left join coa_opening op \n"
                + "on coa.coa_code = op.source_acc_id and coa.comp_code = op.comp_code\n"
                + "where coa.coa_code = '" + coaCode + "'"
                + "group by coa.coa_code";
        execSQL(insertSql);
        String opSql = "insert into tmp_op_cl(coa_code, curr_id, user_code, opening, dr_amt, cr_amt,mac_id) \n"
                + "select coa_code, curr_id, '" + userCode + "', sum(balance), 0, 0," + macId + "\n"
                + "from (\n"
                + "select tof.coa_code, tof.curr_id, ifnull(coa.dr_amt,0)-ifnull(coa.cr_amt,0) balance,\n"
                + "		ifnull(coa.dr_amt,0) dr_amt, ifnull(coa.cr_amt,0) cr_amt,tof.trader_code\n"
                + "	from tmp_op_filter tof, coa_opening coa\n"
                + "	where  tof.comp_code = coa.comp_code and tof.curr_id = coa.cur_code \n"
                + "		and tof.coa_code = coa.source_acc_id \n"
                + "		and tof.user_code = '" + userCode + "' and tof.coa_code = '" + coaCode + "'"
                + "             and (coa.dept_code = '" + dept + "' or '-' = '" + dept + "')"
                + "             and tof.mac_id = " + macId + "\n"
                + "             union all\n"
                + "select tof.coa_code, tof.curr_id, get_dr_cr_amt(gl.source_ac_id, gl.account_id, tof.coa_code, \n"
                + "		ifnull(gl.dr_amt,0), ifnull(gl.cr_amt,0), 'DR')-get_dr_cr_amt(gl.source_ac_id, \n"
                + "             gl.account_id, tof.coa_code, ifnull(gl.dr_amt,0), ifnull(gl.cr_amt,0), 'CR') balance, \n"
                + "		ifnull(gl.dr_amt,0) dr_amt, ifnull(gl.cr_amt,0),tof.trader_code \n"
                + "     from tmp_op_filter tof, gl\n"
                + "	where tof.comp_code = gl.comp_code and (tof.coa_code = gl.source_ac_id or \n"
                + "             tof.coa_code = gl.account_id) and \n"
                + "		tof.curr_id = gl.from_cur_id and gl.gl_date >= '" + opDate + "' "
                + "             and gl.gl_date < '" + Util1.toDateStrMYSQL(clDate, "dd/MM/yyyy") + "' \n"
                + "		and tof.user_code = '" + userCode + "' and (gl.dept_code = '" + dept + "' or '-' = '" + dept + "')"
                + "             and tof.coa_code = '" + coaCode + "' and tof.mac_id = " + macId + ") a \n"
                + "group by a.coa_code, a.curr_id\n";
        execSQL(opSql);
    }

    private void updatePreviousClosing(String opDate, String clDate, String userCode, String dept) throws Exception {
        String updateSql = "update tmp_op_cl cl, (\n"
                + "select coa_code, curr_id, '" + userCode + "', sum(balance) balance, 0, 0 as opening \n"
                + "from (\n"
                + "select tof.coa_code, tof.curr_id, ifnull(coa.dr_amt,0)-ifnull(coa.cr_amt,0) balance,\n"
                + "		ifnull(coa.dr_amt,0) dr_amt, ifnull(coa.cr_amt,0) cr_amt,tof.trader_code\n"
                + "	from tmp_op_filter tof, coa_opening coa\n"
                + "	where  tof.comp_code = coa.comp_code and tof.curr_id = coa.cur_code \n"
                + "		and tof.coa_code = coa.source_acc_id \n"
                + "		and tof.user_code = '" + userCode + "'\n"
                + "        and (coa.dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
                + "union all\n"
                + "select tof.coa_code, tof.curr_id, get_dr_cr_amt(gl.source_ac_id, gl.account_id, tof.coa_code, \n"
                + "		ifnull(gl.dr_amt,0), ifnull(gl.cr_amt,0), 'DR')-get_dr_cr_amt(gl.source_ac_id, \n"
                + "             gl.account_id, tof.coa_code, ifnull(gl.dr_amt,0), ifnull(gl.cr_amt,0), 'CR') balance, \n"
                + "		ifnull(gl.dr_amt,0) dr_amt, ifnull(gl.cr_amt,0),tof.trader_code \n"
                + "     from tmp_op_filter tof, gl\n"
                + "	where tof.comp_code = gl.comp_code and (tof.coa_code = gl.source_ac_id or \n"
                + "             tof.coa_code = gl.account_id) and \n"
                + "		tof.curr_id = gl.from_cur_id and gl.gl_date >= '" + opDate + "'\n"
                + "        and gl.gl_date < '" + clDate + "' \n"
                + "		and tof.user_code = '" + userCode + "' and (gl.dept_code = '" + dept + "' or '-' = '" + dept + "')) a \n"
                + "group by a.coa_code, a.curr_id\n"
                + ") op\n"
                + "set cl.closing = op.balance\n"
                + "where cl.coa_code = op.coa_code;";
        execSQL(updateSql);
    }

    @Override
    public void getOpBalanceByTrader(String coaCode, String opDate, String clDate,
            int level, String curr, String userCode, String dept, String traderCode, String macId, String compCode) throws Exception {
        String delSql = "delete from tmp_op_cl where mac_id = " + macId + "";
        execSQL(delSql);
        String strSql = "insert into tmp_op_cl(coa_code, curr_id, user_code, trader_code, dept_code, dr_amt, cr_amt,mac_id)\n"
                + "select vcc.account_code, vcc.cur_code,'1', vcc.id trader_code ,ifnull(a.dept_code, '-') dept_code,\n"
                + "       sum(a.dr_amt) dr,\n"
                + "       sum(a.cr_amt) cr, " + macId + "\n"
                + "  from v_trader_curr_dept vcc \n"
                + "  left join (\n"
                + "  select coa.comp_code,coa.op_date,coa.source_acc_id, coa.cur_code, coa.trader_code, coa.dept_code, \n"
                + "			        sum(ifnull(coa.dr_amt,0)) dr_amt, sum(ifnull(coa.cr_amt,0)) cr_amt\n"
                + "			   from coa_opening coa, trader t\n"
                + "               where coa.trader_code = t.code and coa.trader_code = '" + traderCode + "'\n"
                + "               group by coa.trader_code \n"
                + "			  union all\n"
                + "			 select g.comp_code,g.gl_date,t.account_code, g.from_cur_id, g.trader_code, g.dept_code,\n"
                + "					sum(get_dr_cr_amt(g.source_ac_id, g.account_id, t.account_code, \n"
                + "					ifnull(g.dr_amt,0), ifnull(g.cr_amt,0), 'DR')) dr_amt,\n"
                + "					sum(get_dr_cr_amt(g.source_ac_id, g.account_id, t.account_code, ifnull(g.dr_amt,0), \n"
                + "                      ifnull(g.cr_amt,0), 'CR')) cr_amt\n"
                + "			   from gl g, trader t\n"
                + "			  where g.trader_code = t.code and g.gl_date < '" + clDate + "' and g.trader_code = '" + traderCode + "'\n"
                + "				and (g.comp_code = " + compCode + " or '-' = '" + compCode + "') "
                + "and (g.from_cur_id = '" + curr + "' or '-' = '" + curr + "') and (g.dept_code = '" + dept + "' or '-' = '" + dept + "') \n"
                + "			  group by g.comp_code,g.gl_date,g.comp_code, g.source_ac_id, g.account_id, g.dept_code, g.from_cur_id, \n"
                + "              g.tran_source, g.trader_code, t.code, t.account_code) a\n"
                + "  on vcc.comp_code = a.comp_code and vcc.account_code = a.source_acc_id and vcc.cur_code = a.cur_code\n"
                + "and vcc.id = a.trader_code  and vcc.dept_code = a.dept_code\n"
                + "where (vcc.account_code = '" + coaCode + "' or '-' = '" + coaCode + "' or vcc.coa_parent = '-') \n"
                + "and (vcc.cur_code = '" + curr + "' or '-' = '" + curr + "') and (a.cr_amt > 0 or a.dr_amt >0) \n"
                + "and (vcc.dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
                + "group by vcc.account_code,vcc.cur_code,vcc.comp_code,vcc.id;";
        execSQL(strSql);
    }

    private void deleteTmp(Integer machineId) throws Exception {
        String delSql = "delete from tmp_op_filter where mac_id = " + machineId + "";
        String delSql1 = "delete from tmp_op_cl where mac_id =" + machineId + "";
        String delSql2 = "delete from tmp_gl_filter where mac_id =" + machineId + "";
        execSQL(delSql, delSql1, delSql2);
    }
}
