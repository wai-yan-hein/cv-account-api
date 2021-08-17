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
public class VAparKey implements Serializable {

    private String traderCode;
    private Integer macId;
    private String compCode;
    private String curCode;
    private String coaCode;
    private String deptCode;

    @Column(name = "trader_code")
    public String getTraderCode() {
        return traderCode;
    }

    public void setTraderCode(String traderCode) {
        this.traderCode = traderCode;
    }

    @Column(name = "cur_code")
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

    @Column(name = "comp_code")
    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    @Column(name = "coa_code")
    public String getCoaCode() {
        return coaCode;
    }

    public void setCoaCode(String coaCode) {
        this.coaCode = coaCode;
    }

    @Column(name = "dept_code")
    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.traderCode);
        hash = 29 * hash + Objects.hashCode(this.macId);
        hash = 29 * hash + Objects.hashCode(this.compCode);
        hash = 29 * hash + Objects.hashCode(this.curCode);
        hash = 29 * hash + Objects.hashCode(this.coaCode);
        hash = 29 * hash + Objects.hashCode(this.deptCode);
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
        final VAparKey other = (VAparKey) obj;
        if (!Objects.equals(this.traderCode, other.traderCode)) {
            return false;
        }
        if (!Objects.equals(this.compCode, other.compCode)) {
            return false;
        }
        if (!Objects.equals(this.curCode, other.curCode)) {
            return false;
        }
        if (!Objects.equals(this.coaCode, other.coaCode)) {
            return false;
        }
        if (!Objects.equals(this.deptCode, other.deptCode)) {
            return false;
        }
        if (!Objects.equals(this.macId, other.macId)) {
            return false;
        }
        return true;
    }

}
