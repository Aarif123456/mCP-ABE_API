/*
 * 
 */
package com.mitu.abe;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.File;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc

/**
 * The Class SerializeUtils.
 */
public class SerializeUtils {

	/* Method has been test okay */

    /**
     * Serialize element.
     *
     * @param arrlist the arrlist
     * @param e       the e
     */
    public static void serializeElement(ArrayList<Byte> arrlist, Element e) {
        byte[] arr_e = e.toBytes();
        serializeUint32(arrlist, arr_e.length);
        byteArrListAppend(arrlist, arr_e);
    }

	/* Method has been test okay */

    /**
     * Unserialize element.
     *
     * @param arr    the arr
     * @param offset the offset
     * @param e      the e
     * @return the int
     */
    public static int unserializeElement(byte[] arr, int offset, Element e) {
        int len;
        int i;
        byte[] e_byte;

        len = unserializeUint32(arr, offset);
        e_byte = new byte[len];
        offset += 4;
        for (i = 0; i < len; i++)
            e_byte[i] = arr[offset + i];
        e.setFromBytes(e_byte);

        return offset + len;
    }

    public static void serializeString(ArrayList<Byte> arrlist, String s) {
        byte[] b = s.getBytes();
        serializeUint32(arrlist, b.length);
        byteArrListAppend(arrlist, b);
    }

	/*
     * Usage:
	 * 
	 * StringBuffer sb = new StringBuffer("");
	 * 
	 * offset = unserializeString(arr, offset, sb);
	 * 
	 * String str = sb.substring(0);
	 */
    public static int unserializeString(byte[] arr, int offset, StringBuffer sb) {
        /*int i;
        int len;
		byte[] str_byte;

		len = unserializeUint32(arr, offset);
		offset += 4;
		str_byte = new byte[len];
		for (i = 0; i < len; i++)
			str_byte[i] = arr[offset + i];

		sb.append(new String(str_byte));
		return offset + len;*/

        int i;
        int len;
        byte[] str_byte;
        StringBuilder s = new StringBuilder();

        len = unserializeUint32(arr, offset);
        offset += 4;

        for (i = 0; i < len; i++)
            s.append(new String(new byte[]{arr[offset + i]}));

        sb.append(s);

        return offset + len;
    }

    /**
     * Serialize bswabe pub.
     */

    private static int rBits = 160;
    private static int qBits = 512;

    public static byte[] serializeBswabePub(AbePub pub) {

        ArrayList<Byte> arrlist = new ArrayList<>();
        int pubCompsLen, i;

        serializeString(arrlist, pub.pairingDesc);
        serializeElement(arrlist, pub.g);

        serializeElement(arrlist, pub.g_hat_alpha);

        pubCompsLen = pub.comps.size();

        serializeUint32(arrlist, pubCompsLen);

        for (i = 0; i < pubCompsLen; i++) {
            serializeString(arrlist, pub.comps.get(i).attr);
            serializeElement(arrlist, pub.comps.get(i).Tj);
        }

        return Byte_arr2byte_arr(arrlist);
    }

    /**
     * Unserialize bswabe pub.
     *
     * @param b the b
     * @return the abe pub
     */
    public static AbePub unserializeBswabePub(byte[] b, String propertyLocation) {
        AbePub pub;
        int offset, i, len;

        pub = new AbePub();
        offset = 0;

        StringBuffer sb = new StringBuffer();
        offset = unserializeString(b, offset, sb);
        pub.pairingDesc = sb.substring(0);


        /*// JPBC Type A pairing generator...
        PairingParametersGenerator generator = new TypeACurveGenerator(rBits, qBits);
        PairingParameters parameters = generator.generate();

        pub.p = PairingFactory.getPairing(parameters);
        Pairing pairing = pub.p;*/
//        File file = new File(PhrActivity.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "a.properties");
        File file= new File(propertyLocation);
        pub.p = PairingFactory.getPairing(file.getAbsolutePath());
        Pairing pairing = pub.p;

        pub.g = pairing.getG1().newElement();

        pub.g_hat_alpha = pairing.getGT().newElement();

        offset = unserializeElement(b, offset, pub.g);

        offset = unserializeElement(b, offset, pub.g_hat_alpha);

        pub.comps = new ArrayList<>();
        len = unserializeUint32(b, offset);
        offset += 4;

        for (i = 0; i < len; i++) {
            AbeAttrComp c = new AbeAttrComp();

            StringBuffer sb1 = new StringBuffer();
            offset = unserializeString(b, offset, sb1);
            c.attr = sb1.substring(0);

            c.Tj = pairing.getG1().newElement();

            offset = unserializeElement(b, offset, c.Tj);

            pub.comps.add(c);
        }

        return pub;
    }

	/* Method has been test okay */

    /**
     * Serialize bswabe msk.
     *
     * @param msk the master key
     * @return the byte[]
     */
    public static byte[] serializeBswabeMsk(AbeMsk msk) {
        ArrayList<Byte> arrlist = new ArrayList<>();
        int mskCompsLen, i;

        serializeElement(arrlist, msk.alpha);

        mskCompsLen = msk.tjs.size();

        serializeUint32(arrlist, mskCompsLen);

        for (i = 0; i < mskCompsLen; i++) {
            serializeString(arrlist, msk.tjs.get(i).attr);
            serializeElement(arrlist, msk.tjs.get(i).tj);
        }

        return Byte_arr2byte_arr(arrlist);
    }

    public static AbeMsk unserializeBswabeMsk(AbePub pub, byte[] b) {
        int offset = 0, i, len;
        AbeMsk msk = new AbeMsk();

        msk.alpha = pub.p.getZr().newElement();

        offset = unserializeElement(b, offset, msk.alpha);

        msk.tjs = new ArrayList<>();
        len = unserializeUint32(b, offset);
        offset += 4;

        for (i = 0; i < len; i++) {
            AbeAttrCompMsk cMsk = new AbeAttrCompMsk();

            StringBuffer sb1 = new StringBuffer();
            offset = unserializeString(b, offset, sb1);
            cMsk.attr = sb1.substring(0);

            cMsk.tj = pub.p.getZr().newElement();

            offset = unserializeElement(b, offset, cMsk.tj);

            msk.tjs.add(cMsk);
        }

        return msk;
    }

    public static byte[] serializeBswabePrvPart1(AbePrvPart1 prv) {
        ArrayList<Byte> arrlist;
        int prvCompsLen, i;

        arrlist = new ArrayList<>();
        prvCompsLen = prv.comps1.size();

        serializeUint32(arrlist, prvCompsLen);

        for (i = 0; i < prvCompsLen; i++) {
            serializeString(arrlist, prv.comps1.get(i).attr);
            serializeElement(arrlist, prv.comps1.get(i).dj1);
        }

        return Byte_arr2byte_arr(arrlist);
    }

    public static byte[] serializeBswabePrvPart2(AbePrvPart2 prv) {
        ArrayList<Byte> arrlist;
        int prvCompsLen, i;

        arrlist = new ArrayList<>();
        prvCompsLen = prv.comps2.size();
        serializeElement(arrlist, prv.d0);
        serializeUint32(arrlist, prvCompsLen);

        for (i = 0; i < prvCompsLen; i++) {
            serializeString(arrlist, prv.comps2.get(i).attr);
            serializeElement(arrlist, prv.comps2.get(i).dj2);
        }

        return Byte_arr2byte_arr(arrlist);
    }

    public static byte[] serializeBswabeMDec(AbeMDec mDec) {
        ArrayList<Byte> arrlist;
        int mDecAttrListLen, i;

        arrlist = new ArrayList<>();
        mDecAttrListLen = mDec.attrs.size();
        serializeElement(arrlist, mDec.c_t_hat);
        serializeUint32(arrlist, mDecAttrListLen);

        for (i = 0; i < mDecAttrListLen; i++) {
            serializeString(arrlist, mDec.attrs.get(i));
        }

        return Byte_arr2byte_arr(arrlist);
    }

    public static AbeMDec unserializeBswabeMDec(AbePub pub, byte[] b) {

        AbeMDec mDec;

        int i, offset, len;

        mDec = new AbeMDec();

        offset = 0;

        mDec.c_t_hat = pub.p.getGT().newElement();
        offset = unserializeElement(b, offset, mDec.c_t_hat);

        mDec.attrs = new ArrayList<>();
        len = unserializeUint32(b, offset);
        offset += 4;

        for (i = 0; i < len; i++) {
            String attr;

            StringBuffer sb2 = new StringBuffer();
            offset = unserializeString(b, offset, sb2);
            attr = sb2.substring(0);

            mDec.attrs.add(attr);
        }

        return mDec;
    }

    public static AbePrvPart2 unserializeBswabePrvPart2(AbePub pub, byte[] b) {

        AbePrvPart2 prvPart2;

        int i, offset, len;

        prvPart2 = new AbePrvPart2();

        offset = 0;

        prvPart2.d0 = pub.p.getG1().newElement();
        offset = unserializeElement(b, offset, prvPart2.d0);

        prvPart2.comps2 = new ArrayList<>();
        len = unserializeUint32(b, offset);
        offset += 4;

        for (i = 0; i < len; i++) {
            AbePrvCompPart2 compPart2 = new AbePrvCompPart2();

            StringBuffer sb2 = new StringBuffer();
            offset = unserializeString(b, offset, sb2);
            compPart2.attr = sb2.substring(0);

            compPart2.dj2 = pub.p.getG1().newElement();

            offset = unserializeElement(b, offset, compPart2.dj2);

            prvPart2.comps2.add(compPart2);
        }

        return prvPart2;
    }

    public static AbePrvPart1 unserializeBswabePrvPart1(AbePub pub, byte[] b) {

        AbePrvPart1 prvPart1;

        int i, offset, len;

        prvPart1 = new AbePrvPart1();

        offset = 0;

        prvPart1.comps1 = new ArrayList<>();
        len = unserializeUint32(b, offset);
        offset += 4;

        for (i = 0; i < len; i++) {
            AbePrvCompPart1 compPart1 = new AbePrvCompPart1();

            StringBuffer sb1 = new StringBuffer();
            offset = unserializeString(b, offset, sb1);
            compPart1.attr = sb1.substring(0);

            compPart1.dj1 = pub.p.getG1().newElement();

            offset = unserializeElement(b, offset, compPart1.dj1);

            prvPart1.comps1.add(compPart1);
        }

        return prvPart1;
    }

    public static byte[] bswabeCphSerialize(AbeCph cph) {
        ArrayList<Byte> arrlist = new ArrayList<>();
        int cphCompsLen, i;

        SerializeUtils.serializeElement(arrlist, cph.c1);
        SerializeUtils.serializeElement(arrlist, cph.c0);
        SerializeUtils.serializePolicy(arrlist, cph.p);

        cphCompsLen = cph.cjis.size();

        serializeUint32(arrlist, cphCompsLen);

        for (i = 0; i < cphCompsLen; i++) {
            serializeString(arrlist, cph.cjis.get(i).attr);
            serializeElement(arrlist, cph.cjis.get(i).cji);
        }

        return Byte_arr2byte_arr(arrlist);
    }

    public static AbeCph bswabeCphUnserialize(AbePub pub, byte[] cphBuf) {
        AbeCph cph = new AbeCph();
        int offset = 0, i, len;
        int[] offset_arr = new int[1];

        cph.c1 = pub.p.getGT().newElement();
        cph.c0 = pub.p.getG1().newElement();

        offset = SerializeUtils.unserializeElement(cphBuf, offset, cph.c1);
        offset = SerializeUtils.unserializeElement(cphBuf, offset, cph.c0);

        offset_arr[0] = offset;
        cph.p = SerializeUtils.unserializePolicy(pub, cphBuf, offset_arr);
        offset = offset_arr[0];

        cph.cjis = new ArrayList<>();
        len = unserializeUint32(cphBuf, offset);
        offset += 4;

        for (i = 0; i < len; i++) {
            AbeCjiComp cjiComp = new AbeCjiComp();

            StringBuffer sb1 = new StringBuffer();
            offset = unserializeString(cphBuf, offset, sb1);
            cjiComp.attr = sb1.substring(0);

            cjiComp.cji = pub.p.getG1().newElement();

            offset = unserializeElement(cphBuf, offset, cjiComp.cji);

            cph.cjis.add(cjiComp);
        }

        return cph;
    }

	/* Method has been test okay */
    /* potential problem: the number to be serialize is less than 2^31 */

    private static void serializeUint32(ArrayList<Byte> arrlist, int k) {
        int i;
        byte b;

        for (i = 3; i >= 0; i--) {
            b = (byte) ((k & (0x000000ff << (i * 8))) >> (i * 8));
            arrlist.add(b);
        }
    }

	/*
     * Usage:
	 * 
	 * You have to do offset+=4 after call this method
	 */
    private static int unserializeUint32(byte[] arr, int offset) {
        int i;
        int r = 0;

        for (i = 3; i >= 0; i--)
            r |= (byte2int(arr[offset++])) << (i * 8);
        return r;
    }


    private static void serializePolicy(ArrayList<Byte> arrlist, AbePolicy p) {
        serializeUint32(arrlist, p.k);

        serializeElement(arrlist, p.si);
        if (p.children == null || p.children.length == 0) {
            serializeUint32(arrlist, 0);
            serializeString(arrlist, p.attr);
            serializeElement(arrlist, p.si);

        } else {
            serializeUint32(arrlist, p.children.length);
            for (int i = 0; i < p.children.length; i++)
                serializePolicy(arrlist, p.children[i]);
        }
    }

    private static AbePolicy unserializePolicy(AbePub pub, byte[] arr, int[] offset) {
        int i;
        int n;
        AbePolicy p = new AbePolicy();
        p.k = unserializeUint32(arr, offset[0]);
        offset[0] += 4;
        p.attr = null;

        p.si = pub.p.getZr().newElement();

        offset[0] = unserializeElement(arr, offset[0], p.si);

		/* children */
        n = unserializeUint32(arr, offset[0]);
        offset[0] += 4;
        if (n == 0) {
            p.children = null;

            StringBuffer sb = new StringBuffer();
            offset[0] = unserializeString(arr, offset[0], sb);
            p.attr = sb.substring(0);

            p.si = pub.p.getZr().newElement();

            offset[0] = unserializeElement(arr, offset[0], p.si);
        } else {
            p.children = new AbePolicy[n];
            for (i = 0; i < n; i++)
                p.children[i] = unserializePolicy(pub, arr, offset);
        }

        return p;
    }

    private static int byte2int(byte b) {
        if (b >= 0)
            return b;
        return (256 + b);
    }

    private static void byteArrListAppend(ArrayList<Byte> arrlist, byte[] b) {
        int len = b.length;
        for (byte value : b) arrlist.add(value);
    }

    public static byte[] Byte_arr2byte_arr(ArrayList<Byte> B) {
        int len = B.size();
        byte[] b = new byte[len];

        for (int i = 0; i < len; i++)
            b[i] = B.get(i);

        return b;
    }

}
