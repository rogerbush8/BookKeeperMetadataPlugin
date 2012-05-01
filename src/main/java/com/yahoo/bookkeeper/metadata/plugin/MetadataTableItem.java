package com.yahoo.bookkeeper.metadata.plugin;

public class MetadataTableItem {
	
	private String key;
	private String value;
	
	public MetadataTableItem (String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey () {
		return key;
	}

	public void setKey (String key) {
		this.key = key;
	}

	public String getValue () {
		return value;
	}

	public void setValue (String value) {
		this.value = value;
	}
}
