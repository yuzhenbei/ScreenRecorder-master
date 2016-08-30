package com.aoaoyi.screenrecorder.widget.drawview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.aoaoyi.screenrecorder.widget.drawview.draw.CanvasDrawer;

import java.util.ArrayList;

import com.aoaoyi.screenrecorder.widget.drawview.draw.PathDrawer;
import com.aoaoyi.screenrecorder.widget.drawview.draw.SerializablePath;
import com.aoaoyi.screenrecorder.widget.drawview.gestures.creator.GestureCreator;
import com.aoaoyi.screenrecorder.widget.drawview.gestures.creator.GestureCreatorListener;
import com.aoaoyi.screenrecorder.widget.drawview.gestures.scale.GestureScaleListener;
import com.aoaoyi.screenrecorder.widget.drawview.gestures.scale.GestureScaler;
import com.aoaoyi.screenrecorder.widget.drawview.gestures.scale.ScalerListener;
import com.aoaoyi.screenrecorder.widget.drawview.gestures.scroller.GestureScrollListener;
import com.aoaoyi.screenrecorder.widget.drawview.gestures.scroller.GestureScroller;
import com.aoaoyi.screenrecorder.widget.drawview.gestures.scroller.ScrollerListener;

public class DrawableView extends View
    implements View.OnTouchListener, ScrollerListener, GestureCreatorListener, ScalerListener {

  private final ArrayList<SerializablePath> paths = new ArrayList<>();

  private GestureScroller gestureScroller;
  private GestureScaler gestureScaler;
  private GestureCreator gestureCreator;
  private int canvasHeight;
  private int canvasWidth;
  private GestureDetector gestureDetector;
  private ScaleGestureDetector scaleGestureDetector;
  private PathDrawer pathDrawer;
  private CanvasDrawer canvasDrawer;
  private SerializablePath currentDrawingPath;

  public DrawableView(Context context) {
    super(context);
    init();
  }

  public DrawableView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public DrawableView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) public DrawableView(Context context, AttributeSet attrs,
                                                               int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    gestureScroller = new GestureScroller(this);
    gestureDetector = new GestureDetector(getContext(), new GestureScrollListener(gestureScroller));
    gestureScaler = new GestureScaler(this);
    scaleGestureDetector =
        new ScaleGestureDetector(getContext(), new GestureScaleListener(gestureScaler));
    gestureCreator = new GestureCreator(this);
    pathDrawer = new PathDrawer();
    canvasDrawer = new CanvasDrawer();
    setOnTouchListener(this);
  }

  public void setConfig(DrawableViewConfig config) {
    if (config == null) {
      throw new IllegalArgumentException("Paint configuration cannot be null");
    }
    canvasWidth = config.getCanvasWidth();
    canvasHeight = config.getCanvasHeight();
    gestureCreator.setConfig(config);
    gestureScaler.setZooms(config.getMinZoom(), config.getMaxZoom());
    gestureScroller.setCanvasBounds(canvasWidth, canvasHeight);
    canvasDrawer.setConfig(config);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    gestureScroller.setViewBounds(w, h);
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    scaleGestureDetector.onTouchEvent(event);
    gestureDetector.onTouchEvent(event);
    gestureCreator.onTouchEvent(event);
    invalidate();
    return true;
  }

  public void undo() {
    if (paths.size() > 0) {
      paths.remove(paths.size() - 1);
      invalidate();
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvasDrawer.onDraw(canvas);
    pathDrawer.onDraw(canvas, currentDrawingPath, paths);
  }

  public void clear() {
    paths.clear();
    invalidate();
  }

  public Bitmap obtainBitmap(Bitmap createdBitmap) {
    return pathDrawer.obtainBitmap(createdBitmap, paths);
  }

  public Bitmap obtainBitmap() {
    return obtainBitmap(Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888));
  }

  @Override
  protected Parcelable onSaveInstanceState() {
    DrawableViewSaveState state = new DrawableViewSaveState(super.onSaveInstanceState());
    state.setPaths(paths);
    return state;
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    if (!(state instanceof DrawableViewSaveState)) {
      super.onRestoreInstanceState(state);
    } else {
      DrawableViewSaveState ss = (DrawableViewSaveState) state;
      super.onRestoreInstanceState(ss.getSuperState());
      paths.addAll(ss.getPaths());
    }
  }

  @Override
  public void onViewPortChange(RectF currentViewport) {
    gestureCreator.onViewPortChange(currentViewport);
    canvasDrawer.onViewPortChange(currentViewport);
  }

  @Override
  public void onCanvasChanged(RectF canvasRect) {
    gestureCreator.onCanvasChanged(canvasRect);
    canvasDrawer.onCanvasChanged(canvasRect);
  }

  @Override
  public void onGestureCreated(SerializablePath serializablePath) {
    paths.add(serializablePath);
  }

  @Override
  public void onCurrentGestureChanged(SerializablePath currentDrawingPath) {
    this.currentDrawingPath = currentDrawingPath;
  }

  @Override
  public void onScaleChange(float scaleFactor) {
    gestureScroller.onScaleChange(scaleFactor);
    gestureCreator.onScaleChange(scaleFactor);
    canvasDrawer.onScaleChange(scaleFactor);
  }
}
