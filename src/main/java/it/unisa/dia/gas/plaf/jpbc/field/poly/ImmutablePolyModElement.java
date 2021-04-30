package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutablePolyModElement<E extends Element> extends PolyModElement<E> {

    public ImmutablePolyModElement(PolyModElement<E> element) {
        super(element.getField());

        coefficients.clear();
        for (int i = 0; i < field.n; i++) {
            coefficients.add((E) element.getCoefficient(i).getImmutable());
        }
        immutable = true;
    }

    @Override
    public Element getImmutable() {
        return this;
    }

    @Override
    public PolyModElement<E> set(Element e) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PolyModElement<E> set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PolyModElement<E> set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PolyModElement<E> setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PolyModElement<E> setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PolyModElement<E> setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PolyModElement<E> setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PolyModElement<E> map(Element e) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PolyModElement<E> twice() {
        return (PolyModElement<E>) duplicate().twice().getImmutable();
    }

    @Override
    public PolyModElement<E> square() {
        return (PolyModElement<E>) duplicate().square().getImmutable();
    }

    @Override
    public PolyModElement<E> invert() {
        return (PolyModElement<E>) duplicate().invert().getImmutable();
    }

    @Override
    public PolyModElement<E> negate() {
        return (PolyModElement<E>) duplicate().negate().getImmutable();
    }

    @Override
    public PolyModElement<E> add(Element e) {
        return (PolyModElement<E>) duplicate().add(e).getImmutable();
    }

    @Override
    public PolyModElement<E> sub(Element e) {
        return (PolyModElement<E>) duplicate().sub(e).getImmutable();
    }

    @Override
    public PolyModElement<E> mul(Element e) {
        return (PolyModElement<E>) duplicate().mul(e).getImmutable();
    }

    @Override
    public PolyModElement<E> mul(int z) {
        return (PolyModElement<E>) duplicate().mul(z).getImmutable();
    }

    @Override
    public PolyModElement<E> mul(BigInteger n) {
        return (PolyModElement<E>) duplicate().mul(n).getImmutable();
    }

    @Override
    public Element pow(BigInteger n) {
        return duplicate().pow(n).getImmutable();
    }

    @Override
    public PolyModElement<E> powZn(Element e) {
        return (PolyModElement<E>) duplicate().powZn(e).getImmutable();
    }

    @Override
    public PolyModElement<E> sqrt() {
        return (PolyModElement<E>) duplicate().sqrt().getImmutable();
    }

    @Override
    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public Element halve() {
        return duplicate().halve().getImmutable();
    }

    @Override
    public Element div(Element element) {
        return duplicate().div(element).getImmutable();
    }

    @Override
    public Element mulZn(Element z) {
        return duplicate().mulZn(z).getImmutable();
    }

}
