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
import org.junit.Test;

import com.yahoo.bookkeeper.metadata.plugin.*;

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
		
		
    		
		MetadataTable myTable = plugin.createTable ("myTable");
		
		
		// ** Test get, put, remove
		
		String k = "k";
		String v = "6";
		VersionedValue vv;
		
		MetadataTableAsyncToSyncConverter myTable2 = new MetadataTableAsyncToSyncConverter (myTable);
		
		// Catch InterruptedException
		try
		{
			myTable2.remove (k);
			vv = myTable2.get (k);
			assertNull ("Precondition failed:  If key is present, remove for tests", vv);
		
		
			// put is unconditional overwrite
		
			vv = myTable2.put (k, v);
		
		
			// get returns Versioned which has a Version and (String) Value
		
			// vv = myTable.get (k);	
			vv = myTable2.get (k);	
			assertNotNull ("Get failed to find key", vv);
		

			myTable2.remove (k);
			vv = myTable2.get (k);	
			assertNull ("Remove failed to delete key", vv);
		
		
			// ** Test compareAndPut
		
			VersionedValue vv2 = myTable2.put (k, "7");
		
			// Change value on versioned and compareAndPut.  Since version is latest, this can
			// be successfully set with compareAndPut
		
			vv2.setValue ("8");
			VersionedValue vv3 = myTable2.compareAndPut (k, vv2);
			assertNotNull ("compareAndPut failed to set when version was greater", vv3);
		
		
			// Make sure that the value is actually stored
		
			vv3 = myTable2.get (k);	
			assertEquals ("compareAndPut succeeded but value didn't get updated in store",
				vv3.getValue (), "8");
		
			VersionedValue vv4 = myTable2.compareAndPut (k, vv2);
			assertNull ("compareAndPut should have returned null (should have failed to put)", vv4);

			VersionedValue vv5 = myTable2.get (k);
			assertEquals ("compareAndPut updated value on a failed put",
				vv5.getValue (), "8");
		
			// Now add several items for a scan
			myTable2.put ("a", "1");
			myTable2.put ("b", "2");
			myTable2.put ("c", "3");
		
			// Simple usage
		
			{
				int maxItems = 10;
				ScanResult.Cursor cursor = null;
				ScanResult result = myTable2.scan (maxItems, cursor);
				Iterator<MetadataTableItem> iter = result.getIterator ();
				while (iter.hasNext ()) {
					MetadataTableItem item = iter.next ();
					System.out.println ("key = " + item.getKey () + " value = " + item.getValue ());
				}
			}
		
		
			// Chunked, iterated usage
			{
				int run = 0;
				int maxItems = 2;
				ScanResult.Cursor cursor = null;
				do {
					run++;
					ScanResult result = myTable2.scan (maxItems, cursor);
					cursor = result.getScanCursor ();
					Iterator<MetadataTableItem> iter = result.getIterator ();
					while (iter.hasNext ()) {
						MetadataTableItem item = iter.next ();
						System.out.println ("run = " + run + " key = " + item.getKey () + " value = " + item.getValue ());
					}
				} while (cursor != null);
			}
		} catch (InterruptedException e)
		{
		}
		

		plugin.destroyTable (myTable);
		plugin.uninit (); 
	}

}
