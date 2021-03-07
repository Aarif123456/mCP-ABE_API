/*
 * 
 */
package com.mitu.abe;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class AbePolicy.
 */
public class AbePolicy {
	/* serialized */

	/* k=1 if leaf, otherwise threshold */
	/** The k. */
	int k;
	/* attribute string if leaf, otherwise null */
	/** The attr. */
	String attr;

	/** The si. */
	Element si; /* Z_r */
	/* array of AbePolicy and length is 0 for leaves */
	/** The children. */
	AbePolicy[] children;

	/* only used during decryption */
	/** The satisfiable. */
	boolean satisfiable;

	/** The min_leaves. */
	int min_leaves;

	/** The attributes */
	int attri;

	/** The satl. */
	ArrayList<Integer> satl = new ArrayList<>();
}