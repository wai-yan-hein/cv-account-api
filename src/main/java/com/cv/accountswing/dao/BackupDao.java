/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.entity.Gl;
import com.cv.accountswing.entity.GlLog;

/**
 *
 * @author Lenovo
 */
public interface BackupDao {

    public GlLog backupGL(Gl gl, String status, String userCode, Integer macId);
}
