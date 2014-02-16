package me.geakstr.engine;

import me.geakstr.engine.core.*;
import me.geakstr.engine.model.Model;

import me.geakstr.engine.model.ResourceBuffer;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public class Main extends Game {
    private Camera camera;
    private Frustum frustum;
    private Shader shader;
    private Transform transform;

    private boolean wasInput;

    public Main(String resDir) {
        super(resDir);
    }

    public void init() {
        glClearColor(0.9f, 0.9f, 0.9f, 1f);
        glClearDepth(1.0f);
        glEnable(GL_TEXTURE_2D);
        glDepthFunc(GL_LEQUAL);
        glEnable(GL_DEPTH_TEST);
        glShadeModel(GL_SMOOTH);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        camera = new Camera(70, (float) Display.getWidth() / (float) Display.getHeight(), 0.1f, 70f);
        camera.setPosition(0, 0, 0);

        frustum = new Frustum();

        shader = new Shader();
        shader.attachVertexShader("shader.vert");
        shader.attachFragmentShader("shader.frag");
        shader.link();

        transform = new Transform();

        ResourceBuffer.loadModels("cube/cube.obj", "cube/top.obj", "axe/axe.obj");
        ResourceBuffer.loadTextures("axe.png", "stone.png", "texture3.png");

        World.init(16, 16, 16, ResourceBuffer.getModels().get("cube/cube.obj").getId());

        World.gen();
        World.renderPrepare();

        wasInput = true;
    }

    public void update(int delta) {
        wasInput = camera.input();
        updateFPS();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        camera.apply();

        if (wasInput) {
            //frustum.update(camera.getProjectionMatrix(), camera.getViewMatrix());
        }

        shader.bind();
        shader.setUniform("mProjection", camera.getProjectionMatrix());
        shader.setUniform("mView", camera.getViewMatrix());
        shader.setUniform("mNormal", camera.getNormalMatrix());
        shader.setUniform("mTransform", transform.getTransform());

        World.render(shader);

        shader.unbind();
    }



    public void dispose() {
        World.renderDispose();
        shader.dispose();
    }

    public static void main(String[] args) {
        new Main(args[0]);
    }
}
