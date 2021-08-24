/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.util.Util1;
import com.cv.accountswing.entity.Gl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author winswe
 */
@Repository
public class GlDaoImpl extends AbstractDao<String, Gl> implements GlDao {

    private static final Logger log = LoggerFactory.getLogger(GlDaoImpl.class);
    @Autowired
    private SeqTableDao dao;

    @Override
    public Gl save(Gl gl) throws Exception {
        persist(gl);
        return gl;
    }

    @Override
    public List<Gl> saveBatchGL(List<Gl> listGL) throws Exception {
        if (listGL != null) {
            if (!listGL.isEmpty()) {
                saveBatch(listGL);
            }
        }

        return listGL;
    }

    @Override
    public Gl findById(String glCode) {
        Gl gl = getByKey(glCode);
        return gl;
    }

    @Override
    public List<Gl> search(String from, String to, String desp, String sourceAcId,
            String acId, String frmCurr, String toCurr, String reference, String dept,
            String vouNo, String cvId, String chequeNo, String compCode, String tranSource,
            String glVouNo, String splitId, String projectId) {
        String strSql = "select o from Gl o ";
        String strFilter = "";

        if (!from.equals("-") && !to.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glDate between '" + Util1.toDateStrMYSQL(from)
                        + "' and '" + Util1.toDateStrMYSQL(to) + "'";
            } else {
                strFilter = strFilter + " and o.glDate between '"
                        + Util1.toDateStrMYSQL(from) + "' and '" + Util1.toDateStrMYSQL(to) + "'";
            }
        } else if (!from.endsWith("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glDate >= '" + Util1.toDateStrMYSQL(from) + "'";
            } else {
                strFilter = strFilter + " and o.glDate >= '" + Util1.toDateStrMYSQL(from) + "'";
            }
        } else if (!to.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glDate <= '" + Util1.toDateStrMYSQL(to) + "'";
            } else {
                strFilter = strFilter + " and o.glDate <= '" + Util1.toDateStrMYSQL(to) + "'";
            }
        }

        if (!desp.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.description like '" + desp + "'";
            } else {
                strFilter = strFilter + " and o.description like '" + desp + "'";
            }
        }

        if (!sourceAcId.equals("-") && !acId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.sourceAcId = '" + sourceAcId + "' and o.accountId = '" + acId + "'";
            } else {
                strFilter = strFilter + " and o.sourceAcId = '" + sourceAcId + "' and o.accountId = '" + acId + "'";
            }
        } else if (!sourceAcId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.sourceAcId = '" + sourceAcId + "' or o.accountId = '" + sourceAcId + "'";
            } else {
                strFilter = strFilter + " and o.sourceAcId = '" + sourceAcId + "' or o.accountId = '" + sourceAcId + "'";
            }
        }

        if (!projectId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.projectId = " + projectId;
            } else {
                strFilter = strFilter + " and o.projectId = " + acId;
            }
        }

        if (!frmCurr.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.curCode = '" + frmCurr + "'";
            } else {
                strFilter = strFilter + " and o.curCode = '" + frmCurr + "'";
            }
        }

        if (!toCurr.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.toCurId = '" + toCurr + "'";
            } else {
                strFilter = strFilter + " and o.toCurId = '" + toCurr + "'";
            }
        }

        if (!reference.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.reference like '%" + reference + "%'";
            } else {
                strFilter = strFilter + " and o.reference like '%" + reference + "%'";
            }
        }

        if (!dept.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.deptId = '" + dept + "'";
            } else {
                strFilter = strFilter + " and o.deptId = '" + dept + "'";
            }
        }

        if (!vouNo.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.vouNo like '%" + vouNo + "%'";
            } else {
                strFilter = strFilter + " and o.vouNo like '%" + vouNo + "%'";
            }
        }

        if (!cvId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.traderCode = " + cvId;
            } else {
                strFilter = strFilter + " and o.traderCode = " + cvId;
            }
        }

        if (!chequeNo.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.chequeNo like '%" + chequeNo + "%'";
            } else {
                strFilter = strFilter + " and o.chequeNo like '%" + chequeNo + "%'";
            }
        }

        if (!compCode.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.compCode = " + compCode;
            } else {
                strFilter = strFilter + " and o.compCode = " + compCode;
            }
        }

        if (!tranSource.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.tranSource = '" + tranSource + "'";
            } else {
                strFilter = strFilter + " and o.tranSource = '" + tranSource + "'";
            }
        }

        if (!glVouNo.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glVouNo = '" + glVouNo + "'";
            } else {
                strFilter = strFilter + " and o.glVouNo = '" + glVouNo + "'";
            }
        }

        if (!splitId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.splitId = '" + splitId + "'";
            } else {
                strFilter = strFilter + " and o.splitId = '" + splitId + "'";
            }
        }

        if (!strFilter.isEmpty()) {
            strSql = strSql + " where " + strFilter;
        }

        List<Gl> listGL = findHSQL(strSql);
        return listGL;
    }

    @Override
    public int delete(String glCode, String option, String userCode, Integer macId) throws Exception {
        backup(glCode, option, userCode, macId);
        String strSql = "delete from Gl o where o.glCode ='" + glCode + "'";
        int cnt = execUpdateOrDelete(strSql);
        return cnt;
    }

    @Override
    public void backup(String glCode, String option, String userCode, Integer macId) {
        try {
            String logCode = getGlLogCode(macId, userCode);
            String sql = "insert into gl_log(gl_code, gl_date, created_date, modify_date, modify_by, \n"
                    + "description, source_ac_id, account_id, cur_code, dr_amt, cr_amt, \n"
                    + "reference, dept_code, voucher_no, user_code, trader_code, comp_code, \n"
                    + "tran_source, gl_vou_no, split_id, intg_upd_status, remark, naration, \n"
                    + "ref_no, mac_id, exchange_id,log_status,log_user_code,log_mac_id,log_gl_code)\n"
                    + "select gl_code, gl_date, created_date, modify_date, modify_by, \n"
                    + "description, source_ac_id, account_id, cur_code, dr_amt, cr_amt, \n"
                    + "reference, dept_code, voucher_no, user_code, trader_code, comp_code, \n"
                    + "tran_source, gl_vou_no, split_id, intg_upd_status, remark, naration, \n"
                    + "ref_no, mac_id, exchange_id,'" + option + "','" + userCode + "'," + macId + ",'" + logCode + "'\n"
                    + "from gl\n"
                    + "where gl_code = '" + glCode + "'";
            execSQL(sql);
        } catch (Exception ex) {
            log.error("backup : " + ex.getMessage());
        }
    }

    @Override
    public int deleteGV(String vouNo, String option, String userCode, Integer macId) {
        String hsql = "select o from Gl o where o.glVouNo = '" + vouNo + "'";
        String delHsql = "delete  from Gl o where o.glVouNo = '" + vouNo + "'";
        List<Gl> gls = findHSQL(hsql);
        if (!gls.isEmpty()) {
            gls.forEach(gl -> {
                backup(gl.getGlCode(), option, userCode, macId);
            });
        }
        return execUpdateOrDelete(delHsql);
    }

    private String getGlLogCode(Integer macId, String compCode) {
        String period = Util1.toDateStr(Util1.getTodayDate(), "MM");
        int seqNo = dao.getSequence(macId, "GL-LOG", period, "-");
        String glLogCode = String.format("%0" + 3 + "d", macId) + period + String.format("%0" + 9 + "d", seqNo);
        return glLogCode;
    }
}
