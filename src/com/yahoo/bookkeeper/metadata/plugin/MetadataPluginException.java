package com.yahoo.bookkeeper.metadata.plugin;

public class MetadataPluginException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetadataPluginException (String message, Throwable e) {
		super (message, e);
	}
}
