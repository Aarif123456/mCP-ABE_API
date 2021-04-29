package it.unisa.dia.gas.plaf.jpbc.util.io;

import java.io.DataOutput;
import java.nio.ByteBuffer;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ByteBufferDataOutput implements DataOutput {

    private final ByteBuffer buffer;

    public ByteBufferDataOutput(ByteBuffer buffer) {
        this.buffer = buffer;
    }


    public void write(int b) {
        throw new IllegalStateException();
    }

    public void write(byte[] b) {
        buffer.put(b);
    }

    public void write(byte[] b, int off, int len) {
        throw new IllegalStateException();
    }

    public void writeBoolean(boolean v) {
        throw new IllegalStateException();
    }

    public void writeByte(int v) {
        throw new IllegalStateException();
    }

    public void writeShort(int v) {
        throw new IllegalStateException();
    }

    public void writeChar(int v) {
        throw new IllegalStateException();
    }

    public void writeInt(int v) {
        buffer.putInt(v);
    }

    public void writeLong(long v) {
        throw new IllegalStateException();
    }

    public void writeFloat(float v) {
        throw new IllegalStateException();
    }

    public void writeDouble(double v) {
        throw new IllegalStateException();
    }

    public void writeBytes(String s) {
        throw new IllegalStateException();
    }

    public void writeChars(String s) {
        throw new IllegalStateException();
    }

    public void writeUTF(String s) {
        throw new IllegalStateException();
    }
}
