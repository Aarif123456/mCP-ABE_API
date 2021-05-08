package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ByteBufferSoftRefBigIntegerArraySector extends ByteBufferBigIntegerArraySector {

    protected final Map<Integer, SoftReference<BigInteger>> cache;


    public ByteBufferSoftRefBigIntegerArraySector(int recordSize, int numRecords) {
        super(recordSize, numRecords);

        cache = new ConcurrentHashMap<>();
    }

    public ByteBufferSoftRefBigIntegerArraySector(int recordSize, int numRecords, String... labels) {
        super(recordSize, numRecords, labels);

        cache = new ConcurrentHashMap<>();
    }


    public synchronized BigInteger getAt(int index) {
        BigInteger result = null;
        SoftReference<BigInteger> sr = cache.get(index);

        if (sr != null)
            result = sr.get();

        if (result == null) {
            result = super.getAt(index);
            cache.put(index, new SoftReference<>(result));
        }

        return result;
    }

    public synchronized void setAt(int index, BigInteger value) {
        cache.put(index, new SoftReference<>(value));

        super.setAt(index, value);
    }

}
