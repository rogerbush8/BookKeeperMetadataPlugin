package com.yahoo.bookkeeper.metadata.plugin;

import java.lang.reflect.Constructor;

public class PluginLoader {
	
	public MetadataPlugin loadPlugin (String name) throws
		ClassNotFoundException, NoSuchMethodException, PluginLoaderException
	{
		// Load a class that implements to MetadataPlugin interface
		
	    ClassLoader classLoader = PluginLoader.class.getClassLoader ();
	    	
	    
	   // Get class loader for our plugin class (implements MetadataPlugin)
	   @SuppressWarnings ("rawtypes")
	   Class cls = classLoader.loadClass (name);

	    
	    // Only one constructor and it takes one string arg (config string)
	    	
	    @SuppressWarnings ("unchecked")
		Constructor<MetadataPlugin> ct = cls.getConstructor ();
	        
	        
	    // Invoke constructor.  There are a variety of errors which may occur.
	    MetadataPlugin plugin;
	    try {
	    	plugin = ct.newInstance ();      
	    } catch (Exception e) {
	    	throw new PluginLoaderException ("Error invoking Plugin constructor", e);
	    }
	    
    	return plugin;
	}

}
