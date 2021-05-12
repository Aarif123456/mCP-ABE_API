package com.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

/*
 * Holds a private key
 */
public class BswabePrv {
    Element d; /* G_2 */
    ArrayList<BswabePrvComp> comps; /* BswabePrvComp */
}