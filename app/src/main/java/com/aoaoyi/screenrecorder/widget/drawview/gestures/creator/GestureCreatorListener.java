package com.aoaoyi.screenrecorder.widget.drawview.gestures.creator;


import com.aoaoyi.screenrecorder.widget.drawview.draw.SerializablePath;

public interface GestureCreatorListener {
  void onGestureCreated(SerializablePath serializablePath);

  void onCurrentGestureChanged(SerializablePath currentDrawingPath);
}
