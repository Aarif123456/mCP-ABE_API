/*
 * 
 */
package com.mitu.cpabe.policy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;

// TODO: Auto-generated Javadoc
/**
 * The Class LangPolicy.
 */
public class LangPolicy {

	/**
	 * Parses the attribute.
	 * 
	 * @author Mitu Kumar Debnath
	 * @param s
	 *            the s
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

		str_arr.sort(new SortByAlphabetic());

		len = str_arr.size();
		res = new String[len];
		for (int i = 0; i < len; i++) {
			res[i] = str_arr.get(i);
		}
		return res;
	}

	/**
	 * The Class SortByAlphabetic.
	 */
	static class SortByAlphabetic implements Comparator<String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(String s1, String s2) {
			if (s1.compareTo(s2) >= 0) {
				return 1;
			}
			return 0;
		}

	}
}