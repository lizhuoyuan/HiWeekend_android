package com.geminno.Bean;

public class Merchant {
    private int mer_id;
    private String mer_name;
    private String mer_address;
    private String mer_tel;
    private String mer_password;
    private String mer_introduce;
    private int type;

    public int getType() {
	return type;
    }

    public void setType(int type) {
	this.type = type;
    }

    public Merchant(String mer_name, String mer_tel, String mer_password,
	    int type) {
	super();
	this.mer_name = mer_name;
	this.mer_tel = mer_tel;
	this.mer_password = mer_password;
	this.type = type;
    }

    public Merchant(int mer_id, String mer_name, String mer_address,
	    String mer_tel, String mer_introduce, int type) {
	this.mer_id = mer_id;
	this.mer_name = mer_name;
	this.mer_address = mer_address;
	this.mer_tel = mer_tel;
	this.mer_introduce = mer_introduce;
	this.type = type;
    }

    public Merchant(int mer_id, String mer_name, String mer_tel,
	    String mer_password) {
	this.mer_id = mer_id;
	this.mer_name = mer_name;
	this.mer_tel = mer_tel;
	this.mer_password = mer_password;
    }

    public Merchant() {

    }

    public int getMer_id() {
	return mer_id;
    }

    public void setMer_id(int mer_id) {
	this.mer_id = mer_id;
    }

    public String getMer_name() {
	return mer_name;
    }

    public void setMer_name(String mer_name) {
	this.mer_name = mer_name;
    }

    public String getMer_address() {
	return mer_address;
    }

    public void setMer_address(String mer_address) {
	this.mer_address = mer_address;
    }

    public String getMer_tel() {
	return mer_tel;
    }

    public void setMer_tel(String mer_tel) {
	this.mer_tel = mer_tel;
    }

    public String getMer_password() {
	return mer_password;
    }

    public void setMer_password(String mer_password) {
	this.mer_password = mer_password;
    }

    public String getMer_introduce() {
	return mer_introduce;
    }

    public void setMer_introduce(String mer_introduce) {
	this.mer_introduce = mer_introduce;
    }

}
