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
 * Created by Dion Segijn on 16/08/15.
 */
public class Pixelate extends ImageView {

    private int cols;
    private boolean clearCanvas = false;
    /* Initialize Paint object once */
    private Paint paint = new Paint();
    public Bitmap drawingCache;
    private boolean render = false;

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
        renderPixels(canvas);
    }

    public void pixelate(int cols) {
        this.cols = cols;
        render = true;
        this.invalidate();
    }

    public void clear() {
        clearCanvas = true;
        invalidate();
    }

    /* Geef de aantal kolommen en rijen
       Bereken de size van elk vierkant
       Bereken de midden positie (xy) van elk vierkant
       Pak de pixel kleur van de xy positie (bitmap)
       Draw op canvas de grote van elk vierkant op de juiste x en y positie */
    private void renderPixels(Canvas canvas) {
        if(!render) return;
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

        // Width for each block
        int rectW = width / cols;
        int rectH = rectW;
        double rows = Math.round((double)height / (double)rectH);

        for (int row = 0; row < rows; row++ ) {

            for (int col = 0; col < cols; col++ ) {
                int midY = (rectH) * row + (rectH / 2);
                int midX = (rectW) * col + (rectW / 2);

                if(midX > width) return;
                if(midY > height) return;

                int pixel = 0;
                try {
                    pixel = bitmap.getPixel(midX, midY);
                } catch(Exception ex) { return; }

                int r = Color.red(pixel);
                int b = Color.blue(pixel);
                int g = Color.green(pixel);
                int a = Color.alpha(pixel);

                paint.setARGB(a, r, g, b);

                int left = (rectH * row);
                int top = (rectW * col);
                int right = ((rectH * row) + rectH);
                int bottom = ((rectW * col) + rectW);

                canvas.drawRect(top, left, bottom, right, paint);
            }
        }

        render = false;
    }
}
