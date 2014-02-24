package me.geakstr.engine;

import me.geakstr.engine.core.*;

import me.geakstr.engine.model.ResourceBuffer;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public class Main extends Game {
    private Camera camera;
    private Shader shader;
    private Transform transform;

    public Main(String resDir) {
        super(resDir);
    }

    public void init() {
        glClearColor(0.9f, 0.9f, 0.9f, 1f);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);

        camera = new Camera(70, (float) Display.getWidth() / (float) Display.getHeight(), 0.1f, 256f);
        camera.setPosition(0, 0, 0);

        shader = new Shader();
        shader.attachVertexShader("shader.vert");
        shader.attachFragmentShader("shader.frag");
        shader.link();

        transform = new Transform();

        ResourceBuffer.loadModels("cube/top.obj");
        ResourceBuffer.loadTextures("stone.png");

        World.init(64, 64, 64, ResourceBuffer.getModels().get("cube/top.obj").getId());

        World.gen();
        World.renderPrepare();
    }

    public void update(int delta) {
        camera.input();
        updateFPS();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        camera.apply();

        shader.bind();
        shader.setUniform("mProjection", camera.getProjectionMatrix());
        shader.setUniform("mView", camera.getViewMatrix());
        shader.setUniform("mNormal", camera.getNormalMatrix());
        shader.setUniform("mTransform", transform.getTransform());
        shader.setUniform("mModelTransform", new Transform().getTransform());

        World.render();

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
