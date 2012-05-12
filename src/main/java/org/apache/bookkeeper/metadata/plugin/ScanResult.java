package org.apache.bookkeeper.metadata.plugin;

import java.util.Iterator;

public interface ScanResult {
	public Iterator<MetadataTableItem> getIterator ();
}
