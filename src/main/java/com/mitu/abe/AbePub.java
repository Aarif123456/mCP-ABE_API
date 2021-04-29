package com.mitu.abe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.ArrayList;

public class AbePub {
    /** The pairing description. */
    public String pairingDesc;
    /* The class used to handle the pairing */
    public Pairing p;

    /* G_1 */
    public Element g; 
    /* G_T */
    public Element g_hat_alpha; 
    
    ArrayList<AbeAttrComp> comps;
}