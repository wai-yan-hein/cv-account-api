/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.entity;

import com.cv.inv.entity.MachineInfo;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author WSwe
 */
@Entity
@Table(name = "gl_log")
public class GlLog implements java.io.Serializable {

    @Id
    @Column(name = "log_gl_code")
    private String logGlCode;
    @ManyToOne
    @JoinColumn(name = "log_user_code")
    private AppUser logUser;
    @ManyToOne
    @JoinColumn(name = "log_mac_id")
    private MachineInfo logMac;
    @Column(name = "log_status")
    private String logStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "log_date")
    private Date logDate;
    //gl
    @Column(name = "gl_code", unique = true, nullable = false)
    private String glCode;
    @Temporal(TemporalType.DATE)
    @Column(name = "gl_date")
    private Date glDate;
    @Column(name = "description", length = 255)
    private String description;
    @Column(name = "source_ac_id")
    private String sourceAcId;
    @Column(name = "account_id")
    private String accountId;
    @Column(name = "cur_code")
    private String curCode;
    @Column(name = "dr_amt")
    private Float drAmt;
    @Column(name = "cr_amt")
    private Float crAmt;
    @Column(name = "reference", length = 50)
    private String reference;
    @Column(name = "dept_code")
    private String deptId;
    @Column(name = "voucher_no", length = 15)
    private String vouNo;
    @Column(name = "trader_code")
    private String traderCode;
    @Column(name = "comp_code")
    private String compCode;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;
    @Column(name = "modify_by", length = 15)
    private String modifyBy;
    @Column(name = "user_code", length = 15)
    private String createdBy;
    @Column(name = "tran_source", length = 25)
    private String tranSource;
    @Column(name = "gl_vou_no", length = 25)
    private String glVouNo; //For general voucher system id
    @Column(name = "split_id")
    private Integer splitId;
    @Column(name = "intg_upd_status", length = 5)
    private String intgUpdStatus; //For integration update status
    @Column(name = "remark", length = 500)
    private String remark;
    @Column(name = "naration", length = 500)
    private String naration;
    @Column(name = "mac_id")
    private Integer macId;
    @Column(name = "ref_no")
    private String refNo;
    @Column(name = "exchange_id")
    private Integer exchangeId;

    public String getLogGlCode() {
        return logGlCode;
    }

    public void setLogGlCode(String logGlCode) {
        this.logGlCode = logGlCode;
    }

    public AppUser getLogUser() {
        return logUser;
    }

    public void setLogUser(AppUser logUser) {
        this.logUser = logUser;
    }

    public MachineInfo getLogMac() {
        return logMac;
    }

    public void setLogMac(MachineInfo logMac) {
        this.logMac = logMac;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public Date getGlDate() {
        return glDate;
    }

    public void setGlDate(Date glDate) {
        this.glDate = glDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceAcId() {
        return sourceAcId;
    }

    public void setSourceAcId(String sourceAcId) {
        this.sourceAcId = sourceAcId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public Float getDrAmt() {
        return drAmt;
    }

    public void setDrAmt(Float drAmt) {
        this.drAmt = drAmt;
    }

    public Float getCrAmt() {
        return crAmt;
    }

    public void setCrAmt(Float crAmt) {
        this.crAmt = crAmt;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getVouNo() {
        return vouNo;
    }

    public void setVouNo(String vouNo) {
        this.vouNo = vouNo;
    }

    public String getTraderCode() {
        return traderCode;
    }

    public void setTraderCode(String traderCode) {
        this.traderCode = traderCode;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTranSource() {
        return tranSource;
    }

    public void setTranSource(String tranSource) {
        this.tranSource = tranSource;
    }

    public String getGlVouNo() {
        return glVouNo;
    }

    public void setGlVouNo(String glVouNo) {
        this.glVouNo = glVouNo;
    }

    public Integer getSplitId() {
        return splitId;
    }

    public void setSplitId(Integer splitId) {
        this.splitId = splitId;
    }

    public String getIntgUpdStatus() {
        return intgUpdStatus;
    }

    public void setIntgUpdStatus(String intgUpdStatus) {
        this.intgUpdStatus = intgUpdStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNaration() {
        return naration;
    }

    public void setNaration(String naration) {
        this.naration = naration;
    }

    public Integer getMacId() {
        return macId;
    }

    public void setMacId(Integer macId) {
        this.macId = macId;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }


}
