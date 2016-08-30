package com.aoaoyi.screenrecorder.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.aoaoyi.screenrecorder.R;
import com.aoaoyi.screenrecorder.util.MLog;

import java.util.ArrayList;

/**
 * Created by yuzhenbei on 2016/6/3.
 */
public class VideoRangeSeekBar extends View {
    /** TAG */
    private static final String TAG = VideoRangeSeekBar.class.getSimpleName();
    /** sync lock */
    private final byte[] SYNC_LOCK = new byte[0];
    private Drawable mPickDrawable = null;
    private int mPickDrawableWidth = 0;
    private int mPickDrawableHeight = 0;
    private int mFrameMarginTop = 2;
    private int mFrameMarginLeft = 4;
    private Paint mPaint;
    private Paint mPaint2;
    private Paint mPaintText;
    private long mVideoDuration = 0;
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;
    private long mFrameTimeOffset = 0;
    private int mFramesToLoad = 0;
    private int mFrameWidth = 0;
    private int mFrameHeight = 0;
    private float mProgressLeft = 0;
    private float mProgressRight = 1;
    private boolean mPressedLeft = false;
    private boolean mPressedRight = false;
    private float mPressDx = 0;
    private int mTextSize = 15;
    private int mStrokeWidth = 2;

    private OnProgressChanged mOnProgressChanged;
    private MediaMetadataRetriever mMediaMetadataRetriever = null;
    private ArrayList<Bitmap> mFrames = new ArrayList<>();
    private AsyncTask<Integer, Integer, Bitmap> mLoadFramesTask = null;

    public VideoRangeSeekBar(Context context) {
        this(context, null);
    }

    public VideoRangeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoRangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoRangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context pContext){
        DisplayMetrics _DM = getResources().getDisplayMetrics();
        mFrameMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mFrameMarginTop, _DM);
        mFrameMarginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mFrameMarginLeft, _DM);
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, _DM);
        mStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mStrokeWidth, _DM);

        mPickDrawable = getResources().getDrawable(R.drawable.videotrimmer);
        if (null != mPickDrawable) {
            mPickDrawableWidth = mPickDrawable.getIntrinsicWidth();
            mPickDrawableHeight = mPickDrawable.getIntrinsicHeight();
        }
        mPaint = new Paint();
        mPaint.setColor(0xff66d1ee);
        mPaint2 = new Paint();
        mPaint2.setColor(0x7f000000);
        mPaintText = new Paint();
        mPaintText.setColor(0xffff0000);
        mPaintText.setStrokeWidth(mStrokeWidth);
        mPaintText.setTextSize(mTextSize);
        mPaintText.setTextAlign(Paint.Align.LEFT);
        mPaintText.setAntiAlias(true);
        mPaintText.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        float _X = event.getX();
        float _Y = event.getY();
        int _Width = getMeasuredWidth() - mPickDrawableWidth;
        int _StartX = (int)(_Width * mProgressLeft) + mPickDrawableWidth / 2;
        int _EndX = (int)(_Width * mProgressRight) + mPickDrawableWidth / 2;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int _AdditionWidth = mPickDrawableWidth - mFrameMarginLeft;
            if (_StartX - _AdditionWidth <= _X && _X <= _StartX + _AdditionWidth && _Y >= 0 && _Y <= getMeasuredHeight()) {
                mPressedLeft = true;
                mPressDx = (int)(_X - _StartX);
                getParent().requestDisallowInterceptTouchEvent(true);
                invalidate();
                return true;
            } else if (_EndX - _AdditionWidth <= _X && _X <= _EndX + _AdditionWidth && _Y >= 0 && _Y <= getMeasuredHeight()) {
                mPressedRight = true;
                mPressDx = (int)(_X - _EndX);
                getParent().requestDisallowInterceptTouchEvent(true);
                invalidate();
                return true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (mPressedLeft) {
                mPressedLeft = false;
                invalidate();
                return true;
            } else if (mPressedRight) {
                mPressedRight = false;
                invalidate();
                return true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mPressedLeft) {
                _StartX = (int)(_X - mPressDx);
                if (_StartX < mPickDrawableWidth / 2) {
                    _StartX = mPickDrawableWidth / 2;
                } else if (_StartX > _EndX) {
                    _StartX = _EndX;
                }
                mProgressLeft = (float)(_StartX - mPickDrawableWidth / 2) / (float)_Width;
                if (mOnProgressChanged != null) {
                    mOnProgressChanged.onLeftProgressChanged(mProgressLeft);
                }
                invalidate();
                return true;
            } else if (mPressedRight) {
                _EndX = (int)(_X - mPressDx);
                if (_EndX < _StartX) {
                    _EndX = _StartX;
                } else if (_EndX > _Width + mPickDrawableWidth / 2) {
                    _EndX = _Width + mPickDrawableWidth / 2;
                }
                mProgressRight = (float)(_EndX - mPickDrawableWidth / 2) / (float)_Width;
                if (mOnProgressChanged != null) {
                    mOnProgressChanged.onRightProgressChanged(mProgressRight);
                }
                invalidate();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int _Width = getMeasuredWidth() - mPickDrawableWidth - mFrameMarginLeft;
        int _Height = getMeasuredHeight();
        int _StartX = (int)(_Width * mProgressLeft) + mPickDrawableWidth / 2;
        int _EndX = (int)(_Width * mProgressRight) + mPickDrawableWidth / 2;
        canvas.save();
        //frame show
        canvas.clipRect(mPickDrawableWidth / 2, _Height / 2, _Width + mPickDrawableWidth / 2 + mFrameMarginLeft, _Height);
        if (mFrames.isEmpty() && mLoadFramesTask == null) {
            loadFrames(0);
        } else {
            int _Offset = 0;
            for (Bitmap bitmap : mFrames) {
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, mPickDrawableWidth / 2 + _Offset * mFrameWidth, _Height / 2 + mFrameMarginTop, null);
                }
                _Offset++;
            }
        }

        //left trim
        canvas.drawRect(mPickDrawableWidth / 2, _Height / 2 + mFrameMarginTop, _StartX, _Height - mFrameMarginTop, mPaint2);
        //right trim
        canvas.drawRect(_EndX + mFrameMarginLeft, _Height / 2 + mFrameMarginTop, mPickDrawableWidth / 2 + _Width + mFrameMarginLeft, _Height - mFrameMarginTop, mPaint2);
        //left line
        canvas.drawRect(_StartX, _Height / 2, _StartX + mFrameMarginTop, _Height, mPaint);
        //right line
        canvas.drawRect(_EndX + mFrameMarginTop, _Height / 2, _EndX + mFrameMarginLeft, _Height, mPaint);
        //top line
        canvas.drawRect(_StartX + mFrameMarginTop, _Height / 2, _EndX + mFrameMarginLeft, _Height / 2 + mFrameMarginTop , mPaint);
        //bottom line
        canvas.drawRect(_StartX + mFrameMarginTop, _Height - mFrameMarginTop, _EndX + mFrameMarginLeft, _Height, mPaint);
        canvas.restore();
        //left seek
        mPickDrawable.setBounds(_StartX - mPickDrawableWidth / 2, _Height / 2 + (_Height / 2 - mPickDrawableHeight) / 2, _StartX + mPickDrawableWidth / 2, _Height - (_Height / 2 - mPickDrawableHeight) / 2);
        mPickDrawable.draw(canvas);
        //right seek
        mPickDrawable.setBounds(_EndX - mPickDrawableWidth / 2 + mFrameMarginLeft, _Height / 2 + (_Height / 2 - mPickDrawableHeight) / 2, _EndX + mPickDrawableWidth / 2  + mFrameMarginLeft, _Height - (_Height / 2 - mPickDrawableHeight) / 2);
        mPickDrawable.draw(canvas);
        //left time
        if (mProgressLeft > 0 && mPressedLeft){
            String _LeftTime = getFormatTime(mProgressLeft);
            Rect _LeftBounds = new Rect();
            mPaintText.getTextBounds(_LeftTime, 0, _LeftTime.length(), _LeftBounds);
            Paint.FontMetricsInt _LeftFontMetrics = mPaintText.getFontMetricsInt();
            int _LeftBaseline = (_Height / 2 - _LeftFontMetrics.bottom + _LeftFontMetrics.top) / 2 - _LeftFontMetrics.top;
            canvas.drawText(_LeftTime, _StartX, _LeftBaseline, mPaintText);
        }
        //right time
        if (mProgressRight < 1 && mPressedRight){
            String _RightTime = getFormatTime(mProgressRight);
            Rect _RightBounds = new Rect();
            mPaintText.getTextBounds(_RightTime, 0, _RightTime.length(), _RightBounds);
            Paint.FontMetricsInt _RightFontMetrics = mPaintText.getFontMetricsInt();
            int _RightBaseline = (_Height / 2 - _RightFontMetrics.bottom + _RightFontMetrics.top) / 2 - _RightFontMetrics.top;
            canvas.drawText(_RightTime, _EndX - _RightBounds.width(), _RightBaseline, mPaintText);
        }

    }

    public void setVideoPath(String pVideoFilePath) {
        mMediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mMediaMetadataRetriever.setDataSource(pVideoFilePath);
            String _VideoDuration = mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (!TextUtils.isEmpty(_VideoDuration)){
                mVideoDuration = Long.parseLong(_VideoDuration);
            }else {
                mVideoDuration = 0;
            }
            String _VideoWidth = mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            if (!TextUtils.isEmpty(_VideoWidth)){
                mVideoWidth = Integer.valueOf(_VideoWidth);
            }else {
                mVideoWidth = 480;
            }
            String _VideoHeight = mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            if (!TextUtils.isEmpty(_VideoHeight)){
                mVideoHeight = Integer.valueOf(_VideoHeight);
            }else {
                mVideoHeight = 800;
            }
        } catch (Exception e) {
            MLog.e(TAG, e.toString());
        }
    }

    private void loadFrames(int pFrameNum) {
        if (mMediaMetadataRetriever == null) {
            return;
        }
        if (pFrameNum == 0) {
            mFrameHeight = getMeasuredHeight() / 2 - mFrameMarginTop * 2;
            int _Be = mVideoHeight / mFrameHeight;
            if(_Be <= 0){
                _Be = 1;
            }
            mFrameWidth = mVideoWidth / _Be;
            mFramesToLoad = (getMeasuredWidth() - mPickDrawableWidth / 2) / mFrameWidth;
            mFrameTimeOffset = mVideoDuration / mFramesToLoad;
        }
        mLoadFramesTask = new AsyncTask<Integer, Integer, Bitmap>() {
            private int _FrameNum = 0;

            @Override
            protected Bitmap doInBackground(Integer... objects) {
                _FrameNum = objects[0];
                Bitmap _Bitmap = null;
                if (isCancelled()) {
                    return null;
                }
                try {
                    _Bitmap = mMediaMetadataRetriever.getFrameAtTime(mFrameTimeOffset * _FrameNum * 1000);
                    if (isCancelled()) {
                        return null;
                    }
                    if (_Bitmap != null) {
                        Bitmap _BitmapResult = Bitmap.createBitmap(mFrameWidth, mFrameHeight, _Bitmap.getConfig());
                        Canvas _Canvas = new Canvas(_BitmapResult);
                        float _ScaleX = (float) mFrameWidth / (float) _Bitmap.getWidth();
                        float _ScaleY = (float) mFrameHeight / (float) _Bitmap.getHeight();
                        float _Scale = _ScaleX > _ScaleY ? _ScaleX : _ScaleY;
                        int _W = (int) (_Bitmap.getWidth() * _Scale);
                        int _H = (int) (_Bitmap.getHeight() * _Scale);
                        Rect _SrcRect = new Rect(0, 0, _Bitmap.getWidth(), _Bitmap.getHeight());
                        Rect _DestRect = new Rect((mFrameWidth - _W) / 2, (mFrameHeight - _H) / 2, _W, _H);
                        _Canvas.drawBitmap(_Bitmap, _SrcRect, _DestRect, null);
                        _Bitmap.recycle();
                        _Bitmap = _BitmapResult;
                    }
                } catch (Exception e) {
                    MLog.e(TAG, e.toString());
                }
                return _Bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (!isCancelled()) {
                    mFrames.add(bitmap);
                    invalidate();
                    if (_FrameNum < mFramesToLoad) {
                        loadFrames(_FrameNum + 1);
                    }
                }
            }
        };

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            mLoadFramesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pFrameNum, null, null);
        } else {
            mLoadFramesTask.execute(pFrameNum, null, null);
        }
    }

    private String getFormatTime(float pProgress){
        long _Duration = (long)(Math.ceil(pProgress * mVideoDuration) / 1000);
        long _Hours = _Duration / 3600;
        long _Minutes = (_Duration % 3600) / 60;
        long _Seconds = _Duration % 60;
        if (_Hours > 0){
            return String.format("%02d:%02d:%02d", _Hours, _Minutes, _Seconds);
        }else{
            return String.format("%02d:%02d", _Minutes, _Seconds);
        }
    }

    public void setOnProgressChanged(OnProgressChanged onProgressChanged) {
        mOnProgressChanged = onProgressChanged;
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }

    public int getVideoWidth() {
        return mVideoWidth;
    }

    public long getVideoDuration() {
        return mVideoDuration;
    }

    public float getProgressRight() {
        return mProgressRight;
    }

    public float getProgressLeft() {
        return mProgressLeft;
    }


    public void clearFrames() {
        for (Bitmap bitmap : mFrames) {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        mFrames.clear();
        if (mLoadFramesTask != null) {
            mLoadFramesTask.cancel(true);
            mLoadFramesTask = null;
        }
        invalidate();
    }

    public void destroy() {
        synchronized (SYNC_LOCK) {
            try {
                if (mMediaMetadataRetriever != null) {
                    mMediaMetadataRetriever.release();
                    mMediaMetadataRetriever = null;
                }
            } catch (Exception e) {
                MLog.e(TAG, e.toString());
            }
        }
        for (Bitmap bitmap : mFrames) {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        mFrames.clear();
        if (mLoadFramesTask != null) {
            mLoadFramesTask.cancel(true);
            mLoadFramesTask = null;
        }
    }

    public interface OnProgressChanged {
        void onLeftProgressChanged(float progress);
        void onRightProgressChanged(float progress);
    }
}
