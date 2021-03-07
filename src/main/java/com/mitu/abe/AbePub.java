/*
 * 
 */
package com.mitu.abe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class AbePub.
 */
public class AbePub {
	/*
	 * A public key
	 */
	/** The pairing desc. */
	public String pairingDesc;
	
	/** The p. */
	public Pairing p;
	
	/** The g. */
	public Element g; /* G_1 */
	
	/** The g_hat_alpha. */
	public Element g_hat_alpha; /* G_T */
	
	/** The comps. */
	ArrayList<AbeAttrComp> comps; /* AbeAttrComp */
}