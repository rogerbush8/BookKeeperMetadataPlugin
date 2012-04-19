import static org.junit.Assert.*;

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
		Versioned vv;
		
		
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
		
		Versioned vv2 = myTable.put (k, "7");
		
		// Change value on versioned and compareAndPut.  Since version is latest, this can
		// be successfully set with compareAndPut
		
		vv2.setValue ("8");
		Versioned vv3 = myTable.compareAndPut (k, vv2);
		assertNotNull ("compareAndPut failed to set when version was greater", vv3);
		
		
		// Make sure that the value is actually stored
		
		vv3 = myTable.get (k);	
		assertEquals ("compareAndPut succeeded but value didn't get updated in store",
				vv3.getValue (), "8");
		
		Versioned vv4 = myTable.compareAndPut (k, vv2);
		assertNull ("compareAndPut should have returned null (should have failed to put)", vv4);

		Versioned vv5 = myTable.get (k);
		assertEquals ("compareAndPut updated value on a failed put",
				vv5.getValue (), "8");

		
		
		plugin.destroyTable (myTable);
		plugin.uninit ();

	}

}
