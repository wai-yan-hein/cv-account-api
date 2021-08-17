/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.entity.Exchange;
import com.cv.accountswing.entity.ExchangeDetail;
import com.cv.accountswing.entity.ExchangeKey;
import com.cv.accountswing.entity.view.VExchange;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lenovo
 */
@Repository
public class ExchangeDaoImpl extends AbstractDao<Integer, Exchange> implements ExchangeDao {

    @Override
    public Exchange save(Exchange ex) throws Exception {
        persist(ex);
        return ex;
    }

    @Override
    public void save(ExchangeDetail ed) throws Exception {
        saveObject(ed);
    }

    @Override
    public List<Exchange> findExchange(String compCode) {
        String hsql = "select o from Exchange o where o.compCode = '" + compCode + "' order by o.exchangeId";
        return findHSQLList(hsql);
    }

    @Override
    public List<ExchangeDetail> getExchangeDetail(Integer exchangeId) {
        String hsql = "select o from ExchangeDetail o where o.key.exId = " + exchangeId + "";
        return findHSQLList(hsql);
    }

    @Override
    public List<VExchange> getLatestExchange(String compCode) {
        List<VExchange> listEx = new ArrayList<>();
        String sql = "select cd.*,c.*\n"
                + "from cur_exchange_detail cd, cur_exchange c\n"
                + "where c.ex_id in \n"
                + "(select max(ex_id) from cur_exchange)\n"
                + "and cd.ex_id = c.ex_id";
        try {
            ResultSet rs = getResultSet(sql);
            if (rs != null) {
                while (rs.next()) {
                    VExchange ex = new VExchange();
                    ExchangeKey key = new ExchangeKey();
                    ex.setKey(key);
                    ex.getKey().setExId(rs.getInt("ex_id"));
                    ex.setExchangeDate(rs.getDate("ex_date"));
                    ex.setRemark(rs.getString("remark"));
                    ex.setCompCode(rs.getString("comp_code"));
                    ex.getKey().setHomeCur(rs.getString("home_cur"));
                    ex.getKey().setExchangeCur(rs.getString("exchange_cur"));
                    ex.setExRate(rs.getDouble("ex_rate"));
                    //ex.setHomeCurName(rs.getString("home_cur_name"));
                    //ex.setHomeCurSymbol(rs.getString("home_cur_symbol"));
                    //ex.setExchangeCurName(rs.getString("ex_cur_name"));
                    //ex.setExchangeCurSymbol(rs.getString("ex_cur_symbol"));
                    listEx.add(ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ExchangeDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listEx;
    }

    @Override
    public Exchange findById(Integer id) {
        return getByKey(id);
    }

    @Override
    public int getLatestExId(String compCode) {
        int id = 0;
        try {
            String sql = "select distinct ex_id from v_exchange where comp_code = '" + compCode + "'";
            ResultSet rs = getResultSet(sql);
            if (rs != null) {
                while (rs.next()) {
                    id = rs.getInt("ex_id");
                }
            }
        } catch (Exception ex) {
        }
        return id;
    }
}
