package com.yahoo.bookkeeper.metadata.plugin;

public class Versioned {
	
    private Version version;
    private String value;

    public Versioned (String value) {
    	this.value = value;
    	this.version = null;
    }
    
    public Versioned (String value, Version version) {
    	this.value = value;
    	this.version = version;
    }
    
    public Version getVersion () {
    	return this.version;
    }
    
    public String getValue () {
    	return this.value;
    }
    
    public void setValue (String value) {
    	this.value = value;
    }
    
}
