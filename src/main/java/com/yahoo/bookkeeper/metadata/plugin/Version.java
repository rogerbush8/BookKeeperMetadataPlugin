package com.yahoo.bookkeeper.metadata.plugin;

public abstract interface Version {
	Occurred compare (Version v);
}


