package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.AbstractAccumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public abstract class AbstractPairingAccumulator extends AbstractAccumulator<Element> implements PairingAccumulator {

    protected final Pairing pairing;


    public AbstractPairingAccumulator(Pairing pairing) {
        this(pairing, pairing.getGT().newOneElement());
    }

    public AbstractPairingAccumulator(Pairing pairing, Element value) {
        this.pairing = pairing;
        result = value;
    }


    public Accumulator<Element> accumulate(Callable<Element> callable) {
        throw new IllegalStateException("Invalid call method!");
    }

    public void addPairing(Element e1, Element e2) {
        super.accumulate(() -> pairing.pairing(e1, e2));

    }

    public PairingAccumulator addPairingInverse(Element e1, Element e2) {
        super.accumulate(() -> pairing.pairing(e1, e2).invert());

        return this;
    }

    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2) {
        super.accumulate(() -> pairingPreProcessing.pairing(e2));

        return this;
    }

}
