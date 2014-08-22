package com.ik.ggnote.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import com.ik.ggnote.model.PathSerializable;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;

@EActivity ( R.layout.activity_drawview ) public class ADrawingView extends ActionBarActivity implements OnClickListener , OnColorChangedListener , OnOpacityChangedListener , OnSaturationChangedListener {

     // --------------------------------- VIEWS
     @ViewById ImageButton       ibShapesCircle , ibShapesRectangle , ibShapesTriangle , ibShapesFreeDrawing , ibDrawText , ibShapesLine;

     @ViewById ImageButton       ibColour1 , ibColour2 , ibColour3 , ibColour4 , ibColour5 , ibColour6 , ibColour7 , ibColour8 , ibColour9 , ibColour10 , ibColour11 , ibColour12 , ibColour13 , ibColour14 , ibColour15 , ibColour16 , ibColour17 , ibColorPicker;
     @ViewById ImageButton       ibSave , ibClearAll , ibRedo , ibUndo , ibThick , ibMedium , ibThin , ibEraser;

     @ViewById ImageButton       ibBrushColours , ibDrawingSettings , ibShapes;

     @ViewById ScrollView        svDrawingSettings , svBrushColour , svDrawShapes;

     @ViewById ImageView         ivShapesDone , ivBrushColoursDone , ibDrawingSettingsDone;

     // canvas
     @ViewById CDrawingView      cDrawingView;

     // colorpicker dialog
     private Dialog              dialogChangeColour;
     // dialog builder
     private AlertDialog.Builder builder;
     // color picker view
     private ColorPicker         picker;
     // indicates level of saturation and opacity in change color dialog
     private TextView            twSaturationBar , twOpacityBar;

     private int                 colorToSet;

     @AfterViews void afterViews() {
          cDrawingView.setColor(Color.WHITE);
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
                    Utils.showCustomToastWithBackgroundColour(ADrawingView.this, getResources().getString(R.string.color_changed), colorToSet);
                    dialogChangeColour.hide();
               }
          });

          ((Button) dialogChangeColour.findViewById(R.id.buttonCancel)).setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {
                    dialogChangeColour.hide();
               }
          });
          ibColour1.setBackgroundResource(R.drawable.button_bg);
          ibColour1.setOnClickListener(this);
          ibColour2.setOnClickListener(this);
          ibColour3.setOnClickListener(this);
          ibColour4.setOnClickListener(this);
          ibColour5.setOnClickListener(this);
          ibColour6.setOnClickListener(this);
          ibColour7.setOnClickListener(this);
          ibColour8.setOnClickListener(this);
          ibColour9.setOnClickListener(this);
          ibColour10.setOnClickListener(this);
          ibColour11.setOnClickListener(this);
          ibColour12.setOnClickListener(this);
          ibColour13.setOnClickListener(this);
          ibColour14.setOnClickListener(this);
          ibColour15.setOnClickListener(this);
          ibColour16.setOnClickListener(this);
          ibColour17.setOnClickListener(this);

          // Inflate your custom layout
          final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);

          // Set up your ActionBar
          ActionBar actionBar = getSupportActionBar();
          // You customization
          actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ab_background)));

          actionBar.setIcon(R.drawable.arrowleft);
          actionBar.setDisplayShowHomeEnabled(true);
          actionBar.setDisplayShowTitleEnabled(false);
          actionBar.setDisplayShowCustomEnabled(true);
          actionBar.setHomeButtonEnabled(true);
          actionBar.setCustomView(actionBarLayout);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setOnClickListener(this);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setBackgroundResource(R.drawable.attach);
          ((TextView) actionBar.getCustomView().findViewById(R.id.text)).setText("Attach drawing");

          try {
               ObjectInputStream oin = new ObjectInputStream(new FileInputStream(new File(ActiveRecord.currentNote.pathToDrawing)));
               @SuppressWarnings ( "unchecked" )
               List <PathSerializable> paths = (List <PathSerializable>) oin.readObject();
               oin.close();
               cDrawingView.setPaths(paths);
               cDrawingView.invalidate();
               cDrawingView.setColor(Color.TRANSPARENT);
          } catch (Exception e) {
               e.printStackTrace();
          }
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          onBackClick();
          return super.onOptionsItemSelected(item);
     }

     private void onBackClick() {
          final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this);
          dialogBuilder.withButton1Text("Cancel").withButton2Text("Save").withIcon(R.drawable.scream).withEffect(Effectstype.Slit).withTitle("Drawing has not been saved.").withMessage("Do you want to save Drawing?").setButton1Click(new View.OnClickListener() {
               @Override public void onClick(View v) {
                    dialogBuilder.dismiss();
                    onBackPressed();
               }
          }).setButton2Click(new OnClickListener() {
               @Override public void onClick(View v) {
                    saveDrawing();
               }
          }).show();
     }

     private void saveDrawing() {
          String filename = cDrawingView.saveDrawingToFile(ADrawingView.this);
          // check return value if null or empty, file has not been created
          if ( null != filename && !filename.equals("") ) {
               ActiveRecord.currentNote.pathToDrawing = filename;
               Utils.logw("File saved to " + filename);
               onBackPressed();
          } else {
               Utils.showCustomToast(ADrawingView.this, "PROBLEMS", R.drawable.work);
          }
     }

     @Click void ibDrawingSettings() {
          inverseHorizontalScrollViewVisibility(svDrawingSettings);
          YoYo.with(Techniques.Landing).duration(700).playOn(svDrawingSettings);
          svBrushColour.setVisibility(View.GONE);
          svDrawShapes.setVisibility(View.GONE);
          ivShapesDone.setVisibility(View.INVISIBLE);
          ivBrushColoursDone.setVisibility(View.INVISIBLE);
          ibDrawingSettingsDone.setVisibility(View.VISIBLE);
     }

     @Click void ibBrushColours() {
          YoYo.with(Techniques.Landing).duration(700).playOn(svBrushColour);
          inverseHorizontalScrollViewVisibility(svBrushColour);
          svDrawingSettings.setVisibility(View.GONE);
          svDrawShapes.setVisibility(View.GONE);

          ivShapesDone.setVisibility(View.INVISIBLE);
          ivBrushColoursDone.setVisibility(View.VISIBLE);
          ibDrawingSettingsDone.setVisibility(View.INVISIBLE);
     }

     @Click void ibShapes() {
          YoYo.with(Techniques.Landing).duration(700).playOn(svDrawShapes);
          inverseHorizontalScrollViewVisibility(svDrawShapes);
          svDrawingSettings.setVisibility(View.GONE);
          svBrushColour.setVisibility(View.GONE);
          ivShapesDone.setVisibility(View.VISIBLE);
          ivBrushColoursDone.setVisibility(View.INVISIBLE);
          ibDrawingSettingsDone.setVisibility(View.INVISIBLE);
     }

     private void inverseHorizontalScrollViewVisibility(ScrollView view) {
          int visibility = view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
          view.setVisibility(visibility);
     }

     @Click void ibDrawText() {
          cDrawingView.disableEraserMode();
          ibDrawText.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          Utils.showCustomToast(ADrawingView.this, R.string.ihaveacc, R.drawable.work);
     }

     @Click void ibColorPicker() {
          ibColorPicker.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          Utils.showCustomToast(ADrawingView.this, R.string.choose_custom_color, R.drawable.brush);
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
          builder.setMessage(getResources().getString(R.string.everything_will_be_clear)).setIcon(R.drawable.blank);
          builder.setPositiveButton(getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
               @Override public void onClick(DialogInterface dialog, int which) {
                    cDrawingView.disableEraserMode();
                    cDrawingView.clearAll();
                    ibClearAll.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
                    Utils.showCustomToast(ADrawingView.this, R.string.clear_drawing, R.drawable.blank);
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
          Utils.showCustomToast(ADrawingView.this, R.string.erase_mode_is_active, R.drawable.erase);
     }

     @Click void ibThin() {
          ibThin.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setBrushSize(15);
          Utils.showCustomToast(ADrawingView.this, R.string.thick_brush, R.drawable.brush);
     }

     @Click void ibMedium() {
          ibMedium.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setBrushSize(10);
          Utils.showCustomToast(ADrawingView.this, R.string.medium_brush_size, R.drawable.brush);
     }

     @Click void ibThick() {
          ibThick.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setBrushSize(5);
          Utils.showCustomToast(ADrawingView.this, R.string.thin_brush_size, R.drawable.brush);
     }

     @Click void ibShapesRectangle() {
          cDrawingView.disableEraserMode();
          ibShapesRectangle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.RECTANGLE);
          Utils.showCustomToast(ADrawingView.this, R.string.draw_rectangle, R.drawable.rectangle);
     }

     @Click void ibShapesCircle() {
          cDrawingView.disableEraserMode();
          ibShapesCircle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.CIRCLE);
          Utils.showCustomToast(ADrawingView.this, R.string.draw_circle, R.drawable.circle);
     }

     @Click void ibShapesLine() {
          cDrawingView.disableEraserMode();
          ibShapesLine.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.LINE);
          Utils.showCustomToast(ADrawingView.this, R.string.draw_line, R.drawable.line);
     }

     @Click void ibShapesTriangle() {
          cDrawingView.disableEraserMode();
          ibShapesTriangle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.TRIANGLE);
          Utils.showCustomToast(ADrawingView.this, R.string.draw_triangle, R.drawable.triangle);
     }

     @Click void ibShapesFreeDrawing() {
          cDrawingView.disableEraserMode();
          ibShapesFreeDrawing.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.STANDART_DRAWING);
          Utils.showCustomToast(ADrawingView.this, R.string.free_drawing, R.drawable.free_drawing);
     }

     @Click void ibUndo() {
          ibUndo.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.redo();
          Utils.showCustomToast(ADrawingView.this, R.string.undo, R.drawable.left_arrow);
     }

     @Click void ibRedo() {
          ibRedo.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          cDrawingView.undo();
          Utils.showCustomToast(ADrawingView.this, R.string.redo, R.drawable.right_arrow);
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

     @Override public void onClick(View v) {
          switch (v.getId()) {
               case R.id.ivRightOkButton:
                    saveDrawing();
                    return;
          }
          cDrawingView.disableEraserMode();
          int color = Color.parseColor(v.getTag().toString());
          cDrawingView.setColor(color);
          v.startAnimation(AnimationManager.load(R.anim.pump_bottom, 500));
          unselectAllColorButtons();
          v.setBackgroundResource(R.drawable.button_bg);
          Utils.showCustomToastWithBackgroundColour(ADrawingView.this, getResources().getString(R.string.color_changed), color);
     }

     private void unselectAllColorButtons() {
          ibColour1.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour2.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour3.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour4.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour5.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour6.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour7.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour8.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour9.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour10.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour11.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour12.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour13.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour14.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour15.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour16.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
          ibColour17.setBackgroundResource(R.drawable.transparent_inside_and_white_border);
     }
}