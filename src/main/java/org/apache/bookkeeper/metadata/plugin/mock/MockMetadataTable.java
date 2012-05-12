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
package org.apache.bookkeeper.metadata.plugin.mock;


import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.NavigableMap;

import org.apache.bookkeeper.metadata.plugin.*;

public class MockMetadataTable implements MetadataTable {
	
	private String name;
	
	private TreeMap<String, String> map = null;

	public MockMetadataTable (MockMetadataPlugin plugin, String name) {
		this.map = new TreeMap<String,String> ();
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
	public void get (MetadataTableCallback<String> cb, String key) {
		String v = mockGet (key);
		int rc = 0;
		cb.complete (rc, v);		
	}
	
	public void put (MetadataTableCallback<String> cb, String key, String value) {
		mockPut (key, value);
		int rc = 0;
		cb.complete (rc, value);
	}
	
	public void remove (MetadataTableCallback<String> cb, String key) {
		mockRemove (key);
		int rc = 0;
		cb.complete (rc, key);
	}
	
	public void compareAndPut (MetadataTableCallback<Boolean> cb, String key, String oldValue, String newValue) {
		boolean success = mockCompareAndPut (key, oldValue, newValue);
		int rc = 0;
		cb.complete (rc, new Boolean (success));
	}
	
	public void scan (MetadataTableCallback<ScanResult> cb, String firstKey, String lastKey) {
		ScanResult result = mockScan (firstKey, lastKey);
		int rc = 0;
		cb.complete (rc, result);
	}
	



	private String mockGet (String key) {
		return map.get (key);
	}

	
	private void mockPut (String key, String value) {	
		map.put (key, value);
	}
	
	private void mockRemove (String key) {
		map.remove (key);
	}
		
	private boolean mockCompareAndPut (String key, String oldValue, String newValue) {
		String v = map.get (key);
		
		if ( ! v.equals(oldValue))
			return false;
		
		map.put (key, newValue);
		
		return true;
	}
	
	private ScanResult mockScan (String firstKey, String lastKey) {
		
		// Nulls are allowed
		
		String effectiveFirstKey = firstKey == null ? map.firstKey () : firstKey;
		String effectiveLastKey = lastKey == null ? map.lastKey () : lastKey;

		MockScanResult result = new MockScanResult ();
		
		NavigableMap<String, String> tempMap = map.tailMap(effectiveFirstKey, true);
		Iterator<Map.Entry<String, String>> iter = tempMap.entrySet ().iterator ();

		while (iter.hasNext ()) {
			Map.Entry<String, String> entry = iter.next();
			String v = entry.getValue ();
			String currentKey = entry.getKey ();
			if (currentKey == effectiveLastKey)
				break;
			
			result.addItem (new MetadataTableItem (currentKey, v));
		}
		
		return result;
	}
}
