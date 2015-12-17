package raytracer.geometrie;

import material.Material;
import raytracer.matVecLib.Normal3;
import raytracer.matVecLib.Point3;
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
     * @param material The color of the Sphere.
     */
    public Sphere(final Material material) {
        super(material);


        this.c=new Point3(0,0,0);
        this.r=1;
    }

    /**
     * This method calculates the intersections of the ray with the sphere.
     * @param r The Ray.
     * @return The hit if there is an intersection with the Sphere. Null if not.
     */
    public Hit hit(Ray r) {

        final double a;
        final double b;
        final double cNor;
        final double t1;
        final double t2;
        final double d;
        final Point3 p;

        b=r.d.dot((r.o.sub(c)).mul(2));
        a=r.d.dot(r.d);
        cNor=r.o.sub(c).dot(r.o.sub(c))-(this.r*this.r);
        d = (b * b) - (4 * a * cNor);

        if(d>0) {

            t1 = (-b + Math.sqrt(d)) / (2 * a);
            t2 = (-b - Math.sqrt(d)) / (2 * a);

            if (t1 >= 0 & t2 >= 0) {
                p=r.at(Math.min(t1, t2));
               return new Hit(Math.min(t1, t2), r, this,p.sub(c).normalized().asNormal() );
            }else if (t1>=0){
                return new Hit(t1,r,this, r.at(t1).sub(c).normalized().asNormal());
            }else if(t2>=0) {

                return new Hit(t2, r, this,r.at(t2).sub(c).normalized().asNormal());
            }
        }else if (d==0){
            final double t3;
            t3=-b/(2*a);
            if (t3>=0){
                return new Hit(t3,r,this,r.at(t3).sub(c).normalized().asNormal());

            }
        }
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
