package com.geminno.Bean;
/**
 * 
 * @author 李卓原
 * 创建时间：2015年11月13日 下午3:14:58
 */
public class AllBean {
	private Action action;
	private Cinema ciname;
	private Movie movie;
	public AllBean(Action action, Cinema ciname, Movie movie) {
		super();
		this.action = action;
		this.ciname = ciname;
		this.movie = movie;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public Cinema getCiname() {
		return ciname;
	}
	public void setCiname(Cinema ciname) {
		this.ciname = ciname;
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
}
