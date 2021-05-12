package com.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;

import java.io.Serializable;
import java.util.ArrayList;


public class BswabePolicy implements Serializable {
    /* serialized */

    /* k=1 if leaf, otherwise threshold */
    int k;
    /* attribute string if leaf, otherwise null */
    String attr;
    Element c;            /* G_1 only for leaves */
    Element cp;        /* G_1 only for leaves */
    /* array of BswabePolicy and length is 0 for leaves */
    BswabePolicy[] children;

    /* only used during encryption */
    BswabePolynomial q;

    /* only used during decryption */
    boolean satisfiable;
    int min_leaves;
    int attributes;
    ArrayList<Integer> satl = new ArrayList<>();
}
