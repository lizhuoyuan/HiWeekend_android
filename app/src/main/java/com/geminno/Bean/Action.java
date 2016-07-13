package com.geminno.Bean;

import java.io.Serializable;

public class Action implements Serializable {
    private Integer a_id;
    private String a_itemname;
    private double a_price;
    private String a_introduce;
    private String a_consult;
    private String a_FAQ;
    private String a_joinex;
    private String a_charge;
    private String a_stime;
    private String a_etime;
    private String a_address;
    private String a_photo;
    private Merchant merchant;
    private Type type;
    private Integer mer_id;
    private Integer t_id;
    private double a_lat;
    private double a_lon;
    public Action(String a_itemname, Double a_price, String a_introduce,
	    String a_consult, String a_FAQ, String a_joinex, String a_charge,
	    String a_stime, String a_etime, String a_address,
	    Merchant merchant, Type type) {
	super();
	this.a_itemname = a_itemname;
	this.a_price = a_price;
	this.a_introduce = a_introduce;
	this.a_consult = a_consult;
	this.a_FAQ = a_FAQ;
	this.a_joinex = a_joinex;
	this.a_charge = a_charge;
	this.a_stime = a_stime;
	this.a_etime = a_etime;
	this.a_address = a_address;
	this.merchant = merchant;
	this.type = type;
    }

    public Action(Integer a_id, String a_itemname, Double a_price,
	    String a_introduce, String a_consult, String a_FAQ,
	    String a_joinex, String a_charge, String a_stime, String a_etime,
	    String a_address, String a_photo, Merchant merchant, Type type) {
	this.a_id = a_id;
	this.a_itemname = a_itemname;
	this.a_price = a_price;
	this.a_introduce = a_introduce;
	this.a_consult = a_consult;
	this.a_charge = a_charge;
	this.a_stime = a_stime;
	this.a_etime = a_etime;
	this.a_address = a_address;
	this.a_FAQ = a_FAQ;
	this.a_joinex = a_joinex;
	this.a_photo = a_photo;
	this.merchant = merchant;
	this.type = type;

    }

    public Action(Integer a_id, String a_itemname, Double a_price,
	    String a_introduce, String a_consult, String a_charge,
	    String a_stime, String a_etime, String a_address) {
	this.a_id = a_id;
	this.a_itemname = a_itemname;
	this.a_price = a_price;
	this.a_introduce = a_introduce;
	this.a_consult = a_consult;
	this.a_charge = a_charge;
	this.a_stime = a_stime;
	this.a_etime = a_etime;
	this.a_address = a_address;

    }

    public Action(Integer a_id, String a_itemname, Double a_price,
	    String a_introduce, String a_consult, String a_charge,
	    String a_stime, String a_etime, String a_address, String a_photo,
	    Merchant merchant, Type type) {
	this.a_id = a_id;
	this.a_itemname = a_itemname;
	this.a_price = a_price;
	this.a_introduce = a_introduce;
	this.a_consult = a_consult;
	this.a_charge = a_charge;
	this.a_stime = a_stime;
	this.a_etime = a_etime;
	this.a_address = a_address;
	this.merchant = merchant;
	this.type = type;
	this.a_photo = a_photo;

    }

    public Action(Integer a_id, String a_itemname, Double a_price,
	    String string2, String a_introduce, String a_consult,
	    String a_charge, String a_stime, String a_etime, String a_address,
	    String a_photo, Merchant merchant, Type type) {
	this.a_id = a_id;
	this.a_itemname = a_itemname;
	this.a_price = a_price;
	this.a_introduce = a_introduce;
	this.a_consult = a_consult;
	this.a_charge = a_charge;
	this.a_stime = a_stime;
	this.a_etime = a_etime;
	this.a_address = a_address;
	this.merchant = merchant;
	this.type = type;
	this.a_photo = a_photo;
    }

    public Action(Integer a_id, String a_itemname, Double a_price,
	    String a_introduce, String a_consult, String a_FAQ,
	    String a_joinex, String a_charge, String a_stime, String a_etime,
	    String a_address, String a_photo, Integer mer_id, Integer t_id,Double a_lat,Double a_lon) {
	this.a_id = a_id;
	this.a_itemname = a_itemname;
	this.a_price = a_price;
	this.a_introduce = a_introduce;
	this.a_consult = a_consult;
	this.a_charge = a_charge;
	this.a_stime = a_stime;
	this.a_etime = a_etime;
	this.a_address = a_address;
	this.a_joinex = a_joinex;
	this.a_FAQ = a_FAQ;
	this.mer_id = mer_id;
	this.t_id = t_id;
	this.a_photo = a_photo;
	this.a_lat = a_lat;
	this.a_lon = a_lon;
    }

    public Action(int a_id, String a_itemname) {
	this.a_id = a_id;
	this.a_itemname = a_itemname;
    }



	public Action() {
	}

	public Merchant getMerchant() {
	return merchant;
    }

    public void setMerchant(Merchant merchant) {
	this.merchant = merchant;
    }

    public Type getType() {
	return type;
    }

    public void setType(Type type) {
	this.type = type;
    }

    public void setA_id(Integer a_id) {
	this.a_id = a_id;
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

    public double getA_price() {
	return a_price;
    }

    public void setA_price(double a_price) {
	this.a_price = a_price;
    }

    public String getA_introduce() {
	return a_introduce;
    }

    public Integer getMer_id() {
		return mer_id;
	}

	public void setMer_id(Integer mer_id) {
		this.mer_id = mer_id;
	}

	public Integer getT_id() {
		return t_id;
	}

	public void setT_id(Integer t_id) {
		this.t_id = t_id;
	}

	public double getA_lat() {
		return a_lat;
	}

	public void setA_lat(double a_lat) {
		this.a_lat = a_lat;
	}

	public double getA_lon() {
		return a_lon;
	}

	public void setA_lon(double a_lon) {
		this.a_lon = a_lon;
	}

	public void setA_introduce(String a_introduce) {
	this.a_introduce = a_introduce;
    }

    public String getA_consult() {
	return a_consult;
    }

    public void setA_consult(String a_consult) {
	this.a_consult = a_consult;
    }

    public String getA_FAQ() {
	return a_FAQ;
    }

    public void setA_FAQ(String a_FAQ) {
	this.a_FAQ = a_FAQ;
    }

    public String getA_joinex() {
	return a_joinex;
    }

    public void setA_joinex(String a_joinex) {
	this.a_joinex = a_joinex;
    }

    public String getA_charge() {
	return a_charge;
    }

    public void setA_charge(String a_charge) {
	this.a_charge = a_charge;
    }

    public String getA_stime() {
	return a_stime;
    }

    public void setA_stime(String a_stime) {
	this.a_stime = a_stime;
    }

    public String getA_etime() {
	return a_etime;
    }

    public void setA_etime(String a_etime) {
	this.a_etime = a_etime;
    }

    public String getA_address() {
	return a_address;
    }

    public void setA_address(String a_address) {
	this.a_address = a_address;
    }

    public String getA_photo() {
	return a_photo;
    }

    public void setA_photo(String a_photo) {
	this.a_photo = a_photo;
    }

    @Override
    public String toString() {
	return "Action [a_id=" + a_id + ", a_itemname=" + a_itemname
		+ ", a_price=" + a_price + ", a_introduce=" + a_introduce
		+ ", a_consult=" + a_consult + ", a_FAQ=" + a_FAQ
		+ ", a_joinex=" + a_joinex + ", a_charge=" + a_charge
		+ ", a_stime=" + a_stime + ", a_etime=" + a_etime
		+ ", a_address=" + a_address + ", a_photo=" + a_photo
		+ ", merchant=" + merchant + ", type=" + type + "]";
    }

}
