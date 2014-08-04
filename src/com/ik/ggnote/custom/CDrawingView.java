package com.ik.ggnote.custom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.ik.chalangeme.R;
import com.ik.ggnote.model.PaintSerializable;
import com.ik.ggnote.model.PathSerializable;
import com.ik.ggnote.utils.Utils;

public class CDrawingView extends View {

     private static String            textToDraw           = "";
     boolean                          isNeedToRedraw       = false;

     private static int               CURRENT_DRAWING_TYPE = ShapesType.STANDART_DRAWING;

     private static Bitmap            moveTextIndicator    = BitmapFactory.decodeResource(Utils.getResources(), R.drawable.calendar);

     // current finger position X
     float                            touchX;
     // current finger position Y
     float                            touchY;
     // X position of first touch
     float                            touchDownX;
     // Y position of first touch
     float                            touchDownY;
     // rectangle of drawing for different types of shapes
     private final RectF              rectangleOfDrawing   = new RectF();
     // canvas bitmap
     public static Bitmap             canvasBitmap , startBitmap;
     // drawing path
     private PathSerializable         drawPath;
     // drawing and canvas paint
     private PaintSerializable        drawPaint , canvasPaint;
     // initial color
     private int                      paintColor           = Color.BLUE;
     // canvas
     private Canvas                   drawCanvas;
     // brush sizes
     private float                    brushSize;
     // step for drawing coordinates (100 pixels)
     private final TextPaint          labelsPaint          = new TextPaint();
     // companies and colours filter (0 means draw everything, 1 means hide all drawings)
     private int                      companyColorFilter   = 0;
     // for UNDO operation
     private List <PathSerializable>  paths                = new ArrayList <PathSerializable>();
     // for UNDO & REDO operation
     private List <PathSerializable>  redoPaths            = new ArrayList <PathSerializable>();
     // for avoiding allocation in onDraw method
     private static PaintSerializable staticPaint          = new PaintSerializable();
     // path for each shape
     private static PathSerializable  shapePath            = new PathSerializable();

     private int                      selectedPathIndex    = -1;
     private boolean                  isEraser             = false;
     private boolean                  isUndoRedoActive;

     public static int                WIDTH;
     public static int                HEIGHT;

     public CDrawingView ( Context context , AttributeSet attrs ) {
          super(context, attrs);
          resetDrawingTools();
          setDrawingCacheEnabled(true);
          setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);
          staticPaint.setAntiAlias(true);
          staticPaint.setStyle(Paint.Style.STROKE);
          staticPaint.setStrokeJoin(Paint.Join.ROUND);
          staticPaint.setStrokeCap(Paint.Cap.ROUND);

          labelsPaint.setFakeBoldText(true);
          labelsPaint.setUnderlineText(true);
          labelsPaint.setTextSize(25);
          labelsPaint.setTypeface(Typeface.SERIF);
     }

     // shapes types constants
     public static class ShapesType {
          public static final int STANDART_DRAWING = -1;
          public static final int CIRCLE           = 0;
          public static final int RECTANGLE        = 1;
          public static final int TRIANGLE         = 2;
          public static final int LINE             = 3;
          public static final int DRAW_TEXT        = 4;

          private ShapesType () {
          }
     }

     public void clearAllPaths() {
          paths = new ArrayList <PathSerializable>();
          redoPaths = new ArrayList <PathSerializable>();
          invalidate();
     }

     public void setTextToDraw(String text) {
          textToDraw = text;
          isNeedToRedraw = true;
          invalidate();
     }

     public void increaseTextSize() {
          labelsPaint.setTextSize(labelsPaint.getTextSize() + 1);
          invalidate();
     }

     public void decreaseTextSize() {
          labelsPaint.setTextSize(labelsPaint.getTextSize() - 1);
          invalidate();
     }

     public void deselectPath() {
          selectedPathIndex = -1;
          invalidate();
     }

     public void setCompanyColourFilter(int companyColorFilter) {
          this.companyColorFilter = companyColorFilter;
          invalidate();
     }

     public int getCompanyColourFilter() {
          return companyColorFilter;
     }

     public static int getCanvasHeight() {
          if ( !Utils.isNull(canvasBitmap) ) { return startBitmap.getHeight(); }
          return 0;
     }

     public static int getCanvasWidth() {
          if ( !Utils.isNull(canvasBitmap) ) { return startBitmap.getWidth(); }
          return 0;
     }

     // set brush size
     public void setBrushSize(float newSize) {
          brushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
          drawPaint.setStrokeWidth(brushSize);
     }

     // setup drawing
     private void resetDrawingTools() {
          drawPath = new PathSerializable();
          drawPaint = new PaintSerializable();
          shapePath = new PathSerializable();
          drawPaint.setColor(Color.TRANSPARENT);
          drawPaint.setAntiAlias(true);
          drawPaint.setStrokeWidth(brushSize);
          drawPaint.setStyle(Paint.Style.STROKE);
          drawPaint.setStrokeJoin(Paint.Join.ROUND);
          drawPaint.setStrokeCap(Paint.Cap.ROUND);

          canvasPaint = new PaintSerializable();
     }

     // size assigned to view
     @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
          super.onSizeChanged(w, h, oldw, oldh);
          // BitmapFactory.Options opt = new BitmapFactory.Options();
          // opt.inMutable = true;
          WIDTH = w;
          HEIGHT = h;
          canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
          startBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
          drawCanvas = new Canvas(canvasBitmap);
     }

     public boolean isNeedAutoSave() {
          return (null != getPaths()) && (!getPaths().isEmpty());
     }

     // draw the view - will be called after any touch event or invalidating drawing surface
     @Override protected void onDraw(Canvas canvas) {

          // draw simple bitmap
          canvas.drawBitmap(startBitmap, 0, 0, canvasPaint);

          // draw all pathes which in array of pathes
          if ( null != paths ) {
               for ( int i = 0; i < paths.size(); i++ ) {
                    if ( null != getPaths().get(i).getTextToDraw() && getPaths().get(i).hasTextToDraw() ) {
                         canvas.drawText(getPaths().get(i).getTextToDraw(), getPaths().get(i).getTextX(), getPaths().get(i).getTextY(), labelsPaint);
                         continue;
                    }

                    // if user select path highlight it
                    if ( i == selectedPathIndex ) {
                         // draw bigger blue line
                         staticPaint.setStrokeWidth(getPaths().get(i).getPaint().brushStrokeWith + 4);
                         staticPaint.setColor(Color.parseColor("#9933ff"));
                         canvas.drawPath(paths.get(i), staticPaint);
                    }
                    staticPaint.setStrokeWidth(getPaths().get(i).getPaint().brushStrokeWith);
                    staticPaint.setColor(getPaths().get(i).getPaint().colour);

                    canvas.drawPath(paths.get(i), staticPaint);
               }
          }

          if ( isUndoRedoActive ) {
               isUndoRedoActive = false;
               return;
          }

          if ( !isEraser ) {
               // draw last path with last selected color
               drawPaint.setColor(paintColor);
          } else {
               drawPaint.setColor(Color.BLACK);
          }

          if ( CURRENT_DRAWING_TYPE != ShapesType.STANDART_DRAWING ) {
               shapePath.reset();
               staticPaint.setStrokeWidth(brushSize);
               staticPaint.setColor(paintColor);

               switch (CURRENT_DRAWING_TYPE) {
                    case ShapesType.CIRCLE:
                         rectangleOfDrawing.set(touchDownX, touchDownY, touchX, touchY);
                         shapePath.addOval(rectangleOfDrawing, Direction.CW);
                         canvas.drawPath(shapePath, staticPaint);
                         break;

                    case ShapesType.RECTANGLE:
                         if ( touchX < touchDownX ) {
                              if ( touchY < touchDownY ) {
                                   rectangleOfDrawing.set(touchX, touchY, touchDownX, touchDownY);
                                   shapePath.addRoundRect(rectangleOfDrawing, 15, 15, Direction.CW);
                              } else {
                                   rectangleOfDrawing.set(touchX, touchDownY, touchDownX, touchY);
                                   shapePath.addRoundRect(rectangleOfDrawing, 15, 15, Direction.CW);
                              }
                         } else {
                              if ( touchY > touchDownY ) {
                                   rectangleOfDrawing.set(touchDownX, touchDownY, touchX, touchY);
                                   shapePath.addRoundRect(rectangleOfDrawing, 15, 15, Direction.CW);
                              } else {
                                   rectangleOfDrawing.set(touchDownX, touchY, touchX, touchDownY);
                                   shapePath.addRoundRect(rectangleOfDrawing, 15, 15, Direction.CW);
                              }
                         }
                         canvas.drawPath(shapePath, staticPaint);
                         break;

                    case ShapesType.DRAW_TEXT:
                         if ( isNeedToRedraw ) {
                              canvas.drawText(textToDraw, 100, 100, labelsPaint);
                              canvas.drawBitmap(moveTextIndicator, 90, 120, null);
                         } else {
                              canvas.drawText(textToDraw, touchX - 40, touchY - 40, labelsPaint);
                              canvas.drawBitmap(moveTextIndicator, touchX - 40, touchY, null);
                         }
                         break;

                    case ShapesType.TRIANGLE:
                         shapePath.moveTo(touchDownX, touchDownY);
                         // line to finger touch
                         shapePath.lineTo(touchX, touchY);
                         shapePath.lineTo(touchDownX + (touchDownX - touchX), touchY);
                         shapePath.lineTo(touchDownX, touchDownY);
                         canvas.drawPath(shapePath, staticPaint);
                         break;

                    case ShapesType.LINE:
                         shapePath.moveTo(touchDownX, touchDownY);
                         shapePath.lineTo(touchX, touchY);
                         canvas.drawPath(shapePath, staticPaint);
                         break;
                    default:
                         Utils.logw("Unknown shape type");
               }
          } else {

               canvas.drawPath(drawPath, drawPaint);
          }
     }

     @Override public boolean onTouchEvent(final MotionEvent event) {
          touchX = event.getX();
          touchY = event.getY();

          // respond to down, move and up events
          switch (event.getAction()) {

               case MotionEvent.ACTION_DOWN:
                    touchDownX = touchX;
                    touchDownY = touchY;

                    isNeedToRedraw = false;

                    if ( CURRENT_DRAWING_TYPE == ShapesType.STANDART_DRAWING ) {
                         drawPath.moveTo(touchX, touchY);
                    }
                    break;

               case MotionEvent.ACTION_MOVE:
                    if ( CURRENT_DRAWING_TYPE == ShapesType.STANDART_DRAWING ) {
                         drawPath.lineTo(touchX, touchY);
                    }
                    break;

               case MotionEvent.ACTION_UP:

                    if ( isEraser ) {
                         // GeneralWhiteBoardActivity.unselectEraserView();
                    }

                    // GeneralWhiteBoardActivity.setFreeDrawingSelected();
                    PaintSerializable ps = new PaintSerializable();
                    ps.brushStrokeWith = drawPaint.getStrokeWidth();
                    ps.colour = drawPaint.getColor();

                    if ( CURRENT_DRAWING_TYPE != ShapesType.STANDART_DRAWING ) {
                         PathSerializable newPath = new PathSerializable();
                         newPath.setPaint(ps);
                         newPath.setSavedCanvasX(CDrawingView.WIDTH);
                         newPath.setSavedCanvasY(CDrawingView.HEIGHT);

                         switch (CURRENT_DRAWING_TYPE) {

                              case ShapesType.DRAW_TEXT:
                                   newPath.setDrawText(touchX - 40, touchY - 40, textToDraw);
                                   getPaths().add(newPath);
                                   CURRENT_DRAWING_TYPE = ShapesType.STANDART_DRAWING;
                                   break;

                              case ShapesType.LINE:
                                   newPath.moveTo(touchDownX, touchDownY);
                                   newPath.lineTo(event.getX(), event.getY());
                                   getPaths().add(newPath);
                                   break;

                              case ShapesType.TRIANGLE:
                                   newPath.moveTo(touchDownX, touchDownY);
                                   // line to finger touch
                                   newPath.lineTo(touchX, touchY);
                                   newPath.lineTo(touchDownX + (touchDownX - touchX), touchY);
                                   newPath.lineTo(touchDownX, touchDownY);
                                   getPaths().add(newPath);
                                   break;

                              case ShapesType.CIRCLE:
                                   rectangleOfDrawing.set(touchDownX, touchDownY, touchX, touchY);
                                   newPath.addOval(rectangleOfDrawing, Direction.CW);
                                   getPaths().add(newPath);
                                   break;

                              case ShapesType.RECTANGLE:
                                   if ( touchX < touchDownX ) {
                                        if ( touchY < touchDownY ) {
                                             rectangleOfDrawing.set(touchX, touchY, touchDownX, touchDownY);
                                             newPath.addRoundRect(rectangleOfDrawing, 15, 15, Direction.CW);
                                        } else {
                                             rectangleOfDrawing.set(touchX, touchDownY, touchDownX, touchY);
                                             newPath.addRoundRect(rectangleOfDrawing, 15, 15, Direction.CW);
                                        }
                                   } else {
                                        if ( touchY > touchDownY ) {
                                             rectangleOfDrawing.set(touchDownX, touchDownY, touchX, touchY);
                                             newPath.addRoundRect(rectangleOfDrawing, 15, 15, Direction.CW);
                                        } else {
                                             rectangleOfDrawing.set(touchDownX, touchY, touchX, touchDownY);
                                             newPath.addRoundRect(rectangleOfDrawing, 15, 15, Direction.CW);
                                        }
                                   }
                                   getPaths().add(newPath);
                                   break;

                              default:
                                   shapePath.setPaint(ps);
                                   // add path
                                   getPaths().add(shapePath);
                                   break;
                         }
                    } else {
                         drawPath.lineTo(touchX, touchY);

                         drawCanvas.drawPath(drawPath, drawPaint);
                         if ( drawPaint.getColor() != Color.TRANSPARENT ) {
                              // set brush to path
                              drawPath.setPaint(ps);
                              // apply WIDTH and HEIGHT scale factor to path for different screen support
                              drawPath.setSavedCanvasX(CDrawingView.WIDTH);
                              drawPath.setSavedCanvasY(CDrawingView.HEIGHT);
                              // add path
                              getPaths().add(drawPath);
                         }

                    }
                    invalidate();

                    resetDrawingTools();
                    break;
               default:
                    return false;
          }
          // redraw
          invalidate();
          return true;
     }

     public int getCurrentColor() {
          return paintColor;
     }

     public void clearAll() {
          paths = new ArrayList <PathSerializable>();
          CURRENT_DRAWING_TYPE = ShapesType.STANDART_DRAWING;
          invalidate();
     }

     public void setEraserMode() {
          isEraser = true;
          drawPaint.setColor(Color.BLACK);
          CURRENT_DRAWING_TYPE = ShapesType.STANDART_DRAWING;
     }

     public void disableEraserMode() {
          isEraser = false;
     }

     // get last path
     public void undo() {
          if ( !getPaths().isEmpty() ) {
               redoPaths.add(getPaths().remove(getPaths().size() - 1));
               isUndoRedoActive = true;
               invalidate();
          }
     }

     public void redo() {
          if ( !redoPaths.isEmpty() ) {
               getPaths().add(redoPaths.remove(redoPaths.size() - 1));
               isUndoRedoActive = true;
               invalidate();
          }
     }

     public void setColor(String newColor) {
          paintColor = Color.parseColor(newColor);
          drawPaint.setColor(paintColor);
     }

     public void setColor(int newColor) {
          paintColor = newColor;
          drawPaint.setColor(paintColor);
     }

     /**
      * @param paths
      *             the paths to set
      */
     public synchronized void setPaths(List <PathSerializable> paths) {
          this.paths = paths;
          redoPaths = new ArrayList <PathSerializable>();
          invalidate();
     }

     /**
      * @return the path
      */
     public List <PathSerializable> getPaths() {
          return this.paths;
     }

     /**
      * Set drawing type: it could be any constant from static ShapeType interface
      * 
      * @param shapeType
      */
     public void setDrawingShape(int shapeType) {
          CURRENT_DRAWING_TYPE = shapeType;
     }

     /**
      * Save existing drawing list to file
      * 
      * @param context
      * @return name of created file or empty string otherwise
      */
     public String saveDrawingToFile(Context context) {
          // created filename
          String filename = "";
          // Create if need directory for saving file
          File dir = context.getDir("note", Context.MODE_PRIVATE);
          // create file inside directody with random name
          File drawing = new File(dir, System.currentTimeMillis() + ".drawing");

          try {
               ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(drawing));
               out.writeObject(this.paths);
               out.close();
               filename = drawing.getAbsolutePath();
          } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }
          return filename;
     }

}
