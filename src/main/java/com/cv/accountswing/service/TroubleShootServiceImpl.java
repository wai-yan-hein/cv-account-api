/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.service;

import com.cv.accountswing.dao.TroubleShootDao;
import java.sql.ResultSet;
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
public class TroubleShootServiceImpl implements TroubleShootService {

    @Autowired
    private TroubleShootDao dao;

    @Override
    public ResultSet executeQuery(String query) {
        return dao.executeQuery(query);
    }

    @Override
    public List<String> shootTri() throws Exception {
        return dao.shootTri();
    }

    @Override
    public List<String> shootAPAR() throws Exception {
        return dao.shootAPAR();
    }

}
