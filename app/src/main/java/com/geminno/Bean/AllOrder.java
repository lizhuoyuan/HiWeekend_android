package com.geminno.Bean;

import java.io.Serializable;

import android.util.SparseArray;

public class AllOrder implements Serializable, Comparable<AllOrder> {
	private int id;
	private int sign;
	private String name;
	private String poster;
	private String introduce;
	private double price;
	private int state;
	private String date;
	private String seat;
	private String session;
	private int credit;
	private double yue;
	private String time;
	private int count;
	private String tel;
	private User user;
	private Movie movie;
	private int c_id;
	private Action action;

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public int getC_id() {
		return c_id;
	}

	public void setC_id(int c_id) {
		this.c_id = c_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public AllOrder() {
		// TODO Auto-generated constructor stub
	}

	public AllOrder(String name, String poster, String introduce, double price,
			int state) {
		super();

		this.name = name;
		this.poster = poster;
		this.introduce = introduce;
		this.price = price;
		this.state = state;

	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public double getYue() {
		return yue;
	}

	public void setYue(double yue) {
		this.yue = yue;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getstate() {
		return state;
	}

	public void setstate(int state) {
		this.state = state;
	}

	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getposter() {
		return poster;
	}

	public void setposter(String poster) {
		this.poster = poster;
	}

	public String getintroduce() {
		return introduce;
	}

	public void setintroduce(String introduce) {
		this.introduce = introduce;
	}

	public double getprice() {
		return price;
	}

	public void setprice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "AllOrder [id=" + id + ", sign=" + sign + ", name=" + name
				+ ", poster=" + poster + ", introduce=" + introduce + ", price="
				+ price + ", state=" + state + ", date=" + date + ", seat="
				+ seat + ", session=" + session + ", credit=" + credit
				+ ", yue=" + yue + ", time=" + time + ", count=" + count
				+ ", tel=" + tel + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + sign;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AllOrder other = (AllOrder) obj;
		if (id != other.id)
			return false;
		if (sign != other.sign)
			return false;
		return true;
	}

	@Override
	public int compareTo(AllOrder another) {
		if (this.getDate().compareTo(another.getDate()) > 0) {
			return -1;
		}
		if (this.getDate().compareTo(another.getDate()) < 0) {
			return 1;
		}
		return 0;
	}

}
