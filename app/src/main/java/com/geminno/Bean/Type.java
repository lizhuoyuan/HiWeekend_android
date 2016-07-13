package com.geminno.Bean;

public class Type {
	private int t_id;
	private String t_name;
	private int t_sign;

	public Type(int t_id, String t_name, int t_sign) {
		this.t_id = t_id;
		this.t_name = t_name;
		this.t_sign = t_sign;
	}

	public Type() {

	}

	public int getT_id() {
		return t_id;
	}

	public void setT_id(int t_id) {
		this.t_id = t_id;
	}

	public String getT_name() {
		return t_name;
	}

	public void setT_name(String t_name) {
		this.t_name = t_name;
	}

	public int getT_sign() {
		return t_sign;
	}

	public void setT_sign(int t_sign) {
		this.t_sign = t_sign;
	}

}
