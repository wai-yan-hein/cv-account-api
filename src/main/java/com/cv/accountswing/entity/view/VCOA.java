/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.entity.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "v_coa")
public class VCOA {

    @Id
    @Column(name = "coa_code")
    private String coaCode;
    @Column(name = "coa_name_eng")
    private String coaName;
    @Column(name = "coa_code_usr")
    private String coaUsrCode;
    @Column(name = "coa_parent")
    private String coaParentCode;
    @Column(name = "coa_parent_name")
    private String coaParentName;
    @Column(name = "coa_parent_usr")
    private String coaParentUsrCode;
    @Column(name = "comp_code")
    private String compCode;

    public VCOA() {
    }

    public String getCoaCode() {
        return coaCode;
    }

    public void setCoaCode(String coaCode) {
        this.coaCode = coaCode;
    }

    public String getCoaName() {
        return coaName;
    }

    public void setCoaName(String coaName) {
        this.coaName = coaName;
    }

    public String getCoaUsrCode() {
        return coaUsrCode;
    }

    public void setCoaUsrCode(String coaUsrCode) {
        this.coaUsrCode = coaUsrCode;
    }

    public String getCoaParentCode() {
        return coaParentCode;
    }

    public void setCoaParentCode(String coaParentCode) {
        this.coaParentCode = coaParentCode;
    }

    public String getCoaParentName() {
        return coaParentName;
    }

    public void setCoaParentName(String coaParentName) {
        this.coaParentName = coaParentName;
    }

    public String getCoaParentUsrCode() {
        return coaParentUsrCode;
    }

    public void setCoaParentUsrCode(String coaParentUsrCode) {
        this.coaParentUsrCode = coaParentUsrCode;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

}
