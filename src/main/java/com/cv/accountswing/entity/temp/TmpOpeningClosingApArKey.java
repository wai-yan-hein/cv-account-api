/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.entity.temp;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author winswe
 */
@Embeddable
public class TmpOpeningClosingApArKey implements Serializable{
    private String coaCode;
    private String curCode;
    private String userCode;
    private Integer cvId;
    private String deptId;
    
    public TmpOpeningClosingApArKey() {}
    
    @Column(name="coa_code", nullable=false, length=25)
    public String getCoaId() {
        return coaCode;
    }

    public void setCoaId(String coaCode) {
        this.coaCode = coaCode;
    }

    @Column(name="curr_id", nullable=false, length=15)
    public String getCurrId() {
        return curCode;
    }

    public void setCurrId(String curCode) {
        this.curCode = curCode;
    }

    @Column(name="user_code", nullable=false, length=15)
    public String getUserId() {
        return userCode;
    }

    public void setUserId(String userCode) {
        this.userCode = userCode;
    }

    @Column(name="trader_code")
    public Integer getCvId() {
        return cvId;
    }

    public void setCvId(Integer cvId) {
        this.cvId = cvId;
    }

    @Column(name="dept_code")
    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.coaCode);
        hash = 23 * hash + Objects.hashCode(this.curCode);
        hash = 23 * hash + Objects.hashCode(this.userCode);
        hash = 23 * hash + Objects.hashCode(this.cvId);
        hash = 23 * hash + Objects.hashCode(this.deptId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TmpOpeningClosingApArKey other = (TmpOpeningClosingApArKey) obj;
        if (!Objects.equals(this.coaCode, other.coaCode)) {
            return false;
        }
        if (!Objects.equals(this.curCode, other.curCode)) {
            return false;
        }
        if (!Objects.equals(this.userCode, other.userCode)) {
            return false;
        }
        if (!Objects.equals(this.cvId, other.cvId)) {
            return false;
        }
        if (!Objects.equals(this.deptId, other.deptId)) {
            return false;
        }
        return true;
    }
}
