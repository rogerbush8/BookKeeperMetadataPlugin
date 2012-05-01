package com.yahoo.bookkeeper.metadata.plugin;

import java.util.Iterator;

public interface ScanResult {
	
	public interface Cursor {
	}
	
	public Cursor getScanCursor ();
	public Iterator<MetadataTableItem> getIterator ();
}
