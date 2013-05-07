package com.example.morningreport;

public class Feed {
	private String title;
	private String url;

	public Feed(String title, String url) {
		this.setTitle(title);
		this.setUrl(url);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
