package com.yahoo.bookkeeper.metadata.plugin;

// import com.yahoo.bookkeeper.metadata.plugin.Version22.Occurred;

public interface VersionedValue {
	public interface Version {
		public enum Occurred {
		    BEFORE, AFTER, CONCURRENTLY
		}
		
		public Occurred compare (Version v);
	}
	
    public Version getVersion ();
    public void setVersion (Version version);
    public String getValue ();
    public void setValue (String value);
}
