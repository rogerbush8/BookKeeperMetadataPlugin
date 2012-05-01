package com.yahoo.bookkeeper.metadata.plugin.mock;

import java.util.Iterator;
import java.util.Vector;

import com.yahoo.bookkeeper.metadata.plugin.MetadataTableItem;
import com.yahoo.bookkeeper.metadata.plugin.ScanResult;


public class MockScanResult implements ScanResult {

	private MockScanCursor cursor = null;
	private Vector<MetadataTableItem> vector = new Vector<MetadataTableItem> ();
	
	public class MockScanCursor implements Cursor {
		
		private String key;
		
		MockScanCursor (String key) {
			this.key = key;
		}
		
		String getKey () {
			return this.key;
		}
	}

	
	MockScanResult () {	
	}
	
	void addItem (MetadataTableItem item) {
		vector.addElement (item);
	}
	
	void setScanCursor (String key) {
		this.cursor = new MockScanCursor (key);
	}
	
	public Cursor getScanCursor () {
		return this.cursor;
	}
	
	public Iterator<MetadataTableItem> getIterator () {
		return vector.iterator ();
	}
}
