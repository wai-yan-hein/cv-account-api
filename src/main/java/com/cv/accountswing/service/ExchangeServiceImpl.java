/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.service;

import com.cv.accountswing.common.DuplicateException;
import com.cv.accountswing.dao.ExchangeDao;
import com.cv.accountswing.entity.Exchange;
import com.cv.accountswing.entity.ExchangeDetail;
import com.cv.accountswing.entity.view.VExchange;
import com.cv.accountswing.util.Util1;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Lenovo
 */
@Service
@Transactional
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ExchangeDao dao;
    @Autowired
    private SeqTableService seqService;

    @Override
    public Exchange save(Exchange ex, List<ExchangeDetail> ed) throws Exception {
        if (Util1.isNull(ex.getExchangeId())) {
            ex.setExchangeId(getId(ex.getMacId(), "Exchange", "-", ex.getCompCode()));
            Exchange valid = dao.findById(ex.getExchangeId());
            if (valid != null) {
                throw new DuplicateException("Duplicate Exchange Id");
            }
        }
        ed.forEach((t) -> {
            t.getKey().setExId(ex.getExchangeId());
            try {
                dao.save(t);
            } catch (Exception ex1) {
            }
        });
        return dao.save(ex);
    }

    private int getId(Integer macId, String option, String period, String compCode) {
        int seqNo = seqService.getSequence(macId, option, period, compCode);
        return seqNo;
    }

    @Override
    public void save(ExchangeDetail ed) throws Exception {
        dao.save(ed);
    }

    @Override
    public List<Exchange> findExchange(String compCode) {
        return dao.findExchange(compCode);
    }

    @Override
    public List<ExchangeDetail> getExchangeDetail(Integer exchangeId) {
        return dao.getExchangeDetail(exchangeId);
    }

    @Override
    public List<VExchange> getLatestExchange(String compCode) {
        return dao.getLatestExchange(compCode);
    }
}
