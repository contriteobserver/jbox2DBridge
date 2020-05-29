package com.example.jbox2DBridge;

import androidx.annotation.Nullable;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

class Dynamics2D {

    private World b2World;
    static private final int VELOCITY_ITERATIONS = 8;
    static private final int POSITION_ITERATIONS = 3;

    private enum ObjectType
    {
        Box,
        Floor,
        Ball
    }

    Dynamics2D()
    {
        b2World = new World(new Vec2(0.0f,-9.81f));
    }

    void Step(float delta)
    {
        b2World.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    void destroyBody(Body body) {
        b2World.destroyBody(body);
    }

    Body CreateBox(Vec2 position, @Nullable Vec2 velocity)
    {
        Vec2 v = velocity == null ? new Vec2() : velocity;

        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.angle = (float)Math.PI/4;
        bodyDef.linearVelocity = v;
        bodyDef.angularVelocity = 0.0f;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = true;
        bodyDef.gravityScale = 1.0f;
        bodyDef.linearDamping = 0.0f;
        bodyDef.angularDamping = 0.0f;
        bodyDef.userData = ObjectType.Box;
        bodyDef.type = BodyType.DYNAMIC;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = null;
        fixtureDef.friction = 0.35f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = false;

        Body body = b2World.createBody(bodyDef);
        body.createFixture(fixtureDef);
        return body;
    }

    Body CreateBall(Vec2 position, @Nullable Vec2 velocity)
    {
        Vec2 v = velocity == null ? new Vec2() : velocity;

        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.angle = 0f;
        bodyDef.linearVelocity = v;
        bodyDef.angularVelocity = 0.0f;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = true;
        bodyDef.gravityScale = 1.0f;
        bodyDef.linearDamping = 0.0f;
        bodyDef.angularDamping = 0.0f;
        bodyDef.userData = ObjectType.Ball;
        bodyDef.type = BodyType.DYNAMIC;

        CircleShape shape = new CircleShape();
        shape.setRadius(1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = null;
        fixtureDef.friction = 0.45f;
        fixtureDef.restitution = 0.75f;
        fixtureDef.density = 2.0f;
        fixtureDef.isSensor = false;

        Body body = b2World.createBody(bodyDef);
        body.createFixture(fixtureDef);
        return body;
    }

    Body CreateBarrier(Vec2 position, float width, float height)
    {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.angle = 0f;
        bodyDef.linearVelocity = new Vec2(0.0f,0.0f);
        bodyDef.angularVelocity = 0.0f;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = true;
        bodyDef.gravityScale = 1.0f;
        bodyDef.linearDamping = 0.0f;
        bodyDef.angularDamping = 0.0f;
        bodyDef.userData = ObjectType.Floor;
        bodyDef.type = BodyType.KINEMATIC;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2f, height/2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = null;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = false;

        Body body = b2World.createBody(bodyDef);
        body.createFixture(fixtureDef);
        return body;
    }
}
