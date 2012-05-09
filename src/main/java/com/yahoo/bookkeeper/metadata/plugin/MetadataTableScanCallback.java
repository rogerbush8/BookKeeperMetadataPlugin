package com.yahoo.bookkeeper.metadata.plugin;


public abstract class MetadataTableScanCallback extends MetadataTableCallback {
	public abstract void complete (int rc, MetadataTable table,
			Object ctx, int maxItems, ScanResult.Cursor cursor, ScanResult scanResult);
}
