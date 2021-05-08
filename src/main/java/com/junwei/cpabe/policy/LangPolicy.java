package com.junwei.cpabe.policy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;

public class LangPolicy {

	public static String[] parseAttribute(String s) {
		ArrayList<String> str_arr = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(s);
		String token;
		String[] res;
		int len;

		while (st.hasMoreTokens()) {
			token = st.nextToken();
			if (token.contains(":")) {
				str_arr.add(token);
			} else {
				System.out.println("Some error happens in the input attribute");
				System.exit(0);
			}
		}

		str_arr.sort(new SortByAlphabetic());

		len = str_arr.size();
		res = new String[len];
		for (int i = 0; i < len; i++)
			res[i] = str_arr.get(i);
		return res;
	}

	public static void main(String[] args) {
		String attr = "objectClass:inetOrgPerson objectClass:organizationalPerson "
				+ "sn:student2 cn:student2 uid:student2 userPassword:student2 "
				+ "ou:idp o:computer mail:student2@sdu.edu.cn title:student";
		String[] arr = parseAttribute(attr);
		for (String s : arr) System.out.println(s);
	}

	static class SortByAlphabetic implements Comparator<String>, Serializable {
		@Override
		public int compare(String s1, String s2) {
			if (s1.compareTo(s2) >= 0)
				return 1;
			return 0;
		}

	}
}
