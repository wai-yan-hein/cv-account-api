/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.service;

import com.cv.accountswing.dao.COAOpeningDaoD;
import com.cv.accountswing.entity.temp.TmpOpeningClosing;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author winswe
 */
@Service
@Transactional
public class COAOpeningDServiceImpl implements COAOpeningDService {

    @Autowired
    private COAOpeningDaoD dao;

    @Override
    public void insertFilter(String coaCode, int level, String opDate,
            String curr, String userCode) throws Exception {
        dao.insertFilter(coaCode, level, opDate, curr, userCode);
    }

    @Override
    public void deleteTmp(String coaCode, String userCode) throws Exception {

    }

    @Override
    public void insertFilterGL(String coaCode, String opDate, int level,
            String curr, String userCode) throws Exception {
        dao.insertFilterGL(coaCode, opDate, level, curr, userCode);
    }

    @Override
    public void genArAp(String compCode, String fromDate, String tranDate,
            String coaCode, String currency, String dept, String traderCode, String userCode) throws Exception {
        dao.genArAp(compCode, fromDate, tranDate, coaCode, currency, dept, traderCode, userCode);
    }

    @Override
    public List<TmpOpeningClosing> getOpBalanceGL(String coaCode, String opDate,
            String clDae, int level, String curr, String compCode, String dept, 
            String macId, String traderCode) throws Exception {
        dao.genOpBalanceGL(coaCode, opDate, clDae, level, curr, compCode, dept, macId, traderCode);
        List<TmpOpeningClosing> listTOC = dao.getOpBalanceGL(coaCode, macId);
        return listTOC;
    }

    @Override
    public void genTriBalance(String compCode, String opDate, String tranDate,
            String coaCode, String currency, String dept, String cvId,
            String userCode, String macId) throws Exception {
        dao.genTriBalance(compCode, opDate, tranDate, coaCode, currency, dept, cvId, macId);
    }

    @Override
    public List<TmpOpeningClosing> getOpBalance(String coaCode, int level, String opDate, String curr, String userCode) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
