package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutableCurveElement<E extends Element, F extends CurveField> extends CurveElement<E, F> {

    public ImmutableCurveElement(CurveElement<E, F> curveElement) {
        super(curveElement);
        x = (E) curveElement.getX().getImmutable();
        y = (E) curveElement.getY().getImmutable();

        immutable = true;
    }

    public Element getImmutable() {
        return this;
    }

    @Override
    public CurveElement set(Element e) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public CurveElement set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public CurveElement set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public CurveElement twice() {
        return (CurveElement) duplicate().twice().getImmutable();
    }

    @Override
    public CurveElement setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public CurveElement setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public CurveElement setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public CurveElement square() {
        return (CurveElement) duplicate().square().getImmutable();
    }

    @Override
    public CurveElement invert() {
        return (CurveElement) duplicate().invert().getImmutable();
    }

    @Override
    public CurveElement negate() {
        return (CurveElement) duplicate().negate().getImmutable();
    }

    @Override
    public CurveElement add(Element e) {
        return (CurveElement) duplicate().add(e).getImmutable();
    }

    @Override
    public CurveElement mul(Element e) {
        return (CurveElement) duplicate().mul(e).getImmutable();
    }

    @Override
    public CurveElement mul(BigInteger n) {
        return (CurveElement) duplicate().mul(n).getImmutable();
    }

    @Override
    public CurveElement mulZn(Element e) {
        return (CurveElement) duplicate().mulZn(e).getImmutable();
    }

    @Override
    public CurveElement powZn(Element e) {
        return (CurveElement) duplicate().powZn(e).getImmutable();
    }

    @Override
    public CurveElement setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytesCompressed(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytesCompressed(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytesX(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytesX(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public Element pow(BigInteger n) {
        return duplicate().pow(n).getImmutable();
    }

    @Override
    public Element halve() {
        return duplicate().halve().getImmutable();
    }

    @Override
    public Element sub(Element element) {
        return duplicate().sub(element).getImmutable();
    }

    @Override
    public Element div(Element element) {
        return duplicate().div(element).getImmutable();
    }

    @Override
    public Element mul(int z) {
        return duplicate().mul(z).getImmutable();
    }

    @Override
    public Element sqrt() {
        return duplicate().sqrt().getImmutable();
    }

}
