package com.ik.ggnote.activities;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.colpicker.ColorPicker;
import com.ik.ggnote.colpicker.ColorPicker.OnColorChangedListener;
import com.ik.ggnote.colpicker.OpacityBar;
import com.ik.ggnote.colpicker.OpacityBar.OnOpacityChangedListener;
import com.ik.ggnote.colpicker.SaturationBar;
import com.ik.ggnote.colpicker.SaturationBar.OnSaturationChangedListener;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.custom.CDrawingView;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;

@EActivity ( R.layout.activity_drawview ) public class ADrawingView extends FragmentActivity implements OnColorChangedListener , OnOpacityChangedListener , OnSaturationChangedListener {

     // --------------------------------- VIEWS
     @ViewById ImageView ivLock;
     @ViewById ImageButton ibShapesCircle , ibShapesRectangle , ibShapesTriangle , ibShapesFreeDrawing , ibDrawText , ibShapesLine;

     @ViewById ImageButton ibColourBlack , ibColourBlue , ibColourGreen , ibColourRed , ibColorPicker;
     @ViewById ImageButton ibSave , ibClearAll , ibRedo , ibUndo , ibThick , ibMedium , ibThin , ibEraser;

     @ViewById ImageButton ibBrushColours , ibDrawingSettings , ibShapes , ibBack;

     @ViewById HorizontalScrollView hsvDrawingSettings , hsvBrushColour , hsvDrawShapes;

     // canvas
     @ViewById CDrawingView         cDrawingView;

     // colorpicker dialog
     private Dialog                 dialogChangeColour;
     // dialog builder
     private AlertDialog.Builder    builder;
     // color picker view
     private ColorPicker            picker;
     // indicates level of saturation and opacity in change color dialog
     private TextView               twSaturationBar , twOpacityBar;

     private int                    colorToSet;

     @AfterViews void afterViews() {
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
                    cDrawingView.disableEraserMode();
                    // color selected by the user.
                    cDrawingView.setColor(colorToSet);
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

     @Click void ibBack() {
          // TODO add dialog ask to save
          onBackPressed();
     }

     @Click void ibDrawingSettings() {
          inverseHorizontalScrollViewVisibility(hsvDrawingSettings);
          hsvBrushColour.setVisibility(View.GONE);
          hsvDrawShapes.setVisibility(View.GONE);
     }

     @Click void ibBrushColours() {
          inverseHorizontalScrollViewVisibility(hsvBrushColour);
          hsvDrawingSettings.setVisibility(View.GONE);
          hsvDrawShapes.setVisibility(View.GONE);
     }

     @Click void ibShapes() {
          inverseHorizontalScrollViewVisibility(hsvDrawShapes);
          hsvDrawingSettings.setVisibility(View.GONE);
          hsvBrushColour.setVisibility(View.GONE);
     }

     private void inverseHorizontalScrollViewVisibility(HorizontalScrollView view) {
          int visibility = view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
          view.setVisibility(visibility);
     }

     @Click void ibSave() {
          String filename = cDrawingView.saveDrawingToFile(this);
          // check return value if null or empty, file has not been created
          if ( null != filename && !filename.equals("") ) {
               ActiveRecord.currentNote.pathToDrawing = filename;
               Utils.logw("File saved to " + filename);
               onBackPressed();
          } else {
               Utils.showCustomToast(ADrawingView.this, "PROBLEMS", R.drawable.text);
          }
     }

     @Click void ivLock() {
          final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);

          dialogBuilder.withButton1Text("Ok").withIcon(R.drawable.scream).withEffect(Effectstype.Slit).withTitle("GGNote").withMessage("This is read-only mode").setButton1Click(new View.OnClickListener() {
               @Override public void onClick(View v) {
                    dialogBuilder.dismiss();
               }
          }).show();
     }

     @Click void ibDrawText() {

          cDrawingView.disableEraserMode();
          ibDrawText.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          Utils.showCustomToast(ADrawingView.this, R.string.ihaveacc, R.drawable.text);
     }

     @Click void ibColorPicker() {
          ibColorPicker.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.choose_custom_color, R.drawable.color_picker);
          try {
               picker.setOldCenterColor(cDrawingView.getCurrentColor());
               picker.setColor(cDrawingView.getCurrentColor());
          } catch (Exception ex) {
               Utils.logw(ex.getMessage());
          }
          dialogChangeColour.show();
     }

     @Click void ibClearAll() {
          builder = new Builder(ADrawingView.this);
          builder.setTitle(getResources().getString(R.string.clear_canvas));
          builder.setMessage(getResources().getString(R.string.everything_will_be_clear)).setIcon(R.drawable.clear);
          builder.setPositiveButton(getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
               @Override public void onClick(DialogInterface dialog, int which) {
                    cDrawingView.disableEraserMode();
                    cDrawingView.clearAll();
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
          cDrawingView.setEraserMode();
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.erase_mode_is_active, R.drawable.eraser);
     }

     @Click void ibThin() {
          ibThin.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setBrushSize(15);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.thick_brush, R.drawable.brush_pencil128);
     }

     @Click void ibMedium() {
          ibMedium.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setBrushSize(10);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.medium_brush_size, R.drawable.brush_pencil128);
     }

     @Click void ibThick() {
          ibThick.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setBrushSize(5);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.thin_brush_size, R.drawable.brush_pencil128);
     }

     @Click void ibShapesRectangle() {
          cDrawingView.disableEraserMode();
          ibShapesRectangle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.RECTANGLE);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.draw_rectangle, R.drawable.rectangle);
     }

     @Click void ibShapesCircle() {
          cDrawingView.disableEraserMode();
          ibShapesCircle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.CIRCLE);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.draw_circle, R.drawable.circle);
     }

     @Click void ibShapesLine() {
          cDrawingView.disableEraserMode();
          ibShapesLine.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.LINE);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.draw_line, R.drawable.line);
     }

     @Click void ibShapesTriangle() {
          cDrawingView.disableEraserMode();
          ibShapesTriangle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.TRIANGLE);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.draw_triangle, R.drawable.triangle);
     }

     @Click void ibShapesFreeDrawing() {
          cDrawingView.disableEraserMode();
          ibShapesFreeDrawing.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.STANDART_DRAWING);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.free_drawing, R.drawable.free_drawing);
     }

     @Click void ibColourBlue() {
          cDrawingView.disableEraserMode();
          ibColourBlue.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setColor(Color.BLUE);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.brush_color_is_set_to_blue, R.drawable.circle_blue);
     }

     @Click void ibColourGreen() {
          cDrawingView.disableEraserMode();
          ibColourGreen.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setColor(Color.GREEN);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.brush_color_is_set_to_green, R.drawable.circle_green);
     }

     @Click void ibColourRed() {
          cDrawingView.disableEraserMode();
          ibColourRed.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setColor(Color.RED);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.brush_color_is_set_to_red, R.drawable.circle_red);
     }

     @Click void ibColourBlack() {
          cDrawingView.disableEraserMode();
          ibColourBlack.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setColor(Color.BLACK);
          // Utils.showCustomToast(GeneralWhiteBoardActivity.this, R.string.brush_color_is_black, R.drawable.circle_black);
     }

     @Click void ibUndo() {
          ibUndo.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.redo();
     }

     @Click void ibRedo() {
          ibRedo.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.undo();
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