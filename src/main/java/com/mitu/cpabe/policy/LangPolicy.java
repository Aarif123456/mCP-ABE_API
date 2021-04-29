package com.mitu.cpabe.policy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;

/* The class that outlines the grammar of the ciphertext policy */
public class LangPolicy {

	/**
	 * Parses the attribute.
	 * 
	 * @author Mitu Kumar Debnath
	 * @param s the string of attributes to be parsed
	 * @return the string[]
	 */
	public static String[] parseAttribute(String s) {

		ArrayList<String> str_arr = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(s);
		String token;
		String[] res;
		int len;

		while (st.hasMoreTokens()) {
			token = st.nextToken();

			str_arr.add(token);
		}
		str_arr.sort(String::compareTo);

		len = str_arr.size();
		res = new String[len];
		for (int i = 0; i < len; i++) {
			res[i] = str_arr.get(i);
		}
		return res;
	}
}