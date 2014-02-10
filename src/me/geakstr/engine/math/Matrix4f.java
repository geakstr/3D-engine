package me.geakstr.engine.math;

public class Matrix4f extends org.lwjgl.util.vector.Matrix4f {
    private static final long serialVersionUID = 1L;

    public Matrix4f() {
        super();
    }

    public static Matrix4f mul(Matrix4f left, Matrix4f right, Matrix4f dest) {
        if (dest == null) dest = new Matrix4f();

        dest.m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03;
        dest.m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03;
        dest.m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03;
        dest.m03 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03;
        dest.m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13;
        dest.m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13;
        dest.m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13;
        dest.m13 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13;
        dest.m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23;
        dest.m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23;
        dest.m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23;
        dest.m23 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23;
        dest.m30 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33;
        dest.m31 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33;
        dest.m32 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33;
        dest.m33 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33;

        return dest;
    }

    public float determinant() {
        float f;
        f = m00
                * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32)
                - m13 * m22 * m31
                - m11 * m23 * m32
                - m12 * m21 * m33);
        f -= m01
                * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32)
                - m13 * m22 * m30
                - m10 * m23 * m32
                - m12 * m20 * m33);
        f += m02
                * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31)
                - m13 * m21 * m30
                - m10 * m23 * m31
                - m11 * m20 * m33);
        f -= m03
                * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31)
                - m12 * m21 * m30
                - m10 * m22 * m31
                - m11 * m20 * m32);
        return f;
    }

    private static float determinant3x3(float t00, float t01, float t02,
                                        float t10, float t11, float t12,
                                        float t20, float t21, float t22) {
        return t00 * (t11 * t22 - t12 * t21)
                + t01 * (t12 * t20 - t10 * t22)
                + t02 * (t10 * t21 - t11 * t20);
    }

    public static Matrix4f invert(Matrix4f src, Matrix4f dest) {
        float determinant = src.determinant();

        if (determinant != 0) {
            /*
             * m00 m01 m02 m03
			 * m10 m11 m12 m13
			 * m20 m21 m22 m23
			 * m30 m31 m32 m33
			 */
            if (dest == null)
                dest = new Matrix4f();
            float determinant_inv = 1f / determinant;

            // first row
            float t00 = determinant3x3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
            float t01 = -determinant3x3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
            float t02 = determinant3x3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
            float t03 = -determinant3x3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
            // second row
            float t10 = -determinant3x3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
            float t11 = determinant3x3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
            float t12 = -determinant3x3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
            float t13 = determinant3x3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
            // third row
            float t20 = determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32, src.m33);
            float t21 = -determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32, src.m33);
            float t22 = determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31, src.m33);
            float t23 = -determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31, src.m32);
            // fourth row
            float t30 = -determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22, src.m23);
            float t31 = determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22, src.m23);
            float t32 = -determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21, src.m23);
            float t33 = determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21, src.m22);

            // transpose and divide by the determinant
            dest.m00 = t00 * determinant_inv;
            dest.m11 = t11 * determinant_inv;
            dest.m22 = t22 * determinant_inv;
            dest.m33 = t33 * determinant_inv;
            dest.m01 = t10 * determinant_inv;
            dest.m10 = t01 * determinant_inv;
            dest.m20 = t02 * determinant_inv;
            dest.m02 = t20 * determinant_inv;
            dest.m12 = t21 * determinant_inv;
            dest.m21 = t12 * determinant_inv;
            dest.m03 = t30 * determinant_inv;
            dest.m30 = t03 * determinant_inv;
            dest.m13 = t31 * determinant_inv;
            dest.m31 = t13 * determinant_inv;
            dest.m32 = t23 * determinant_inv;
            dest.m23 = t32 * determinant_inv;
            return dest;
        } else
            return null;
    }

}
