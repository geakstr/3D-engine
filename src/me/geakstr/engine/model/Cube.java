package me.geakstr.engine.model;

public class Cube extends Model {

    public static enum Type {
        CUBE(-1), TOP(21), BOTTOM(18), LEFT(30), RIGHT(24), NEAR(27), FAR(33);

        private int offsetLeft, offsetRight;

        private Type(int offsetRight) {
            this.offsetRight = offsetRight;
            this.offsetLeft = offsetRight - 18;
        }

        public int getOffsetLeft() {
            return offsetLeft;
        }

        public int getOffsetRight() {
            return offsetRight;
        }
    }

    public Cube(int id, int x, int y, int z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
        super(id, x, y, z, rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
    }

    public Cube(int id, int x, int y, int z, float rotX, float rotY, float rotZ) {
        this(id, x, y, z, rotX, rotY, rotZ, 1, 1, 1);
    }

    public Cube(int id, int x, int y, int z) {
        this(id, x, y, z, 0, 0, 0, 1, 1, 1);
    }

    public Cube(int id) {
        this(id, 0, 0, 0);
    }


}
