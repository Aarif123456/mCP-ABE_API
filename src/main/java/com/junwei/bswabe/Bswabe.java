package com.junwei.bswabe;

import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.MalformedPolicyException;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Pattern;

public class Bswabe {

	private static final Pattern OF = Pattern.compile("of");

	/*
	 * Generate a public key and corresponding master secret key.
	 */
	public static void setup(BswabePub pub, BswabeMsk msk, Map<String, String> loadMap) {
		Element alpha, beta_inv;

		pub.pairingDesc = "dummy";
		pub.p = PairingFactory.getPairing(loadMap);
		Pairing pairing = pub.p;

		pub.g = pairing.getG1().newElement();
		pub.f = pairing.getG1().newElement();
		pub.h = pairing.getG1().newElement();
		pub.gp = pairing.getG2().newElement();
		pub.g_hat_alpha = pairing.getGT().newElement();
		alpha = pairing.getZr().newElement();
		msk.beta = pairing.getZr().newElement();
		msk.g_alpha = pairing.getG2().newElement();

		alpha.setToRandom();
		msk.beta.setToRandom();
		pub.g.setToRandom();
		pub.gp.setToRandom();

		msk.g_alpha = pub.gp.duplicate();
		msk.g_alpha.powZn(alpha);

		beta_inv = msk.beta.duplicate();
		beta_inv.invert();
		pub.f = pub.g.duplicate();
		pub.f.powZn(beta_inv);

		pub.h = pub.g.duplicate();
		pub.h.powZn(msk.beta);

		pub.g_hat_alpha = pairing.pairing(pub.g, msk.g_alpha);
	}

	/*
	 * Generate a private key with the given set of attributes.
	 */
	public static BswabePrv keygen(BswabePub pub, BswabeMsk msk, String[] attrs)
			throws NoSuchAlgorithmException {
		BswabePrv prv = new BswabePrv();
		Element g_r, r, beta_inv;
		Pairing pairing;

		/* initialize */
		pairing = pub.p;
		prv.d = pairing.getG2().newElement();
		/* g-r */
		pairing.getG2().newElement();
		r = pairing.getZr().newElement();
		/* beta_inv */
		pairing.getZr().newElement();

		/* compute */
		r.setToRandom();
		g_r = pub.gp.duplicate();
		g_r.powZn(r);

		prv.d = msk.g_alpha.duplicate();
		prv.d.mul(g_r);
		beta_inv = msk.beta.duplicate();
		beta_inv.invert();
		prv.d.powZn(beta_inv);

		int i, len = attrs.length;
		prv.comps = new ArrayList<>();
		for (i = 0; i < len; i++) {
			BswabePrvComp comp = new BswabePrvComp();
			Element h_rp;
			Element rp;

			comp.attr = attrs[i];

			comp.d = pairing.getG2().newElement();
			comp.dp = pairing.getG1().newElement();
			h_rp = pairing.getG2().newElement();
			rp = pairing.getZr().newElement();

			elementFromString(h_rp, comp.attr);
			rp.setToRandom();

			h_rp.powZn(rp);

			comp.d = g_r.duplicate();
			comp.d.mul(h_rp);
			comp.dp = pub.g.duplicate();
			comp.dp.powZn(rp);

			prv.comps.add(comp);
		}

		return prv;
	}

	/*
	 * Pick a random group element and encrypt it under the specified access
	 * policy. The resulting ciphertext is returned and the Element given as an
	 * argument (which need not be initialized) is set to the random group
	 * element.
	 * 
	 * After using this function, it is normal to extract the random data in m
	 * using the pbc functions element_length_in_bytes and element_to_bytes and
	 * use it as a key for hybrid encryption.
	 * 
	 * The policy is specified as a simple string which encodes a postorder
	 * traversal of threshold tree defining the access policy. As an example,
	 * 
	 * "foo bar fim 2of3 baf 1of2"
	 * 
	 * specifies a policy with two threshold gates and four leaves. It is not
	 * possible to specify an attribute with whitespace in it (although "_" is
	 * allowed).
	 * 
	 * Numerical attributes and any other fancy stuff are not supported.
	 * 
	 * Returns null if an error occurred, in which case a description can be
	 * retrieved by calling bswabe_error().
	 */
	public static BswabeCphKey enc(BswabePub pub, String policy) throws MalformedPolicyException, NoSuchAlgorithmException {
		BswabeCphKey keyCph = new BswabeCphKey();
		BswabeCph cph = new BswabeCph();
		Element s, m;

		/* initialize */
		Pairing pairing = pub.p;
		s = pairing.getZr().newElement();
		m = pairing.getGT().newElement();
		cph.cs = pairing.getGT().newElement();
		cph.c = pairing.getG1().newElement();
		cph.p = parsePolicyPostfix(policy);

		/* compute */
		m.setToRandom();
		s.setToRandom();
		cph.cs = pub.g_hat_alpha.duplicate();
		cph.cs.powZn(s); /* num_exps++; */
		cph.cs.mul(m); /* num_muls++; */

		cph.c = pub.h.duplicate();
		cph.c.powZn(s); /* num_exps++; */

		fillPolicy(cph.p, pub, s);

		keyCph.cph = cph;
		keyCph.key = m;

		return keyCph;
	}

	/*
     * Delegate a subset of attribute of an existing private key.
     */
    public static BswabePrv delegate(BswabePub pub, BswabePrv prv_src, String[] attrs_subset)
            throws NoSuchAlgorithmException, IllegalArgumentException {

            BswabePrv prv = new BswabePrv();
            Element g_rt, rt, f_at_rt;
            Pairing pairing;

            /* initialize */
            pairing = pub.p;
            prv.d = pairing.getG2().newElement();

            g_rt = pairing.getG2().newElement();
            rt = pairing.getZr().newElement();
            f_at_rt = pairing.getZr().newElement();

            /* compute */
            rt.setToRandom();
            f_at_rt = pub.f.duplicate();
            f_at_rt.powZn(rt);
            prv.d = prv_src.d.duplicate();
            prv.d.mul(f_at_rt);

            g_rt = pub.g.duplicate();
            g_rt.powZn(rt);

            int i, len = attrs_subset.length;
            prv.comps = new ArrayList<BswabePrvComp>();

            for (i = 0; i < len; i++) {
                BswabePrvComp comp = new BswabePrvComp();
                Element h_rtp;
                Element rtp;

                comp.attr = attrs_subset[i];

                BswabePrvComp comp_src = new BswabePrvComp();
                boolean comp_src_init = false;

                for (int j = 0; j < prv_src.comps.size(); ++j) {
                    if (prv_src.comps.get(j).attr.equals(comp.attr)) {
                        comp_src = prv_src.comps.get(j);
                        comp_src_init = true;
                        break;
                    }
                }

                if (!comp_src_init) {
                    throw new IllegalArgumentException("comp_src_init == false");
                }

                comp.d = pairing.getG2().newElement();
                comp.dp = pairing.getG1().newElement();
                h_rtp = pairing.getG2().newElement();
                rtp = pairing.getZr().newElement();

                elementFromString(h_rtp, comp.attr);
                rtp.setToRandom();

                h_rtp.powZn(rtp);

                comp.d = g_rt.duplicate();
                comp.d.mul(h_rtp);
                comp.d.mul(comp_src.d);

                comp.dp = pub.g.duplicate();
                comp.dp.powZn(rtp);
                comp.dp.mul(comp_src.dp);


                prv.comps.add(comp);
            }

            return prv;
        }

	/*
	 * Decrypt the specified ciphertext using the given private key, filling in
	 * the provided element m (which need not be initialized) with the result.
	 * 
	 * Returns true if decryption succeeded, false if this key does not satisfy
	 * the policy of the ciphertext (in which case m is unaltered).
	 */
	public static BswabeElementBoolean dec(BswabePub pub, BswabePrv prv,
			BswabeCph cph) throws AttributesNotSatisfiedException{
		Element t;
		Element m;
		BswabeElementBoolean beb = new BswabeElementBoolean();
		/* m */
		pub.p.getGT().newElement();
		t = pub.p.getGT().newElement();

		checkSatisfy(cph.p, prv);
		if (!cph.p.satisfiable) {
			throw new AttributesNotSatisfiedException("cannot decrypt, attributes in key do not satisfy policy");
		}

		pickSatisfyMinLeaves(cph.p, prv);

		decFlatten(t, cph.p, prv, pub);

		m = cph.cs.duplicate();
		m.mul(t); /* num_muls++; */

		t = pub.p.pairing(cph.c, prv.d);
		t.invert();
		m.mul(t); /* num_muls++; */

		beb.e = m;
		beb.b = true;

		return beb;
	}

	private static void decFlatten(Element r, BswabePolicy p, BswabePrv prv,
			BswabePub pub) {
		Element one;
		one = pub.p.getZr().newElement();
		one.setToOne();
		r.setToOne();

		decNodeFlatten(r, one, p, prv, pub);
	}

	private static void decNodeFlatten(Element r, Element exp, BswabePolicy p,
			BswabePrv prv, BswabePub pub) {
		if (p.children == null || p.children.length == 0)
			decLeafFlatten(r, exp, p, prv, pub);
		else
			decInternalFlatten(r, exp, p, prv, pub);
	}

	private static void decLeafFlatten(Element r, Element exp, BswabePolicy p,
			BswabePrv prv, BswabePub pub) {
		BswabePrvComp c;
		Element s, t;

		c = prv.comps.get(p.attributes);
		/* s */
		pub.p.getGT().newElement();
		/* t */
		pub.p.getGT().newElement();

		s = pub.p.pairing(p.c, c.d); /* num_pairings++; */
		t = pub.p.pairing(p.cp, c.dp); /* num_pairings++; */
		t.invert();
		s.mul(t); /* num_muls++; */
		s.powZn(exp); /* num_exps++; */

		r.mul(s); /* num_muls++; */
	}

	private static void decInternalFlatten(Element r, Element exp,
			BswabePolicy p, BswabePrv prv, BswabePub pub) {
		int i;
		Element t, expNew;

		t = pub.p.getZr().newElement();
		/* expNew */
		pub.p.getZr().newElement();

		for (i = 0; i < p.satl.size(); i++) {
			lagrangeCoefficient(t, p.satl, p.satl.get(i));
			expNew = exp.duplicate();
			expNew.mul(t);
			decNodeFlatten(r, expNew, p.children[p.satl.get(i) - 1], prv, pub);
		}
	}

	private static void lagrangeCoefficient(Element r, ArrayList<Integer> s, int i) {
		int j, k;
		Element t;

		t = r.duplicate();

		r.setToOne();
		for (k = 0; k < s.size(); k++) {
			j = s.get(k);
			if (j == i)
				continue;
			t.set(-j);
			r.mul(t); /* num_muls++; */
			t.set(i - j);
			t.invert();
			r.mul(t); /* num_muls++; */
		}
	}

	private static void pickSatisfyMinLeaves(BswabePolicy p, BswabePrv prv) {
		int i, k, l, c_i;
		int len;
		ArrayList<Integer> c = new ArrayList<>();

		if (p.children == null || p.children.length == 0)
			p.min_leaves = 1;
		else {
			len = p.children.length;
			for (i = 0; i < len; i++)
				if (p.children[i].satisfiable)
					pickSatisfyMinLeaves(p.children[i], prv);

			for (i = 0; i < len; i++)
				c.add(i);

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

	private static void checkSatisfy(BswabePolicy p, BswabePrv prv) {
		int i, l;
		String prvAttr;

		p.satisfiable = false;
		if (p.children == null || p.children.length == 0) {
			for (i = 0; i < prv.comps.size(); i++) {
				prvAttr = prv.comps.get(i).attr;
				if (prvAttr.compareTo(p.attr) == 0) {
					p.satisfiable = true;
					p.attributes = i;
					break;
				}
			}
		} else {
			for (i = 0; i < p.children.length; i++)
				checkSatisfy(p.children[i], prv);

			l = 0;
			for (i = 0; i < p.children.length; i++)
				if (p.children[i].satisfiable)
					l++;

			if (l >= p.k)
				p.satisfiable = true;
		}
	}

	private static void fillPolicy(BswabePolicy p, BswabePub pub, Element e)
			throws NoSuchAlgorithmException {
		int i;
		Element r, t, h;
		Pairing pairing = pub.p;
		r = pairing.getZr().newElement();
		t = pairing.getZr().newElement();
		h = pairing.getG2().newElement();

		p.q = randPoly(p.k - 1, e);

		if (p.children == null || p.children.length == 0) {
			p.c = pairing.getG1().newElement();
			p.cp = pairing.getG2().newElement();

			elementFromString(h, p.attr);
			p.c = pub.g.duplicate();
            p.c.powZn(p.q.coefficients[0]);
			p.cp = h.duplicate();
			p.cp.powZn(p.q.coefficients[0]);
		} else {
			for (i = 0; i < p.children.length; i++) {
				r.set(i + 1);
				evalPoly(t, p.q, r);
				fillPolicy(p.children[i], pub, t);
			}
		}

	}

	private static void evalPoly(Element r, BswabePolynomial q, Element x) {
		int i;
		Element s, t;
		/* s */
		r.duplicate();
		t = r.duplicate();

		r.setToZero();
		t.setToOne();

		for (i = 0; i < q.deg + 1; i++) {
			/* r += q->coefficients[i] * t */
			s = q.coefficients[i].duplicate();
			s.mul(t); 
			r.add(s);

			/* t *= x */
			t.mul(x);
		}

	}

	private static BswabePolynomial randPoly(int deg, Element zeroVal) {
		int i;
		BswabePolynomial q = new BswabePolynomial();
		q.deg = deg;
		q.coefficients = new Element[deg + 1];

		for (i = 0; i < deg + 1; i++)
			q.coefficients[i] = zeroVal.duplicate();

		q.coefficients[0].set(zeroVal);

		for (i = 1; i < deg + 1; i++)
			q.coefficients[i].setToRandom();

		return q;
	}

	private static BswabePolicy parsePolicyPostfix(String s) throws MalformedPolicyException {
		String[] toks;
		ArrayList<BswabePolicy> stack = new ArrayList<>();
		BswabePolicy root;

		toks = s.split(" ");

		for (String tok : toks) {
			int i, k, n;
			if (!tok.contains("of")) {
				stack.add(baseNode(1, tok));
			} else {
				BswabePolicy node;

				/* parse kof n node */
				String[] k_n = OF.split(tok);
				k = Integer.parseInt(k_n[0]);
				n = Integer.parseInt(k_n[1]);

				if (k < 1) {
					throw new MalformedPolicyException("error parsing " + s
							+ ": trivially satisfied operator " + tok);
				} else if (k > n) {
					throw new MalformedPolicyException("error parsing " + s
							+ ": unsatisfiable operator " + tok);
				} else if (n == 1) {
					throw new MalformedPolicyException("error parsing " + s
							+ ": identity operator " + tok);
				} else if (n > stack.size()) {
					throw new MalformedPolicyException("error parsing " + s
							+ ": stack underflow at " + tok);
				}

				/* pop n things and fill in children */
				node = baseNode(k, null);
				node.children = new BswabePolicy[n];

				for (i = n - 1; i >= 0; i--)
					node.children[i] = stack.remove(stack.size() - 1);

				/* push result */
				stack.add(node);
			}
		}

		if (stack.size() > 1) {
			throw new MalformedPolicyException("error parsing " + s
					+ ": extra node left on the stack");
		} else if (stack.size() < 1) {
			throw new MalformedPolicyException("error parsing " + s + ": empty policy");
		}

		root = stack.get(0);
		return root;
	}

	private static BswabePolicy baseNode(int k, String s) {
		BswabePolicy p = new BswabePolicy();

		p.k = k;
		if (!(s == null))
			p.attr = s;
		else
			p.attr = null;
		p.q = null;

		return p;
	}

	private static void elementFromString(Element h, String s)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] digest = md.digest(s.getBytes());
		h.setFromHash(digest, 0, digest.length);
	}

	private static class IntegerComparator implements Comparator<Integer>, Serializable {
		final BswabePolicy policy;

		public IntegerComparator(BswabePolicy p) {
            policy = p;
		}

		@Override
		public int compare(Integer o1, Integer o2) {
			int k, l;

			k = policy.children[o1].min_leaves;
			l = policy.children[o2].min_leaves;

			return Integer.compare(k, l);
		}
	}
}
