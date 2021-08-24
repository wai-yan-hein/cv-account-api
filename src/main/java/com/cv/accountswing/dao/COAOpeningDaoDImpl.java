
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.util.Util1;
import com.cv.accountswing.entity.temp.TmpOpeningClosing;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author winswe
 */
@Repository
public class COAOpeningDaoDImpl extends AbstractDao<Long, TmpOpeningClosing> implements COAOpeningDaoD {

    private static final Logger logger = LoggerFactory.getLogger(COAOpeningDaoDImpl.class);

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
                strFilter = "cur_code = '" + curr + "'";
            } else {
                strFilter = strFilter + " and cur_code = '" + curr + "'";
            }
        }

        if (strFilter.isEmpty()) {
            strFilter = "tran_source = 'OPENING'";
        } else {
            strFilter = strFilter + " and tran_source = 'OPENING'";
        }

        String strSql = "insert into tmp_op_filter(comp_code, coa_code, op_tran_id_d, curr_id, op_date, user_code) "
                + "select gl.comp_code, source_ac_id, gl_id, cur_code, gl_date, '" + userCode + "' "
                + "from gl, chart_of_account coa where gl.comp_code = coa.comp_code "
                + "and gl.source_ac_id = coa.coa_code and coa.level >= " + level;

        if (!strFilter.isEmpty()) {
            strSql = strSql + " and " + strFilter;
        }

        execSQL(strSql);
    }

    @Override
    public List<TmpOpeningClosing> getOpBalanceGL(String coaCode, String macId) {
        String strHSql = "select o from TmpOpeningClosing o where o.key.coaCode = '" + coaCode + "' and o.key.macId = " + macId + " ";
        List<TmpOpeningClosing> listTOC = findHSQLList(strHSql);
        return listTOC;
    }

    @Override
    public void genArAp(String compCode, String stDate,
            String enDate, String coaCode, String currency, String dept,
            String traderCode, String macId) throws Exception {
        String delSql = "delete from tmp_op_cl_apar where mac_id = " + macId + "";
        execSQL(delSql);
        String strSql = "insert into tmp_op_cl_apar(coa_code, cur_code, trader_code, dept_code, dr_amt, cr_amt,mac_id,comp_code)\n"
                + "select gl.account_id, gl.cur_code,gl.trader_code,gl.dept_code,sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, \n"
                + "			gl.account_id, gl.dr_amt, gl.cr_amt, 'DR')) dr_amt,\n"
                + "                     sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, gl.account_id, gl.dr_amt, \n"
                + "			gl.cr_amt, 'CR')) cr_amt," + macId + ",gl.comp_code\n"
                + "     from (	select source_ac_id,account_id, cur_code,trader_code,dept_code,sum(ifnull(dr_amt,0)) dr_amt,sum(ifnull(cr_amt,0)) cr_amt,comp_code\n"
                + "			from gl \n"
                + "			where account_id in (select distinct account_code from trader where comp_code='" + compCode + "')\n"
                + "			and date(gl_date) between '" + stDate + "' \n"
                + "			and '" + enDate + "' and (dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
                + "			and comp_code = '" + compCode + "' and (cur_code = '" + currency + "' or '-'='" + currency + "')\n"
                + "                     and trader_code is not null\n"
                + "			group by  cur_code, account_id,trader_code) gl\n"
                + "group by gl.account_id, gl.cur_code,gl.trader_code";
        execSQL(strSql);
    }

    @Override
    public void genTriBalance(String compCode, String stDate,
            String enDate, String opDate, String currency, String dept, String macId) throws Exception {
        deleteTmp(Integer.parseInt(macId));
        String strSql1 = "insert into tmp_tri(coa_code, curr_id, dr_amt, cr_amt,mac_id,dept_code) \n"
                + "select coa_code, curr_id,if(sum(dr_amt-cr_amt)>0, sum(dr_amt-cr_amt),0), if(sum(dr_amt-cr_amt)<0, sum(dr_amt-cr_amt)*-1,0)," + macId + ",dept_code\n"
                + "from (\n"
                + "	select op.source_acc_id as coa_code, op.cur_code as curr_id,\n"
                + "		   sum(ifnull(op.dr_amt,0)) dr_amt, sum(ifnull(op.cr_amt,0)) cr_amt,dept_code\n"
                + "	from  coa_opening op\n"
                + "	where date(op.op_date) = '" + opDate + "' \n"
                + "		and (op.dept_code = '" + dept + "' or '-' = '" + dept + "' )\n"
                + "             and (cur_code = '" + currency + "' or '-'='" + currency + "')\n"
                + "	group by op.source_acc_id, op.cur_code\n"
                + "			union all\n"
                + "	select gl.source_ac_id, gl.cur_code,sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, \n"
                + "			gl.source_ac_id, gl.dr_amt, gl.cr_amt, 'DR')) dr_amt,\n"
                + "                     sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, gl.source_ac_id, gl.dr_amt, \n"
                + "			gl.cr_amt, 'CR')) cr_amt,dept_code\n"
                + "	from (	select source_ac_id,account_id, cur_code,sum(ifnull(dr_amt,0)) dr_amt,sum(ifnull(cr_amt,0)) cr_amt,dept_code\n"
                + "			from gl \n"
                + "			where source_ac_id in (select coa_code from chart_of_account where comp_code='" + compCode + "')\n"
                + "			and date(gl_date) between '" + stDate + "' \n"
                + "			and '" + enDate + "' and (dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
                + "			and comp_code = '" + compCode + "' and (cur_code = '" + currency + "' or '-'='" + currency + "')\n"
                + "			group by account_id, cur_code, source_ac_id) gl\n"
                + "	group by gl.account_id, gl.cur_code, gl.source_ac_id\n"
                + "			union all \n"
                + "    select gl.account_id, gl.cur_code,sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, \n"
                + "			gl.account_id, gl.dr_amt, gl.cr_amt, 'DR')) dr_amt,\n"
                + "                     sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, gl.account_id, gl.dr_amt, \n"
                + "			gl.cr_amt, 'CR')) cr_amt,dept_code\n"
                + "     from (	select source_ac_id,account_id, cur_code,sum(ifnull(dr_amt,0)) dr_amt,sum(ifnull(cr_amt,0)) cr_amt,dept_code\n"
                + "			from gl \n"
                + "			where account_id in (select coa_code from chart_of_account where comp_code='" + compCode + "')\n"
                + "			and date(gl_date) between '" + stDate + "' \n"
                + "			and '" + enDate + "' and (dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
                + "			and comp_code = '" + compCode + "' and (cur_code = '" + currency + "' or '-'='" + currency + "')\n"
                + "			group by account_id, cur_code, source_ac_id) gl\n"
                + "	group by gl.account_id, gl.cur_code, gl.source_ac_id) a\n"
                + "group by coa_code, curr_id";
        execSQL(strSql1);
        logger.info("inserting opening end.");
        //updatePreviousClosing(opDate, tranDate, userCode, dept);
    }

    @Override
    public void genOpBalanceGL(String coaCode, String opDate, String clDate,
            int level, String curr, String compCode, String dept, String macId,
            String traderCode) throws Exception {
        deleteTmp(Integer.parseInt(macId));
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
                + "        and (trader_code = '" + traderCode + "' or '-' = '" + traderCode + "')\n"
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
                + "        and (trader_code = '" + traderCode + "' or '-' = '" + traderCode + "')\n"
                + "	group by acc_code,cur_code) a \n"
                + "group by a.acc_code, a.cur_code";
        /* String strSql = "insert into tmp_op_cl(coa_code, curr_id, trader_code, dept_code, dr_amt, cr_amt,mac_id)\n"
        + "select vcc.account_code, vcc.cur_code, vcc.id trader_code ,ifnull(a.dept_code, '-') dept_code,\n"
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
        + "group by vcc.account_code,vcc.cur_code,vcc.comp_code,vcc.id;";*/
 /*String strSql = "insert into tmp_op_cl(coa_code, curr_id,opening,mac_id) \n"
 + "select coa_code, curr_id,sum(dr_amt - cr_amt) opening," + macId + "\n"
 + "from (\n"
 + "	select op.source_acc_id as coa_code, op.cur_code as curr_id,\n"
 + "		   sum(ifnull(op.dr_amt,0)) dr_amt, sum(ifnull(op.cr_amt,0)) cr_amt,dept_code\n"
 + "	from  coa_opening op\n"
 + "	where date(op.op_date) = '" + opDate + "' \n"
 + "		and (op.dept_code = '" + dept + "' or '-' = '" + dept + "' )\n"
 + "             and (cur_code = '" + curr + "' or '-'='" + curr + "')\n"
 + "             and (trader_code = '" + traderCode + "' or '-' ='" + traderCode + "')\n"
 + "             and (source_acc_id = '" + coaCode + "' or '-' = '" + coaCode + "')\n"
 + "             and comp_code = '" + compCode + "'\n"
 + "	group by op.source_acc_id, op.cur_code\n"
 + "			union all\n"
 + "	select gl.source_ac_id, gl.cur_code,sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, \n"
 + "			gl.source_ac_id, gl.dr_amt, gl.cr_amt, 'DR')) dr_amt,\n"
 + "                     sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, gl.source_ac_id, gl.dr_amt, \n"
 + "			gl.cr_amt, 'CR')) cr_amt,dept_code\n"
 + "	from (	select source_ac_id,account_id, cur_code,sum(ifnull(dr_amt,0)) dr_amt,sum(ifnull(cr_amt,0)) cr_amt,dept_code\n"
 + "			from gl \n"
 + "			where source_ac_id in (select coa_code from chart_of_account where comp_code='" + compCode + "'\n"
 + "                     and  (coa_code = '" + coaCode + "' or '-' = '" + coaCode + "'))\n"
 + "			and date(gl_date)= '" + opDate + "' and date(gl_date) < '" + clDate + "' \n"
 + "                     and (dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
 + "			and comp_code = '" + compCode + "' and (cur_code = '" + curr + "' or '-'='" + curr + "')\n"
 + "			and (trader_code = '" + traderCode + "' or '-' ='" + traderCode + "')\n"
 + "			group by account_id, cur_code, source_ac_id) gl\n"
 + "	group by gl.account_id, gl.cur_code, gl.source_ac_id\n"
 + "			union all \n"
 + "    select gl.account_id, gl.cur_code,sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, \n"
 + "			gl.account_id, gl.dr_amt, gl.cr_amt, 'DR')) dr_amt,\n"
 + "                     sum(get_dr_cr_amt(gl.source_ac_id, gl.account_id, gl.account_id, gl.dr_amt, \n"
 + "			gl.cr_amt, 'CR')) cr_amt,dept_code\n"
 + "     from (	select source_ac_id,account_id, cur_code,sum(ifnull(dr_amt,0)) dr_amt,sum(ifnull(cr_amt,0)) cr_amt,dept_code\n"
 + "			from gl \n"
 + "			where account_id in (select coa_code from chart_of_account where comp_code='" + compCode + "'\n"
 + "                     and  (coa_code = '" + coaCode + "' or '-' = '" + coaCode + "'))\n"
 + "			and date(gl_date) = '" + opDate + "' and date(gl_date) < '" + clDate + "' \n"
 + "            and (dept_code = '" + dept + "' or '-' = '" + dept + "')\n"
 + "			and comp_code = '" + compCode + "' and (cur_code = '" + curr + "' or '-'='" + curr + "')\n"
 + "			and (trader_code = '" + traderCode + "' or '-' ='" + traderCode + "')\n"
 + "			group by account_id, cur_code, source_ac_id) gl\n"
 + "	group by gl.account_id, gl.cur_code, gl.source_ac_id) a\n"
 + "group by coa_code, curr_id";*/
        execSQL(opSql);
    }

    private void deleteTmp(Integer machineId) throws Exception {
        String delSql = "delete from tmp_op_filter where mac_id = " + machineId + "";
        String delSql1 = "delete from tmp_tri where mac_id =" + machineId + "";
        String delSql2 = "delete from tmp_gl_filter where mac_id =" + machineId + "";
        String delSql3 = "delete from tmp_op_cl where mac_id =" + machineId + "";
        execSQL(delSql, delSql1, delSql2, delSql3);
    }
}
