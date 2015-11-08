package com.example.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

public class CircularSeekbar extends View {

	private int backColor = Color.LTGRAY;
	private int foreColor = 0xff33b5e5;
	private CircleStyle style;
	private int barWidth;

	private Paint circlePaint, progressPaint, textPaint;
	private Point viewCenter = new Point();
	private Paint pointerInnerCircle, pointerMiddleCircle,
			pointerMiddleCircleTouched, pointerOuterCircle;

	private int pointerMiddleColor = 0x9633b5e5;
	private int pointerMiddleColorTouched = 0x6433b5e5;

	private boolean pointerTouched = false;
	private float[] lastPointerPosition = new float[2];

	private Path path = new Path();
	private Path pathProgress = new Path();
	private RectF oval = new RectF();
	private boolean invert = false;
	private String label;
	private int labelTextSize;
	private final int minAngleDefault = 270;
	private final int minAngleInvert = 90;
	private final int maxAngle = 180;
	private PathMeasure pathMeasure = new PathMeasure();
	private float[] pointerXY = new float[2];

	private int minProgress = 0;
	private int maxProgress = 100;
	private int progress = 0;

	private boolean pointerTouch = false;

	public CircularSeekbar(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.CircularSeekbar);
		try {
			invert = ta.getBoolean(R.styleable.CircularSeekbar_invert, false);
			this.setLabel(String.valueOf(ta
					.getText(R.styleable.CircularSeekbar_labelText)));
			this.setLabelTextSize(ta.getInt(
					R.styleable.CircularSeekbar_labelTextSize, 30));
		} finally {
			ta.recycle();
		}

		init();
	}

	private void init() {
		setPaint();
	}

	/**
	 * Function sets Paint properties.
	 */
	private void setPaint() {
		circlePaint = new Paint();
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(this.getBackColor());
		circlePaint.setDither(true);
		circlePaint.setStyle(Paint.Style.STROKE);
		circlePaint.setStrokeJoin(Paint.Join.ROUND);
		circlePaint.setStrokeCap(Paint.Cap.ROUND);
		circlePaint.setStrokeWidth(2);

		progressPaint = new Paint();
		progressPaint.setAntiAlias(true);
		progressPaint.setColor(this.getForeColor());
		progressPaint.setDither(true);
		progressPaint.setStyle(Paint.Style.STROKE);
		progressPaint.setStrokeJoin(Paint.Join.ROUND);
		progressPaint.setStrokeCap(Paint.Cap.ROUND);
		progressPaint.setStrokeWidth(5);

		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(this.getLabelTextSize());
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setAntiAlias(true);

		pointerInnerCircle = new Paint();
		pointerInnerCircle.setAntiAlias(true);
		pointerInnerCircle.setColor(this.getForeColor());
		pointerInnerCircle.setStyle(Paint.Style.FILL);

		pointerMiddleCircle = new Paint();
		pointerMiddleCircle.setAntiAlias(true);
		pointerMiddleCircle.setColor(this.pointerMiddleColor);
		pointerMiddleCircle.setStyle(Paint.Style.FILL);

		pointerMiddleCircleTouched = new Paint();
		pointerMiddleCircleTouched.setAntiAlias(true);
		pointerMiddleCircleTouched.setColor(this.pointerMiddleColorTouched);
		pointerMiddleCircleTouched.setStyle(Paint.Style.FILL);

		pointerOuterCircle = new Paint();
		pointerOuterCircle.setAntiAlias(true);
		pointerOuterCircle.setColor(this.getForeColor());
		pointerOuterCircle.setStyle(Paint.Style.STROKE);
		pointerOuterCircle.setStrokeJoin(Paint.Join.ROUND);
		pointerOuterCircle.setStrokeCap(Paint.Cap.ROUND);
		pointerOuterCircle.setStrokeWidth((float) 2.5);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		setPath();

		// Label zeichnen.
		if (invert) {
			canvas.drawText(this.getLabel(), this.getWidth()
					- getStringWidth(this.getLabel()), this.getHeight() / 2
					+ getStringHeight(this.getLabel()) / 2, textPaint);
		} else {
			canvas.drawText(this.getLabel(), 0, this.getHeight() / 2
					+ getStringHeight(this.getLabel()) / 2, textPaint);
		}

		// Kreise zeichnen.
		canvas.drawPath(path, circlePaint);
		canvas.drawPath(pathProgress, progressPaint);

		// Pointer zeichnen.
		setPointerXY();

		canvas.drawCircle(pointerXY[0], pointerXY[1], 6, pointerInnerCircle);

		if (pointerTouched) {
			canvas.drawCircle(pointerXY[0], pointerXY[1], 20,
					pointerMiddleCircleTouched);
			canvas.drawCircle(pointerXY[0], pointerXY[1], 20,
					pointerOuterCircle);
		} else {
			canvas.drawCircle(pointerXY[0], pointerXY[1], 20,
					pointerMiddleCircle);
		}

	}

	private void setPointerXY() {
		pathMeasure.setPath(pathProgress, false);
		if (pathMeasure.getPosTan(pathMeasure.getLength(), pointerXY, null)) {
			this.setLastPointerPosition(pointerXY);
		}
	}

	private void setPath() {
		viewCenter.x = this.getWidth() / 2;
		viewCenter.y = this.getHeight() / 2;

		if (invert == true) {
			oval.set(10, 10, this.getWidth() * 2, this.getHeight() - 10);

			path.addArc(oval, this.getMinAngleInvert(), this.getMaxAngle());

			pathProgress.addArc(oval, this.getMinAngleInvert(), progress);

		} else {
			oval.set(0 - this.getWidth(), 10, this.getWidth() - 10,
					this.getHeight() - 10);

			path.addArc(oval, this.getMinAngleDefault(), this.getMaxAngle());

			pathProgress.addArc(oval, this.getMinAngleInvert(), progress * -1);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		VelocityTracker velocity = VelocityTracker.obtain();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			if (event.getX() >= this.getLastPointerPosition()[0] - 20
					&& event.getX() <= this.getLastPointerPosition()[0] + 20
					&& event.getY() >= this.getLastPointerPosition()[1] - 20
					&& event.getY() <= this.getLastPointerPosition()[1] + 20)
				this.pointerTouch = true;
			else
				this.pointerTouch = false;

			return true;
		case MotionEvent.ACTION_UP:
			this.pointerTouch = false;
			return true;
		case MotionEvent.ACTION_MOVE:

			if (pointerTouch) {
				
				velocity.addMovement(event);
				velocity.computeCurrentVelocity(1000);
				float y_velocity = velocity.getYVelocity();
				System.out.println(y_velocity);
				
				if (this.getLastPointerPosition()[1] - event.getY() > 0) {
					// up
					this.progress = progress + 5;
					this.invalidate();
				} else {
					// down
					this.progress = progress - 5;
					this.invalidate();
				}
				
				velocity.recycle();
			}

			return true;
		default:
			return super.onTouchEvent(event);
		}
		
	}

	private enum CircleStyle {
		Circle, HalfCircle, Ellipse
	}

	public int getForeColor() {
		return foreColor;
	}

	public void setForeColor(int foreColor) {
		this.foreColor = foreColor;
	}

	public int getBackColor() {
		return backColor;
	}

	public void setBackColor(int backColor) {
		this.backColor = backColor;
	}

	public int getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(int barWidth) {
		this.barWidth = barWidth;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getLabelTextSize() {
		return labelTextSize;
	}

	public void setLabelTextSize(int labelTextSize) {
		this.labelTextSize = labelTextSize;
	}

	public CircleStyle getStyle() {
		return style;
	}

	public void setStyle(CircleStyle style) {
		this.style = style;
	}

	private int getStringWidth(String s) {

		Rect bounds = new Rect();
		Paint textPaint = new Paint();
		textPaint.setTextSize(this.getLabelTextSize());
		textPaint.getTextBounds(s, 0, s.length(), bounds);
		return bounds.width();
	}

	private int getStringHeight(String s) {

		Rect bounds = new Rect();
		Paint textPaint = new Paint();
		textPaint.setTextSize(this.getLabelTextSize());
		textPaint.getTextBounds(s, 0, s.length(), bounds);
		return bounds.height();
	}

	public int getMinAngleDefault() {
		return minAngleDefault;
	}

	public int getMinAngleInvert() {
		return minAngleInvert;
	}

	public int getMaxAngle() {
		return maxAngle;
	}

	private float[] getLastPointerPosition() {
		return lastPointerPosition;
	}

	private void setLastPointerPosition(float[] lastPointerPosition) {
		this.lastPointerPosition = lastPointerPosition;
	}

	public int getMinProgress() {
		return minProgress;
	}

	public void setMinProgress(int minProgress) {
		this.minProgress = minProgress;
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

}
