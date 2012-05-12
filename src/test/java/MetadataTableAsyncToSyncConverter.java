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
import org.apache.bookkeeper.metadata.plugin.*;


// Converts async calls to sync calls for MetadataTable.  Currently not
// intended to be used other than for simple functional tests, however,
// could be developed into a sync API.

public class MetadataTableAsyncToSyncConverter {
	private MetadataTable table;
	
	class HeldValue<T> {
		private boolean finished = false;
		private T value = null;
		
		boolean isFinished () {
			return finished;
		}
		
		T getValue () {
			return value;
		}
		
		void setValue (T value) {
			this.finished = true;
			this.value = value;
		}
	}
	
	
	
	public MetadataTableAsyncToSyncConverter (MetadataTable table) {
		this.table = table;
	}
	
	public String get (String key) throws InterruptedException {
		
		final HeldValue<String> returnValue = new HeldValue<String> ();
		
		MetadataTableCallback<String> cb = new MetadataTableCallback<String> () {
			public void complete (int rc, String value) {
				returnValue.setValue (value);
			}
		};
	
		// make the actual async call
		
		this.table.get (cb, key);
		
		// busy wait
		while ( ! returnValue.isFinished ()) {
		}
		
		return returnValue.getValue ();
	}
	
	
	public void put (String key, String value) throws InterruptedException {
		
		final HeldValue<String> returnValue = new HeldValue<String> ();
	
		MetadataTableCallback<String> cb = new MetadataTableCallback<String> () {
			public void complete (int rc, String value) {
				returnValue.setValue (value);
			}
		};

		// make the actual async call
	
		this.table.put (cb, key, value);
	
		// busy wait
		while ( ! returnValue.isFinished ()) {
		}
	
		return;
	}
	
	public void remove (String key) throws InterruptedException {
		
		final HeldValue<String> returnValue = new HeldValue<String> ();
	
		MetadataTableCallback<String> cb = new MetadataTableCallback<String> () {
			public void complete (int rc, String key) {
				returnValue.setValue (null);
			}
		};

		// make the actual async call
	
		this.table.remove (cb, key);
	
		// busy wait
		while ( ! returnValue.isFinished ()) {
		}
	
		return;
	}
	

	public boolean compareAndPut (String key, String oldValue, String newValue) throws InterruptedException {
		
		final HeldValue<Boolean> returnValue = new HeldValue<Boolean> ();
	
		MetadataTableCallback<Boolean> cb = new MetadataTableCallback<Boolean> () {
			public void complete (int rc, Boolean success) {
				returnValue.setValue (success);
			}
		};

		// make the actual async call
	
		this.table.compareAndPut (cb, key, oldValue, newValue);
	
		// busy wait
		while ( ! returnValue.isFinished ()) {
		}
	
		return returnValue.getValue ();
	}
	
	public ScanResult scan (String firstKey, String lastKey) throws InterruptedException {
		
		final HeldValue<ScanResult> returnValue = new HeldValue<ScanResult> ();
	
		MetadataTableCallback<ScanResult> cb = new MetadataTableCallback<ScanResult> () {
			public void complete (int rc, ScanResult result) {
				returnValue.setValue (result);
			}
		};

		// make the actual async call
	
		this.table.scan (cb, firstKey, lastKey);
	
		// busy wait
		while ( ! returnValue.isFinished ()) {
		}
	
		return returnValue.getValue ();
	}
	
	
}
