/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.accountswing.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author WSwe
 */
@Entity
@Table(name = "company_info")
public class CompanyInfo implements java.io.Serializable {

    @Id
    @Column(name = "comp_code", unique = true, nullable = false)
    private String compCode;
    @Column(name = "name", length = 255)
    private String name;
    @Column(name = "phone", length = 255)
    private String phone;
    @Column(name = "email", length = 255)
    private String email;
    @Column(name = "short_code", length = 5)
    private String shortCode;
    @Column(name = "security_code", length = 255)
    private String securityCode;
    @Column(name = "parent", length = 15)
    private String parent;
    @Column(name = "address", length = 15)
    private String address;
    @Column(name = "active")
    private Boolean active;
    @ManyToOne
    @JoinColumn(name = "business_type")
    private BusinessType businessType;
    @Temporal(TemporalType.DATE)
    @Column(name = "finicial_period_from")
    private Date finicialPeriodFrom;
    @Temporal(TemporalType.DATE)
    @Column(name = "finicial_period_to")
    private Date finicialPeriodTo;
    @Column(name = "owner")
    private Integer owner;
    @Transient
    private String roleCode;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private AppUser updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private AppUser createdBy;
    @Column(name = "mac_id")
    private Integer macId;
    @Column(name = "user_code")
    private String userCode;
    @ManyToOne
    @JoinColumn(name = "cur_code")
    private Currency currency;

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public Date getFinicialPeriodFrom() {
        return finicialPeriodFrom;
    }

    public void setFinicialPeriodFrom(Date finicialPeriodFrom) {
        this.finicialPeriodFrom = finicialPeriodFrom;
    }

    public Date getFinicialPeriodTo() {
        return finicialPeriodTo;
    }

    public void setFinicialPeriodTo(Date finicialPeriodTo) {
        this.finicialPeriodTo = finicialPeriodTo;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return name;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public AppUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(AppUser updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getMacId() {
        return macId;
    }

    public void setMacId(Integer macId) {
        this.macId = macId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}
