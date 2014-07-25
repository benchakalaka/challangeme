package com.ik.chalangeme.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ik.chalangeme.R;
import com.ik.chalangeme.colpicker.ColorPicker;
import com.ik.chalangeme.colpicker.ColorPicker.OnColorChangedListener;
import com.ik.chalangeme.colpicker.OpacityBar;
import com.ik.chalangeme.colpicker.OpacityBar.OnOpacityChangedListener;
import com.ik.chalangeme.colpicker.SaturationBar;
import com.ik.chalangeme.colpicker.SaturationBar.OnSaturationChangedListener;
import com.ik.chalangeme.custom.DrawingView;
import com.ik.chalangeme.utils.Utils;
import com.ik.chalangeme.utils.Utils.AnimationManager;

@EActivity ( R.layout.activity_drawview ) public class ADrawingView extends FragmentActivity implements OnColorChangedListener , OnOpacityChangedListener , OnSaturationChangedListener {

     // --------------------------------- VIEWS
     @ViewById ImageButton       ibShapesCircle , ibShapesRectangle , ibShapesTriangle , ibShapesFreeDrawing , ibDrawText , ibShapesLine;

     @ViewById ImageButton       ibColorsPanel , ibColourBlack , ibColourBlue , ibColourGreen , ibColourRed , ibColorPicker;
     @ViewById ImageButton       ibClearAll , ibPencilSettings , ibRedo , ibUndo , ibThick , ibMedium , ibThin , ibEraser;

     @ViewById DrawingView       drawingView;

     private Dialog              dialogChangeColour;
     private AlertDialog.Builder builder;
     private ColorPicker         picker;

     private TextView            twSaturationBar , twOpacityBar;

     private int                 colorToSet;

     @AfterViews void afterViews() {
          builder = new Builder(getApplicationContext());
          picker = new ColorPicker(getApplicationContext());
          dialogChangeColour = new Dialog(this);
          // dialogInputText = new Dialog(this);

          dialogChangeColour.setContentView(R.layout.dialog_color_picker);
          // dialogInputText.setContentView(R.layout.dialog_input_text_for_drawing);
          dialogChangeColour.setTitle(R.string.choose_color_saturation_opacity);
          // final EditText ed = (EditText) dialogInputText.findViewById(R.id.dialog_edittext_input_text_to_draw);
          // ed.setLines(1);

          twSaturationBar = ((TextView) dialogChangeColour.findViewById(R.id.twSaturationBar));
          twOpacityBar = ((TextView) dialogChangeColour.findViewById(R.id.twOpacityBar));

          picker = (ColorPicker) dialogChangeColour.findViewById(R.id.picker);
          OpacityBar opacityBar = (OpacityBar) dialogChangeColour.findViewById(R.id.opacitybar);
          SaturationBar saturationBar = (SaturationBar) dialogChangeColour.findViewById(R.id.saturationbar);

          picker.addOpacityBar(opacityBar);
          picker.addSaturationBar(saturationBar);

          // adds listener to the colorpicker which is implemented in the activity
          picker.setOnColorChangedListener(this);
          // to turn of showing the old color
          picker.setShowOldCenterColor(true);
          // set listener when opacity has been changed
          opacityBar.setOnOpacityChangedListener(this);
          // set listener when saturation has been changed
          saturationBar.setOnSaturationChangedListener(this);

          ((Button) dialogChangeColour.findViewById(R.id.buttonOk)).setOnClickListener(new OnClickListener() {
               @Override public void onClick(View v) {
                    drawingView.disableEraserMode();
                    // color selected by the user.
                    drawingView.setColor(colorToSet);
                    // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.new_color_has_been_set, R.drawable.color_picker);
                    dialogChangeColour.hide();
               }
          });

          ((Button) dialogChangeColour.findViewById(R.id.buttonCancel)).setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {
                    dialogChangeColour.hide();
               }
          });
     }

     @UiThread @Click void ibDrawText() {
          drawingView.disableEraserMode();
          ibDrawText.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.text_mode_is_active, R.drawable.text);
          // dialogInputText.show();
     }

     @Click void ibColorPicker() {
          ibColorPicker.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.choose_custom_color, R.drawable.color_picker);
          try {
               picker.setOldCenterColor(drawingView.getCurrentColor());
               picker.setColor(drawingView.getCurrentColor());
          } catch (Exception ex) {
               Utils.logw(ex.getMessage());
          }
          dialogChangeColour.show();
     }

     @Click void ibClearAll() {
          builder.setTitle(getResources().getString(R.string.clear_canvas));
          builder.setMessage(getResources().getString(R.string.everything_will_be_clear)).setIcon(R.drawable.clear);
          builder.setPositiveButton(getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
               @Override public void onClick(DialogInterface dialog, int which) {
                    drawingView.disableEraserMode();
                    drawingView.clearAll();
                    ibClearAll.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
                    // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.whiteboard_clear, R.drawable.clear);
                    dialog.dismiss();
               }
          });
          builder.setNegativeButton(getResources().getString(android.R.string.no), new DialogInterface.OnClickListener() {
               @Override public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
               }
          });
          builder.create().show();
     }

     @Click void ibEraser() {
          ibEraser.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setEraserMode();
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.erase_mode_is_active, R.drawable.eraser);
     }

     @Click void ibThin() {
          ibThin.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setBrushSize(15);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.thick_brush, R.drawable.brush_pencil128);
     }

     @Click void ibMedium() {
          ibMedium.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setBrushSize(10);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.medium_brush_size, R.drawable.brush_pencil128);
     }

     @Click void ibThick() {
          ibThick.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setBrushSize(5);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.thin_brush_size, R.drawable.brush_pencil128);
     }

     @Click void ibShapesRectangle() {
          drawingView.disableEraserMode();
          ibShapesRectangle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setDrawingShape(DrawingView.ShapesType.RECTANGLE);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.draw_rectangle, R.drawable.rectangle);
     }

     @Click void ibShapesCircle() {
          drawingView.disableEraserMode();
          ibShapesCircle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setDrawingShape(DrawingView.ShapesType.CIRCLE);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.draw_circle, R.drawable.circle);
     }

     @Click void ibShapesLine() {
          drawingView.disableEraserMode();
          ibShapesLine.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setDrawingShape(DrawingView.ShapesType.LINE);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.draw_line, R.drawable.line);
     }

     @Click void ibShapesTriangle() {
          drawingView.disableEraserMode();
          ibShapesTriangle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setDrawingShape(DrawingView.ShapesType.TRIANGLE);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.draw_triangle, R.drawable.triangle);
     }

     @Click void ibShapesFreeDrawing() {
          drawingView.disableEraserMode();
          ibShapesFreeDrawing.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setDrawingShape(DrawingView.ShapesType.STANDART_DRAWING);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.free_drawing, R.drawable.free_drawing);
     }

     @Click void ibColourBlue() {
          drawingView.disableEraserMode();
          ibColourBlue.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setColor(Color.BLUE);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.brush_color_is_set_to_blue, R.drawable.circle_blue);
     }

     @Click void ibColourGreen() {
          drawingView.disableEraserMode();
          ibColourGreen.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setColor(Color.GREEN);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.brush_color_is_set_to_green, R.drawable.circle_green);
     }

     @Click void ibColourRed() {
          drawingView.disableEraserMode();
          ibColourRed.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setColor(Color.RED);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.brush_color_is_set_to_red, R.drawable.circle_red);
     }

     @Click void ibColourBlack() {
          drawingView.disableEraserMode();
          ibColourBlack.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.setColor(Color.BLACK);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.brush_color_is_black, R.drawable.circle_black);
     }

     @Click void ibUndo() {
          // ibUndo.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.redo();
     }

     @Click void ibRedo() {
          // ibRedo.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          drawingView.undo();
     }

     @Override public void onColorChanged(int color) {
          colorToSet = color;
     }

     @Override public void onOpacityChanged(int opacity) {
          twOpacityBar.setText(Math.abs(Math.round(opacity / 2.55) - 100) + "%");
     }

     @Override public void onSaturationChanged(int saturation) {
          twSaturationBar.setText(saturation + "%");
     }

}
