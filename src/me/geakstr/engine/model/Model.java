package me.geakstr.engine.model;

import java.nio.FloatBuffer;

import me.geakstr.engine.math.Vector3f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;

public class Model {
    private int modelID;

    private float x, y, z;

    private int vboVertexID;

    public Model(int modelID) {
        this(modelID, 0, 0, 0);
    }

    public Model(int modelID, float x, float y, float z) {
        this.modelID = modelID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Model clone() {
        return new Model(modelID);
    }

    public void setup() {
        setup(x, y, z);
    }

    public void setup(float posX, float posY, float posZ) {
        x = posX;
        y = posY;
        z = posZ;

        vboVertexID = glGenBuffers();

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(9 * ResourceBuffer.getFaces(modelID).size());
        for (Face face : ResourceBuffer.getFaces(modelID)) {
            // Get the first vertex of the face
            Vector3f v1 = ResourceBuffer.getVertices(modelID).get((int) face.getVertex().x - 1);
            vertexBuffer.put(v1.x + posX).put(v1.y + posY).put(v1.z + posZ);

            // Get the second vertex of the face
            Vector3f v2 = ResourceBuffer.getVertices(modelID).get((int) face.getVertex().y - 1);
            vertexBuffer.put(v2.x + posX).put(v2.y + posY).put(v2.z + posZ);

            // Get the third vertex of the face
            Vector3f v3 = ResourceBuffer.getVertices(modelID).get((int) face.getVertex().z - 1);
            vertexBuffer.put(v3.x + posX).put(v3.y + posY).put(v3.z + posZ);
        }
        vertexBuffer.rewind();

        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        boolean textured = ResourceBuffer.getTextured(modelID);
        if (textured) glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, 9 * ResourceBuffer.getFaces(modelID).size());

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);

        if (textured) glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public void dispose() {
        glDeleteBuffers(vboVertexID);
        glDeleteBuffers(ResourceBuffer.getVboNormalID(modelID));
        glDeleteBuffers(ResourceBuffer.getVboColorlID(modelID));
        if (ResourceBuffer.getTextured(modelID)) glDeleteBuffers(ResourceBuffer.getVboTextureID(modelID));
    }


    public int getModelID() {
        return modelID;
    }

}
