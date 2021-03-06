package de.uulm.mi.mind_android.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import de.uulm.mi.mind_android.R;

public class ScaleImageView extends ImageView implements OnTouchListener {
    private Context mContext;
    private float MAX_SCALE = 2f;

    private Matrix mMatrix;
    private final float[] mMatrixValues = new float[9];

    // display width height.
    private int mWidth;
    public int mHeight;

    private int mIntrinsicWidth;
    public int mIntrinsicHeight;

    private float mScale;
    private float mMinScale;

    private float mPrevDistance;
    private boolean isScaling;

    private int mPrevMoveX;
    private int mPrevMoveY;
    private GestureDetector mDetector;

    String TAG = "ScaleImageView";
    private int imgWidth;
    private int imgHeight;

    public ScaleImageView(Context context, AttributeSet attr) {
        super(context, attr);
        this.mContext = context;
        initialize();
    }

    public ScaleImageView(Context context) {
        super(context);
        this.mContext = context;
        initialize();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.initialize();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        this.initialize();
    }

    private void initialize() {
        // Get BMP so we can get its values:
        BitmapFactory.Options dime = new BitmapFactory.Options();
        dime.inJustDecodeBounds = true;
        Bitmap suxs = BitmapFactory.decodeResource(getResources(), R.drawable.o27_n3, dime);
        imgHeight = dime.outHeight;
        imgWidth = dime.outWidth;
        Log.d("SUXS", "Height: " + imgHeight);
        // -----------------------------------
        this.setScaleType(ScaleType.MATRIX);
        this.mMatrix = new Matrix();
        Drawable d = getDrawable();
        if (d != null) {
            mIntrinsicWidth = d.getIntrinsicWidth();
            mIntrinsicHeight = d.getIntrinsicHeight();
            setOnTouchListener(this);
        }
        mDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                maxZoomTo((int) e.getX(), (int) e.getY());
                cutting();
                return super.onDoubleTap(e);
            }
        });

    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        mWidth = r - l;
        mHeight = b - t;

        mMatrix.reset();
        int r_norm = r - l;
        mScale = (float) r_norm / (float) mIntrinsicWidth;

        int paddingHeight = 0;
        int paddingWidth = 0;
        // scaling vertical
        if (mScale * mIntrinsicHeight > mHeight) {
            mScale = (float) mHeight / (float) mIntrinsicHeight;
            mMatrix.postScale(mScale, mScale);
            paddingWidth = (r - mWidth) / 2;
            paddingHeight = 0;
            // scaling horizontal
        } else {
            mMatrix.postScale(mScale, mScale);
            paddingHeight = (b - mHeight) / 2;
            paddingWidth = 0;
        }
        mMatrix.postTranslate(paddingWidth, paddingHeight);

        setImageMatrix(mMatrix);
        mMinScale = mScale;
        zoomTo(mScale, mWidth / 2, mHeight / 2);
        cutting();
        return super.setFrame(l, t, r, b);
    }

    protected float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    public float getScale() {
        return getValue(mMatrix, Matrix.MSCALE_X);
    }

    private float getTranslateX() {
        return getValue(mMatrix, Matrix.MTRANS_X);
    }

    private float getTranslateY() {
        return getValue(mMatrix, Matrix.MTRANS_Y);
    }

    /**
     * Vodoo math magic.
     *
     * @return
     */
    public float getCoordX() {
        return (-getTranslateX() + (mWidth / 2)) / (getScale() * (mIntrinsicWidth / imgWidth));
    }

    /**
     * Also vodoo math magic.
     *
     * @return
     */
    public float getCoordY() {
        return (-getTranslateY() + (mHeight / 2)) / (getScale() * mIntrinsicHeight / imgHeight);
    }

    protected void maxZoomTo(int x, int y) {
        if (mMinScale != getScale() && (getScale() - mMinScale) > 0.1f) {
            // threshold 0.1f
            float scale = mMinScale / getScale();
            zoomTo(scale, x, y);
        } else {
            float scale = MAX_SCALE / getScale();
            zoomTo(scale, x, y);
        }
    }

    public void zoomTo(float scale, int x, int y) {
        if (getScale() * scale < mMinScale) {
            return;
        }
        if (scale >= 1 && getScale() * scale > MAX_SCALE) {
            return;
        }
        mMatrix.postScale(scale, scale);
        // move to center
        mMatrix.postTranslate(-(mWidth * scale - mWidth) / 2, -(mHeight * scale - mHeight) / 2);

        // move x and y distance
        mMatrix.postTranslate(-(x - (mWidth / 2)) * scale, 0);
        mMatrix.postTranslate(0, -(y - (mHeight / 2)) * scale);
        setImageMatrix(mMatrix);
    }

    public void cutting() {

        int paddingX = (mWidth / 2);
        int paddingY = (mHeight / 2);

        int width = (int) (mIntrinsicWidth * getScale());
        int height = (int) (mIntrinsicHeight * getScale());

        // right border
        if (getTranslateX() < -width + mWidth - paddingX) {
            mMatrix.postTranslate(-getTranslateX() - width + mWidth - paddingX, 0);
        }

        // left border
        if (getTranslateX() > paddingX) {
            mMatrix.postTranslate(-getTranslateX() + paddingX, 0);
        }

        // bottom border
        if (getTranslateY() < -height + mHeight - paddingY) {
            mMatrix.postTranslate(0, -getTranslateY() - height + mHeight - paddingY);
        }

        //top border
        if (getTranslateY() > paddingY) {
            mMatrix.postTranslate(0, -getTranslateY() + paddingY);
        }


/* //centers the view on zoom out, requires some adjustments
        if (width < mWidth) {
            mMatrix.postTranslate((mWidth - width) / 2, 0);
        }
        if (height < mHeight) {
            mMatrix.postTranslate(0, (mHeight - height) / 2);
        }*/
        setImageMatrix(mMatrix);
    }

    private float distance(float x0, float x1, float y0, float y1) {
        float x = x0 - x1;
        float y = y0 - y1;
        return FloatMath.sqrt(x * x + y * y);
    }

    private float dispDistance() {
        return FloatMath.sqrt(mWidth * mWidth + mHeight * mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector.onTouchEvent(event)) {
            return true;
        }
        int touchCount = event.getPointerCount();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_1_DOWN:
            case MotionEvent.ACTION_POINTER_2_DOWN:
                if (touchCount >= 2) {
                    float distance = distance(event.getX(0), event.getX(1), event.getY(0), event.getY(1));
                    mPrevDistance = distance;
                    isScaling = true;
                } else {
                    mPrevMoveX = (int) event.getX();
                    mPrevMoveY = (int) event.getY();
                }
            case MotionEvent.ACTION_MOVE:
                if (touchCount >= 2 && isScaling) {
                    float dist = distance(event.getX(0), event.getX(1), event.getY(0), event.getY(1));
                    float scale = (dist - mPrevDistance) / dispDistance();
                    mPrevDistance = dist;
                    scale += 1;
                    scale = scale * scale;
                    zoomTo(scale, mWidth / 2, mHeight / 2);
                    cutting();
                } else if (!isScaling) {
                    int distanceX = mPrevMoveX - (int) event.getX();
                    int distanceY = mPrevMoveY - (int) event.getY();
                    mPrevMoveX = (int) event.getX();
                    mPrevMoveY = (int) event.getY();
                    mMatrix.postTranslate(-distanceX, -distanceY);
                    cutting();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_POINTER_2_UP:
                if (event.getPointerCount() <= 1) {
                    isScaling = false;
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouchEvent(event);
    }

}