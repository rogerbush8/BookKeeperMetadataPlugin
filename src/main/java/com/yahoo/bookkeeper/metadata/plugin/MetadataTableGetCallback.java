package com.yahoo.bookkeeper.metadata.plugin;

public abstract class MetadataTableGetCallback extends MetadataTableCallback {
	public abstract void complete (int rc, MetadataTable table,
			Object ctx, String key, VersionedValue vv);
}
