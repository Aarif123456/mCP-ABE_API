package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.io.DataInputStream;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class FieldStreamReader {

    private final Field field;
    private final byte[] buffer;
    private final int offset;
    private final DataInputStream dis;
    private final ExByteArrayInputStream bais;
    private int cursor;


    public FieldStreamReader(Field field, byte[] buffer, int offset) {
        this.field = field;
        this.buffer = buffer;
        this.offset = offset;

        cursor = offset;

        bais = new ExByteArrayInputStream(buffer, offset, buffer.length - offset);
        dis = new DataInputStream(bais);
    }


    public void reset() {
        cursor = offset;
    }

    public Element readElement() {
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
