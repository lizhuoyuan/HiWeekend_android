package com.geminno.Bean;

/**
 * 
 * @author 李卓原 创建时间：2015-10-21 下午7:11:10
 */
public class Quiz {
	private int q_id;
	private Action action;
	private User user;
	private String q_cont;
	private String q_answer;
	private String q_time;
	private String a_itemname;
	private int a_id;
	private String u_name;
	private int u_id;

	public Quiz(String q_time, String u_name, String q_cont, String a_itemname,
			int a_id, int u_id) {
		this.q_time = q_time;
		this.u_name = u_name;
		this.q_cont = q_cont;
		this.a_itemname = a_itemname;
		this.a_id = a_id;
		this.u_id = u_id;
	}

	public Quiz(String q_time, String q_cont, String q_answer) {
		this.q_time = q_time;
		this.q_cont = q_cont;
		this.q_answer = q_answer;
	}

	public Quiz(String q_time, String q_cont, int u_id) {
		this.q_time = q_time;
		this.q_cont = q_cont;
		this.u_id = u_id;
	}

	public String getA_itemname() {
		return a_itemname;
	}

	public void setA_itemname(String a_itemname) {
		this.a_itemname = a_itemname;
	}

	public String getU_name() {
		return u_name;
	}

	public void setU_name(String u_name) {
		this.u_name = u_name;
	}

	public int getA_id() {
		return a_id;
	}

	public void setA_id(int a_id) {
		this.a_id = a_id;
	}

	public int getU_id() {
		return u_id;
	}

	public void setU_id(int u_id) {
		this.u_id = u_id;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getQ_id() {
		return q_id;
	}

	public void setQ_id(int q_id) {
		this.q_id = q_id;
	}

	public String getQ_cont() {
		return q_cont;
	}

	public void setQ_cont(String q_cont) {
		this.q_cont = q_cont;
	}

	public String getQ_answer() {
		return q_answer;
	}

	public void setQ_answer(String q_answer) {
		this.q_answer = q_answer;
	}

	public String getQ_time() {
		return q_time;
	}

	public void setQ_time(String q_time) {
		this.q_time = q_time;
	}

}
