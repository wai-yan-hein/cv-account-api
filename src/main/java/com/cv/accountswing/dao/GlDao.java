/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.dao;

import com.cv.accountswing.entity.Gl;
import java.util.List;

/**
 *
 * @author winswe
 */
public interface GlDao {

    public Gl save(Gl gl) throws Exception;

    public List<Gl> saveBatchGL(List<Gl> listGL) throws Exception;

    public Gl findById(String glCode);

    public List<Gl> search(String from, String to, String desp, String sourceAcId,
            String acId, String frmCurr, String toCurr, String reference, String dept,
            String vouNo, String cvId, String chequeNo, String compCode, String tranSource,
            String glVouNo, String splitId, String projectId);

    public int delete(String glCode, String option, String userCode, Integer macId) throws Exception;

    public void backup(String glCode, String option, String userCode, Integer macId);

    public int deleteGV(String vouNo, String option, String userCode, Integer macId);
}
