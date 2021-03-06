package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

import it.unisa.dia.gas.plaf.jpbc.util.io.ByteBufferDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.ByteBufferDataOutput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataOutput;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ByteBufferBigIntegerArraySector implements ArraySector<BigInteger> {

    protected final int offset;
    protected final int recordSize;
    protected final int recordLength;
    protected final int numRecords;
    protected final int lengthInBytes;
    protected ByteBuffer buffer;
    protected PairingDataInput in;
    protected PairingDataOutput out;

    protected Map<String, Integer> labelsMap;


    public ByteBufferBigIntegerArraySector(int recordSize, int numRecords) {
        lengthInBytes = 4 + ((recordSize + 4) * numRecords);

        offset = 4;
        this.recordSize = recordSize;
        recordLength = recordSize + 4;
        this.numRecords = numRecords;
    }

    public ByteBufferBigIntegerArraySector(int recordSize, int numRecords, String... labels) {
        this(recordSize, numRecords);

        labelsMap = new HashMap<>(labels.length);
        for (int i = 0; i < labels.length; i++) {
            labelsMap.put(labels[i], i);
        }
    }


    public int getLengthInBytes() {
        return lengthInBytes;
    }

    public int getSize() {
        return numRecords;
    }

    public synchronized ArraySector<BigInteger> mapTo(Mode mode, ByteBuffer buffer) {
        this.buffer = buffer;
        in = new PairingDataInput(new ByteBufferDataInput(buffer));
        out = new PairingDataOutput(new ByteBufferDataOutput(buffer));

        switch (mode) {
            case INIT:
                try {
                    out.writeInt(numRecords);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case READ:
                break;
            default:
                throw new IllegalStateException("Invalid mode!");
        }

        return this;
    }

    public synchronized BigInteger getAt(int index) {
        try {
            buffer.position(offset + (index * recordLength));
            return in.readBigInteger();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void setAt(int index, BigInteger value) {
        try {
            buffer.position(offset + (index * recordLength));
            out.writeBigInteger(value, recordSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BigInteger getAt(String label) {
        if (labelsMap == null)
            throw new IllegalStateException();

        return getAt(labelsMap.get(label));
    }

    public void setAt(String label, BigInteger value) {
        if (labelsMap == null)
            throw new IllegalStateException();

        setAt(labelsMap.get(label), value);
    }
}
