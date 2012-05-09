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
