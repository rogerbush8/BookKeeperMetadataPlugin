package com.yahoo.bookkeeper.metadata.plugin;

public interface MetadataTable {
	public String getName ();
	public void asyncGet (MetadataTableGetCallback cb, Object ctx, String key);
	public void asyncPut (MetadataTablePutCallback cb, Object ctx, String key, String value);  // Overwrite
	public void asyncRemove (MetadataTableRemoveCallback cb, Object ctx, String key);
	public void asyncCompareAndPut (MetadataTableCompareAndPutCallback cb, Object ctx, String key,
			VersionedValue versionedValue);
	public void asyncScan (MetadataTableScanCallback cb, Object ctx, int maxItems, ScanResult.Cursor cursor);
}
