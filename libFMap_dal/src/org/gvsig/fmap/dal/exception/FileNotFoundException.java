package org.gvsig.fmap.dal.exception;

import java.io.File;

public class FileNotFoundException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 8748874874719052534L;
	private final static String MESSAGE_FORMAT = "File '%(file)' not found.";
	private final static String MESSAGE_KEY = "_OpenException";

	public FileNotFoundException(String filePath) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("file", filePath);
	}

	public FileNotFoundException(File file) {
		this(file.getAbsolutePath());
	}

}
