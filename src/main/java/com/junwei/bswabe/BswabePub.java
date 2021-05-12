package com.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class BswabePub {
    /*
     * The pairing description.
     */
    public String pairingDesc;
    /* The class used to handle the pairing */
    public Pairing p;
    public Element g;                /* G_1 */
    public Element h;                /* G_1 */
    public Element f;                /* G_1 */
    public Element gp;            /* G_2 */
    public Element g_hat_alpha;    /* G_T */
}
