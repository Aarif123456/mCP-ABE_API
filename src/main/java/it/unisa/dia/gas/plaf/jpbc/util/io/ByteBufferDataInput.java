package it.unisa.dia.gas.plaf.jpbc.util.io;

import java.io.DataInput;
import java.nio.ByteBuffer;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ByteBufferDataInput implements DataInput {

    private final ByteBuffer byteBuffer;

    public ByteBufferDataInput(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public void readFully(byte[] b) {
        byteBuffer.get(b);
    }

    public void readFully(byte[] b, int off, int len) {
        throw new IllegalStateException();
    }

    public int skipBytes(int n) {
        throw new IllegalStateException();
    }

    public boolean readBoolean() {
        throw new IllegalStateException();
    }

    public byte readByte() {
        return byteBuffer.get();
    }

    public int readUnsignedByte() {
        throw new IllegalStateException();
    }

    public short readShort() {
        throw new IllegalStateException();
    }

    public int readUnsignedShort() {
        throw new IllegalStateException();
    }

    public char readChar() {
        throw new IllegalStateException();
    }

    public int readInt() {
        return byteBuffer.getInt();
    }

    public long readLong() {
        return byteBuffer.getLong();
    }

    public float readFloat() {
        throw new IllegalStateException();
    }

    public double readDouble() {
        throw new IllegalStateException();
    }

    public String readLine() {
        throw new IllegalStateException();
    }

    public String readUTF() {
        throw new IllegalStateException();
    }

}
