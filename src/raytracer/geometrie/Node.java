package raytracer.geometrie;

import raytracer.Ray;

import java.util.List;

/**
 * This class represents a node object
 * Created by Juliand on 15.12.15.
 */
public class Node extends Geometry {

    public final Transform t;
    public final List<Geometry>g;


    /**
     * This constructor builds a new node.
     * @param t
     * @param list
     */
    public Node(final Transform t,final List<Geometry>g ){
        if(t==null)throw new IllegalArgumentException("t have to be not null");
        if(g==null)throw new IllegalArgumentException("list have to be not null");
        this.t=t;
        this.g=g;
    }

    /**
     * The given ray will be transformed.
     * @param r
     * @return
     */
    public Hit hit(final Ray r) {
        if(r==null)throw new IllegalArgumentException("r have to be not null");

        Ray transRay = t.mul(r);
        double t1 = Double.MAX_VALUE;
        double t2 = 0.000001;
        Hit lhit = null;

        for(Geometry geometry:g ){
            Hit h= geometry.hit(transRay);
            if (h.t<t1 && h.t > t2){
                t1= h.t;
                lhit = h;
            }
        }

        if(lhit==null)return null;
        return new Hit(lhit.t,r,lhit.geo,t.mul(lhit.nor));
    }

    @Override
    public String toString() {
        return "Node{}";
    }

}