package me.geakstr.engine.model.Cube;

public enum CubeType {
    CUBE(new float[] {}, new float[] {}),
    TOP(new float[] {
            0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f,  0.5f,
            0.5f,  0.5f,  0.5f
    }, new float[] {
            0.999900f, 0.999899f,
            0.000100f, 0.999900f,
            0.999899f, 0.000100f,
            0.000100f, 0.999900f,
            0.000100f, 0.000100f,
            0.999899f, 0.000100f
    }),
    BOTTOM(new float[] {},new float[] {}),
    LEFT(new float[] {}, new float[] {}),
    RIGHT(new float[] {}, new float[] {}),
    NEAR(new float[] {}, new float[] {}),
    FAR(new float[] {}, new float[] {});

    private final float[] vertices;
    private final float[] texCoords;

    private CubeType(float[] vertices, float[] texCoords) {
        this.vertices = vertices;
        this.texCoords = texCoords;
    }

    public float[] vertices() {
        return vertices;
    }

    public float[] texCoords() {
        return texCoords;
    }
}
