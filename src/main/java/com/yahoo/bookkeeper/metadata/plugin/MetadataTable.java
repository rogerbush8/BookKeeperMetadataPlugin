package com.yahoo.bookkeeper.metadata.plugin;

public interface MetadataTable {
	public String getName ();
	public VersionedValue get (String key);
	public VersionedValue put (String key, String value);  // Overwrite
	public void remove (String key);
	public VersionedValue compareAndPut (String key, VersionedValue v);
	public ScanResult scan (int maxItems, ScanResult.Cursor cursor);
}
