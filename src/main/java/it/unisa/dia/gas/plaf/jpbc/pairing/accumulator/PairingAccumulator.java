package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public interface PairingAccumulator extends Accumulator<Element> {

    void addPairing(Element e1, Element e2);

    PairingAccumulator addPairingInverse(Element e1, Element e2);

    PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2);

}
