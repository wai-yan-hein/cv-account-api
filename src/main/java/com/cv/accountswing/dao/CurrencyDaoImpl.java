/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.entity.Currency;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author WSwe
 */
@Repository
public class CurrencyDaoImpl extends AbstractDao<String, Currency> implements CurrencyDao {

    @Override
    public Currency save(Currency cur) {
        persist(cur);
        return cur;
    }

    @Override
    public Currency findById(String id) {
        return getByKey(id);
    }

    @Override
    public List<Currency> search(String code, String name, String compCode) {
        String strSql = "select o from Currency o ";
        String strFilter = "";

        if (!code.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.code like '" + code + "%'";
            } else {
                strFilter = strFilter + " and o.code like '" + code + "%'";
            }
        }

        if (!name.equals("-")) {
            if (strFilter.isEmpty()) {
                strFilter = "o.currencyName like '%" + name + "%'";
            } else {
                strFilter = strFilter + " and o.currencyName like '%" + name + "%'";
            }
        }

        if (!strFilter.isEmpty()) {
            strSql = strSql + " where " + strFilter;
        }

        List<Currency> listCurrency = findHSQL(strSql);
        return listCurrency;
    }

    @Override
    public int delete(String code, String compCode) {
        String strSql = "delete from Currency o where o.code = '" + code
                + "' and o.compCode = " + compCode;
        int cnt = execUpdateOrDelete(strSql);
        return cnt;
    }
}
