/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.service;

import com.cv.accountswing.entity.Exchange;
import com.cv.accountswing.entity.ExchangeDetail;
import com.cv.accountswing.entity.view.VExchange;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface ExchangeService {

    public Exchange save(Exchange ex, List<ExchangeDetail> ed) throws Exception;

    public void save(ExchangeDetail ed) throws Exception;

    public List<Exchange> findExchange(String compCode);

    public List<ExchangeDetail> getExchangeDetail(Integer exchangeId);

    public List<VExchange> getLatestExchange(String compCode);

}
