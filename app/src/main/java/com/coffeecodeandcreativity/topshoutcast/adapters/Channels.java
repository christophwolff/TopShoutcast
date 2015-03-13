package com.coffeecodeandcreativity.topshoutcast.adapters;

public class Channels {

	String channelName, url, genre;


    public Channels(String channelName, String url, String genre) {

		this.channelName = channelName;
		this.url = url;
        this.genre = genre;
	}

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

    public String getGenre() {return genre;  }

    public void setGenre(String genre) { this.genre = genre; }

}
