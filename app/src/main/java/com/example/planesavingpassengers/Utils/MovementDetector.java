package com.example.planesavingpassengers.Utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.planesavingpassengers.Interfaces.MovementCallback;

public class MovementDetector {
    private MovementCallback movementCallback;
    private SensorManager sensorManager;
    private Sensor sensor/*accelerometerSensor*/;

    private long timeStemp = 0;

    private SensorEventListener sensorEventListener;

    public MovementDetector(Context context, MovementCallback _movementCallback/*, SensorManager sensorManager*/) {
        // Get the sensor manager
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE)/*sensorManager*/;
        // Get the accelerometer sensor
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Get the step counter sensor
        this.movementCallback = _movementCallback;

        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                // Get the sensor type
                float x = event.values[0];
//                float y = event.values[1];

                sensePlaneMovement(x/*, y*/);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void sensePlaneMovement(float x) {
        if (System.currentTimeMillis() - timeStemp > 100) {
            timeStemp = System.currentTimeMillis();
            if (x > 4.0) {
                // Notify the listener
                if (movementCallback != null) {
                    movementCallback.stepLeft();
                }
            }
            if (x < -4.0) {
                // Notify the listener
                if (movementCallback != null) {
                    movementCallback.stepRight();
                }
            }
        }
    }

    public void start() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}

