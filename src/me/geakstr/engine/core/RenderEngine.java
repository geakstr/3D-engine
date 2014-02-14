package me.geakstr.engine.core;

import me.geakstr.engine.model.Cube;
import me.geakstr.engine.model.ResourceBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL32.*;

public class RenderEngine {
    public static void start(int model) {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        if (ResourceBuffer.getTextured(model)) {
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            glBindTexture(GL_TEXTURE_2D, ResourceBuffer.getTexturesID(model));
            glBindBuffer(GL_ARRAY_BUFFER, ResourceBuffer.getVboTextureID(model));
            glTexCoordPointer(2, GL_FLOAT, 0, 0);
        }

        glBindBuffer(GL_ARRAY_BUFFER, ResourceBuffer.getVboNormalID(model));
        glNormalPointer(GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, ResourceBuffer.getVboColorlID(model));
        glColorPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, ResourceBuffer.getVboVertexID(model));
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ResourceBuffer.getVboIndexID(model));
    }

    private static void transform(int x, int y, int z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ, Shader shader) {
        Transform transform = new Transform();
        if (x != 0 || y != 0 || z != 0 || rotX != 0 || rotY != 0 || rotZ != 0 || scaleX != 1 || scaleY != 1 || scaleZ != 1) {
            if (x != 0 || y != 0 || z != 0) transform.translate(x, y, z);
            if (rotX != 0 || rotY != 0 || rotZ != 0) transform.rotate(rotX, rotY, rotZ);
            if (scaleX != 1 || scaleY != 1 || scaleZ != 1) transform.scale(scaleX, scaleY, scaleZ);
        }
        shader.setUniform("mModelTransform", transform.getTransform());
    }

    public static void render(int model, int x, int y, int z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ, Shader shader) {
        transform(x, y, z, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, shader);
        glDrawElements(GL_TRIANGLES, ResourceBuffer.getVboIndexSize(model), GL_UNSIGNED_INT, 0);
    }

    public static void render(int model, int x, int y, int z, Shader shader) {
        render(model, x, y, z, 0, 0, 0, 1, 1, 1, shader);
    }

    public static void renderCube(int x, int y, int z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ, Shader shader, Cube.Type[] sides) {
        if (sides == null || sides.length == 0 || (sides.length == 1 && sides[0] == Cube.Type.CUBE)) {
            render(World.getCubeId(), x, y, z, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, shader);
            return;
        }

        transform(x, y, z, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, shader);
        for (Cube.Type side : sides) {
            glDrawElementsBaseVertex(GL_TRIANGLES, 3, GL_UNSIGNED_INT, 0, side.getOffsetLeft());
            glDrawElementsBaseVertex(GL_TRIANGLES, 3, GL_UNSIGNED_INT, 0, side.getOffsetRight());
        }
    }

    public static void renderCube(int x, int y, int z, Shader shader, Cube.Type[] sides) {
        renderCube(x, y, z, 0, 0, 0, 1, 1, 1, shader, sides);
    }

    public static void end(int model) {
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
        if (ResourceBuffer.getTextured(model)) glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public static void dispose(int model) {
        glDeleteBuffers(ResourceBuffer.getVboVertexID(model));
        glDeleteBuffers(ResourceBuffer.getVboNormalID(model));
        glDeleteBuffers(ResourceBuffer.getVboColorlID(model));
        if (ResourceBuffer.getTextured(model)) glDeleteBuffers(ResourceBuffer.getVboTextureID(model));
    }

}
