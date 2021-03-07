/*
 * 
 */
package com.mitu.abe;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class AbeCph.
 */
public class AbeCph {
	/*
	 * A ciphertext. Note that this library only handles encrypting a single
	 * group element, so if you want to encrypt something bigger, you will have
	 * to use that group element as a symmetric key for hybrid encryption (which
	 * you do yourself).
	 */
	/** The c1. */
	public Element c1; /* G_T */
	
	/** The c0. */
	public Element c0; /* G_1 */
	
	/** The p. */
	public AbePolicy p;
	
	/** The cjis. */
	public ArrayList<AbeCjiComp> cjis; /* AbeCjiComp */
}