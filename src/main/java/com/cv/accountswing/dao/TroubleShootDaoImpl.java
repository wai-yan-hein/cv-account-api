
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.entity.TraderType;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lenovo
 */
@Repository
public class TroubleShootDaoImpl extends AbstractDao<String, TraderType> implements TroubleShootDao {

    @Override
    public List<String> shootTri() throws Exception {
        List<String> logs = new ArrayList<>();
        //check source acc
        String sql = "select distinct source_ac_id,tran_source  from gl where source_ac_id not in(\n"
                + "select coa_code from chart_of_account) ";
        ResultSet rs = getResultSet(sql);
        if (rs != null) {
            while (rs.next()) {
                String sourceAcc = rs.getString("source_ac_id");
                String tranSource = rs.getString("tran_source");
                logs.add(tranSource + " : Gl take Source Account which not exist in Chart Of Accouunt : " + sourceAcc);
            }
        }
        //check account acc
        String sql1 = "select distinct account_id,tran_source  from gl where account_id not in(\n"
                + "select coa_code from chart_of_account) ";
        ResultSet rs1 = getResultSet(sql1);
        if (rs1 != null) {
            while (rs1.next()) {
                String account = rs1.getString("account_id");
                String tranSource = rs1.getString("tran_source");
                logs.add(tranSource + " : Gl take Account which not exist in Chart Of Accouunt : " + account);
            }
        }
        //check gl date
        String sql2 = "select gl_code,tran_source from gl where (gl_date is null or gl_date = '') ";
        ResultSet rs2 = getResultSet(sql2);
        if (rs2 != null) {
            while (rs2.next()) {
                String glCode = rs2.getString("gl_code");
                String tranSource = rs2.getString("tran_source");
                logs.add(tranSource + " : Gl date is null in Gl Code : " + glCode);
            }
        }
        //check same account
        String sql3 = "select gl_code,tran_source from gl where source_ac_id = account_id ";
        ResultSet rs3 = getResultSet(sql3);
        if (rs3 != null) {
            while (rs3.next()) {
                String glCode = rs3.getString("gl_code");
                String tranSource = rs3.getString("tran_source");
                logs.add(tranSource + " : Source Account Code and Accound Code are the same in Gl Code : " + glCode);
            }
        }
        //check gl date
        String sql4 = "select gl_code,tran_source from gl where (dept_code is null or dept_code = '') ";
        ResultSet rs4 = getResultSet(sql4);
        if (rs4 != null) {
            while (rs4.next()) {
                String glCode = rs4.getString("gl_code");
                String tranSource = rs4.getString("tran_source");
                logs.add(tranSource + " : Department is null in Gl Code : " + glCode);
            }
        }
        return logs;
    }

    @Override
    public ResultSet executeQuery(String query) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> shootAPAR() throws Exception {
        List<String> logs = new ArrayList<>();
        return logs;
    }

}
