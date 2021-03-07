/*
 * 
 */
package com.mitu.cpabe;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Common {
	
	/**
	 * Suck file.
	 *
	 * @param inputfile the inputfile
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */

	public static byte[] suckFile(String inputfile) throws IOException {
		InputStream is = new FileInputStream(inputfile);
		int size = is.available();
		byte[] content = new byte[size];
		is.read(content);
		is.close();
		return content;
	}

	/* write byte[] into outputfile */
	/**
	 * Spit file.
	 *
	 * @param outputfile the outputfile
	 * @param b the b
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void spitFile(String outputfile, byte[] b) throws IOException {
		PrintWriter p = new PrintWriter(outputfile);
		p.close();
		OutputStream os = new FileOutputStream(outputfile);
		os.write(b);
		os.close();
	}

	/**
	 * Write cpabe file. - The reason for making a file before uploading is to allow upload of files of an
	 * arbitrary size. Instead, of using a stringBuilder to create a string then uploading the resulting string
	 *
	 * @param cphBuf the cph buf
	 * @param aesBuf the aes buf
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeCpabeFile(String fileName, String userID,
			byte[] cphBuf, byte[] aesBuf) throws IOException {
		
		ByteArrayOutputStream os = writeCpabeData(cphBuf,  aesBuf);
		var fs = new FileOutputStream(fileName); // ** WILL REMOVE ONCE AWS WORKS **
		System.err.println("FILENAME:"+fileName);
		fs.write(os.toByteArray()); // ** WILL REMOVE ONCE AWS WORKS *
	}

	/**
	 * Read cpabe file.
	 *
	 * @param encfile the encfile
	 * @return the byte[][]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[][] readCpabeFile(String encfile, String userID) throws IOException {
		int i, len;
		// InputStream is = getFileFromAWS(encfile, userID);
		InputStream is = new FileInputStream(encfile);
		return readCpabeData(is);
	}
	
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
		// /* write m_buf */
		// for (i = 3; i >= 0; i--)
		// 	os.write(((mBuf.length & (0xff << 8 * i)) >> 8 * i));
		// os.write(mBuf);

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
		byte[]  aesBuf, cphBuf; //mBuf,

		// /* read m buf */
		// len = 0;
		// for (i = 3; i >= 0; i--)
		// 	len |= is.read() << (i * 8);
		// mBuf = new byte[len];
		// is.read(mBuf);
		/* read aes buf */
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
		// res[2] = mBuf;
		return res;
	}
}