package com.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;

import java.io.Serializable;

public class BswabePolynomial implements Serializable {
    int deg;
    /* coefficients from [0] x^0 to [deg] x^deg */
    Element[] coefficients; /* G_T (of length deg+1) */
}
