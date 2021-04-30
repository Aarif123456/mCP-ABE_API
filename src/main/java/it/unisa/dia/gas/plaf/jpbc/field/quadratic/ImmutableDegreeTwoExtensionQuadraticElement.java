package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutableDegreeTwoExtensionQuadraticElement<E extends Element> extends DegreeTwoExtensionQuadraticElement<E> {

    public ImmutableDegreeTwoExtensionQuadraticElement(DegreeTwoExtensionQuadraticElement<E> element) {
        super((DegreeTwoExtensionQuadraticField) element.getField());

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
    public DegreeTwoExtensionQuadraticElement square() {
        return (DegreeTwoExtensionQuadraticElement) duplicate().square().getImmutable();
    }

    @Override
    public DegreeTwoExtensionQuadraticElement invert() {
        return (DegreeTwoExtensionQuadraticElement) duplicate().invert().getImmutable();
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
    public DegreeTwoExtensionQuadraticElement mul(Element e) {
        return (DegreeTwoExtensionQuadraticElement) duplicate().mul(e).getImmutable();
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
    public DegreeTwoExtensionQuadraticElement sqrt() {
        return (DegreeTwoExtensionQuadraticElement) duplicate().sqrt().getImmutable();
    }

    @Override
    public QuadraticElement powZn(Element n) {
        return (QuadraticElement) duplicate().powZn(n).getImmutable();
    }

    @Override
    public QuadraticElement setFromHash(byte[] source, int offset, int length) {
        return (QuadraticElement) duplicate().setFromHash(source, offset, length).getImmutable();
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
