package com.geminno.Bean;

import java.io.Serializable;

public class User implements Serializable {
    private int u_id;
    private String u_name;
    private String u_pwd;
    private String u_tel;
    private String u_pic;
    private String r_name;
    private String u_sex;
    private int u_credit;
    private int u_paynum;
    private double u_yue;

    public User(int u_id, String u_name, String u_pwd, String u_tel) {
	super();
	this.u_id = u_id;
	this.u_name = u_name;
	this.u_pwd = u_pwd;
	this.u_tel = u_tel;
    }

    public User() {

    }

    public int getU_paynum() {
	return u_paynum;
    }

    public void setU_paynum(int u_paynum) {
	this.u_paynum = u_paynum;
    }

    public double getU_yue() {
	return u_yue;
    }

    public void setU_yue(double u_yue) {
	this.u_yue = u_yue;
    }

    public int getU_id() {
	return u_id;
    }

    public void setU_id(int u_id) {
	this.u_id = u_id;
    }

    public String getU_name() {
	return u_name;
    }

    public void setU_name(String u_name) {
	this.u_name = u_name;
    }

    public String getU_pwd() {
	return u_pwd;
    }

    public void setU_pwd(String u_pwd) {
	this.u_pwd = u_pwd;
    }

    public String getU_pic() {
	return u_pic;
    }

    public void setU_pic(String u_pic) {
	this.u_pic = u_pic;
    }

    public String getU_tel() {
	return u_tel;
    }

    public void setU_tel(String u_tel) {
	this.u_tel = u_tel;
    }

    public String getR_name() {
	return r_name;
    }

    public void setR_name(String r_name) {
	this.r_name = r_name;
    }

    public String getU_sex() {
	return u_sex;
    }

    public void setU_sex(String u_sex) {
	this.u_sex = u_sex;
    }

    public int getU_credit() {
	return u_credit;
    }

    public void setU_credit(int u_credit) {
	this.u_credit = u_credit;
    }

}