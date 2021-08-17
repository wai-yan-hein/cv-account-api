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
public class TmpOpeningClosingKey implements Serializable {

    private String coaCode;
    private String curCode;
    private Integer macId;

    public TmpOpeningClosingKey() {
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

    @Column(name = "mac_id")
    public Integer getMacId() {
        return macId;
    }

    public void setMacId(Integer macId) {
        this.macId = macId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.coaCode);
        hash = 89 * hash + Objects.hashCode(this.curCode);
        hash = 89 * hash + Objects.hashCode(this.macId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TmpOpeningClosingKey other = (TmpOpeningClosingKey) obj;
        if (!Objects.equals(this.coaCode, other.coaCode)) {
            return false;
        }
        if (!Objects.equals(this.curCode, other.curCode)) {
            return false;
        }
        return Objects.equals(this.macId, other.macId);
    }
    
}
