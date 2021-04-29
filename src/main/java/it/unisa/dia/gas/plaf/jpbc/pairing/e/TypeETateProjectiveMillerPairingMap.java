package it.unisa.dia.gas.plaf.jpbc.pairing.e;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.AbstractPairingMap;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TypeETateProjectiveMillerPairingMap extends AbstractPairingMap {
    protected final TypeEPairing pairing;


    public TypeETateProjectiveMillerPairingMap(TypeEPairing pairing) {
        super(pairing);

        this.pairing = pairing;
    }


    public Element pairing(Point in1, Point in2) {
        Element out = pairing.Fq.newElement();
        Point QR = (Point) in2.duplicate().add(pairing.R);

        e_miller_proj(out, in1, QR, pairing.R);
        out.pow(pairing.phikonr);

        return new GTFiniteElement(this, (GTFiniteField) pairing.getGT(), out);
    }

    public void finalPow(Element element) {
        element.pow(pairing.phikonr);
    }


    void e_miller_proj(Element res, Point P, Point QR, Point R) {
        //collate divisions
        int n;
        Element cca = ((CurveField) P.getField()).getA();
        int i;

        Element Zx, Zy;
        Element Px = P.getX();
        Element numx = QR.getX();
        Element numy = QR.getY();
        Element denomx = R.getX();
        Element denomy = R.getY();

        Element a = pairing.Fq.newElement();
        Element b = pairing.Fq.newElement();
        Element c = pairing.Fq.newElement();
        Element e0 = pairing.Fq.newElement();
        Element e1 = pairing.Fq.newElement();

        Element z = pairing.Fq.newOneElement();
        Element z2 = pairing.Fq.newOneElement();

        Element v = pairing.Fq.newOneElement();
        Element vd = pairing.Fq.newOneElement();
        Element v1 = pairing.Fq.newOneElement();
        Element vd1 = pairing.Fq.newOneElement();

        Point Z = (Point) P.duplicate();
        Zx = Z.getX();
        Zy = Z.getY();
        Point Z1 = (Point) P.getField().newElement();

        n = pairing.exp1;
        for (i = 0; i < n; i++) {
            v.square();
            vd.square();

            do_tangent(v, vd, a, b, c, e0, e1, z, z2, Zx, Zy,  cca, numx, numy, denomx, denomy);
            proj_double(Zx, Zy, e0, e1, a, b, z, z2, cca);
            do_vertical(vd, v, Zx, e0, z2, numx, denomx);
        }

        pointToAffine(Zx, Zy, z, z2, e0);

        if (pairing.sign1 < 0) {
            v1.set(vd);
            vd1.set(v);
            do_vertical(vd1, v1, Zx, e0, z2, numx, denomx);
            Z1.set(Z).negate();
        } else {
            v1.set(v);
            vd1.set(vd);
            Z1.set(Z);
        }
        n = pairing.exp2;
        for (; i < n; i++) {
            v.square();
            vd.square();

            do_tangent(v, vd, a, b, c, e0, e1, z, z2, Zx, Zy,  cca, numx, numy, denomx, denomy);
            proj_double(Zx, Zy, e0, e1, a, b, z, z2, cca);
            do_vertical(vd, v, Zx, e0, z2, numx, denomx);
        }

        pointToAffine(Zx, Zy, z, z2, e0);


        v.mul(v1);
        vd.mul(vd1);
        do_line(v, vd, Z, Z1, a, b, c, e0, e1, numx, numy, denomx, denomy);
        Z.add(Z1);
        do_vertical(vd, v, Zx, e0, z2, numx, denomx);

        if (pairing.sign0 > 0) {
            do_vertical(v, vd, Px, e0, z2, numx, denomx);
        }

        vd.invert();
        res.set(v).mul(vd);
    }


    void proj_double(Element Zx, Element Zy, Element e0, Element e1, Element e2, Element e3, Element z, Element z2, Element cca) {

        e0.set(Zx).square();
        e1.set(e0).twice();
        e0.add(e1);
        e1.set(z2).square();
        e1.mul(cca);
        e0.add(e1);



        z.mul(Zy);
        z.twice();
        z2.set(z).square();

        e2.set(Zy).square();
        e1.set(Zx).mul(e2);

        e1.twice().twice();


        e3.set(e1).twice();
        Zx.set(e0).square();
        Zx.sub(e3);


        e2.square();
        e2.twice().twice().twice();


        e1.sub(Zx);
        e0.mul(e1);
        Zy.set(e0).sub(e2);
    }

    void do_tangent(Element e, Element edenom, Element a, Element b, Element c, Element e0, Element e1, Element z, Element z2,
                    Element Zx, Element Zy, Element cca, Element numx, Element numy, Element denomx, Element denomy
    ) {

        a.set(z2).square();
        a.mul(cca);
        b.set(Zx).square();

        e0.set(b).twice();
        b.add(e0);
        a.add(b);
        a.negate();

        e0.set(Zy).twice();
        b.set(e0).mul(z2);
        b.mul(z);

        c.set(Zx).mul(a);
        a.mul(z2);
        e0.mul(Zy);
        c.add(e0).negate();

        e0.set(a).mul(numx);
        e1.set(b).mul(numy);
        e0.add(e1).add(c);
        e.mul(e0);

        e0.set(a).mul(denomx);
        e1.set(b).mul(denomy);
        e0.add(e1).add(c);
        edenom.mul(e0);
    }

    void do_vertical(Element e, Element edenom, Element Ax, Element e0, Element z2, Element numx, Element denomx) {
        e0.set(numx).mul(z2);
        e0.sub(Ax);
        e.mul(e0);

        e0.set(denomx).mul(z2);
        e0.sub(Ax);
        edenom.mul(e0);
    }

    void do_line(Element e, Element edenom, Point A, Point B, Element a, Element b, Element c, Element e0, Element e1,
                 Element numx, Element numy, Element denomx, Element denomy) {

        Element Ax = A.getX();
        Element Ay = A.getY();
        Element Bx = B.getX();
        Element By = B.getY();

        b.set(Bx).sub(Ax);
        a.set(Ay).sub(By);
        c.set(Ax).mul(By);
        e0.set(Ay).mul(Bx);
        c.sub(e0);

        e0.set(a).mul(numx);
        e1.set(b).mul(numy);
        e0.add(e1);
        e0.add(c);
        e.mul(e0);

        e0.set(a).mul(denomx);
        e1.set(b).mul(denomy);
        e0.add(e1).add(c);
        edenom.mul(e0);
    }

}
