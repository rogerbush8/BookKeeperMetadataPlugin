package com.yahoo.bookkeeper.metadata.plugin.mock;

import com.yahoo.bookkeeper.metadata.plugin.*;

import java.util.HashMap;

public class MockMetadataTable implements MetadataTable {
	
	private String name;
	private long versionCounter = 1;

	
	// The simplest Mock table
	
	private HashMap<String,Versioned> map = null;

	public MockMetadataTable (MockMetadataPlugin plugin, String name) {
		this.map = new HashMap<String,Versioned> ();
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
	public Versioned get (String key) {
		return map.get (key);
	}
	
	public Versioned put (String key, String value) {
		
		// TODO synchronized
		Version vr = new MockVersion (versionCounter++);
		Versioned vv = new Versioned (value, vr);
		map.put (key, vv);
		return vv;
	}
	
	public void remove (String key) {
		map.remove (key);
	}
	
	// If successful set, Versioned returned, otherwise null.
	// Our versioned object must have a version >= to current
	// version of object that is set.
	
	public Versioned compareAndPut (String key, Versioned v) {
		Versioned vv = map.get (key);

		// If no existing value or incoming value version >= existing version
		// then store (success)
		
		if (vv == null ||
			v.getVersion ().compare (vv.getVersion ()) != Occurred.BEFORE)
		{
			// update version and store
			Version vr = new MockVersion (versionCounter++);
			Versioned vv2 = new Versioned (v.getValue (), vr);
			map.put (key, vv2);
			return vv2;
		}
		
		// else fail
		
		return null;
	}
	
	

}
