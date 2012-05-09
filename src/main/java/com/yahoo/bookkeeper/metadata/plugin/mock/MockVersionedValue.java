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

import com.yahoo.bookkeeper.metadata.plugin.VersionedValue;

public class MockVersionedValue implements VersionedValue {
	
	public class Version implements VersionedValue.Version {
		private long count;
		
		public Version (long count) {
			// TODO synchronized
			this.count = count;
		}
		
		public VersionedValue.Version.Occurred compare (VersionedValue.Version v) {
			MockVersionedValue.Version mv = (MockVersionedValue.Version) v;
			if (this.count < mv.count)
				return VersionedValue.Version.Occurred.BEFORE;
			else if (this.count > mv.count)
				return VersionedValue.Version.Occurred.AFTER;
			else
				return VersionedValue.Version.Occurred.CONCURRENTLY;
		}

	}
		
	private Version version;
	private String value;
	    
	public MockVersionedValue (String value, long count) {
		this.value = value;
	    this.version = new Version (count);
	}
	    
	public VersionedValue.Version getVersion () {
		return this.version;
	}
	    
	public void setVersion (VersionedValue.Version version) {
		this.version = (MockVersionedValue.Version) version;
	}
	    
	public String getValue () {
		return this.value;
	}
	    
	public void setValue (String value) {
		this.value = value;
	}
}
