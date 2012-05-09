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
import com.yahoo.bookkeeper.metadata.plugin.*;


// Converts async calls to sync calls for MetadataTable.  Currently not
// intended to be used other than for simple functional tests, however,
// could be developed into a sync API.

public class MetadataTableAsyncToSyncConverter {
	private MetadataTable table;
	
	// HeldValue is useful for local, anonymous callbacks which require
	// values passed by "final pointers"
	
	class HeldValue<T> {
		private boolean finished = false;
		private T vv = null;
		
		boolean isFinished () {
			return finished;
		}
		
		T getValue () {
			return vv;
		}
		
		void setValue (T vv) {
			this.finished = true;
			this.vv = vv;
		}
	}
	
	
	public MetadataTableAsyncToSyncConverter (MetadataTable table) {
		this.table = table;
	}
	
	
	public VersionedValue get (String key) throws InterruptedException {
		
		final HeldValue<VersionedValue> returnValue = new HeldValue<VersionedValue> ();
		
		MetadataTableGetCallback cb = new MetadataTableGetCallback () {
			public void complete (int rc, MetadataTable table,
					Object ctx, String key, VersionedValue vv) {
				returnValue.setValue (vv);
			}
		};
	
		// make the actual async call
		
		this.table.asyncGet (cb, new Object (), key);
		
		// busy wait
		while ( ! returnValue.isFinished ()) {
		}
		
		return returnValue.getValue ();
	}

	public VersionedValue put (String key, String value) throws InterruptedException {
	
		final HeldValue<VersionedValue> returnValue = new HeldValue<VersionedValue> ();
	
		MetadataTablePutCallback cb = new MetadataTablePutCallback () {
			public void complete (int rc, MetadataTable table,
					Object ctx, String key, VersionedValue vv) {
				returnValue.setValue (vv);
			}
		};

		// make the actual async call
	
		this.table.asyncPut (cb, new Object (), key, value);
	
		// busy wait
		while ( ! returnValue.isFinished ()) {
		}
	
		return returnValue.getValue ();
	}
	
	public void remove (String key) throws InterruptedException {
		
		final HeldValue<VersionedValue> returnValue = new HeldValue<VersionedValue> ();
	
		MetadataTableRemoveCallback cb = new MetadataTableRemoveCallback () {
			public void complete (int rc, MetadataTable table,
					Object ctx, String key) {
				returnValue.setValue (null);
			}
		};

		// make the actual async call
	
		this.table.asyncRemove (cb, new Object (), key);
	
		// busy wait
		while ( ! returnValue.isFinished ()) {
		}
	
		return;
	}
	
	public VersionedValue compareAndPut (String key, VersionedValue versionedValue) throws InterruptedException {
		
		final HeldValue<VersionedValue> returnValue = new HeldValue<VersionedValue> ();
	
		MetadataTableCompareAndPutCallback cb = new MetadataTableCompareAndPutCallback () {
			public void complete (int rc, MetadataTable table,
					Object ctx, String key, VersionedValue vv) {
				returnValue.setValue (vv);
			}
		};

		// make the actual async call
	
		this.table.asyncCompareAndPut (cb, new Object (), key, versionedValue);
	
		// busy wait
		while ( ! returnValue.isFinished ()) {
		}
	
		return returnValue.getValue ();
	}
	
	public ScanResult scan (int maxItems, ScanResult.Cursor cursor) throws InterruptedException {
		
		final HeldValue<ScanResult> returnValue = new HeldValue<ScanResult> ();
	
		MetadataTableScanCallback cb = new MetadataTableScanCallback () {
			public void complete (int rc, MetadataTable table,
					Object ctx, int maxItems, ScanResult.Cursor cursor, ScanResult scanResult) {
				returnValue.setValue (scanResult);
			}
		};

		// make the actual async call
	
		this.table.asyncScan (cb, new Object (), maxItems, cursor);
	
		// busy wait
		while ( ! returnValue.isFinished ()) {
		}
	
		return returnValue.getValue ();
	}
	
	
}
