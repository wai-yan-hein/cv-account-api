/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.service;

import com.cv.accountswing.entity.temp.TmpOpeningClosing;
import java.util.List;

/**
 *
 * @author winswe
 */
public interface COAOpeningDService {

    public void insertFilter(String coaCode, int level, String opDate,
            String curr, String userCode) throws Exception;

    public List<TmpOpeningClosing> getOpBalance(String coaCode, int level, String opDate,
            String curr, String userCode) throws Exception;

    public void deleteTmp(String coaCode, String userCode) throws Exception;

    public void insertFilterGL(String coaCode, String opDate, int level,
            String curr, String userCode) throws Exception;

    public List<TmpOpeningClosing> getOpBalanceGL(String coaCode, String opDate,
            String clDae, int level, String curr, String compCode, String dept,
            String macId, String traderCode) throws Exception;

    public void genTriBalance(String compCode, String opDate,
            String tranDate, String coaCode, String currency, String dept,
            String cvId, String userCode, String macId) throws Exception;

    public void genArAp(String compCode, String fromDate, String tranDate,
            String coaCode, String currency, String dept, String traderCode, String userCode) throws Exception;

}
