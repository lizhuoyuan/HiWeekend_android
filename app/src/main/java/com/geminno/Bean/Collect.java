package com.geminno.Bean;

public class Collect {
    private int ce_id;
    private int a_id;
    private String ce_time;
    private String a_itemname;

    public Collect(int ce_id, int a_id, String ce_time, String a_itemname) {
	super();
	this.ce_id = ce_id;
	this.a_id = a_id;
	this.ce_time = ce_time;
	this.a_itemname = a_itemname;
    }

    public int getCe_id() {
	return ce_id;
    }

    public void setCe_id(int ce_id) {
	this.ce_id = ce_id;
    }

    public String getCe_time() {
	return ce_time;
    }

    public void setCe_time(String ce_time) {
	this.ce_time = ce_time;
    }

    public int getA_id() {
	return a_id;
    }

    public void setA_id(int a_id) {
	this.a_id = a_id;
    }

    public String getA_itemname() {
	return a_itemname;
    }

    public void setA_itemname(String a_itemname) {
	this.a_itemname = a_itemname;
    }

}
