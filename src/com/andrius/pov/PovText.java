package com.andrius.pov;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

public class PovText {
	private static final long FRAME_LENGTH = 100;
	private static final int WINDOW_WIDTH = 1;
	private static final int SCALE = 20;
	
	private static Paint paint;
	private static Bitmap prerendered;

	private Rect source;
	private static Rect destination;
	private int stepCount;
	private int direction = 1;
	private long lastStep;
	
	public PovText(String text) {
		prerendered = textToBitmap(text, 15, Color.GREEN);
		int height = prerendered.getHeight();
		int width = prerendered.getWidth();
		source = new Rect();

		source.top = 0;
		source.left = 0;
		source.bottom = height - 1;
		source.right = WINDOW_WIDTH;

		destination = new Rect(source);
		destination.bottom += 5;
		destination.top += 5;
		destination.right += 5;
		destination.left += 5;
		stepCount = width;
	}

	private Bitmap textToBitmap(String text, float textSize, int textColor) {
		Paint paint = new Paint();
		paint.setTextSize(textSize);
		paint.setColor(textColor);
		paint.setTextAlign(Align.LEFT);

		int width = (int) (paint.measureText(text) + 0.5f);
		float baseline = (int) (-paint.ascent() + 0.5f);
		int height = (int) (baseline + paint.descent() + 0.5f);

		Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(image);
		canvas.drawText(text, 0, baseline, paint);
		return image;
	}

	public void draw(Canvas canvas, long deltaTime) {
		moveSource(deltaTime);
		
		canvas.setMatrix(null);
		canvas.scale(SCALE, SCALE);
		canvas.drawBitmap(prerendered, source, destination, paint);
		
	}

	private void moveSource(long deltaTime) {
		// Increase total time of this frame
		// If it is longer than max frame length move to next frame
		// Do that till frames catch up to current time
		// for(lastStep += deltaTime; lastStep > FRAME_LENGTH; lastStep %= FRAME_LENGTH) {
			source.left += direction;
			source.right += direction;
			if(source.left < 0) {
				source.left = stepCount - WINDOW_WIDTH;
				source.right = stepCount;
			}
			
			if(source.right > stepCount) {
				source.left = 0;
				source.right = WINDOW_WIDTH;
			}
		// }
	}
	
	public void swapDirection() {
		direction = -direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
}
