/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.service;

import com.cv.accountswing.dao.DepartmentDao;
import com.cv.accountswing.entity.Department;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author WSwe
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentDao dao;
    @Autowired
    SeqTableService seqService;
    @Autowired
    SystemPropertyService spService;

    @Override
    public Department save(Department dept) {
        if (dept.getDeptCode() == null || dept.getDeptCode().isEmpty()) {
            Integer macId = dept.getMacId();
            String compCode = dept.getCompCode();
            String depCode = getDepCode(macId, "DEP", "-", compCode);
            dept.setDeptCode(depCode);
        }
        dept = dao.save(dept);
        return dept;
    }

    @Override
    public Department findById(String id) {
        Department dept = dao.findById(id);
        return dept;
    }

    @Override
    public List<Department> search(String code, String name, String compCode, String userCode, String partentId) {
        List<Department> listDept = dao.search(code, name, compCode, userCode, partentId);
        return listDept;
    }

    @Override
    public int delete(String code) {
        int cnt = dao.delete(code);
        return cnt;
    }

    @Override
    public List<Department> findAll() {
        return dao.findAll();
    }

    private String getDepCode(Integer macId, String option, String period, String compCode) {
        int seqNo = seqService.getSequence(macId, option, period, compCode);
        String tmpCatCode = String.format("%0" + 3 + "d", macId) + period + String.format("%0" + 4 + "d", seqNo);
        return tmpCatCode;
    }

}
