package com.ik.ggnote.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Dialog;
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
import com.devspark.appmsg.AppMsg;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ik.ggnote.R;
import com.ik.ggnote.colpicker.ColorPicker;
import com.ik.ggnote.colpicker.ColorPicker.OnColorChangedListener;
import com.ik.ggnote.colpicker.OpacityBar;
import com.ik.ggnote.colpicker.SaturationBar;
import com.ik.ggnote.constants.ActiveRecord;
import com.ik.ggnote.custom.CDrawingView;
import com.ik.ggnote.model.PathSerializable;
import com.ik.ggnote.utils.Utils;
import com.ik.ggnote.utils.Utils.AnimationManager;

@EActivity ( R.layout.activity_drawview ) public class ADrawingView extends ActionBarActivity implements OnClickListener , OnColorChangedListener {

     // --------------------------------- VIEWS
     @ViewById ImageButton      ibShapesCircle , ibShapesRectangle , ibShapesTriangle , ibShapesFreeDrawing , ibShapesLine;

     @ViewById ImageButton      ibColour1 , ibColour2 , ibColour3 , ibColour4 , ibColour5 , ibColour6 , ibColour7 , ibColour8 , ibColour9 , ibColour10 , ibColour11 , ibColour12 , ibColour13 , ibColour14 , ibColour15 , ibColour16 , ibColour17 , ibColorPicker;
     @ViewById ImageButton      ibClearAll , ibRedo , ibUndo , ibThick , ibMedium , ibThin , ibEraser;

     @ViewById ScrollView       svDrawingSettings , svBrushColour , svDrawShapes;

     @ViewById ImageView        ibBrushColours , ibDrawingSettings , ibShapes , ivShapesDone , ivBrushColoursDone , ibDrawingSettingsDone , ivColorAndShape;

     // canvas
     @ViewById CDrawingView     cDrawingView;

     // colorpicker dialog
     private Dialog             dialogChangeColour;
     // color picker view
     private ColorPicker        picker;

     private int                colorToSet;

     SaturationBar              saturationBar;

     // Saving dialog
     private NiftyDialogBuilder dialogBuilder;

     @AfterViews void afterViews() {
          cDrawingView.setBrushSize(5);
          picker = new ColorPicker(getApplicationContext());
          dialogChangeColour = new Dialog(this);
          // dialogInputText = new Dialog(this);

          dialogChangeColour.setContentView(R.layout.dialog_color_picker);
          // dialogInputText.setContentView(R.layout.dialog_input_text_for_drawing);
          dialogChangeColour.setTitle(R.string.choose_color_saturation_opacity);

          picker = (ColorPicker) dialogChangeColour.findViewById(R.id.picker);
          OpacityBar opacityBar = (OpacityBar) dialogChangeColour.findViewById(R.id.opacitybar);
          saturationBar = (SaturationBar) dialogChangeColour.findViewById(R.id.saturationbar);

          picker.addOpacityBar(opacityBar);
          picker.addSaturationBar(saturationBar);

          // adds listener to the colorpicker which is implemented in the activity
          picker.setOnColorChangedListener(this);
          // to turn of showing the old color
          picker.setShowOldCenterColor(true);

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
          ((ImageView) actionBar.getCustomView().findViewById(R.id.ivRightOkButton)).setImageResource(R.drawable.ok);
          actionBar.getCustomView().findViewById(R.id.ivRightOkButton).setOnTouchListener(Utils.touchListener);
          ((TextView) actionBar.getCustomView().findViewById(R.id.text1)).setText(R.string.attached_drawing);

          try {
               if ( null != ActiveRecord.currentNote.pathToDrawing ) {
                    ObjectInputStream oin = new ObjectInputStream(new FileInputStream(new File(ActiveRecord.currentNote.pathToDrawing)));
                    @SuppressWarnings ( "unchecked" )
                    List <PathSerializable> paths = (List <PathSerializable>) oin.readObject();
                    oin.close();
                    cDrawingView.setPaths(paths);
                    cDrawingView.invalidate();
               }
               cDrawingView.setColor(Color.WHITE);
               cDrawingView.setDrawingShape(CDrawingView.ShapesType.STANDART_DRAWING);
          } catch (Exception e) {
               e.printStackTrace();
          }

          dialogBuilder = new NiftyDialogBuilder(this);

          ibBrushColours.setOnTouchListener(Utils.touchListener);
          ibDrawingSettings.setOnTouchListener(Utils.touchListener);
          ibShapes.setOnTouchListener(Utils.touchListener);
     }

     @Override protected void onPause() {
          super.onPause();
          dialogBuilder.dismiss();
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
          onBackClick();
          return super.onOptionsItemSelected(item);
     }

     private void onBackClick() {
          dialogBuilder.withButton1Text(getResources().getString(android.R.string.cancel)).withButton2Text(getResources().getString(R.string.save)).withIcon(R.drawable.warning).withEffect(Effectstype.Slit).withTitle(getResources().getString(R.string.drawing_has_not_been_saved))
                    .withMessage(getResources().getString(R.string.save_drawing)).setButton1Click(new View.OnClickListener() {
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
               Utils.showCustomToast(ADrawingView.this, R.string.drawing_has_been_saved, R.drawable.ok);
          } else {
               Utils.showCustomToast(ADrawingView.this, R.string.cannot_save_drawing, R.drawable.warning);
          }
          onBackPressed();
     }

     @Click void ibDrawingSettings() {
          inverseHorizontalScrollViewVisibility(svDrawingSettings);
          YoYo.with(Techniques.Landing).duration(700).playOn(svDrawingSettings);
          svBrushColour.setVisibility(View.GONE);
          svDrawShapes.setVisibility(View.GONE);
          ivShapesDone.setVisibility(View.INVISIBLE);
          ivBrushColoursDone.setVisibility(View.INVISIBLE);
          ibDrawingSettingsDone.setVisibility(View.VISIBLE);
          Utils.showStickyNotification(ADrawingView.this, R.string.drawing_settings, AppMsg.STYLE_INFO, 1500);
     }

     @Click void ibBrushColours() {
          YoYo.with(Techniques.Landing).duration(700).playOn(svBrushColour);
          inverseHorizontalScrollViewVisibility(svBrushColour);
          svDrawingSettings.setVisibility(View.GONE);
          svDrawShapes.setVisibility(View.GONE);

          ivShapesDone.setVisibility(View.INVISIBLE);
          ivBrushColoursDone.setVisibility(View.VISIBLE);
          ibDrawingSettingsDone.setVisibility(View.INVISIBLE);
          Utils.showStickyNotification(ADrawingView.this, R.string.brush_color, AppMsg.STYLE_INFO, 1500);
     }

     @Click void ibShapes() {
          YoYo.with(Techniques.Landing).duration(700).playOn(svDrawShapes);
          inverseHorizontalScrollViewVisibility(svDrawShapes);
          svDrawingSettings.setVisibility(View.GONE);
          svBrushColour.setVisibility(View.GONE);
          ivShapesDone.setVisibility(View.VISIBLE);
          ivBrushColoursDone.setVisibility(View.INVISIBLE);
          ibDrawingSettingsDone.setVisibility(View.INVISIBLE);
          Utils.showStickyNotification(ADrawingView.this, R.string.shapes, AppMsg.STYLE_INFO, 1500);
     }

     private void inverseHorizontalScrollViewVisibility(ScrollView view) {
          int visibility = view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
          view.setVisibility(visibility);
     }

     @Click void ibColorPicker() {
          ibColorPicker.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          Utils.showStickyNotification(ADrawingView.this, R.string.choose_custom_color, AppMsg.STYLE_CONFIRM, 1500);
          try {
               picker.setOldCenterColor(cDrawingView.getCurrentColor());
               picker.setColor(cDrawingView.getCurrentColor());
               saturationBar.setSaturation(0.9f);

          } catch (Exception ex) {
               Utils.logw(ex.getMessage());
          }
          dialogChangeColour.show();
     }

     @Click void ibClearAll() {

          dialogBuilder.withButton1Text(getResources().getString(android.R.string.cancel)).withButton2Text(getResources().getString(android.R.string.yes)).withIcon(R.drawable.warning).withEffect(Effectstype.Slit).withTitle(getResources().getString(R.string.clear_canvas))
                    .withMessage(getResources().getString(R.string.do_you_really_want_clear_canvas)).setButton1Click(new View.OnClickListener() {
                         @Override public void onClick(View v) {
                              dialogBuilder.dismiss();
                         }
                    }).setButton2Click(new OnClickListener() {
                         @Override public void onClick(View v) {
                              ivColorAndShape.setImageResource(R.drawable.free_drawing);
                              cDrawingView.disableEraserMode();
                              cDrawingView.clearAll();
                              ibClearAll.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
                              Utils.showStickyNotification(ADrawingView.this, R.string.clear_drawing, AppMsg.STYLE_CONFIRM, 1500);
                              dialogBuilder.dismiss();
                         }
                    }).show();
     }

     @Click void ibEraser() {
          ibEraser.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          cDrawingView.setEraserMode();
          ivColorAndShape.setBackgroundColor(Color.TRANSPARENT);
          ivColorAndShape.setImageResource(R.drawable.erase);
          Utils.showStickyNotification(ADrawingView.this, R.string.erase_mode_is_active, AppMsg.STYLE_CONFIRM, 1500);
     }

     @Click void ibThin() {
          ibThin.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          cDrawingView.setBrushSize(15);
          Utils.showStickyNotification(ADrawingView.this, R.string.thick_brush, AppMsg.STYLE_INFO, 1500);
     }

     @Click void ibMedium() {
          ibMedium.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          cDrawingView.setBrushSize(10);
          Utils.showStickyNotification(ADrawingView.this, R.string.medium_brush_size, AppMsg.STYLE_INFO, 1500);
     }

     @Click void ibThick() {
          ibThick.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          cDrawingView.setBrushSize(5);
          Utils.showStickyNotification(ADrawingView.this, R.string.thin_brush_size, AppMsg.STYLE_INFO, 1500);
     }

     @Click void ibShapesRectangle() {
          cDrawingView.disableEraserMode();
          ibShapesRectangle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.RECTANGLE);
          ivColorAndShape.setImageResource(R.drawable.rectangle);
          Utils.showStickyNotification(ADrawingView.this, R.string.draw_rectangle, AppMsg.STYLE_CONFIRM, 1500);
     }

     @Click void ibShapesCircle() {
          cDrawingView.disableEraserMode();
          ibShapesCircle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.CIRCLE);
          ivColorAndShape.setImageResource(R.drawable.circle);
          Utils.showStickyNotification(ADrawingView.this, R.string.draw_circle, AppMsg.STYLE_CONFIRM, 1500);
     }

     @Click void ibShapesLine() {
          cDrawingView.disableEraserMode();
          ibShapesLine.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.LINE);
          ivColorAndShape.setImageResource(R.drawable.line);
          Utils.showStickyNotification(ADrawingView.this, R.string.draw_line, AppMsg.STYLE_CONFIRM, 1500);
     }

     @Click void ibShapesTriangle() {
          cDrawingView.disableEraserMode();
          ibShapesTriangle.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.TRIANGLE);
          ivColorAndShape.setImageResource(R.drawable.triangle);
          Utils.showStickyNotification(ADrawingView.this, R.string.draw_triangle, AppMsg.STYLE_CONFIRM, 1500);
     }

     @Click void ibShapesFreeDrawing() {
          cDrawingView.disableEraserMode();
          ibShapesFreeDrawing.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          cDrawingView.setDrawingShape(CDrawingView.ShapesType.STANDART_DRAWING);
          ivColorAndShape.setImageResource(R.drawable.free_drawing);
          Utils.showStickyNotification(ADrawingView.this, R.string.free_drawing, AppMsg.STYLE_CONFIRM, 1500);
     }

     @Click void ibUndo() {
          ibUndo.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          cDrawingView.redo();
          Utils.showStickyNotification(ADrawingView.this, R.string.redo, AppMsg.STYLE_CONFIRM, 1500);
     }

     @Click void ibRedo() {
          ibRedo.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
          cDrawingView.undo();
          Utils.showStickyNotification(ADrawingView.this, R.string.undo, AppMsg.STYLE_CONFIRM, 1500);

     }

     @Override public void onColorChanged(int color) {
          colorToSet = color;
     }

     @Override public void onClick(View v) {
          switch (v.getId()) {
               case R.id.ivRightOkButton:
                    saveDrawing();
                    return;
          }
          cDrawingView.disableEraserMode();
          int color = Color.parseColor(v.getTag().toString());
          // if color white -> image become invisible, becouse it's white as well, so display black background and white image
          if ( color == Color.WHITE ) {
               ivColorAndShape.setBackgroundColor(Color.TRANSPARENT);
          } else {
               ivColorAndShape.setBackgroundColor(color);
          }
          cDrawingView.setColor(color);
          v.startAnimation(AnimationManager.load(R.anim.pump_bottom, 1000));
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