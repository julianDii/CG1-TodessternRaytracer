package material;

import licht.Light;
import raytracer.Color;
import raytracer.Ray;
import raytracer.World;
import raytracer.geometrie.Hit;
import raytracer.matVecLib.Normal3;
import raytracer.matVecLib.Point3;
import raytracer.matVecLib.Vector3;

/**
 * This class represents a reflective material.
 * Created by Juliand on 02.12.15.
 */
public class ReflectiveMaterial extends Material {

    /**
     * The diffuse component of the material.
     */
    private final Color diffuse;

    /**
     * The specular component of the material.
     */
    private final Color specular;

    /**
     * The exponent component of the material
     */
    private final int exponent;

    /**
     * The reflection component of the material.
     */
    private final Color reflection;

    /**
     * This constructor builds a Reflective material.
     * @param diffuse
     * @param specular
     * @param exponent
     * @param reflection
     */

    public ReflectiveMaterial(final Color diffuse, final Color specular, final int exponent, final Color reflection ){

        if(diffuse==null)throw new IllegalArgumentException("diffuse can not be null");
        if(specular==null)throw new IllegalArgumentException("diffuse can not be null");
        if(reflection==null)throw new IllegalArgumentException("diffuse can not be null");

        this.diffuse=diffuse;
        this.specular=specular;
        this.exponent=exponent;
        this.reflection=reflection;
    }

    /**
     * This method calculates the color for the certain pixel if there is a hit.
     * @param hit
     * @param world
     * @param tracer
     * @return
     */
    public Color colorFor(final Hit hit, final World world,final Tracer tracer) {

        if(hit==null)throw new IllegalArgumentException("Diffuse can not be null");
        if(world==null)throw new IllegalArgumentException("World can not be null");
        if(tracer==null)throw new IllegalArgumentException("Tracer can not be null");

        Color returnColor = diffuse.mul(world.ambient);
        final Point3 hitPoint = hit.ray.at(hit.t);
        final double factor = hit.nor.dot(hit.ray.d.mul(-1))*2;
        final Vector3 e = (hit.ray.d.mul(-1));

        for (final Light li : world.lightList){

            if(li.illuminates(hitPoint,world)){


                final Vector3 lightVector = li.directionFrom(hitPoint).normalized();
                final Color lightColor = li.color;
                final Vector3 reflectedVector = lightVector.reflectedOn(hit.nor);
                final double max = Math.max(0,hit.nor.dot(lightVector));
                returnColor = returnColor.add(diffuse.mul(lightColor.mul(max)));
                returnColor = returnColor.add(specular.mul(lightColor.mul(Math.pow(Math.max(0,reflectedVector.dot(e)),exponent))));
            }
        }
        final Color reflColor = tracer.colorFor(new Ray(hitPoint,hit.ray.d.add(hit.nor.mul(factor)).normalized()));
        returnColor = (reflection.mul(reflColor.add(returnColor)));

        return returnColor;
    }

}
