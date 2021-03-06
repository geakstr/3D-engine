package me.geakstr.engine.core;

import me.geakstr.engine.math.Vector3f;
import me.geakstr.engine.math.Matrix4f;

public class Transform {
	private Matrix4f translation;
	private Matrix4f rotation;
	private Matrix4f scale;
	
	public Transform() {
		translation = new Matrix4f();
		rotation = new Matrix4f();
		scale = new Matrix4f();
	}
	
	public void translate(Vector3f translation) {
		Matrix4f.translate(translation, this.translation, this.translation);
	}
	
	public void translate(float x, float y, float z) {
		translate(new Vector3f(x, y, z));
	}
	
	public void rotate(Vector3f rotation) {
		rotate(rotation.x, rotation.y, rotation.z);
	}
	
	public void rotate(float x, float y, float z) {
		Matrix4f.rotate((float) Math.toRadians(x), Vector3f.xAxis, rotation, rotation);
	    Matrix4f.rotate((float) Math.toRadians(y), Vector3f.yAxis, rotation, rotation);
	    Matrix4f.rotate((float) Math.toRadians(z), Vector3f.zAxis, rotation, rotation);
	}
	
	public void scale(Vector3f scale) {
		Matrix4f.scale(scale, this.scale, this.scale);
	}
	
	public void scale(float x, float y, float z) {
		scale(new Vector3f(x, y, z));
	}
	
	public Matrix4f getTransform() {
		return Matrix4f.mul(translation, Matrix4f.mul(rotation, scale, null), null);
	}
}
