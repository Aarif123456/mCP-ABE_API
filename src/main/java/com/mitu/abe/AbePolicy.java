package com.mitu.abe;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

public class AbePolicy {
    /* serialized */

    /* k=1 if leaf, otherwise threshold */
    /**
     * The k.
     */
    int k;
    /* attribute string if leaf, otherwise null */
    String attr;
    Element si; /* Z_r */
    /* array of AbePolicy and length is 0 for leaves */
    AbePolicy[] children;

    /* only used during decryption */
    boolean satisfiable;
    int min_leaves;
    int attributes;
    ArrayList<Integer> satl = new ArrayList<>();
}