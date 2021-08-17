/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.entity.temp.TmpOpeningClosingKey;
import com.cv.accountswing.entity.view.VTriBalance;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author winswe
 */
@Repository
public class VTriBalanceDaoImpl extends AbstractDao<TmpOpeningClosingKey, VTriBalance> implements VTriBalanceDao {

    @Override
    public List<VTriBalance> getTriBalance(String macId, String coaCode, String currency) {
        String hsql = "select o from VTriBalance o";
        String strFilter = "";
        if (!coaCode.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.key.coaCode in (" + coaCode + ")";
            } else {
                strFilter = strFilter + " and o.key.coaCode in (" + coaCode + ")";
            }
        }
        if (!currency.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.key.curCode ='" + currency + "'";
            } else {
                strFilter = strFilter + " and o.key.curCode ='" + currency + "'";
            }
        }
        if (!strFilter.isEmpty()) {
            hsql = hsql + " where " + strFilter + " and o.macId = " + macId + " order by o.usrCoaCode";
        } else {
            hsql = hsql + " where  o.macId = " + macId + " order by o.usrCoaCode";
        }
        return findHSQL(hsql);
    }
}
