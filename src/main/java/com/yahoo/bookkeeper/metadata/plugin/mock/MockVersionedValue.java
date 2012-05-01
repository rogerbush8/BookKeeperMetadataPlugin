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
