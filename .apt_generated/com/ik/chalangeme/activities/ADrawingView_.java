//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.ik.chalangeme.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import com.ik.chalangeme.R.id;
import com.ik.chalangeme.R.layout;
import com.ik.chalangeme.custom.CDrawingView;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ADrawingView_
    extends ADrawingView
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_drawview);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static ADrawingView_.IntentBuilder_ intent(Context context) {
        return new ADrawingView_.IntentBuilder_(context);
    }

    public static ADrawingView_.IntentBuilder_ intent(Fragment supportFragment) {
        return new ADrawingView_.IntentBuilder_(supportFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        ibShapesTriangle = ((ImageButton) hasViews.findViewById(id.ibShapesTriangle));
        ibColourGreen = ((ImageButton) hasViews.findViewById(id.ibColourGreen));
        ibShapesRectangle = ((ImageButton) hasViews.findViewById(id.ibShapesRectangle));
        ibShapesFreeDrawing = ((ImageButton) hasViews.findViewById(id.ibShapesFreeDrawing));
        ibRedo = ((ImageButton) hasViews.findViewById(id.ibRedo));
        ibEraser = ((ImageButton) hasViews.findViewById(id.ibEraser));
        ibPencilSettings = ((ImageButton) hasViews.findViewById(id.ibPencilSettings));
        ibClearAll = ((ImageButton) hasViews.findViewById(id.ibClearAll));
        cDrawingView = ((CDrawingView) hasViews.findViewById(id.cDrawingView));
        ibThick = ((ImageButton) hasViews.findViewById(id.ibThick));
        ibColourBlue = ((ImageButton) hasViews.findViewById(id.ibColourBlue));
        ibColorsPanel = ((ImageButton) hasViews.findViewById(id.ibColorsPanel));
        ibColorPicker = ((ImageButton) hasViews.findViewById(id.ibColorPicker));
        ibDrawText = ((ImageButton) hasViews.findViewById(id.ibDrawText));
        ibUndo = ((ImageButton) hasViews.findViewById(id.ibUndo));
        ibShapesCircle = ((ImageButton) hasViews.findViewById(id.ibShapesCircle));
        ibThin = ((ImageButton) hasViews.findViewById(id.ibThin));
        ibMedium = ((ImageButton) hasViews.findViewById(id.ibMedium));
        ibColourRed = ((ImageButton) hasViews.findViewById(id.ibColourRed));
        ibColourBlack = ((ImageButton) hasViews.findViewById(id.ibColourBlack));
        ibShapesLine = ((ImageButton) hasViews.findViewById(id.ibShapesLine));
        {
            View view = hasViews.findViewById(id.ibThick);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibThick();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibColorPicker);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibColorPicker();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibUndo);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibUndo();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibClearAll);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibClearAll();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibDrawText);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibDrawText();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibThin);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibThin();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibShapesLine);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibShapesLine();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibColourRed);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibColourRed();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibShapesRectangle);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibShapesRectangle();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibShapesTriangle);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibShapesTriangle();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibMedium);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibMedium();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibColourBlue);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibColourBlue();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibColourGreen);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibColourGreen();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibRedo);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibRedo();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibShapesFreeDrawing);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibShapesFreeDrawing();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibShapesCircle);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibShapesCircle();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibColourBlack);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibColourBlack();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibEraser);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ADrawingView_.this.ibEraser();
                    }

                }
                );
            }
        }
        afterViews();
    }

    @Override
    public void ibDrawText() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ADrawingView_.super.ibDrawText();
            }

        }
        );
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, ADrawingView_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, ADrawingView_.class);
        }

        public Intent get() {
            return intent_;
        }

        public ADrawingView_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent_, requestCode);
            } else {
                if (context_ instanceof Activity) {
                    ((Activity) context_).startActivityForResult(intent_, requestCode);
                } else {
                    context_.startActivity(intent_);
                }
            }
        }

    }

}
