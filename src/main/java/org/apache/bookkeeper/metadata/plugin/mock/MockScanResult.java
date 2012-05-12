
package org.apache.bookkeeper.metadata.plugin.mock;

import java.util.Iterator;
import java.util.Vector;

import org.apache.bookkeeper.metadata.plugin.MetadataTableItem;
import org.apache.bookkeeper.metadata.plugin.ScanResult;

public class MockScanResult implements ScanResult {

	private Vector<MetadataTableItem> vector = new Vector<MetadataTableItem> ();
	
		
	MockScanResult () {	
	}
		
	void addItem (MetadataTableItem item) {
		vector.addElement (item);
	}
		
	public Iterator<MetadataTableItem> getIterator () {
		return vector.iterator ();
	}
}
