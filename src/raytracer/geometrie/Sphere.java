package raytracer.geometrie;

import raytracer.matVecLib.Point3;
import raytracer.Color;
import raytracer.Ray;

/**
 * This class represents a Sphere.
 * Created by Julian on 03.11.15.
 * Developer Julian Dobrot
 */
public class Sphere extends Geometry {

    /**
     * The center point of the Sphere.
     */
    final Point3 c;

    /**
     * The Radius value of the Sphere.
     */
    final double r;

    /**
     * This constructor builds a new Sphere.
     * @param c The center point of the Sphere.
     * @param r The radius of the Sphere.
     * @param color The color of the Sphere.
     */
    public Sphere(final Point3 c, final double r, final Color color) {
        super(color);

        if(c==null)throw new IllegalArgumentException("c has to be not null");
        if(color==null)throw new IllegalArgumentException("color has to be not null");

        this.c=c;
        this.r=r;
    }

    @Override
    public Hit hit(Ray r) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sphere sphere = (Sphere) o;

        if (Double.compare(sphere.r, r) != 0) return false;
        return c.equals(sphere.c);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = c.hashCode();
        temp = Double.doubleToLongBits(r);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "c=" + c +
                ", r=" + r +
                '}';
    }
}