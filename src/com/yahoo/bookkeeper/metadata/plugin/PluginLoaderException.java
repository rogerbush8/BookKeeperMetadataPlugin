package com.yahoo.bookkeeper.metadata.plugin;

public class PluginLoaderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PluginLoaderException (String message, Throwable e) {
		super (message, e);
	}
}
