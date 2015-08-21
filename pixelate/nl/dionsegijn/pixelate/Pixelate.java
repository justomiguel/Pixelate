package nl.dionsegijn.pixelate;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Dion Segijn (danielmartinus) on 16/08/15.
 */
public class Pixelate extends ImageView {

    private boolean clearCanvas = false;
    private boolean isTouched = false;
    private boolean render = false;

    private int cols;
    public Bitmap drawingCache;
    private Paint paint = new Paint();

    /* Properties for drawing just a certain area on the canvas */
    private int touchedX;
    private int touchedY;
    private int touchedSize;

    public Pixelate(Context context) {
        super(context);
        setDrawingCacheEnabled(true);
    }

    public Pixelate(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
    }

    public Pixelate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDrawingCacheEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Pixelate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setDrawingCacheEnabled(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(clearCanvas) {
            canvas.drawColor(Color.TRANSPARENT);
            clearCanvas = false;
            return;
        }

        if(isTouched) {
            renderPixels(canvas);
            isTouched = false;
            return;
        }
        renderPixels(canvas);
    }

    public void clear() {
        clearCanvas = true;
        invalidate();
    }

    public void pixelate(int cols) {
        this.cols = cols;
        render = true;
        this.invalidate();
    }

    public void pixelateArea(int x, int y, int size, int density) {
        cols = density;
        touchedX = x;
        touchedY = y;
        touchedSize = size;
        isTouched = true;
        this.invalidate();
    }

    private void renderPixels(Canvas canvas) {
        if(!render && !isTouched) return;
        if(cols < 1) cols = 1;

        Bitmap bitmap = null;
        if(isDrawingCacheEnabled()) {
            bitmap = Bitmap.createBitmap(getDrawingCache());
            if(drawingCache == null) drawingCache = bitmap;
            else bitmap = drawingCache;
        } else {
            bitmap = ((BitmapDrawable)getDrawable()).getBitmap();
        }

        if(bitmap == null) throw new NullPointerException("View does not contain image");

        // get width and height of the entire bitmap
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int blockSize = 0;
        int startX = 0;
        int startY = 0;
        double rows = 0;
        if(isTouched) {
            blockSize = touchedSize / cols;
            startX = touchedX - (touchedSize / 2);
            startY = touchedY - (touchedSize / 2);
            rows = Math.round((double)touchedSize / (double)blockSize);
        } else {
            blockSize = width / cols;
            rows = Math.round((double)height / (double)blockSize);
        }

        for (int row = 0; row < rows; row++ ) {

            for (int col = 0; col < cols; col++ ) {
                int midY = (blockSize) * row + (blockSize / 2) + startY;
                int midX = (blockSize) * col + (blockSize / 2) + startX; // for example: (150 * 2) + 75 = 225

                if(midX > width) return;
                if(midY > height) return;

                int pixel = 0;
                try {
                    pixel = bitmap.getPixel(midX, midY);
                } catch(Exception ex) {return;}

                int r = Color.red(pixel);
                int b = Color.blue(pixel);
                int g = Color.green(pixel);
                int a = Color.alpha(pixel);

                paint.setARGB(a, r, g, b);

                int left = (blockSize * row) + startY;
                int top = (blockSize * col) + startX;
                int right = ((blockSize * row) + blockSize) + startY;
                int bottom = ((blockSize * col) + blockSize) + startX;

                canvas.drawRect(top, left, bottom, right, paint);
            }
        }

        isTouched = false;
        render = false;
    }
}
