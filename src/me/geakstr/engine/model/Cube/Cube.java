package me.geakstr.engine.model.Cube;

import me.geakstr.engine.model.Model;

public class Cube extends Model {

    public static final float diffuse = 0.64f;

    public static enum Side {
        TOP {
            private final float[] vertices = new float[]{
                    0.5f, 0.5f, -0.5f,
                    -0.5f, 0.5f, -0.5f,
                    0.5f, 0.5f, 0.5f,
                    -0.5f, 0.5f, -0.5f,
                    -0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f
            };
            private final float[] normals = new float[]{
                    0f, 1f, 0f
            };
            private final float[] texCoords = new float[]{
                    0.999900f, 0.999899f,
                    0.000100f, 0.999900f,
                    0.999899f, 0.000100f,
                    0.000100f, 0.999900f,
                    0.000100f, 0.000100f,
                    0.999899f, 0.000100f
            };

            @Override
            public float[] vertices() {
                return vertices;
            }

            @Override
            public float[] normals() {
                return normals;
            }

            @Override
            public float[] texCoords() {
                return texCoords;
            }
        },
        BOTTOM {
            private final float[] vertices = new float[]{
                    0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, 0.5f,
                    -0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, 0.5f,
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, -0.5f, -0.5f
            };
            private final float[] normals = new float[]{
                    0f, -1f, 0f
            };
            private final float[] texCoords = new float[]{
                    0.999900f, 0.999900f,
                    0.000100f, 0.999900f,
                    0.999900f, 0.000100f,
                    0.000100f, 0.999900f,
                    0.000100f, 0.000100f,
                    0.999900f, 0.000100f
            };

            @Override
            public float[] vertices() {
                return vertices;
            }

            @Override
            public float[] normals() {
                return normals;
            }

            @Override
            public float[] texCoords() {
                return texCoords;
            }
        },
        LEFT {
            private final float[] vertices = new float[]{
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, 0.5f, 0.5f,
                    -0.5f, -0.5f, -0.5f,
                    -0.5f, 0.5f, 0.5f,
                    -0.5f, 0.5f, -0.5f,
                    -0.5f, -0.5f, -0.5f
            };
            private final float[] normals = new float[]{
                    -1f, 0f, 0f
            };
            private final float[] texCoords = new float[]{
                    1.000442f, -0.000441f,
                    1.000442f, 1.000441f,
                    -0.000442f, -0.000441f,
                    1.000442f, 1.000441f,
                    -0.000442f, 1.000442f,
                    -0.000442f, -0.000441f,
            };

            @Override
            public float[] vertices() {
                return vertices;
            }

            @Override
            public float[] normals() {
                return normals;
            }

            @Override
            public float[] texCoords() {
                return texCoords;
            }
        },
        RIGHT {
            private final float[] vertices = new float[]{
                    0.5f, -0.5f, -0.5f,
                    0.5f, 0.5f, -0.5f,
                    0.5f, -0.5f, 0.5f,
                    0.5f, 0.5f, -0.5f,
                    0.5f, 0.5f, 0.5f,
                    0.5f, -0.5f, 0.5f
            };
            private final float[] normals = new float[]{
                    1f, 0f, 0f
            };
            private final float[] texCoords = new float[]{
                    0.999900f, 0.000100f,
                    0.999900f, 0.999900f,
                    0.000100f, 0.000100f,
                    0.999900f, 0.999900f,
                    0.000100f, 0.999900f,
                    0.000100f, 0.000100f
            };

            @Override
            public float[] vertices() {
                return vertices;
            }

            @Override
            public float[] normals() {
                return normals;
            }

            @Override
            public float[] texCoords() {
                return texCoords;
            }
        },
        NEAR {
            private final float[] vertices = new float[]{
                    0.5f, -0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    -0.5f, -0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    -0.5f, 0.5f, 0.5f,
                    -0.5f, -0.5f, 0.5f
            };
            private final float[] normals = new float[]{
                    0f, 0f, 1f
            };
            private final float[] texCoords = new float[]{
                    0.999900f, 0.000100f,
                    0.999900f, 0.999900f,
                    0.000100f, 0.000100f,
                    0.999900f, 0.999900f,
                    0.000100f, 0.999900f,
                    0.000100f, 0.000100f
            };

            @Override
            public float[] vertices() {
                return vertices;
            }

            @Override
            public float[] normals() {
                return normals;
            }

            @Override
            public float[] texCoords() {
                return texCoords;
            }
        },
        FAR {
            private final float[] vertices = new float[]{
                    0.5f, 0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    -0.5f, 0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    -0.5f, -0.5f, -0.5f,
                    -0.5f, 0.5f, -0.5f
            };
            private final float[] normals = new float[]{
                    0f, 0f, -1f
            };
            private final float[] texCoords = new float[]{
                    -0.000441f, 1.000442f,
                    -0.000441f, -0.000442f,
                    1.000442f, 1.000442f,
                    -0.000441f, -0.000442f,
                    1.000442f, -0.000441f,
                    1.000442f, 1.000442f
            };

            @Override
            public float[] vertices() {
                return vertices;
            }

            @Override
            public float[] normals() {
                return normals;
            }

            @Override
            public float[] texCoords() {
                return texCoords;
            }
        };

        public abstract float[] vertices();

        public abstract float[] normals();

        public abstract float[] texCoords();
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
