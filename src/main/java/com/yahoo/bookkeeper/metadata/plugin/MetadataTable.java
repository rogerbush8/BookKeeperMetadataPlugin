package com.yahoo.bookkeeper.metadata.plugin;

public interface MetadataTable {
	public String getName ();
	public Versioned get (String key);
	public Versioned put (String key, String value);  // Overwrite
	public void remove (String key);
	public Versioned compareAndPut (String key, Versioned v);
}
