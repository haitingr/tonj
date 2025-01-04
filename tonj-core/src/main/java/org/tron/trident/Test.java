package org.tron.trident;

import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.proto.Response.TransactionExtention;

public class Test {

	public static void main(String[] args) throws IllegalException {
		ApiWrapper wrapper=ApiWrapper.ofNile("");
		TransactionExtention te=wrapper.transfer("TZ4UXDV5ZhNW7fb2AMSbgfAEZ7hWsnYS2g", "TPswDDCAWhJAZGdHPidFg5nEf8TkNToDX1", 1000L);
		System.out.println(te);
	}

}
