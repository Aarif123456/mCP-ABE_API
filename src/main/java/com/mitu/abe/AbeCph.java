package com.mitu.abe;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

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
    public Element c1; /* G_T */
    public Element c0; /* G_1 */
    public AbePolicy p;
    public ArrayList<AbeCjiComp> cjis; /* AbeCjiComp */
}