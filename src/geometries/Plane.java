package geometries;

import materials.Material;
import matVecLib.Normal3;
import matVecLib.Point3;
import raytracer.Hit;
import raytracer.Ray;
import texture.TextureCoord2D;

/**
 * Created by Julian on 03.11.15. 
 * Developer Charline Waldrich
 */
public class Plane extends Geometry {

	final Point3 a;
	final Normal3 n;

	/**
	 * Initializes a new plane object.
	 * @param a point lying on the plane. Cannot be null.
	 * @param n: normal of the plane. Cannot be null.
	 * @param color of the plane. Cannot be null.
	 */
	public Plane(final Material material) {
		super(material);

		this.a = new Point3 (0,0,0);
		this.n = new Normal3(0,1,0);
		
		if (a == null) throw new IllegalArgumentException("I need a point lying on the plane!");
		if (n == null) throw new IllegalArgumentException("The normal should not be null.");
	}

	/**
	 * If the given ray and the plane do have a point of intersection, it returns a hit object. 
	 * It contains the factor t which we need to know at which length of the ray it hits the 
	 * object. It also holds the ray itself (r) and the color of the plane (color).
	 * @param ray is needed to calculate weather the ray hits the object or not.
	 */
	@Override
    public Hit hit (final Ray r) {

    	if (r == null) throw new IllegalArgumentException("The Ray of the Camera is missing!");
    	
    	final double denominator = r.d.dot(n);

    	if (denominator != 0) {

    		final double t = n.dot(a.sub(r.o))/denominator;

			if (t < 0) {

    			return null;
    		}
			Point3 point = r.at(t);
    		return new Hit(t, r, this, n, new TextureCoord2D(point.x, -point.z));
    	}

    	return null;
    }

	@Override
	public String toString() {
		return "Plane [a=" + a + ", n=" + n + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((n == null) ? 0 : n.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plane other = (Plane) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (n == null) {
			if (other.n != null)
				return false;
		} else if (!n.equals(other.n))
			return false;
		return true;
	}

}