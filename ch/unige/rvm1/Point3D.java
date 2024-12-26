/*
 * User: Carlo Jelmini
 * Date: Jan 15, 2002
 * Time: 10:30:20 PM
 */
package ch.unige.rvm1;

public class Point3D {

    public Point3D() {
    }

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setLocation(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setLocation(Point3D point3D) {
        x = point3D.x;
        y = point3D.y;
        z = point3D.z;
    }

    public void translate(float dx, float dy, float dz) {
        x += dx;
        y += dy;
        z += dz;
    }

    public float x,y,z;
}
