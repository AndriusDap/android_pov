package com.andrius.pov;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PovViewer extends Activity {
	SurfaceView surfaceView;
	SurfaceHolder holder;
	Runnable task;
	Thread thread;
	PovText text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_pov);

		Intent i = this.getIntent();
		String message = i.getExtras().getString("Message");

		text = new PovText(message);
		surfaceView = (SurfaceView) findViewById(R.id.surface_view);
		holder = surfaceView.getHolder();
		task = new Runnable() {
			@Override
			public void run() {
				long time = System.currentTimeMillis();
				long oldTime = time;
				while (!Thread.interrupted()) {
					Canvas c = holder.lockCanvas();
					if (c != null) {
						text.draw(c, time - oldTime);
						holder.unlockCanvasAndPost(c);
					}

					oldTime = time;
					time = System.currentTimeMillis();
				}
			}
		};
		holder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				thread = new Thread(task);
				thread.start();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				thread.interrupt();
			}
		});

		SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

		sensorManager.registerListener(new SensorEventListener() {
			boolean peak = false;

			@Override
			public void onSensorChanged(SensorEvent event) {
				if (Math.abs(event.values[0]) > 15 && !peak) {
					peak = true;
					Log.v("Accelerometer", "Peak");
					text.swapDirection();
				} else {
					if (Math.abs(event.values[0]) < 5 && peak) {
						peak = false;
						Log.v("Accelerometer", "Not peak");
					}
				}
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}

		}, sensor, SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (thread != null) {
			thread.interrupt();
		}
	}
}
