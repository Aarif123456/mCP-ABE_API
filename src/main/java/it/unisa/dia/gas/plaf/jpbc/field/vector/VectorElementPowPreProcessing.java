package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class VectorElementPowPreProcessing implements ElementPowPreProcessing {
    protected final VectorField field;
    protected final ElementPowPreProcessing[] processings;

    public VectorElementPowPreProcessing(VectorElement vector) {
        field = vector.getField();
        processings = new ElementPowPreProcessing[vector.getSize()];
        for (int i = 0; i < processings.length; i++) {
            processings[i] = vector.getAt(i).getElementPowPreProcessing();
        }
    }

    public Element pow(BigInteger n) {
        List<Element> coeff = new ArrayList<>(processings.length);
        for (ElementPowPreProcessing processing : processings) {
            coeff.add(processing.pow(n));
        }
        return new VectorElement(field, coeff);
    }

    public Element powZn(Element n) {
        List<Element> coeff = new ArrayList<>(processings.length);
        for (ElementPowPreProcessing processing : processings) {
            coeff.add(processing.powZn(n));
        }
        return new VectorElement(field, coeff);
    }

    public byte[] toBytes() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public Field getField() {
        return field;
    }
}
