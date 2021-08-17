/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.entity.temp.TmpOpeningClosing;
import org.springframework.stereotype.Repository;

/**
 *
 * @author winswe
 */
@Repository
public class COAOpeningDaoImpl extends AbstractDao<Long, TmpOpeningClosing> implements COAOpeningDao {

    @Override
    public void GenerateZero(String tranIdH, String compCode, String currCode) throws Exception {
        String strSql = "insert into acc_opening_d(tran_id_h, coa_code, curr_code, dr_amt, cr_amt) "
                + "select " + tranIdH + ", coa_code, cur_code, 0, 0 "
                + "from v_coa_opening where comp_code = '" + compCode
                + "' and cur_code = '" + currCode + "'";
        execSQL(strSql);
    }

    @Override
    public void deleteOpening(Long id) throws Exception {
        String strSql1 = "delete from acc_opening_d where tran_id_h = " + id;
        String strSql2 = "delete from acc_opening_h where tran_id = " + id;

        execSQL(strSql1, strSql2);
    }

    @Override
    public void GenerateZeroGL(String opDate, String userCode, String compCode,
            String currCode, String dept, String coaGroup) throws Exception {
        if (!dept.equals("-") && !currCode.equals("-")) {
            String strSql = "insert into gl(gl_date, source_ac_id, cur_code, dr_amt, "
                    + "cr_amt, user_code, comp_code, tran_source, created_date, dept_code) "
                    + "select '" + opDate + "', child_coa_code, cur_code, 0, 0, '" + userCode + "', '"
                    + compCode + "', 'OPENING', sysdate(), '" + dept
                    + "' from v_coa_tree where coa_level2 >= 3 and cur_code = '" + currCode
                    + " and comp_code1 = '" + compCode + "' and coa_parent1 in (" + coaGroup + ")";
            execSQL(strSql);

            String strSql1 = "insert into gl(gl_date, source_ac_id, cur_code, dr_amt, "
                    + "cr_amt, user_code, comp_code, tran_source, created_date, dept_code, trader_code) "
                    + "select '" + opDate + "', coa_code, '" + currCode + "', 0, 0, '" + userCode + "', '"
                    + compCode + "', 'OPENING', sysdate(), '" + dept + "', trader_code"
                    + " from v_cv_coa where comp_code = " + compCode;
            execSQL(strSql1);
        }

    }

    @Override
    public void deleteOpeningGL(String opDate, String compCode, String currCode, String dept) throws Exception {
        String strSql = "delete from gl where gl_date = '" + opDate + "' and tran_source = 'OPENING' and "
                + "comp_code = " + compCode + " and cur_code = '" + currCode + "'";

        if (!dept.equals("-")) {
            strSql = strSql + " and dept_code = '" + dept + "'";
        }
        execSQL(strSql);
    }

    @Override
    public void insertCoaOpening(String opDate, String compCode, String currCode,
            String dept, String userCode) throws Exception {
        String strSql = "insert into gl(gl_date, source_ac_id, cur_code, dr_amt, "
                + "cr_amt, user_code, comp_code, tran_source, created_date, dept_code) "
                + "select '" + opDate + "', coa_code, cur_code, 0, 0, '" + userCode + "', '"
                + compCode + "', 'OPENING', sysdate(), dept_code "
                + " from v_coa_curr_dept where level >= 3 "
                + "and (cur_code = '" + currCode + "' or '" + currCode + "' = '-')"
                + " and (dept_code = '" + dept + "' or '" + dept + "' = '-')"
                + " and comp_code = " + compCode;
        execSQL(strSql);
    }

    @Override
    public void generateZeroOpening(String opDate, String userCode, String compCode, String currCode, String dept, String coaGroup) throws Exception {
        if (!dept.equals("-") && !currCode.equals("-")) {
            String strSql = "insert into coa_opening(op_date, source_acc_id, cur_code, dr_amt, "
                    + "cr_amt, user_code, comp_code, tran_source, created_date, dept_code) "
                    + "select '" + opDate + "', child_coa_code, '" + currCode + "', 0, 0, '" + userCode + "', '"
                    + compCode + "', 'OPENING', sysdate(), '" + dept
                    + "' from v_coa_tree where coa_level2 >= 3 \n"
                    + " and comp_code1 = '" + compCode + "' and coa_parent1 in (" + coaGroup + ")";
            execSQL(strSql);

            String strSql1 = "insert into coa_opening(op_date, source_acc_id, cur_code, dr_amt, "
                    + "cr_amt, user_code, comp_code, tran_source, created_date, dept_code, trader_code) "
                    + "select '" + opDate + "', coa_code, '" + currCode + "', 0, 0, '" + userCode + "', '"
                    + compCode + "', 'OPENING', sysdate(), '" + dept + "', trader_code"
                    + " from v_cv_coa where comp_code = " + compCode;
            execSQL(strSql1);
        }
    }

    @Override
    public void deleteOpening(String opDate, String compCode, String currCode, String dept) throws Exception {
        String strSql = "delete from coa_opening where op_date = '" + opDate + "' and tran_source = 'OPENING' and "
                + "comp_code = " + compCode + " and cur_code = '" + currCode + "'";

        if (!dept.equals("-")) {
            strSql = strSql + " and dept_code = '" + dept + "'";
        }
        execSQL(strSql);
    }
}
