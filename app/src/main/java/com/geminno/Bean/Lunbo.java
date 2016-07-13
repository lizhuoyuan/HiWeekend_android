package com.geminno.Bean;

public class Lunbo {
	private int iconResid;
	private String intro;

	public Lunbo(int iconResid, String intro) {
		super();
		this.iconResid = iconResid;
		this.intro = intro;
	}

	public int getIconResid() {
		return iconResid;
	}

	public void setIconResid(int iconResid) {
		this.iconResid = iconResid;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

}
