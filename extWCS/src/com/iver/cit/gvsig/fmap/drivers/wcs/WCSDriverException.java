package com.iver.cit.gvsig.fmap.drivers.wcs;

import org.gvsig.fmap.dal.exception.ReadException;


public class WCSDriverException extends ReadException {

	public WCSDriverException(String l,Throwable exception) {
		super(l,exception);
		init();
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_driver_layer";
		formatString = "Can´t load driver of the layer: %(layer) ";
	}
	 /**
     * Cuts the message text to force its lines to be shorter or equal to
     * lineLength.
     * @param message, the message.
     * @param lineLength, the max line length in number of characters.
     * @return the formated message.
     */
    private static String format(String message, int lineLength){
        if (message.length() <= lineLength) {
			return message;
		}
        String[] lines = message.split("\n");
        String theMessage = "";
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.length()<lineLength) {
				theMessage += line+"\n";
			} else {
                String[] chunks = line.split(" ");
                String newLine = "";
                for (int j = 0; j < chunks.length; j++) {
                    int currentLength = newLine.length();
                    chunks[j] = chunks[j].trim();
                    if (chunks[j].length()==0) {
						continue;
					}
                    if ((currentLength + chunks[j].length() + " ".length()) <= lineLength) {
						newLine += chunks[j] + " ";
					} else {
                        newLine += "\n"+chunks[j]+" ";
                        theMessage += newLine;
                        newLine = "";
                    }
                }

            }
        }
        return theMessage;
    }
}
