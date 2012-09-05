package org.gvsig.fmap.dal.exception;


public class UnsupportedVersionException extends DataException {
	/**
	 *
	 */
	private static final long serialVersionUID = 4614439562201218739L;
	private final static String MESSAGE_FORMAT = "Version '%(formatVersion) of '%(storeName)' not supported'.";
	private final static String MESSAGE_KEY = "_UnsupportedVersionException";

	public UnsupportedVersionException(String storeName, String formatVersion) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("storeName", storeName);
		setValue("formatVersion", formatVersion);
	}
}
