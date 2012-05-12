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
import static org.junit.Assert.*;


import java.util.Iterator;

import org.apache.bookkeeper.metadata.plugin.*;
import org.junit.Test;


public class MainTest {
	
	@Test
	public void testUsage () {

		String pluginName =
				"com.yahoo.bookkeeper.metadata.plugin.mock.MockMetadataPlugin";
		
		String config = "config=value";
		
		PluginLoader loader = new PluginLoader ();
		
		MetadataPlugin plugin = null;
		try
		{
			plugin = loader.loadPlugin (pluginName);
			plugin.init (config);
    	} catch (MetadataPluginException e) {
    		fail ("Plugin init failure");
    	} catch (Exception e) {
    		fail ("Plugin failure " + e);
    	}
		
		
    		
		MetadataTable myActualTable = plugin.createTable ("myTable");
		
		
		// ** Test get, put, remove
		
		String k = "a";
		String v;
		
		// Catch InterruptedException
		try
		{
			// Use a wrapper for testing sync calls
			MetadataTableAsyncToSyncConverter myTable = new MetadataTableAsyncToSyncConverter (myActualTable);
			
			// Make sure key isn't there
			myTable.remove (k);
			
			// Should be OK to delete a non-existent element
			myTable.remove (k);		
			
			v = myTable.get (k);
			assertNull ("Doing get on a non-existent element returns null", v);
		
		
			// put is unconditional overwrite
		
			myTable.put (k, "12");
			
		
			v = myTable.get (k);	
			assertNotNull ("Get key we just put", v);
			assertEquals ("Put stores value", v, "12");
			
			myTable.put (k, "13");
			v = myTable.get (k);
			assertEquals ("Put overwrites existing value", v, "13");

			myTable.remove (k);
			v = myTable.get (k);	
			assertNull ("Remove key we just put", v);
		
		
			// ** Test compareAndPut
			
			String oldValue = "7";
			myTable.put (k, oldValue);
		
			// Let's try a failure
			String newValue = "8";
			String notOldValue = newValue;
			boolean ok = myTable.compareAndPut (k, notOldValue, newValue);
			assertFalse ("compareAndPut returns false when stored value is different from oldValue", ok);
		
			// Should still have oldValue in it
			v = myTable.get (k);
			assertEquals ("compareAndPut shouldn't change value if it returns false", oldValue, v);
			
			// Now successfully store value
			ok = myTable.compareAndPut(k, oldValue, newValue);
			assertTrue ("compareAndPut returns true when oldValue == stored value", ok);
			v = myTable.get (k);
			assertEquals ("compareAndPut updates value when it returns true", newValue, v);
	
		
			// Now add several items for a scan
			myTable.put ("b", "2");
			myTable.put ("c", "3");
			myTable.put ("d", "4");
			myTable.put ("e", "5");


			// Simple usage - scan entire table
			int count = 0;
			{
				ScanResult result = myTable.scan (null, null);
				Iterator<MetadataTableItem> iter = result.getIterator ();
				while (iter.hasNext ()) {
					count++;
					MetadataTableItem item = iter.next ();
					System.out.println ("key = " + item.getKey () + " value = " + item.getValue ());
				}
			}

			// Scan in two passes, from implied firstKey (null) to "b" (non-inclusive), and from
			// "b" (inclusive) to implied lastKey (null).
			
			int count2 = 0;
			{
				ScanResult result = myTable.scan (null, "b");
				Iterator<MetadataTableItem> iter = result.getIterator ();
				while (iter.hasNext ()) {
					count2++;
					MetadataTableItem item = iter.next ();
					System.out.println ("key = " + item.getKey () + " value = " + item.getValue ());
				}	
			}
			
			{
				ScanResult result = myTable.scan ("b", null);
				Iterator<MetadataTableItem> iter = result.getIterator ();
				while (iter.hasNext ()) {
					count2++;
					MetadataTableItem item = iter.next ();
					System.out.println ("key = " + item.getKey () + " value = " + item.getValue ());
				}	
			}
			
			assertEquals ("Scanning from (null, null) has the same count as (null, key), (key, null)",
					count, count2);
			
		} catch (InterruptedException e)
		{
		}

		plugin.destroyTable (myActualTable);
		plugin.uninit (); 
	}

}
