package com.yahoo.bookkeeper.metadata.plugin.mock;

import com.yahoo.bookkeeper.metadata.plugin.*;

public class MockMetadataPlugin implements MetadataPlugin {
	
	public MockMetadataPlugin () {
	}
	
	public String getName () {
		return "MockMetadataPlugin";
	}
	
	public String getVersion () {
		return "1.0";
	}
	
	public void init (String config) throws MetadataPluginException {
		// Decompose config string as k=v pairs and use to
		// initialize.
	}
	
	public void uninit () {
	}
	
	public MetadataTable createTable (String name) {
		return new MockMetadataTable (this, name);
	}
	
	public void destroyTable (MetadataTable table) {
	}
}
