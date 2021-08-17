/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.entity.view;

import com.cv.accountswing.entity.ExchangeKey;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "v_exchange")
public class VExchange implements Serializable {

    @EmbeddedId
    private ExchangeKey key;
    @Temporal(TemporalType.DATE)
    @Column(name = "ex_date")
    private Date exchangeDate;
    @Column(name = "remark")
    private String remark;
    @Column(name = "comp_code")
    private String compCode;
    @Column(name = "home_cur_name")
    private String homeCurName;
    @Column(name = "home_cur_symbol")
    private String homeCurSymbol;
    @Column(name = "ex_cur_name")
    private String exchangeCurName;
    @Column(name = "ex_cur_symbol")
    private String exchangeCurSymbol;
    @Column(name = "ex_rate")
    private Double exRate;

    public VExchange() {
    }

    public ExchangeKey getKey() {
        return key;
    }

    public void setKey(ExchangeKey key) {
        this.key = key;
    }

    public Date getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getHomeCurName() {
        return homeCurName;
    }

    public void setHomeCurName(String homeCurName) {
        this.homeCurName = homeCurName;
    }

    public String getHomeCurSymbol() {
        return homeCurSymbol;
    }

    public void setHomeCurSymbol(String homeCurSymbol) {
        this.homeCurSymbol = homeCurSymbol;
    }

    public String getExchangeCurName() {
        return exchangeCurName;
    }

    public void setExchangeCurName(String exchangeCurName) {
        this.exchangeCurName = exchangeCurName;
    }

    public String getExchangeCurSymbol() {
        return exchangeCurSymbol;
    }

    public void setExchangeCurSymbol(String exchangeCurSymbol) {
        this.exchangeCurSymbol = exchangeCurSymbol;
    }

    public Double getExRate() {
        return exRate;
    }

    public void setExRate(Double exRate) {
        this.exRate = exRate;
    }

}
