package com.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.ArrayList;
import java.util.Map;

public class SerializeUtils {

	public static void serializeElement(ArrayList<? super Byte> arraylist, Element e) {
		byte[] arr_e = e.toBytes();
		serializeUint32(arraylist, arr_e.length);
		byteArrListAppend(arraylist, arr_e);
	}

	public static int unserializeElement(byte[] arr, int offset, Element e) {
		int len;
		int i;
		byte[] e_byte;

		len = unserializeUint32(arr, offset);
		e_byte = new byte[(int) len];
		offset += 4;
		for (i = 0; i < len; i++)
			e_byte[i] = arr[offset + i];
		e.setFromBytes(e_byte);

		return offset + len;
	}

	public static void serializeString(ArrayList<? super Byte> arrayList, String s) {
		byte[] b = s.getBytes();
		serializeUint32(arrayList, b.length);
		byteArrListAppend(arrayList, b);
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

		int i;
		int len;
		byte[] str_byte;

		len = unserializeUint32(arr, offset);
		offset += 4;
		str_byte = new byte[len];
		for (i = 0; i < len; i++)
			str_byte[i] = arr[offset + i];



		sb.append(new String(str_byte));
		return offset + len;
	}

	public static byte[] serializeBswabePub(BswabePub pub) {
		ArrayList<Byte> arrayList = new ArrayList<>();
	
		serializeString(arrayList, pub.pairingDesc);
		serializeElement(arrayList, pub.g);
		serializeElement(arrayList, pub.h);
		serializeElement(arrayList, pub.gp);
		serializeElement(arrayList, pub.g_hat_alpha);
	
		return Byte_arr2byte_arr(arrayList);
	}

	public static BswabePub unserializeBswabePub(byte[] b, Map<String, String> loadMap) {
		BswabePub pub;
		int offset;
	
		pub = new BswabePub();
		offset = 0;
	
		StringBuffer sb = new StringBuffer();
		offset = unserializeString(b, offset, sb);
		pub.pairingDesc = sb.substring(0);
	
		pub.p = PairingFactory.getPairing(loadMap);
		Pairing pairing = pub.p;
	
		pub.g = pairing.getG1().newElement();
		pub.h = pairing.getG1().newElement();
		pub.gp = pairing.getG2().newElement();
		pub.g_hat_alpha = pairing.getGT().newElement();
	
		offset = unserializeElement(b, offset, pub.g);
		offset = unserializeElement(b, offset, pub.h);
		offset = unserializeElement(b, offset, pub.gp);
		offset = unserializeElement(b, offset, pub.g_hat_alpha);
	
		return pub;
	}

	public static byte[] serializeBswabeMsk(BswabeMsk msk) {
		ArrayList<Byte> arrayList = new ArrayList<>();
	
		serializeElement(arrayList, msk.beta);
		serializeElement(arrayList, msk.g_alpha);
	
		return Byte_arr2byte_arr(arrayList);
	}

	public static BswabeMsk unserializeBswabeMsk(BswabePub pub, byte[] b) {
		int offset = 0;
		BswabeMsk msk = new BswabeMsk();
	
		msk.beta = pub.p.getZr().newElement();
		msk.g_alpha = pub.p.getG2().newElement();
	
		offset = unserializeElement(b, offset, msk.beta);
		offset = unserializeElement(b, offset, msk.g_alpha);
	
		return msk;
	}

	public static byte[] serializeBswabePrv(BswabePrv prv) {
		ArrayList<Byte> arrayList;
		int prvCompsLen, i;
	
		arrayList = new ArrayList<>();
		prvCompsLen = prv.comps.size();
		serializeElement(arrayList, prv.d);
		serializeUint32(arrayList, prvCompsLen);
	
		for (i = 0; i < prvCompsLen; i++) {
			serializeString(arrayList, prv.comps.get(i).attr);
			serializeElement(arrayList, prv.comps.get(i).d);
			serializeElement(arrayList, prv.comps.get(i).dp);
		}
	
		return Byte_arr2byte_arr(arrayList);
	}

	public static BswabePrv unserializeBswabePrv(BswabePub pub, byte[] b) {
		BswabePrv prv;
		int i, offset, len;
	
		prv = new BswabePrv();
		offset = 0;
	
		prv.d = pub.p.getG2().newElement();
		offset = unserializeElement(b, offset, prv.d);
	
		prv.comps = new ArrayList<>();
		len = unserializeUint32(b, offset);
		offset += 4;
	
		for (i = 0; i < len; i++) {
			BswabePrvComp c = new BswabePrvComp();
	
			StringBuffer sb = new StringBuffer();
			offset = unserializeString(b, offset, sb);
			c.attr = sb.substring(0);
	
			c.d = pub.p.getG2().newElement();
			c.dp = pub.p.getG2().newElement();
	
			offset = unserializeElement(b, offset, c.d);
			offset = unserializeElement(b, offset, c.dp);
	
			prv.comps.add(c);
		}
	
		return prv;
	}

	public static byte[] bswabeCphSerialize(BswabeCph cph) {
		ArrayList<Byte> arrayList = new ArrayList<>();
		serializeElement(arrayList, cph.cs);
		serializeElement(arrayList, cph.c);
		serializePolicy(arrayList, cph.p);

		return Byte_arr2byte_arr(arrayList);
	}

	public static BswabeCph bswabeCphUnserialize(BswabePub pub, byte[] cphBuf) {
		BswabeCph cph = new BswabeCph();
		int offset = 0;
		int[] offset_arr = new int[1];

		cph.cs = pub.p.getGT().newElement();
		cph.c = pub.p.getG1().newElement();

		offset = unserializeElement(cphBuf, offset, cph.cs);
		offset = unserializeElement(cphBuf, offset, cph.c);

		offset_arr[0] = offset;
		cph.p = unserializePolicy(pub, cphBuf, offset_arr);
		/* new offset is  offset_arr[0] - if you need to add more code */

		return cph;
	}

	/* potential problem: the number to be serialize is less than 2^31 */
	private static void serializeUint32(ArrayList<? super Byte> arrayList, int k) {
		int i;
		byte b;
	
		for (i = 3; i >= 0; i--) {
			b = (byte) ((k & (0x000000ff << (i * 8))) >> (i * 8));
			arrayList.add(b);
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

	private static void serializePolicy(ArrayList<? super Byte> arrayList, BswabePolicy p) {
		serializeUint32(arrayList, p.k);
	
		if (p.children == null || p.children.length == 0) {
			serializeUint32(arrayList, 0);
			serializeString(arrayList, p.attr);
			serializeElement(arrayList, p.c);
			serializeElement(arrayList, p.cp);
		} else {
			serializeUint32(arrayList, p.children.length);
			for (int i = 0; i < p.children.length; i++)
				serializePolicy(arrayList, p.children[i]);
		}
	}

	private static BswabePolicy unserializePolicy(BswabePub pub, byte[] arr,
			int[] offset) {
		int i;
		int n;
		BswabePolicy p = new BswabePolicy();
		p.k = unserializeUint32(arr, offset[0]);
		offset[0] += 4;
		p.attr = null;
	
		/* children */
		n = unserializeUint32(arr, offset[0]);
		offset[0] += 4;
		if (n == 0) {
			p.children = null;
	
			StringBuffer sb = new StringBuffer();
			offset[0] = unserializeString(arr, offset[0], sb);
			p.attr = sb.substring(0);
	
			p.c = pub.p.getG1().newElement();
			p.cp = pub.p.getG1().newElement();
	
			offset[0] = unserializeElement(arr, offset[0], p.c);
			offset[0] = unserializeElement(arr, offset[0], p.cp);
		} else {
			p.children = new BswabePolicy[n];
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

	private static void byteArrListAppend(ArrayList<? super Byte> arrayList, byte[] b) {
		for (byte value : b) arrayList.add(value);
	}

	private static byte[] Byte_arr2byte_arr(ArrayList<Byte> B) {
		int len = B.size();
		byte[] b = new byte[len];
	
		for (int i = 0; i < len; i++)
			b[i] = B.get(i);
	
		return b;
	}

}
