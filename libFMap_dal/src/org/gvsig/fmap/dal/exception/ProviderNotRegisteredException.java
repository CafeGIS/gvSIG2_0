package org.gvsig.fmap.dal.exception;

public class ProviderNotRegisteredException extends DataException {
	/**
	 *
	 */
	private static final long serialVersionUID = 4873697202452898968L;
	private final static String MESSAGE_FORMAT = "'%(providerName) not registered'.";
	private final static String MESSAGE_KEY = "_ProviderNotRegisteredException";

	public ProviderNotRegisteredException(String providerName) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("providerName", providerName);
	}
}
