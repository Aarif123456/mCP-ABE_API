package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.Pool;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ProductPairingAccumulator implements PairingAccumulator {

    private final Pairing pairing;
    private final Element[] in1;
    private final Element[] in2;
    private int cursor;
    private Element result;


    public ProductPairingAccumulator(Pairing pairing, int n) {
        this.pairing = pairing;
        in1 = new Element[n];
        in2 = new Element[n];
        cursor = 0;
    }


    public Accumulator<Element> accumulate(Callable<Element> callable) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Accumulator<Element> awaitTermination() {
        awaitResult();

        return this;
    }

    public Element getResult() {
        return result;
    }

    public Pool<Element> submit(Callable<Element> callable) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Pool<Element> submit(Runnable runnable) {
        throw new IllegalStateException("Not supported!!!");
    }

    public void addPairing(Element e1, Element e2) {
        in1[cursor] = e1;
        in2[cursor++] = e2;

    }

    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2) {
        throw new IllegalStateException("Not supported!!!");
    }

    public PairingAccumulator addPairingInverse(Element e1, Element e2) {
        throw new IllegalStateException("Not supported!!!");
    }

    public Element awaitResult() {
        return (result = pairing.pairing(in1, in2));
    }
}
