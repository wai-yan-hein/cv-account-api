/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.service;

import com.cv.accountswing.common.DuplicateException;
import com.cv.accountswing.dao.BackupDao;
import com.cv.accountswing.dao.ExchangeDao;
import com.cv.accountswing.dao.GlDao;
import com.cv.accountswing.entity.Gl;
import com.cv.accountswing.util.Util1;
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
public class GlServiceImpl implements GlService {

    @Autowired
    private GlDao dao;
    @Autowired
    private SeqTableService seqService;
    @Autowired
    private BackupDao backupDao;
    @Autowired
    private ExchangeDao exDao;

    @Override
    public Gl save(Gl gl) throws Exception {
        if (Util1.isNull(gl.getGlCode())) {
            Integer macId = gl.getMacId();
            String compCode = gl.getCompCode();
            String period = Util1.toDateStr(Util1.getTodayDate(), "MM");
            String glCode = getGLCode(macId, "GL", period, compCode);
            Gl valid = findById(glCode);
            if (valid == null) {
                gl.setGlCode(glCode);
                gl.setExchangeId(exDao.getLatestExId(compCode));
            } else {
                throw new DuplicateException("Duplicate Code.");
            }
        } else {
            dao.backup(gl.getGlCode(), "EDIT", gl.getModifyBy(), gl.getMacId());
        }
        return dao.save(gl);
    }

    @Override
    public List<Gl> saveBatchGL(List<Gl> listGL) throws Exception {
        dao.saveBatchGL(listGL);
        return listGL;
    }

    @Override
    public Gl findById(String glCode) {
        Gl gl = dao.findById(glCode);
        return gl;
    }

    @Override
    public List<Gl> search(String from, String to, String desp, String sourceAcId,
            String acId, String frmCurr, String toCurr, String reference, String dept,
            String vouNo, String cvId, String chequeNo, String compCode, String tranSource,
            String glVouNo, String splitId, String projectId) {
        List<Gl> listGL = dao.search(from, to, desp, sourceAcId, acId, frmCurr, toCurr,
                reference, dept, vouNo, cvId, chequeNo, compCode, tranSource, glVouNo,
                splitId, projectId);
        return listGL;
    }

    @Override
    public int delete(String glCode, String option, String userCode, Integer macId) throws Exception {
        int cnt = dao.delete(glCode, option, userCode, macId);
        return cnt;
    }

    private String getGLCode(Integer macId, String option, String period, String compCode) {
        int seqNo = seqService.getSequence(macId, option, period, compCode);
        String tmpCatCode = String.format("%0" + 3 + "d", macId) + period + String.format("%0" + 9 + "d", seqNo);
        return tmpCatCode;
    }

    @Override
    public int deleteGV(String vouNo, String option, String userCode, Integer macId) throws Exception {
        return dao.deleteGV(vouNo, option, userCode, macId);
    }
}
