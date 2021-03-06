package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.io.DataInputStream;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class PairingStreamReader {

    private final Pairing pairing;
    private final byte[] buffer;
    private final int offset;
    private final DataInputStream dis;
    private final ExByteArrayInputStream bais;
    private int cursor;


    public PairingStreamReader(Pairing pairing, byte[] buffer, int offset) {
        this.pairing = pairing;
        this.buffer = buffer;
        this.offset = offset;

        cursor = offset;

        bais = new ExByteArrayInputStream(buffer, offset, buffer.length - offset);
        dis = new DataInputStream(bais);
    }


    public void reset() {
        cursor = offset;
    }

    public Element[] readElements(int... ids) {
        Element[] elements = new Element[ids.length];

        for (int i = 0; i < ids.length; i++) {
            Field field = pairing.getFieldAt(ids[i]);
            elements[i] = field.newElementFromBytes(buffer, cursor);
            jump(field.getLengthInBytes(elements[i]));
        }

        return elements;
    }

    public Element[] readElements(int id, int count) {
        Element[] elements = new Element[count];

        Field field = pairing.getFieldAt(id);
        for (int i = 0; i < count; i++) {
            elements[i] = field.newElementFromBytes(buffer, cursor);
            jump(field.getLengthInBytes(elements[i]));
        }

        return elements;
    }

    public Element[] readG1Elements(int count) {
        return readElements(1, count);
    }


    public Element readG1Element() {
        Element element = pairing.getG1().newElementFromBytes(buffer, cursor);
        jump(pairing.getG1().getLengthInBytes(element));

        return element;
    }

    public Element readGTElement() {
        Element element = pairing.getGT().newElementFromBytes(buffer, cursor);
        jump(pairing.getGT().getLengthInBytes(element));
        return element;
    }

    public Element readFieldElement(Field field) {
        Element element = field.newElementFromBytes(buffer, cursor);
        jump(field.getLengthInBytes(element));
        return element;
    }

    public String readString() {
        try {
            return dis.readUTF();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            cursor = bais.getPos();
        }
    }

    public int readInt() {
        try {
            return dis.readInt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            cursor = bais.getPos();
        }
    }


    private void jump(int length) {
        cursor += length;
        bais.skip(length);
    }

}
