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
