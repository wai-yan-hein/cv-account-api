/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.entity.AppUser;
import com.cv.accountswing.entity.Gl;
import com.cv.accountswing.entity.GlLog;
import com.cv.accountswing.util.Util1;
import com.cv.inv.entity.MachineInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lenovo
 */
@Repository
public class BackupDaoImpl extends AbstractDao<String, GlLog> implements BackupDao {

    private final Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
    @Autowired
    private SeqTableDao dao;

    @Override
    public GlLog backupGL(Gl gl, String status, String userCode, Integer macId) {
        GlLog glLog = gson.fromJson(gson.toJson(gl), GlLog.class);
        glLog.setLogGlCode(getGlLogCode(macId, gl.getCompCode()));
        glLog.setLogMac(new MachineInfo(macId));
        glLog.setLogStatus(status);
        glLog.setLogUser(new AppUser(userCode));
        persist(glLog);
        return glLog;
    }

    private String getGlLogCode(Integer macId, String compCode) {
        String period = Util1.toDateStr(Util1.getTodayDate(), "MM");
        int seqNo = dao.getSequence(macId, "GL-LOG", period, "-");
        String glLogCode = String.format("%0" + 3 + "d", macId) + period + String.format("%0" + 9 + "d", seqNo);
        return glLogCode;
    }

}
