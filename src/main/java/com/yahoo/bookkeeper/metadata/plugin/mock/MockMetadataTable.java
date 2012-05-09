/**
 * Copyright 2012 Yahoo! Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 */
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
	
	public void asyncGet (MetadataTableGetCallback cb, Object ctx, String key) {
		VersionedValue vv = mockGet (key);
		int rc = 0;
		cb.complete (rc, this, ctx, key, vv);
	}
	
	public void asyncPut (MetadataTablePutCallback cb, Object ctx, String key, String value) {
		VersionedValue vv = mockPut (key, value);
		int rc = 0;
		cb.complete (rc, this, ctx, key, vv);
	}
	
	public void asyncRemove (MetadataTableRemoveCallback cb, Object ctx, String key) {
		mockRemove (key);
		int rc = 0;
		cb.complete (rc, this, ctx, key);
	}
	
	public void asyncCompareAndPut (MetadataTableCompareAndPutCallback cb,
			Object ctx, String key, VersionedValue versionedValue) {
		VersionedValue vv = mockCompareAndPut (key, versionedValue);
		int rc = 0;
		cb.complete (rc, this, ctx, key, vv);
	}
	
	public void asyncScan (MetadataTableScanCallback cb,
			Object ctx, int maxItems, ScanResult.Cursor cursor) {
		ScanResult scanResult = mockScan (maxItems, cursor);
		int rc = 0;
		cb.complete (rc, this, ctx, maxItems, cursor, scanResult);
	}
	
	// 


	private VersionedValue mockGet (String key) {
		return map.get (key);
	}

	
	private VersionedValue mockPut (String key, String value) {	
		// TODO synchronized
		VersionedValue vv = new MockVersionedValue (value, versionCounter++);
		map.put (key, vv);
		return vv;
	}
	
	private void mockRemove (String key) {
		map.remove (key);
	}
	
	// If successful set, Versioned returned, otherwise null.
	// Our versioned object must have a version >= to current
	// version of object that is set.
	
	private VersionedValue mockCompareAndPut (String key, VersionedValue v) {
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
		
	private ScanResult mockScan (int maxItems, ScanResult.Cursor cursor) {

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
