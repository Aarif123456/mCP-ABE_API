package com.mitu.cpabe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Common {

    /**
     * Return a ByteArrayOutputStream instead of writing to a file.
     *
     * @param cphBuf the cph buf
     * @param aesBuf the aes buf
     * @return the byte array output stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static ByteArrayOutputStream writeCpabeData(byte[] cphBuf,
                                                       byte[] aesBuf) throws IOException {
        int i;
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        /* write aes_buf */
        for (i = 3; i >= 0; i--)
            os.write(((aesBuf.length & (0xff << 8 * i)) >> 8 * i));
        os.write(aesBuf);

        /* write cph_buf */
        for (i = 3; i >= 0; i--)
            os.write(((cphBuf.length & (0xff << 8 * i)) >> 8 * i));
        os.write(cphBuf);

        os.close();
        return os;
    }

    /**
     * Read data from an InputStream instead of taking it from a file.
     *
     * @param is the is
     * @return the byte[][]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static byte[][] readCpabeData(InputStream is) throws IOException {
        int i, len;

        byte[][] res = new byte[2][];
        byte[] aesBuf, cphBuf;

        len = 0;
        for (i = 3; i >= 0; i--)
            len |= is.read() << (i * 8);
        aesBuf = new byte[len];
        is.read(aesBuf);

        /* read cph buf */
        len = 0;
        for (i = 3; i >= 0; i--)
            len |= is.read() << (i * 8);
        cphBuf = new byte[len];
        is.read(cphBuf);

        is.close();
        res[0] = aesBuf;
        res[1] = cphBuf;
        return res;
    }
}