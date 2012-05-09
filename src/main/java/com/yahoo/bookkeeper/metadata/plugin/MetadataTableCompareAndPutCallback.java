package com.yahoo.bookkeeper.metadata.plugin;


public abstract class MetadataTableCompareAndPutCallback extends MetadataTableCallback {
	public abstract void complete (int rc, MetadataTable table,
			Object ctx, String key, VersionedValue vv);
}
