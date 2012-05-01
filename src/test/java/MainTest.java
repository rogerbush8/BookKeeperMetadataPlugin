import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.yahoo.bookkeeper.metadata.plugin.*;
public class MainTest {
	
	@Test
	public void test () {
		
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
		
		
		myTable.remove (k);
		vv = myTable.get (k);	
		assertNull ("Precondition failed:  If key is present, remove for tests", vv);

		
		// put is unconditional overwrite
		
		vv = myTable.put (k, v);
		
		
		// get returns Versioned which has a Version and (String) Value
		
		vv = myTable.get (k);	
		assertNotNull ("Get failed to find key", vv);
		

		myTable.remove (k);
		vv = myTable.get (k);	
		assertNull ("Remove failed to delete key", vv);
		
		
		// ** Test compareAndPut
		
		VersionedValue vv2 = myTable.put (k, "7");
		
		// Change value on versioned and compareAndPut.  Since version is latest, this can
		// be successfully set with compareAndPut
		
		vv2.setValue ("8");
		VersionedValue vv3 = myTable.compareAndPut (k, vv2);
		assertNotNull ("compareAndPut failed to set when version was greater", vv3);
		
		
		// Make sure that the value is actually stored
		
		vv3 = myTable.get (k);	
		assertEquals ("compareAndPut succeeded but value didn't get updated in store",
				vv3.getValue (), "8");
		
		VersionedValue vv4 = myTable.compareAndPut (k, vv2);
		assertNull ("compareAndPut should have returned null (should have failed to put)", vv4);

		VersionedValue vv5 = myTable.get (k);
		assertEquals ("compareAndPut updated value on a failed put",
				vv5.getValue (), "8");
		
		// Now add several items for a scan
		myTable.put ("a", "1");
		myTable.put ("b", "2");
		myTable.put ("c", "3");
		
		// Simple usage
		
		{
			int maxItems = 10;
			ScanResult.Cursor cursor = null;
			ScanResult result = myTable.scan (maxItems, cursor);
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
				ScanResult result = myTable.scan (maxItems, cursor);
				cursor = result.getScanCursor ();
				Iterator<MetadataTableItem> iter = result.getIterator ();
				while (iter.hasNext ()) {
					MetadataTableItem item = iter.next ();
					System.out.println ("run = " + run + " key = " + item.getKey () + " value = " + item.getValue ());
				}
			} while (cursor != null);
		}
		

		plugin.destroyTable (myTable);
		plugin.uninit ();

	}

}
