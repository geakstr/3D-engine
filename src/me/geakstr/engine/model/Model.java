package me.geakstr.engine.model;

import me.geakstr.engine.core.Shader;
import me.geakstr.engine.core.Transform;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

public class Model {
    private int id;

    private int x, y, z;
    private float rotX, rotY, rotZ;
    private float scaleX, scaleY, scaleZ;
    private Transform transform;

    public Model(int id, int x, int y, int z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.transform = new Transform();
        if (x != 0 || y != 0 || z != 0) this.transform.translate(x, y, z);
        if (rotX != 0 || rotY != 0 || rotZ != 0) this.transform.rotate(rotX, rotY, rotZ);
        if (scaleX != 0 || scaleY != 0 || scaleZ != 0) this.transform.scale(scaleX, scaleY, scaleZ);
    }

    public Model(int id, int x, int y, int z, float rotX, float rotY, float rotZ) {
        this(id, x, y, z, rotX, rotY, rotZ, 0, 0, 0);
    }

    public Model(int id, int x, int y, int z) {
        this(id, x, y, z, 0, 0, 0, 0, 0, 0);
    }

    public Model(int id) {
        this(id, 0, 0, 0);
    }

    public Model clone(int x, int y, int z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
        return new Model(id, x, y, z, rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
    }

    public Model clone(int x, int y, int z, float rotX, float rotY, float rotZ) {
        return clone(x, y, z, rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
    }

    public Model clone(int x, int y, int z) {
        return clone(x, y, z, rotX, rotY, rotZ);
    }

    public Model clone(boolean clear) {
        return clear ? clone(0, 0, 0, 0, 0, 0, 0, 0, 0) : clone(x, y, z, rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
    }

    public Model clone() {
        return clone(false);
    }

    public static void render(int id, int x, int y, int z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ, Shader shader) {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        boolean isTextured = ResourceBuffer.getTextured(id);

        if (isTextured) {
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            glBindTexture(GL_TEXTURE_2D, ResourceBuffer.getTexturesID(id));
            glBindBuffer(GL_ARRAY_BUFFER, ResourceBuffer.getVboTextureID(id));
            glTexCoordPointer(2, GL_FLOAT, 0, 0);
        }

        glBindBuffer(GL_ARRAY_BUFFER, ResourceBuffer.getVboNormalID(id));
        glNormalPointer(GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, ResourceBuffer.getVboColorlID(id));
        glColorPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, ResourceBuffer.getVboVertexID(id));
        glVertexPointer(3, GL_FLOAT, 0, 0);

        if (x != 0 || y != 0 || z != 0 || rotX != 0 || rotY != 0 || rotZ != 0 || scaleX != 0 || scaleY != 0 || scaleZ != 0) {
            Transform transform = new Transform();
            if (x != 0 || y != 0 || z != 0) transform.translate(x, y, z);
            if (rotX != 0 || rotY != 0 || rotZ != 0) transform.rotate(rotX, rotY, rotZ);
            if (scaleX != 0 || scaleY != 0 || scaleZ != 0) transform.scale(scaleX, scaleY, scaleZ);
            shader.setUniform("mModelTransform", transform.getTransform());
        }
        glDrawArrays(GL_TRIANGLES, 0, 9 * ResourceBuffer.getFaces(id).size());
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
        if (isTextured) glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public void dispose() {
        glDeleteBuffers(ResourceBuffer.getVboVertexID(id));
        glDeleteBuffers(ResourceBuffer.getVboNormalID(id));
        glDeleteBuffers(ResourceBuffer.getVboColorlID(id));
        if (ResourceBuffer.getTextured(id)) glDeleteBuffers(ResourceBuffer.getVboTextureID(id));
    }


    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }

    public Transform getTransform() {
        return transform;
    }

}
