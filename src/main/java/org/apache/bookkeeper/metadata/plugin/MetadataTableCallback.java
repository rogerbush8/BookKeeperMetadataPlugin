package org.apache.bookkeeper.metadata.plugin;

public abstract class MetadataTableCallback<T> {
	public abstract void complete (int rc, T value);
}
