/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "cur_exchange_detail")
public class ExchangeDetail implements Serializable {

    @EmbeddedId
    private ExchangeKey key;
    @Column(name = "ex_rate")
    private Float exRate;

    public ExchangeDetail() {
    }

    public ExchangeDetail(ExchangeKey key, Float exRate) {
        this.key = key;
        this.exRate = exRate;
    }

    public ExchangeKey getKey() {
        return key;
    }

    public void setKey(ExchangeKey key) {
        this.key = key;
    }

    public Float getExRate() {
        return exRate;
    }

    public void setExRate(Float exRate) {
        this.exRate = exRate;
    }

}
