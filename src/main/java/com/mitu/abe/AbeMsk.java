package com.mitu.abe;

import it.unisa.dia.gas.jpbc.Element;
import java.util.ArrayList;
/*
 * A master secret key
 */
public class AbeMsk {
	public Element alpha; /* Z_r */
	ArrayList<AbeAttrCompMsk> tjs; /* Z_r */
}