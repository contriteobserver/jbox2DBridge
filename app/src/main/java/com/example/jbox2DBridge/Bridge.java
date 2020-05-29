package com.example.jbox2DBridge;

import android.graphics.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.IDiffuseMethod;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.RectangularPrism;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.scene.Scene;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.lang.Math.toDegrees;

class Bridge {
    private HashMap<Body, Object3D> map;
    private Dynamics2D dynamics2D;
    private IDiffuseMethod lambert;
    private long mLastRender;

    Bridge() {
        dynamics2D = new Dynamics2D();
        lambert = new DiffuseMethod.Lambert();
        map = new HashMap<>();
    }

    void Update() {
        final long currentTime = System.nanoTime();
        final float delta = (currentTime - mLastRender) / 1e9f;
        mLastRender = currentTime;
        dynamics2D.Step(delta);
    }

    void InitKinematics(Scene scene) {
        Body floor = dynamics2D.CreateBarrier(new Vec2(7, 0), 10, 2);
        Material gray = new Material();
        gray.setColor(Color.LTGRAY);
        gray.setAmbientColor(Color.GRAY);
        gray.setDiffuseMethod(lambert);
        gray.enableLighting(true);
        Object3D floorPrism = new RectangularPrism(10,2,3);
        floorPrism.setPosition(floor.getPosition().x, floor.getPosition().y, 0);
        floorPrism.setMaterial(gray);
        //floorPrism.setDrawingMode(GLES20.GL_LINES);
        scene.addChild(floorPrism);
    }

    void InitDynamics(Scene scene) {
        Body ball = dynamics2D.CreateBall(new Vec2(9.1f,15), null);
        Material cyan = new Material();
        cyan.setColor(Color.CYAN);
        cyan.setAmbientColor(Color.GRAY);
        cyan.setDiffuseMethod(lambert);
        cyan.enableLighting(true);
        Object3D sphere = new Sphere(1,12,12);
        sphere.setPosition(ball.getPosition().x, ball.getPosition().y, 0);
        sphere.setRotation(Vector3.NEG_Z, toDegrees(ball.getAngle()));
        sphere.setMaterial(cyan);
        //sphere.setDrawingMode(GLES20.GL_LINES);
        scene.addChild(sphere);
        map.put(ball, sphere);

        Body box = dynamics2D.CreateBox(new Vec2(9.3f,12), null);
        Material yellow = new Material();
        yellow.setColor(Color.YELLOW);
        yellow.setAmbientColor(Color.GRAY);
        yellow.setDiffuseMethod(lambert);
        yellow.enableLighting(true);
        Object3D cube = new Cube(2);
        cube.setPosition(box.getPosition().x, box.getPosition().y, 0);
        cube.setRotation(Vector3.NEG_Z, toDegrees(box.getAngle()));
        cube.setMaterial(yellow);
        //cube.setDrawingMode(GLES20.GL_LINES);
        scene.addChild(cube);
        map.put(box, cube);
    }

    void ClearDynamics(Scene scene) {
        for(Iterator<Map.Entry<Body, Object3D>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Body, Object3D> entry = it.next();
            dynamics2D.destroyBody(entry.getKey());
            scene.removeChild(entry.getValue());
            it.remove();
        }
    }

    void SyncDynamics(Scene scene) {
        for (Map.Entry<Body, Object3D> entry: map.entrySet()) {
            Body body = entry.getKey();
            Object3D object3D = entry.getValue();
            object3D.setPosition(body.getPosition().x, body.getPosition().y, 0);
            object3D.setRotation(Vector3.NEG_Z, toDegrees(body.getAngle()));
        }
    }

    void PruneDynamics(Scene scene) {
        for(Iterator<Map.Entry<Body, Object3D>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Body, Object3D> entry = it.next();
            if(entry.getKey().getPosition().y < -16) {
                dynamics2D.destroyBody(entry.getKey());
                scene.removeChild(entry.getValue());
                it.remove();
            }
        }
    }
}
