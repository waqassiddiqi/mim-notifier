package mim.renewal.util;

import org.apache.log4j.Logger;

public class UsersNotChargedLogger {
	private Logger log = Logger.getLogger(getClass().getName());
	
	public void addEntry(String msisdn) {
		log.info(msisdn);
	}
}
