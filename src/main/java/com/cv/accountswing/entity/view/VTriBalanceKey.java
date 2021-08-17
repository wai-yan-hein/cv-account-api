/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.entity.view;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author winswe
 */
@Embeddable
public class VTriBalanceKey implements Serializable {

    private String coaCode;
    private String curCode;
    private Integer compCode;

    public VTriBalanceKey() {
    }

    @Column(name = "coa_code", nullable = false, length = 25)
    public String getCoaCode() {
        return coaCode;
    }

    public void setCoaCode(String coaCode) {
        this.coaCode = coaCode;
    }

    @Column(name = "curr_id", nullable = false, length = 15)
    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    @Column(name = "comp_code")
    public Integer getCompCode() {
        return compCode;
    }

    public void setCompCode(Integer compCode) {
        this.compCode = compCode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.coaCode);
        hash = 53 * hash + Objects.hashCode(this.curCode);
        hash = 53 * hash + Objects.hashCode(this.compCode);
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
        final VTriBalanceKey other = (VTriBalanceKey) obj;
        if (!Objects.equals(this.coaCode, other.coaCode)) {
            return false;
        }
        if (!Objects.equals(this.curCode, other.curCode)) {
            return false;
        }
        return Objects.equals(this.compCode, other.compCode);
    }
}
