package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

import it.unisa.dia.gas.plaf.jpbc.util.collection.FlagMap;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ByteBufferLatchSoftRefBigIntegerArraySector extends ByteBufferSoftRefBigIntegerArraySector {

    protected final FlagMap<Integer> flags;


    public ByteBufferLatchSoftRefBigIntegerArraySector(int recordSize, int numRecords) {
        super(recordSize, numRecords);

        flags = new FlagMap<>();
    }

    public ByteBufferLatchSoftRefBigIntegerArraySector(int recordSize, int numRecords, String... labels) {
        super(recordSize, numRecords, labels);

        flags = new FlagMap<>();
    }


    public BigInteger getAt(int index) {
        flags.get(index);

        return super.getAt(index);
    }

    public void setAt(int index, BigInteger value) {
        super.setAt(index, value);

        flags.set(index);
    }

}
