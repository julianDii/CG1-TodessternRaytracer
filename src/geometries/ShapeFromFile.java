/*
 * MIT License
 *
 * Copyright (c) 2016 Julian Dobrot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package geometries;
import materials.Material;
import raytracer.Hit;
import raytracer.Ray;
import matVecLib.Normal3;
import matVecLib.Point3;
import raytracer.Transform;
import texture.TextureCoord2D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * This Class represents an OBJLoader.
 * It's possible to load scenes from an .obj file.
 * To use this class u have to  1. create a property file in your directory (raytracer-todesstern).
 * 2. Add this to the property file: BASE_URL_MODEL="/Users/..here you have to enter your specific path to the img
 * folder.../src/assets/models/"
 *
 */

public class ShapeFromFile extends Geometry {

    /**
     * The empty String hull for the path to the img folder loaded from the properties.
     */
    private static String BASE_URL_MODELS = "";

    /**
     * The properties object to load individual base paths for different users.
     */
    private static Properties properties = new Properties();

    /**
     * describes comments in the file.
     */
    private final static String COMMENT = "#";

    /**
     * describes texture coordinate points in the file.
     */
    private final static String VERTEX_TEXTURE = "vt";

    /**
     * describes a normal in the file.
     */
    private final static String VERTEX_NORMAL = "vn";

    /**
     * describes a vertex in the file.
     */
    private final static String VERTEX = "v";

    /**
     * describes an area in the file.
     */
    private final static String FACE = "f";

    /**
     * splitter
     */
    private final static String SPLITTER = " ";

    /**
     * seperator
     */
    private final static String SEPERATOR = "/";


    private ArrayList<Geometry> triangles;
    private ArrayList<Point3> vertexPoints;
    private ArrayList<Normal3> vertexNormalPoints;
    private ArrayList<Point3> vertexTexturePoints;
    private ArrayList<Point3> vertexFaces;
    private ArrayList<Integer> vertexNormalFaces;
    private ArrayList<Point3> vertexTextureFaces;

    private int currentLineNumber;
    private int currentFaceNumber;
    private boolean vtToProcess;
    private boolean vnToProcess;

    /**
     * the name of the file
     */
    public final String modelName;

    /**
     * creates a new ShapeFromFile object having a filname and a materials as param
     * @param modelName of the OBJ File
     * @param material for the image
     */

    public ShapeFromFile(final String modelName, final Material material) {
        super(material);

        if (modelName == null)throw new IllegalArgumentException("fileName has to be not null");
        if (material == null)throw new IllegalArgumentException("materials has to be not null");

        this.modelName = modelName;
        this.triangles = new ArrayList<>();
        this.vertexPoints = new ArrayList<>();
        this.vertexNormalPoints = new ArrayList<>();
        this.vertexTexturePoints = new ArrayList<>();
        this.vertexFaces = new ArrayList<>();
        this.vertexNormalFaces = new ArrayList<>();
        this.vertexTextureFaces = new ArrayList<>();
        this.currentLineNumber = 0;
        this.currentFaceNumber = 0;
        this.vtToProcess = false;
        this.vnToProcess = false;
    }

    /**
     * this method parses the given OBJ File
     * @return a new node object with triangles
     */
    public Node OBJLoader() {

        try {

            properties.load(new FileInputStream("todesstern.properties"));
            BASE_URL_MODELS = properties.get("BASE_URL_MODELS").toString();
            System.out.println(BASE_URL_MODELS);
            File f = new File(BASE_URL_MODELS + modelName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                line = line.trim();

                if (line.length() == 0) {
                    continue;
                }

                if (line.startsWith(ShapeFromFile.COMMENT)) {

                    continue;

                } else if (line.startsWith(ShapeFromFile.VERTEX_TEXTURE)) {

                    vertexTexture(line.substring(ShapeFromFile.VERTEX_TEXTURE.length()).trim());

                } else if (line.startsWith(ShapeFromFile.VERTEX_NORMAL)) {

                    vertexNormal(line.substring(ShapeFromFile.VERTEX_NORMAL.length()).trim());

                } else if (line.startsWith(ShapeFromFile.VERTEX)) {

                    processVertex(line.substring(ShapeFromFile.VERTEX.length()).trim());

                } else if (line.startsWith(ShapeFromFile.FACE)) {

                    processFace(line.substring(ShapeFromFile.FACE.length()).trim());

                } else {

                    System.out.println("line " + currentLineNumber + " is unknown: " + line + "|");

                }

                this.currentLineNumber++;
            }

            for (int i = 0; i < vertexFaces.size(); i++) {

                final Point3 currentVF = vertexFaces.get(i);

                Point3 a = new Point3(0, 0, 0);
                Point3 b = new Point3(0, 0, 0);
                Point3 c = new Point3(0, 0, 0);

                try {

                    a = vertexPoints.get((int) Math.abs(currentVF.x) - 1);
                    b = vertexPoints.get((int) Math.abs(currentVF.y) - 1);
                    c = vertexPoints.get((int) Math.abs(currentVF.z) - 1);
                }

                catch(ArrayIndexOutOfBoundsException msg) {
                    System.err.println("weird indices");
                }

                final Normal3 normal;
                if (this.vnToProcess) {

                    normal = vertexNormalPoints.get(vertexNormalFaces.get(i) - 1).mul(-1);

                } else {

                   normal = b.sub(a).x(c.sub(a)).normalized().asNormal();
                }
                triangles.add(new Triangle(a, b, c, normal, normal, normal,material,new TextureCoord2D(0, 0),new TextureCoord2D(0, 0),new TextureCoord2D(0, 0)));

            }

            bufferedReader.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
        return new Node (new Transform(), triangles);
    }

    private void processFace (final String line) {

        this.currentFaceNumber++;

        StringTokenizer st = new StringTokenizer(line, ShapeFromFile.SPLITTER);
        int count = st.countTokens();

        int v[] = new int[count];
        int vt[] = new int[count];
        int vn[] = new int[count];

        for (int i = 0; i < count; i++) {

            String[] splitted = st.nextToken().split(ShapeFromFile.SEPERATOR);
            v[i] = Integer.parseInt(splitted[0], 10);

            if (splitted.length > 1) {

                if (splitted[1].length() != 0) {

                    this.vtToProcess = true;
                    vt[i] = Integer.parseInt(splitted[1], 10);

                }

                if (splitted.length == 3) {

                    this.vnToProcess = true;
                    vn[i] = Integer.parseInt(splitted[2], 10);

                }
            }
        }

        vertexFaces.add(new Point3(v[0], v[1], v[2]));

        if (this.vtToProcess) {

            vertexTextureFaces.add(new Point3(vt[0], vt[1], vt[2]));
        }
        if (this.vnToProcess) {

            vertexNormalFaces.add(vn[0]);

        }
    }

    private void processVertex (final String line) {

        final float coords[] = new float[3];
        StringTokenizer st = new StringTokenizer(line, ShapeFromFile.SPLITTER);

        for (int i = 0; st.hasMoreTokens(); i++) {

            coords[i] = Float.parseFloat(st.nextToken());

        }

        vertexPoints.add(new Point3(coords[0], coords[1], coords[2]));
    }

    private void vertexNormal (final String line) {

        final float coords[] = new float[3];
        StringTokenizer st = new StringTokenizer(line, ShapeFromFile.SPLITTER);

        for (int i = 0; st.hasMoreTokens(); i++) {

            coords[i] = Float.parseFloat(st.nextToken());
        }

        vertexNormalPoints.add(new Normal3(coords[0], coords[1], coords[2]));
    }

    private void vertexTexture (final String line) {

        final float coords[] = new float[3];
        StringTokenizer st = new StringTokenizer(line, ShapeFromFile.SPLITTER);

        for (int i = 0; st.hasMoreTokens(); i++) {

            coords[i] = Float.parseFloat(st.nextToken());

        }
        vertexTexturePoints.add(new Point3(coords[0], coords[1], coords[2]));
    }


    @Override
    public Hit hit(Ray r) {

        return null;
    }

    @Override
    public String toString() {
        return "ShapeFromFile{" +
                "triangles=" + triangles +
                ", vertexPoints=" + vertexPoints +
                ", vertexNormalPoints=" + vertexNormalPoints +
                ", vertexTexturePoints=" + vertexTexturePoints +
                ", vertexFaces=" + vertexFaces +
                ", vertexNormalFaces=" + vertexNormalFaces +
                ", vertexTextureFaces=" + vertexTextureFaces +
                ", currentLineNumber=" + currentLineNumber +
                ", currentFaceNumber=" + currentFaceNumber +
                ", vtToProcess=" + vtToProcess +
                ", vnToProcess=" + vnToProcess +
                ", fileName='" + modelName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ShapeFromFile that = (ShapeFromFile) o;

        if (currentLineNumber != that.currentLineNumber) return false;
        if (currentFaceNumber != that.currentFaceNumber) return false;
        if (vtToProcess != that.vtToProcess) return false;
        if (vnToProcess != that.vnToProcess) return false;
        if (triangles != null ? !triangles.equals(that.triangles) : that.triangles != null) return false;
        if (vertexPoints != null ? !vertexPoints.equals(that.vertexPoints) : that.vertexPoints != null) return false;
        if (vertexNormalPoints != null ? !vertexNormalPoints.equals(that.vertexNormalPoints) : that.vertexNormalPoints != null)
            return false;
        if (vertexTexturePoints != null ? !vertexTexturePoints.equals(that.vertexTexturePoints) : that.vertexTexturePoints != null)
            return false;
        if (vertexFaces != null ? !vertexFaces.equals(that.vertexFaces) : that.vertexFaces != null) return false;
        if (vertexNormalFaces != null ? !vertexNormalFaces.equals(that.vertexNormalFaces) : that.vertexNormalFaces != null)
            return false;
        if (vertexTextureFaces != null ? !vertexTextureFaces.equals(that.vertexTextureFaces) : that.vertexTextureFaces != null)
            return false;
        return !(modelName != null ? !modelName.equals(that.modelName) : that.modelName != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (triangles != null ? triangles.hashCode() : 0);
        result = 31 * result + (vertexPoints != null ? vertexPoints.hashCode() : 0);
        result = 31 * result + (vertexNormalPoints != null ? vertexNormalPoints.hashCode() : 0);
        result = 31 * result + (vertexTexturePoints != null ? vertexTexturePoints.hashCode() : 0);
        result = 31 * result + (vertexFaces != null ? vertexFaces.hashCode() : 0);
        result = 31 * result + (vertexNormalFaces != null ? vertexNormalFaces.hashCode() : 0);
        result = 31 * result + (vertexTextureFaces != null ? vertexTextureFaces.hashCode() : 0);
        result = 31 * result + currentLineNumber;
        result = 31 * result + currentFaceNumber;
        result = 31 * result + (vtToProcess ? 1 : 0);
        result = 31 * result + (vnToProcess ? 1 : 0);
        result = 31 * result + (modelName != null ? modelName.hashCode() : 0);
        return result;
    }
}