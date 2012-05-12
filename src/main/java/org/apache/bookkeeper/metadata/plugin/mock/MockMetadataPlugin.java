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

import org.apache.bookkeeper.metadata.plugin.*;

public class MockMetadataPlugin implements MetadataPlugin {
	
	public MockMetadataPlugin () {
	}
	
	public String getName () {
		return "MockMetadataPlugin";
	}
	
	public String getVersion () {
		return "1.0";
	}
	
	public void init (String config) throws MetadataPluginException {
		// Decompose config string as k=v pairs and use to
		// initialize.
	}
	
	public void uninit () {
	}
	
	public MetadataTable createTable (String name) {
		return new MockMetadataTable (this, name);
	}
	
	public void destroyTable (MetadataTable table) {
	}
}
