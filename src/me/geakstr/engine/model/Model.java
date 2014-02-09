package me.geakstr.engine.model;

import me.geakstr.engine.core.Shader;
import me.geakstr.engine.core.Transform;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

public class Model {
    private int id;

    private float x, y, z;
    private float rotX, rotY, rotZ;
    private Transform transform;

    public Model(int id, float x, float y, float z, float rotX, float rotY, float rotZ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        transform = new Transform();
        if (x != 0 || y != 0 || z != 0) transform.translate(x, y, z);
        if (rotX != 0 || rotY != 0 || rotZ != 0) transform.rotate(rotX, rotY, rotZ);
    }

    public Model(int id, float x, float y, float z) {
        this(id, x, y, z, 0, 0, 0);
    }

    public Model(int id) {
        this(id, 0, 0, 0);
    }

    public Model clone() {
        return clone(x, y, z, rotX, rotY, rotZ);
    }

    public Model clone(float x, float y, float z) {
        return clone(x, y, z, rotX, rotY, rotZ);
    }

    public Model clone(float x, float y, float z, float rotX, float rotY, float rotZ) {
        return new Model(id, x, y, z, rotX, rotY, rotZ);
    }

    public void render(Shader shader) {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        boolean isTextured = ResourceBuffer.getTextured(id);
        if (isTextured) glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, ResourceBuffer.getVboVertexID(id));
        glVertexPointer(3, GL_FLOAT, 0, 0);

        shader.setUniform("mModelTransform", transform.getTransform());
        glDrawArrays(GL_TRIANGLES, 0, 9 * ResourceBuffer.getFaces(id).size());

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

}
