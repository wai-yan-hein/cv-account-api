/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.entity.view.VGl;
import com.cv.accountswing.util.Util1;
import java.sql.ResultSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author WSwe
 */
@Repository
public class VGlDaoImpl extends AbstractDao<String, VGl> implements VGlDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(VGlDaoImpl.class);

    @Override
    public List<VGl> find(String glVouNo) {
        List<VGl> listVGL = search("-", "-", "-", "-", "-", "-", "-", "-", "-",
                "-", "-", "-", "-", "GV", glVouNo, "-", "-", "-", "-", "-", "-");
        return listVGL;
    }

    @Override
    public List<VGl> search(String from, String to, String desp, String sourceAcId,
            String acId, String frmCurr, String toCurr, String reference, String dept,
            String vouNo, String cvId, String chequeNo, String compCode, String tranSource,
            String glVouNo, String deptName, String traderName, String splitId,
            String projectId, String traderType, String crdAmt) {
        String strSql = "select o from VGl o ";
        String strFilter = "";

        if (!from.equals("-") && !to.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glDate between '" + Util1.toDateStrMYSQL(from, "dd/MM/yyyy")
                        + "' and '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            } else {
                strFilter = strFilter + " and o.glDate between '"
                        + Util1.toDateStrMYSQL(from, "dd/MM/yyyy") + "' and '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            }
        } else if (!from.endsWith("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glDate >= '" + Util1.toDateStrMYSQL(from, "dd/MM/yyyy") + "'";
            } else {
                strFilter = strFilter + " and o.glDate >= '" + Util1.toDateStrMYSQL(from, "dd/MM/yyyy") + "'";
            }
        } else if (!to.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glDate <= '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            } else {
                strFilter = strFilter + " and o.glDate <= '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            }
        }

        if (!desp.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.description like '" + desp + "%'";
            } else {
                strFilter = strFilter + " and o.description like '" + desp + "%'";
            }
        }

        if (!reference.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.reference like '" + reference + "%'";
            } else {
                strFilter = strFilter + " and o.reference like '" + reference + "%'";
            }
        }
        /*if (!acId.equals("-")) {
        if (strFilter.isEmpty()) {
        strFilter = "(o.accountId = '" + acId + "' or o.sourceAcId = '" + acId + "')";
        } else {
        strFilter = strFilter + " and (o.accountId = '" + acId + "' or o.sourceAcId = '" + acId + "')";
        }
        }*/
        if (!acId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "(o.accountId in (" + acId + ") or o.sourceAcId in (" + acId + ")";
            } else {
                strFilter = strFilter + " and (o.accountId in (" + acId + ") or o.sourceAcId in (" + acId + "))";
            }
        }
        if (!sourceAcId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "(o.accountId = '" + sourceAcId + "' or o.sourceAcId = '" + sourceAcId + "')";
            } else {
                strFilter = strFilter + " and (o.accountId = '" + sourceAcId + "' or o.sourceAcId = '" + sourceAcId + "')";
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

        /*if(!cvId.equals("-")){
            if(strFilter.isEmpty()){
                strFilter = "o.traderCode like '%" + cvId + "%'";
            }else{
                strFilter = strFilter + " and o.traderCode like '%" + cvId + "%'";
            }
        }*/
        if (!cvId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.traderCode = '" + cvId + "'";
            } else {
                strFilter = strFilter + " and o.traderCode = '" + cvId + "'";
            }
        }

        if (!compCode.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.compCode = " + compCode;
            } else {
                strFilter = strFilter + " and o.compCode = " + compCode;
            }
        }

        if (!glVouNo.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glVouNo = '" + glVouNo + "'";
            } else {
                strFilter = strFilter + " and o.glVouNo = '" + glVouNo + "'";
            }
        }

        if (!deptName.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.deptName like '%" + deptName + "%'";
            } else {
                strFilter = strFilter + " and o.deptName like '%" + deptName + "%'";
            }
        }

        if (!traderName.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.traderName like '%" + traderName + "%'";
            } else {
                strFilter = strFilter + " and o.traderName like '%" + traderName + "%'";
            }
        }

        if (!splitId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.splitId = " + splitId;
            } else {
                strFilter = strFilter + " and o.splitId = " + splitId;
            }
        }
        if (!traderType.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.traderCode like '" + traderType + "%'";
            } else {
                strFilter = strFilter + " and o.traderCode like '" + traderType + "%'";
            }
        }
        if (!crdAmt.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.crAmt = " + crdAmt + "";
            } else {
                strFilter = strFilter + " and o.crAmt = " + crdAmt + "";
            }
        }

        if (!strFilter.isEmpty()) {
            strSql = strSql + " where " + strFilter + " order by o.glDate";
        }
        LOGGER.info("Search VGL Query :" + strSql);
        List<VGl> listVGL = findHSQL(strSql);
        return listVGL;
    }

    @Override
    public List<VGl> searchGlDrCr(String from, String to, String sourceAcId,
            String frmCurr, String dept, String cvId, String compCode, String option) {
        String strSql = "select o from VGl o ";
        String strFilter = "";

        if (!from.equals("-") && !to.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "date(o.glDate) between '" + Util1.toDateStrMYSQL(from, "dd/MM/yyyy")
                        + "' and '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            } else {
                strFilter = strFilter + " and o.glDate between '"
                        + Util1.toDateStrMYSQL(from, "dd/MM/yyyy") + "' and '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            }
        } else if (!from.endsWith("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "date(o.glDate) >= '" + Util1.toDateStrMYSQL(from, "dd/MM/yyyy") + "'";
            } else {
                strFilter = strFilter + " and o.glDate >= '" + Util1.toDateStrMYSQL(from, "dd/MM/yyyy") + "'";
            }
        } else if (!to.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "date(o.glDate) <= '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            } else {
                strFilter = strFilter + " and o.glDate <= '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            }
        }

        if (!sourceAcId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "(o.sourceAcId = '" + sourceAcId + "' or o.accountId = '" + sourceAcId + "')";
            } else {
                strFilter = strFilter + " and (o.sourceAcId = '" + sourceAcId + "' or o.accountId = '" + sourceAcId + "')";
            }
        }

        if (!frmCurr.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.fromCurId = '" + frmCurr + "'";
            } else {
                strFilter = strFilter + " and o.fromCurId = '" + frmCurr + "'";
            }
        }

        if (!dept.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.deptId = '" + dept + "'";
            } else {
                strFilter = strFilter + " and o.deptId = '" + dept + "'";
            }
        }

        if (!cvId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.traderCode ='" + cvId + "' ";
            } else {
                strFilter = strFilter + " and o.traderCode ='" + cvId + "' ";
            }
        }

        if (!compCode.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.compCode = '" + compCode + "'";
            } else {
                strFilter = strFilter + " and o.compCode = '" + compCode + "'";
            }
        }

        /*if(option.equals("DR")){
            if(strFilter.isEmpty()){
                strFilter = "o.drAmt > 0 and (o.crAmt = 0 or o.crAmt is null)";
            }else{
                strFilter = strFilter + " and o.drAmt > 0 and (o.crAmt = 0 or o.crAmt is null)";
            }
        }else{
            if(strFilter.isEmpty()){
                strFilter = "o.crAmt > 0 and (o.drAmt = 0 or o.drAmt is null)";
            }else{
                strFilter = strFilter + " and o.crAmt > 0 and (o.drAmt = 0 or o.drAmt is null)";
            }
        }*/
        if (!strFilter.isEmpty()) {
            strSql = strSql + " where " + strFilter;
        }
        LOGGER.info("VGL Search Query :" + strSql);
        List<VGl> listVGL = findHSQL(strSql);
        return listVGL;
    }

    @Override
    public List<VGl> getCrDrVoucher(String vouNo, String compCode) {
        String strSql = "select o from VGl o where o.vouNo = '" + vouNo
                + "' and o.compCode = '" + compCode + "' order by o.glCode";
        List<VGl> listVGL = findHSQL(strSql);
        return listVGL;
    }

    @Override
    public VGl findById(String glCode) {
        return getByKey(glCode);
    }

    @Override
    public ResultSet searchM(String from, String to, String desp, String sourceAcId,
            String acId, String frmCurr, String toCurr, String reference, String dept,
            String vouNo, String cvId, String chequeNo, String compCode, String tranSource,
            String glVouNo, String deptName, String traderName, String splitId,
            String projectId, String debAmt, String crdAmt) throws Exception {
        String strSql = "select o from (    SELECT \n"
                + "        gl.gl_code AS gl_code,\n"
                + "        gl.gl_date AS gl_date,\n"
                + "        gl.created_date AS created_date,\n"
                + "        gl.modify_date AS modify_date,\n"
                + "        gl.modify_by AS modify_by,\n"
                + "        gl.description AS description,\n"
                + "        gl.source_ac_id AS source_ac_id,\n"
                + "        gl.account_id AS account_id,\n"
                + "        gl.to_cur_id AS to_cur_id,\n"
                + "        gl.from_cur_id AS from_cur_id,\n"
                + "        gl.ex_rate AS ex_rate,\n"
                + "        gl.dr_amt AS dr_amt,\n"
                + "        gl.cr_amt AS cr_amt,\n"
                + "        gl.reference AS reference,\n"
                + "        gl.dept_code AS dept_code,\n"
                + "        gl.voucher_no AS voucher_no,\n"
                + "        gl.user_code AS user_code,\n"
                + "        gl.trader_code AS trader_code,\n"
                + "        gl.cheque_no AS cheque_no,\n"
                + "        gl.comp_code AS comp_code,\n"
                + "        gl.gst AS gst,\n"
                + "        gl.tran_source AS tran_source,\n"
                + "        gl.bank_code AS bank_code,\n"
                + "        gl.gl_vou_no AS gl_vou_no,\n"
                + "        gl.split_id AS split_id,\n"
                + "        gl.remark AS remark,\n"
                + "        gl.from_desp AS from_desp,\n"
                + "        gl.to_desp AS to_desp,\n"
                + "        gl.naration AS naration,\n"
                + "        gl.project_id AS project_id,\n"
                + "        coa1.coa_name_eng AS source_acc_name,\n"
                + "        coa2.coa_name_eng AS acc_name,\n"
                + "        cur1.cur_name AS fcur_name,\n"
                + "        cur2.cur_name AS tcur_name,\n"
                + "        dept.dept_name AS dept_name,\n"
                + "        dept.usr_code AS usr_code,\n"
                + "        ba.bank_name AS bank_name,\n"
                + "        t.trader_name AS trader_name,\n"
                + "        t.discriminator AS discriminator,\n"
                + "        coa1.coa_code_usr AS source_acc_code,\n"
                + "        coa2.coa_code_usr AS acc_code,\n"
                + "        coa1.coa_parent AS source_acc_parent,\n"
                + "        coa2.coa_parent AS acc_parent\n"
                + "    FROM\n"
                + "        (((((((gl\n"
                + "        LEFT JOIN chart_of_account coa1 ON (gl.source_ac_id = coa1.coa_code))\n"
                + "        LEFT JOIN chart_of_account coa2 ON (gl.account_id = coa2.coa_code))\n"
                + "        JOIN currency cur1 ON (gl.from_cur_id = cur1.cur_code))\n"
                + "        LEFT JOIN trader t ON (gl.trader_code = t.code))\n"
                + "        LEFT JOIN currency cur2 ON (gl.to_cur_id = cur2.cur_code))\n"
                + "        LEFT JOIN department dept ON (gl.dept_code = dept.dept_code))\n"
                + "        LEFT JOIN bank ba ON (gl.bank_code = ba.bank_code))) o ";
        String strFilter = "";

        if (!from.equals("-") && !to.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glDate between '" + Util1.toDateStrMYSQL(from, "dd/MM/yyyy")
                        + "' and '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            } else {
                strFilter = strFilter + " and o.glDate between '"
                        + Util1.toDateStrMYSQL(from, "dd/MM/yyyy") + "' and '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            }
        } else if (!from.endsWith("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glDate >= '" + Util1.toDateStrMYSQL(from, "dd/MM/yyyy") + "'";
            } else {
                strFilter = strFilter + " and o.glDate >= '" + Util1.toDateStrMYSQL(from, "dd/MM/yyyy") + "'";
            }
        } else if (!to.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glDate <= '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            } else {
                strFilter = strFilter + " and o.glDate <= '" + Util1.toDateStrMYSQL(to, "dd/MM/yyyy") + "'";
            }
        }

        if (!desp.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.description like '" + desp + "%'";
            } else {
                strFilter = strFilter + " and o.description like '" + desp + "%'";
            }
        }

        if (!sourceAcId.equals("-") && !acId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "(o.sourceAcId = '" + sourceAcId
                        + "' or o.sourceAccParent = '" + sourceAcId + "') and (o.accountId = '" + acId
                        + "' or o.accParent = '" + acId + "')";
            } else {
                strFilter = strFilter + " and (o.sourceAcId = '" + sourceAcId
                        + "' or o.sourceAccParent = '" + sourceAcId + "') and (o.accountId = '" + acId
                        + "' or o.accParent = '" + acId + "')";
            }
        } else if (!sourceAcId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "(o.sourceAcId = '" + sourceAcId + "' or o.accountId = '" + sourceAcId
                        + "' or o.sourceAccParent = '" + sourceAcId + "' or o.accParent = '" + sourceAcId + "')";
            } else {
                strFilter = strFilter + " and (o.sourceAcId = '" + sourceAcId + "' or o.accountId = '" + sourceAcId
                        + "' or o.sourceAccParent = '" + sourceAcId + "' or o.accParent = '" + sourceAcId + "')";
            }
        }

        if (!projectId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.projectId = " + projectId;
            } else {
                strFilter = strFilter + " and o.projectId = " + projectId;
            }
        }

        if (!frmCurr.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.fromCurId = '" + frmCurr + "'";
            } else {
                strFilter = strFilter + " and o.fromCurId = '" + frmCurr + "'";
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
                strFilter = "o.reference like '" + reference + "%'";
            } else {
                strFilter = strFilter + " and o.reference like '" + reference + "%'";
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

        /*if(!cvId.equals("-")){
            if(strFilter.isEmpty()){
                strFilter = "o.traderCode like '%" + cvId + "%'";
            }else{
                strFilter = strFilter + " and o.traderCode like '%" + cvId + "%'";
            }
        }*/
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
        } else {
            if (strFilter.isEmpty()) {
                strFilter = "(o.tranSource <> 'OPENING' or o.tranSource is null)";
            } else {
                strFilter = strFilter + " and (o.tranSource <> 'OPENING' or o.tranSource is null)";
            }
        }

        if (!glVouNo.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.glVouNo = '" + glVouNo + "'";
            } else {
                strFilter = strFilter + " and o.glVouNo = '" + glVouNo + "'";
            }
        }

        if (!deptName.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.deptName like '%" + deptName + "%'";
            } else {
                strFilter = strFilter + " and o.deptName like '%" + deptName + "%'";
            }
        }

        if (!traderName.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.traderName like '%" + traderName + "%'";
            } else {
                strFilter = strFilter + " and o.traderName like '%" + traderName + "%'";
            }
        }

        if (!splitId.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.splitId = " + splitId;
            } else {
                strFilter = strFilter + " and o.splitId = " + splitId;
            }
        }
        if (!debAmt.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.drAmt = '" + debAmt + "'";
            } else {
                strFilter = strFilter + " and o.drAmt = '" + debAmt + "'";
            }
        }
        if (!crdAmt.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.crAmt = '" + crdAmt + "'";
            } else {
                strFilter = strFilter + " and o.crAmt = '" + crdAmt + "'";
            }
        }

        if (!strFilter.isEmpty()) {
            strSql = strSql + " where " + strFilter + " order by o.glCode";
        }

        ResultSet rs = getResultSet(strSql);
        return rs;
    }
}
