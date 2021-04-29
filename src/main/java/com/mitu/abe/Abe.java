package com.mitu.abe;

import com.mitu.utils.Utility;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.NoSuchDecryptionTokenFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.StringTokenizer;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class Abe {

    /**
     * Setup.
     *
     * @param pub   the public key
     * @param msk   the master-key
     * @param attrs the attribute
     */

    public static void setup(AbePub pub, AbeMsk msk, String[] attrs, Map<String, String> loadMap) {

        Element g_alpha;
        pub.pairingDesc = "dummy";
        pub.p = PairingFactory.getPairing(loadMap);
        Pairing pairing = pub.p;

        pub.g = pairing.getG1().newElement();

        pub.g_hat_alpha = pairing.getGT().newElement();

        msk.alpha = pairing.getZr().newElement();

        pairing.getG1().newElement();

        msk.alpha.setToRandom();
        pub.g.setToRandom();

        g_alpha = pub.g.duplicate();
        g_alpha.powZn(msk.alpha);

        int i, len = attrs.length;
        pub.comps = new ArrayList<>();
        msk.tjs = new ArrayList<>();

        for (i = 0; i < len; i++) {
            AbeAttrComp comp = new AbeAttrComp();
            AbeAttrCompMsk compMsk = new AbeAttrCompMsk();

            comp.attr = attrs[i];
            compMsk.attr = attrs[i];

            comp.Tj = pairing.getG1().newElement();

            compMsk.tj = pairing.getZr().newElement();

            compMsk.tj.setToRandom();

            comp.Tj = pub.g.duplicate();
            comp.Tj.powZn(compMsk.tj);

            pub.comps.add(comp);
            msk.tjs.add(compMsk);
        }

        pub.g_hat_alpha = pairing.pairing(pub.g, g_alpha);
    }

	/*
     * Generate a private key with the given set of attributes.
	 */

    /**
     * Keygen.
     *
     * @param pub   the pub
     * @param msk   the msk
     * @param attrs the attrs
     * @return the abe prv
     */
    public static AbePrv keygen(AbePub pub, AbeMsk msk, String[] attrs) {

        AbePrv prv = new AbePrv();
        AbePrvPart1 prvPart1 = new AbePrvPart1();
        AbePrvPart2 prvPart2 = new AbePrvPart2();

        Element uj, uid, g_uid, tj_inv, uj_inv_tj, uid_sub_uj;
        Pairing pairing;

		/* initialize */
        pairing = pub.p;

        g_uid = pairing.getG1().newElement();
        prvPart2.d0 = pairing.getG1().newElement();
        uj = pairing.getZr().newElement();
        uj_inv_tj = pairing.getZr().newElement();
        uid_sub_uj = pairing.getZr().newElement();
        uid = pairing.getZr().newElement();

		/* compute */
        uid.setToRandom();

        g_uid = pub.g.duplicate();
        g_uid.powZn(uid);

        prvPart2.d0 = pub.g.duplicate();
        prvPart2.d0.powZn(msk.alpha);
        prvPart2.d0.div(g_uid);

        int i, len = attrs.length;

        prvPart1.comps1 = new ArrayList<>();
        prvPart2.comps2 = new ArrayList<>();

        for (i = 0; i < len; i++) {

            AbePrvCompPart1 compPart1 = new AbePrvCompPart1();
            AbePrvCompPart2 compPart2 = new AbePrvCompPart2();

            Element g_uj;

            compPart1.attr = attrs[i];
            compPart2.attr = attrs[i];

            uj = pairing.getZr().newElement();
            tj_inv = pairing.getZr().newElement();
            uj_inv_tj = pairing.getZr().newElement();
            uid_sub_uj = pairing.getZr().newElement();

            for (int j = 0; j < msk.tjs.size(); j++) {
                if (msk.tjs.get(j).attr.equals(attrs[i])) {
                    tj_inv = msk.tjs.get(j).tj.duplicate();
                    tj_inv.invert();
                    break;
                }
            }

            uj.setToRandom();

            uj_inv_tj = uj.duplicate();
            uj_inv_tj.mulZn(tj_inv);

            uid_sub_uj = uid.duplicate();
            uid_sub_uj.sub(uj);
            uid_sub_uj.mulZn(tj_inv);

            compPart1.dj1 = pairing.getG1().newElement();
            compPart2.dj2 = pairing.getG1().newElement();

            g_uj = pairing.getG1().newElement();

            g_uj = pub.g.duplicate();
            g_uj.powZn(uj);

            compPart1.dj1 = pub.g.duplicate();
            compPart1.dj1.powZn(uj_inv_tj);

            compPart2.dj2 = pub.g.duplicate();
            compPart2.dj2.powZn(uid_sub_uj);

            prvPart1.comps1.add(compPart1);
            prvPart2.comps2.add(compPart2);
        }
        prv.prv1 = prvPart1;
        prv.prv2 = prvPart2;
        return prv;
    }

    /**
     * Enc.
     *
     * @param pub    the pub
     * @param policy the policy
     * @return the abe cph key
     */
    public static AbeCphKey enc(AbePub pub, String policy) {
        AbeCphKey keyCph = new AbeCphKey();
        AbeCph cph = new AbeCph();
        Element s, m;

		/* initialize */
        Pairing pairing = pub.p;
        s = pairing.getZr().newElement();
        m = pairing.getGT().newElement();
        cph.c1 = pairing.getGT().newElement();
        cph.c0 = pairing.getG1().newElement();
        cph.p = parsePolicyPostfix(policy);

		/* compute */
        m.setToRandom();
        s.setToRandom();
        cph.c1 = pub.g_hat_alpha.duplicate();
        cph.c1.powZn(s); /* num_exps++; */

        cph.c1.mul(m); /* num_muls++; */

        cph.c0 = pub.g.duplicate();
        cph.c0.powZn(s); /* num_exps++; */

        cph.cjis = new ArrayList<>();

        AbeCphKey.s = s.duplicate();

        fillPolicy(cph.p, pub, s, cph.cjis, keyCph);

        keyCph.cph = cph;
        keyCph.key = m;

        return keyCph;
    }

	/*
	 * Decrypt the specified ciphertext using the given private key, filling in
	 * the provided element m (which need not be initialized) with the result.
	 * 
	 * Returns true if decryption succeeded, false if this key does not satisfy
	 * the policy of the ciphertext (in which case m is unaltered).
	 */

    /**
     * M_dec.
     *
     * @param pub      the pub
     * @param prvPart1 the prv part1
     * @param cph      the cph
     * @return the abe m dec
     */
    public static AbeMDec m_dec(AbePub pub, AbePrvPart1 prvPart1, AbeCph cph) throws AttributesNotSatisfiedException,
            NoSuchDecryptionTokenFoundException {

        AbeMDec mDec = new AbeMDec();

        Element cji = pub.p.getG1().newElement();
        Element dj1 = pub.p.getG1().newElement();

        Element c_t_hat = pub.p.getGT().newElement();

        ArrayList<String> attrs = new ArrayList<>();

        try {
            attrs = small_set_of_satisfied_attrs(cph, prvPart1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (attrs == null) {
            throw new AttributesNotSatisfiedException("Attributes are not satisfied");
        }

        boolean revoked = is_any_attr_revoked(attrs);
        if (revoked) {
            System.err.println("Attribute has been revoked!!");
            throw new NoSuchDecryptionTokenFoundException("Attributes have been revoked");
        } else {
            for (String attr : attrs) {

                cji = pub.p.getG1().newElement();
                dj1 = pub.p.getG1().newElement();

                for (int j = 0; j < cph.cjis.size(); j++) {
                    if (attr.equals(cph.cjis.get(j).attr)) {
                        cji = cph.cjis.get(j).cji.duplicate();
                        break;
                    }
                }
                for (int k = 0; k < prvPart1.comps1.size(); k++) {
                    if (attr.equals(prvPart1.comps1.get(k).attr)) {
                        dj1 = prvPart1.comps1.get(k).dj1.duplicate();
                        break;
                    }
                }

                c_t_hat.add(pub.p.pairing(cji, dj1));

            }

            mDec.attrs = attrs;
            mDec.c_t_hat = pub.p.getGT().newElement();
            mDec.c_t_hat = c_t_hat.duplicate();

            return mDec;
        }

    }

    /**
     * Checks if is _any_attr_revoked.
     *
     * @param attrs the attrs
     * @return true, if is _any_attr_revoked
     */
    public static boolean is_any_attr_revoked(ArrayList<String> attrs) {

        // This the Attribute Revocation List (ARL) coming from server DB.
        ArrayList<String> revocation_list = new ArrayList<>();
        revocation_list = Utility.getaRLs();
        boolean revoked = false;

        for (String attr : attrs) {
            for (String s : revocation_list) {
                if (attr.equals(s)) {
                    revoked = true;
                    break;
                }
            }
        }

        return revoked;
    }

    /**
     * Dec.
     *
     * @param pub      the pub
     * @param prvPart2 the prv part2
     * @param cph      the cph
     * @param mDec     the m dec
     * @return the element
     */
    public static Element dec(AbePub pub, AbePrvPart2 prvPart2, AbeCph cph, AbeMDec mDec) {

        Element c_t_double_hat = pub.p.getGT().newElement();
        Element dj2 = pub.p.getG1().newElement();
        Element cji = pub.p.getG1().newElement();
        Element p_c_t_hat_c_t_double_hat = pub.p.getGT().newElement();
        Element div_op = pub.p.getGT().newElement();

        for (int i = 0; i < mDec.attrs.size(); i++) {
            for (int j = 0; j < prvPart2.comps2.size(); j++) {
                if (mDec.attrs.get(i).equals(prvPart2.comps2.get(j).attr)) {
                    dj2 = prvPart2.comps2.get(j).dj2.duplicate();
                    break;
                }
            }
            for (int k = 0; k < cph.cjis.size(); k++) {
                if (mDec.attrs.get(i).equals(cph.cjis.get(k).attr)) {
                    cji = cph.cjis.get(k).cji.duplicate();
                    break;
                }
            }

            c_t_double_hat.add(pub.p.pairing(cji, dj2));
        }

        p_c_t_hat_c_t_double_hat = mDec.c_t_hat.duplicate();
        p_c_t_hat_c_t_double_hat.mul(c_t_double_hat);
        p_c_t_hat_c_t_double_hat.mul(pub.p.pairing(cph.c0, prvPart2.d0));

        div_op = cph.c1.duplicate();
        div_op.div(p_c_t_hat_c_t_double_hat);

        return div_op;

    }

    /**
     * Pick satisfy min leaves.
     *
     * @param p        the p
     * @param prvPart1 the prv part1
     */
    private static void pickSatisfyMinLeaves(AbePolicy p, AbePrvPart1 prvPart1) {
        int i, k, l, c_i;
        int len;
        ArrayList<Integer> c = new ArrayList<>();

        if (p.children == null || p.children.length == 0) {
            p.min_leaves = 1;

        } else {
            len = p.children.length;
            for (i = 0; i < len; i++) {
                if (p.children[i].satisfiable) {
                    pickSatisfyMinLeaves(p.children[i], prvPart1);
                }
            }

            for (i = 0; i < len; i++) {
                c.add(i);
            }

            c.sort(new IntegerComparator(p));

            p.satl = new ArrayList<>();
            p.min_leaves = 0;
            l = 0;

            for (i = 0; i < len && l < p.k; i++) {
                c_i = c.get(i); /* c[i] */

                if (p.children[c_i].satisfiable) {
                    l++;
                    p.min_leaves += p.children[c_i].min_leaves;
                    k = c_i + 1;
                    p.satl.add(k);
                }
            }
        }
    }

    /**
     * Check satisfy.
     *
     * @param p        the p
     * @param prvPart1 the prv part1
     */
    private static void checkSatisfy(AbePolicy p, AbePrvPart1 prvPart1) {
        int i, l;
        String prvAttr;

        p.satisfiable = false;
        if (p.children == null || p.children.length == 0) {
            for (i = 0; i < prvPart1.comps1.size(); i++) {
                prvAttr = prvPart1.comps1.get(i).attr;

                if (prvAttr.compareTo(p.attr) == 0) {

                    p.satisfiable = true;
                    p.attri = i;
                    break;
                }
            }
        } else {
            for (i = 0; i < p.children.length; i++) {
                checkSatisfy(p.children[i], prvPart1);
            }

            l = 0;
            for (i = 0; i < p.children.length; i++) {
                if (p.children[i].satisfiable) {
                    l++;
                }
            }

            if (l >= p.k) {
                p.satisfiable = true;
            }
        }
    }

    /**
     * Small_set_of_satisfied_attrs.
     *
     * @param cph      the cph
     * @param prvPart1 the prv part1
     * @return the array list
     */
    public static ArrayList<String> small_set_of_satisfied_attrs(AbeCph cph, AbePrvPart1 prvPart1) {

        ArrayList<String> attrs_list = new ArrayList<>();

        checkSatisfy(cph.p, prvPart1);
        if (!cph.p.satisfiable) {
            System.err.println("cannot decrypt, attributes in key do not satisfy policy");

            return null;
        }

        pickSatisfyMinLeaves(cph.p, prvPart1);

        get_the_min_list(cph.p, attrs_list);

        // return new String[] { "small", "set", "of", "satisfied", "attributes"
        // };
        return attrs_list;
    }

    /**
     * Gets the _the_min_list.
     *
     * @param policy the policy
     * @param attrs  the attrs
     */
    public static void get_the_min_list(AbePolicy policy, ArrayList<String> attrs) {

        if (policy.satisfiable) {
            if (policy.k == 2) {

                for (int i = 0; i < policy.children.length; i++) {
                    get_the_min_list(policy.children[i], attrs);
                }
            } else if (policy.k == 1 && policy.attr == null) {
                int pos = policy.satl.get(0);
                get_the_min_list(policy.children[pos - 1], attrs);

            } else if (policy.k == 1) {
                attrs.add(policy.attr);
            }
        }
    }

    /**
     * Fill policy.
     *
     * @param p      the p
     * @param pub    the pub
     * @param e      the e
     * @param cjis   the cjis
     * @param cphKey the cph key
     */
    private static void fillPolicy(AbePolicy p, AbePub pub, Element e, ArrayList<AbeCjiComp> cjis, AbeCphKey cphKey) {
        int i;
        Element si, t, cji;
        Pairing pairing = pub.p;
        si = pairing.getZr().newElement();
        t = pairing.getZr().newElement();
        cji = pairing.getG1().newElement();

        p.si = e.duplicate();

        if (p.children != null && p.k == 2) {

            for (i = 0; i < p.children.length; i++) {

                if (i == 1) {
                    t = AbeCphKey.s.duplicate();
                    t.sub(p.children[0].si);
                    si = t.duplicate();
                } else {
                    si.setToRandom();
                }
                fillPolicy(p.children[i], pub, si, cjis, cphKey);
            }

        } else if (p.children != null && p.k == 1) {

            for (i = 0; i < p.children.length; i++) {

                fillPolicy(p.children[i], pub, AbeCphKey.s, cjis, cphKey);
            }

        } else if (p.children == null || p.children.length == 0) {

            // For each leaf attribute aji, cji is calculated.

            for (int j = 0; j < pub.comps.size(); j++) {
                if (p.attr.equals(pub.comps.get(j).attr)) {
                    cji = pub.comps.get(j).Tj.duplicate();
                    cji.powZn(p.si);

                    AbeCjiComp cjiComp = new AbeCjiComp();
                    cjiComp.attr = p.attr;
                    cjiComp.cji = pairing.getG1().newElement();
                    cjiComp.cji = cji.duplicate();

                    cjis.add(cjiComp);
                    break;
                }
            }

        }

    }

    /**
     * Parses the policy postfix.
     *
     * @param s the s
     * @return the abe policy
     */
    private static AbePolicy parsePolicyPostfix(String s)  {
        s = s.replaceAll("AND", "&");
        s = s.replaceAll(" ", "");

        s = s.replaceAll("OR", "|");
        StringTokenizer st = new StringTokenizer(s, "(&|)", true);

        return parsePolicyPostfix(st);
    }

    /**
     * Does the real work of parsing, now given a tokenizer for the string.
     *
     * @param st the st
     * @return the abe policy
     */
    private static AbePolicy parsePolicyPostfix(StringTokenizer st) {
        String token = st.nextToken();
        AbePolicy abePolicy = null;
        if (token.equals("(")) {
            // Inner node
            AbePolicy left = parsePolicyPostfix(st);
            String thresholdGate = st.nextToken();
            AbePolicy right = parsePolicyPostfix(st);
            st.nextToken();

            int k = 0;

            if (thresholdGate.equalsIgnoreCase("&")) {
                k = 2;
            } else if (thresholdGate.equalsIgnoreCase("|")) {
                k = 1;
            }

            abePolicy = baseNode(k, null);
            abePolicy.children = new AbePolicy[2];
            abePolicy.children[0] = left;
            abePolicy.children[1] = right;

            return abePolicy;
        } else {
            // Leaf
            return baseNode(1, token);
        }
    }

    /**
     * Base node.
     *
     * @param k the k
     * @param s the s
     * @return the abe policy
     */
    private static AbePolicy baseNode(int k, String s) {
        AbePolicy p = new AbePolicy();

        p.k = k;
        if (!(s == null)) {
            p.attr = s;
        } else {
            p.attr = null;
        }

        return p;
    }

    /**
     * The Class IntegerComparator.
     */
    private static class IntegerComparator implements Comparator<Integer> {

        /**
         * The policy.
         */
        final AbePolicy policy;


        /**
         * Instantiates a new integer comparator.
         *
         * @param p the p
         */
        public IntegerComparator(AbePolicy p) {
            this.policy = p;
        }


        /*
         * (non-Javadoc)
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(Integer o1, Integer o2) {
            int k, l;

            k = policy.children[o1].min_leaves;
            l = policy.children[o2].min_leaves;

            return Integer.compare(k, l);
        }
    }
}
