package com.yahoo.bookkeeper.metadata.plugin.mock;

import com.yahoo.bookkeeper.metadata.plugin.Occurred;
import com.yahoo.bookkeeper.metadata.plugin.Version;

public class MockVersion implements Version {
	private long count;
	
	public MockVersion (long count) {
		// TODO synchronized
		this.count = count;
	}
	
	public Occurred compare (Version v) {
		MockVersion mv = (MockVersion) v;
		if (this.count < mv.count)
			return Occurred.BEFORE;
		else if (this.count > mv.count)
			return Occurred.AFTER;
		else
			return Occurred.CONCURRENTLY;
	}

}
