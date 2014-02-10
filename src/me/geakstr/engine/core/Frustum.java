package me.geakstr.engine.core;

import me.geakstr.engine.math.Matrix4f;

public class Frustum {
    private float frustum[][];

    public Frustum() {
        frustum = new float[6][4];
    }

    public void update(final Matrix4f projection, final Matrix4f view) {
        Matrix4f a = Matrix4f.mul(projection, view, null);

        // Extract the numbers for the Right plane
        frustum[0][0] = a.m03 - a.m00;
        frustum[0][1] = a.m13 - a.m10;
        frustum[0][2] = a.m23 - a.m20;
        frustum[0][3] = a.m33 - a.m30;

        // Left
        frustum[1][0] = a.m03 + a.m00;
        frustum[1][1] = a.m13 + a.m10;
        frustum[1][2] = a.m23 + a.m20;
        frustum[1][3] = a.m33 + a.m30;

        // Bottom
        frustum[2][0] = a.m03 + a.m01;
        frustum[2][1] = a.m13 + a.m11;
        frustum[2][2] = a.m23 + a.m21;
        frustum[2][3] = a.m33 + a.m31;

        // Top
        frustum[3][0] = a.m03 - a.m01;
        frustum[3][1] = a.m13 - a.m11;
        frustum[3][2] = a.m23 - a.m21;
        frustum[3][3] = a.m33 - a.m31;

        // Far
        frustum[4][0] = a.m03 - a.m02;
        frustum[4][1] = a.m13 - a.m12;
        frustum[4][2] = a.m23 - a.m22;
        frustum[4][3] = a.m33 - a.m32;

        // Near
        frustum[5][0] = a.m03 + a.m02;
        frustum[5][1] = a.m13 + a.m12;
        frustum[5][2] = a.m23 + a.m22;
        frustum[5][3] = a.m33 + a.m32;

        // Normalize
        for (int i = 0; i < 6; i++) {
            float t = (float) Math.sqrt(frustum[i][0] * frustum[i][0] + frustum[i][1] * frustum[i][1] + frustum[i][2] * frustum[i][2]);
            for (int j = 0; j < 4; j++) {
                frustum[i][j] /= t;
            }
        }
    }

    public int checkCube(float x, float y, float z, float size) {
        int c2 = 0;
        for (int p = 0; p < 6; p++) {
            int c = 0;
            if (frustum[p][0] * (x - size) + frustum[p][1] * (y - size) + frustum[p][2] * (z - size) + frustum[p][3] > 0)
                c++;
            if (frustum[p][0] * (x + size) + frustum[p][1] * (y - size) + frustum[p][2] * (z - size) + frustum[p][3] > 0)
                c++;
            if (frustum[p][0] * (x - size) + frustum[p][1] * (y + size) + frustum[p][2] * (z - size) + frustum[p][3] > 0)
                c++;
            if (frustum[p][0] * (x + size) + frustum[p][1] * (y + size) + frustum[p][2] * (z - size) + frustum[p][3] > 0)
                c++;
            if (frustum[p][0] * (x - size) + frustum[p][1] * (y - size) + frustum[p][2] * (z + size) + frustum[p][3] > 0)
                c++;
            if (frustum[p][0] * (x + size) + frustum[p][1] * (y - size) + frustum[p][2] * (z + size) + frustum[p][3] > 0)
                c++;
            if (frustum[p][0] * (x - size) + frustum[p][1] * (y + size) + frustum[p][2] * (z + size) + frustum[p][3] > 0)
                c++;
            if (frustum[p][0] * (x + size) + frustum[p][1] * (y + size) + frustum[p][2] * (z + size) + frustum[p][3] > 0)
                c++;
            if (c == 0)
                return 0;
            if (c == 8)
                c2++;
        }
        return (c2 == 6) ? 2 : 1;
    }
}
