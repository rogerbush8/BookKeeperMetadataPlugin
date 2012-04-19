package com.yahoo.bookkeeper.metadata.plugin;

public interface MetadataPlugin {
	public String getName ();
	public String getVersion ();
	public void init (String config) throws MetadataPluginException;
	public void uninit ();
	public MetadataTable createTable (String name);
	public void destroyTable (MetadataTable table);
}
