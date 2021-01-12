package com.noahhusby.sledgehammer.addons.terramap.network;

/**
 * Thrown when failing to encode a packet
 * 
 * @author SmylerMC
 *
 */
public class PacketEncodingException extends Exception {

	public PacketEncodingException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 292700699985519227L;

}
