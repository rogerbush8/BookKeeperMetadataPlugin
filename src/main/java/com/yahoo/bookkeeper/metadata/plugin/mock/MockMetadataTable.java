package com.yahoo.bookkeeper.metadata.plugin.mock;

import com.yahoo.bookkeeper.metadata.plugin.*;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.NavigableMap;

public class MockMetadataTable implements MetadataTable {
	
	private String name;
	private long versionCounter = 1;
	
	private TreeMap<String,VersionedValue> map = null;

	public MockMetadataTable (MockMetadataPlugin plugin, String name) {
		this.map = new TreeMap<String,VersionedValue> ();
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
	public VersionedValue get (String key) {
		return map.get (key);
	}
	
	public VersionedValue put (String key, String value) {	
		// TODO synchronized
		VersionedValue vv = new MockVersionedValue (value, versionCounter++);
		map.put (key, vv);
		return vv;
	}
	
	public void remove (String key) {
		map.remove (key);
	}
	
	// If successful set, Versioned returned, otherwise null.
	// Our versioned object must have a version >= to current
	// version of object that is set.
	
	public VersionedValue compareAndPut (String key, VersionedValue v) {
		VersionedValue vv = map.get (key);

		// If no existing value or incoming value version >= existing version
		// then store (success)
		
		if (vv == null ||
			v.getVersion ().compare (vv.getVersion ()) != VersionedValue.Version.Occurred.BEFORE)
		{
			// update version and store
			VersionedValue vv2 = new MockVersionedValue (v.getValue (), versionCounter++);
			map.put (key, vv2);
			return vv2;
		}
		
		// else fail
		
		return null;
	}

	
	public ScanResult scan (int maxItems, ScanResult.Cursor cursor) {

		String startKey = (cursor == null) ? map.firstKey () : ((MockScanResult.MockScanCursor) cursor).getKey ();
		
		MockScanResult result = new MockScanResult ();
		
		int count = 0;
		NavigableMap<String, VersionedValue> tempMap = map.tailMap(startKey, true);
		Iterator<Map.Entry<String, VersionedValue>> iter = tempMap.entrySet ().iterator ();

		while (iter.hasNext ()) {
			count++;
			Map.Entry<String, VersionedValue> entry = iter.next();
			VersionedValue vv = entry.getValue ();
			String currentKey = entry.getKey ();
			
			if (count > maxItems)
			{
				result.setScanCursor (currentKey);
				break;
			}
			
			result.addItem (new MetadataTableItem (currentKey, vv.getValue ()));
		}
		
		return result;
	}
}
