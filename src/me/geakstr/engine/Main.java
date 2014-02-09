package me.geakstr.engine;

import me.geakstr.engine.core.Camera;
import me.geakstr.engine.core.Game;
import me.geakstr.engine.core.Shader;
import me.geakstr.engine.core.Transform;
import me.geakstr.engine.math.Matrix4f;
import me.geakstr.engine.math.Vector3f;
import me.geakstr.engine.model.Model;

import me.geakstr.engine.model.ResourceBuffer;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Main extends Game {
	private Camera camera;
	private Shader baseShader;
	private Transform transform;

    private List<Model> models;
    Model box;

    int flag = 0;

    public Main(String resDir) {
        super(resDir);
    }

	public void init() {
        Display.setTitle("3D Engine");
		
		camera = new Camera();
		camera.setPosition(0, 0, -8);

        ResourceBuffer.loadModels("cube/cube.obj");
        ResourceBuffer.loadBuffers();

        models = new ArrayList<Model>();

        box = ResourceBuffer.getModels().get("cube/cube.obj");

        for (float z = 0, ang = 0; z <= 10; z += 2) {
            for (float y = 0; y <= 10; y += 2) {
                for (float x = 0; x <= 10; x += 2, ang += 1) {
                    models.add(box.clone(x, y, z, ang, ang, ang));
                }
            }
        }

        baseShader = new Shader();
        baseShader.attachVertexShader("shader.vert");
        baseShader.attachFragmentShader("shader.frag");
        baseShader.link();

		
		transform = new Transform();
		//transform.rotate(new Vector3f(120f, 120f, 120f));

        flag = 0;
		
		glCullFace(GL_FRONT_FACE);
	}

	public void update() {
		camera.input();
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		
		camera.apply();

        baseShader.bind();
        baseShader.setUniform("mProjection", camera.getProjectionMatrix());
        baseShader.setUniform("mView", camera.getViewMatrix());
        baseShader.setUniform("lightPos", camera.getPosition());
        baseShader.setUniform("texture", 0);
        baseShader.setUniform("mNormal", camera.getNormalMatrix());
        baseShader.setUniform("mTransform", transform.getTransform());

        for (Model model : models) {
            model.render(baseShader);
        }


        baseShader.unbind();
	}

	public void resized() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public void dispose() {
        for (Model model : models) model.dispose();
        baseShader.dispose();
	}

	public static void main(String[] args) {
		new Main(args[0]);
	}
}
