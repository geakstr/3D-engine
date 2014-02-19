package me.geakstr.engine.model;

public class Cube extends Model {

    public static enum Type {
        TOP(new float[] {
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f
            },
            new float[] // normals
            {
                -0.0f, 1.0f, 0.0f
            },
            new float[] // texCoords
            {
                0.000100f, 0.999900f,
                0.999900f, 0.999899f,
                0.999899f, 0.000100f,
                0.000100f, 0.000100f
            }
        );

        private final float[] vertices;
        private final float[] normals;
        private final float[] texCoords;

        private Type(float[] vertices, float[] normals, float[] texCoords) {
            this.vertices = vertices;
            this.normals = normals;
            this.texCoords = texCoords;
        }

        public float[] vertices() {
            return vertices;
        }

        public float[] normals() {
            return normals;
        }

        public float[] texCoords() {
            return texCoords;
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
