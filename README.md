Screen Recorder
=====
1.实现了屏幕录制功能，包括Default和Advanced两种模式。  
2.实现了MP4转gif功能，效果不好。
3.实现了流行视频编辑中的trim的两边拖动的VideoRangeSeekBar。
4.ArcMenu
解释：
=====
Default录制方式使用到的接口：
通过使用 [MediaProjectionManager](https://developer.android.com/reference/android/media/projection/MediaProjectionManager.html), [VirtualDisplay](https://developer.android.com/reference/android/hardware/display/VirtualDisplay.html), 
[AudioRecord](https://developer.android.com/reference/android/media/MediaRecorder.html)等API，故而仅支持Android 5.0。
Advanced录制方式：
通过使用 [MediaProjectionManager](https://developer.android.com/reference/android/media/projection/MediaProjectionManager.html), [VirtualDisplay](https://developer.android.com/reference/android/hardware/display/VirtualDisplay.html), 
[AudioRecord](https://developer.android.com/reference/android/media/AudioRecord.html),
[MediaCodec](http://developer.android.com/reference/android/media/MediaCodec.html) 以及 [MediaMuxer](http://developer.android.com/reference/android/media/MediaMuxer.html) 等API，故而仅支持Android 5.0。
