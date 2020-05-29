package com.example.jbox2DBridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.SurfaceView;

public class MainActivity extends AppCompatActivity {
    Renderer renderer;
    SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        renderer = new SceneRenderer(this);
        surfaceView = findViewById(R.id.glsl_content);
        surfaceView.setSurfaceRenderer(renderer);
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                renderer.onTouchEvent(motionEvent);
                return false;
            }
        });
    }

    class SceneRenderer extends Renderer {
        boolean resetRequested = false;
        Bridge bridge = new Bridge();

        public SceneRenderer(Context context) {
            super(context);
        }

        @Override
        protected void initScene() {
            getCurrentScene().setBackgroundColor(Color.BLUE);

            DirectionalLight key = new DirectionalLight(-3,-4,-5);
            key.setPower(1);
            getCurrentScene().addLight(key);

            bridge.InitKinematics(getCurrentScene());
            bridge.InitDynamics(getCurrentScene());

            getCurrentCamera().setPosition(14,10,24);
            getCurrentCamera().setLookAt(7,5,0);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            resetRequested = true;
        }

        @Override
        protected void onRender(long ellapsedRealtime, double deltaTime) {
            super.onRender(ellapsedRealtime, deltaTime);
            bridge.Update();

            if(resetRequested) {
                bridge.ClearDynamics(getCurrentScene());
                bridge.InitDynamics(getCurrentScene());
                resetRequested = false;
            } else {
                bridge.SyncDynamics(getCurrentScene());
                bridge.PruneDynamics(getCurrentScene());
            }
        }

    }
}
