package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutableQuadraticElement<E extends Element> extends QuadraticElement<E> {

    public ImmutableQuadraticElement(QuadraticElement<E> element) {
        super(element.getField());

        x = (E) element.getX().getImmutable();
        y = (E) element.getY().getImmutable();

        immutable = true;
    }

    @Override
    public Element getImmutable() {
        return this;
    }

    @Override
    public QuadraticElement set(Element e) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public QuadraticElement set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public QuadraticElement set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public QuadraticElement setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public QuadraticElement setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public QuadraticElement setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public QuadraticElement twice() {
        return (QuadraticElement) duplicate().twice().getImmutable();
    }

    @Override
    public QuadraticElement mul(int z) {
        return (QuadraticElement) duplicate().mul(z).getImmutable();
    }

    @Override
    public QuadraticElement square() {
        return (QuadraticElement) duplicate().square().getImmutable();
    }

    @Override
    public QuadraticElement invert() {
        return (QuadraticElement) duplicate().invert().getImmutable();
    }

    @Override
    public QuadraticElement negate() {
        return (QuadraticElement) duplicate().negate().getImmutable();
    }

    @Override
    public QuadraticElement add(Element e) {
        return (QuadraticElement) duplicate().add(e).getImmutable();
    }

    @Override
    public QuadraticElement sub(Element e) {
        return (QuadraticElement) duplicate().sub(e).getImmutable();
    }

    @Override
    public QuadraticElement mul(Element e) {
        return (QuadraticElement) duplicate().mul(e).getImmutable();
    }

    @Override
    public QuadraticElement mul(BigInteger n) {
        return (QuadraticElement) duplicate().mul(n).getImmutable();
    }

    @Override
    public QuadraticElement mulZn(Element e) {
        return (QuadraticElement) duplicate().mulZn(e).getImmutable();
    }

    @Override
    public QuadraticElement sqrt() {
        return (QuadraticElement) duplicate().sqrt().getImmutable();
    }

    @Override
    public QuadraticElement powZn(Element n) {
        return (QuadraticElement) duplicate().powZn(n).getImmutable();
    }

    @Override
    public QuadraticElement setFromHash(byte[] source, int offset, int length) {
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
    public Element div(Element element) {
        return duplicate().div(element).getImmutable();
    }

}
