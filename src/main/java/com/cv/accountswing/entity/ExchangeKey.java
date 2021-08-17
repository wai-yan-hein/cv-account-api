/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Lenovo
 */
@Embeddable
public class ExchangeKey implements Serializable {

    @Column(name = "home_cur")
    private String homeCur;
    @Column(name = "exchange_cur")
    private String exchangeCur;
    @Column(name = "ex_id")
    private Integer exId;

    public ExchangeKey() {
    }

    public ExchangeKey(String exchangeCur) {
        this.exchangeCur = exchangeCur;
    }

    public String getHomeCur() {
        return homeCur;
    }

    public void setHomeCur(String homeCur) {
        this.homeCur = homeCur;
    }

    public String getExchangeCur() {
        return exchangeCur;
    }

    public void setExchangeCur(String exchangeCur) {
        this.exchangeCur = exchangeCur;
    }

    public Integer getExId() {
        return exId;
    }

    public void setExId(Integer exId) {
        this.exId = exId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.homeCur);
        hash = 47 * hash + Objects.hashCode(this.exchangeCur);
        hash = 47 * hash + Objects.hashCode(this.exId);
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
        final ExchangeKey other = (ExchangeKey) obj;
        if (!Objects.equals(this.homeCur, other.homeCur)) {
            return false;
        }
        if (!Objects.equals(this.exchangeCur, other.exchangeCur)) {
            return false;
        }
        return Objects.equals(this.exId, other.exId);
    }

}
