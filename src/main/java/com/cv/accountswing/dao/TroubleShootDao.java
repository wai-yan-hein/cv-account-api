/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface TroubleShootDao {

    public List<String> shootTri() throws Exception;

    public List<String> shootAPAR() throws Exception;

    public ResultSet executeQuery(String query);
}
