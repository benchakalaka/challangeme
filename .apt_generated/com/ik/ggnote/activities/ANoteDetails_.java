//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.ik.ggnote.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ik.ggnote.R.id;
import com.ik.ggnote.R.layout;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ANoteDetails_
    extends ANoteDetails
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_note_details);
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

    public static ANoteDetails_.IntentBuilder_ intent(Context context) {
        return new ANoteDetails_.IntentBuilder_(context);
    }

    public static ANoteDetails_.IntentBuilder_ intent(Fragment supportFragment) {
        return new ANoteDetails_.IntentBuilder_(supportFragment);
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
        twNoteType = ((TextView) hasViews.findViewById(id.twNoteType));
        ivCloseInfo = ((ImageView) hasViews.findViewById(id.ivCloseInfo));
        ibViewPinOnMap = ((ImageView) hasViews.findViewById(id.ibViewPinOnMap));
        ivViewPinPhotoDone = ((ImageView) hasViews.findViewById(id.ivViewPinPhotoDone));
        ivNoteType = ((ImageView) hasViews.findViewById(id.ivNoteType));
        ivInfo = ((ImageView) hasViews.findViewById(id.ivInfo));
        llBottomDescritption = ((LinearLayout) hasViews.findViewById(id.llBottomDescritption));
        ivAlarm = ((ImageView) hasViews.findViewById(id.ivAlarm));
        ivCreatedDateAndTime = ((ImageView) hasViews.findViewById(id.ivCreatedDateAndTime));
        ivViewPinOnMapDone = ((ImageView) hasViews.findViewById(id.ivViewPinOnMapDone));
        twAlarm = ((TextView) hasViews.findViewById(id.twAlarm));
        ibViewPinPhoto = ((ImageView) hasViews.findViewById(id.ibViewPinPhoto));
        ibViewDraw = ((ImageView) hasViews.findViewById(id.ibViewDraw));
        ivViewDrawDone = ((ImageView) hasViews.findViewById(id.ivViewDrawDone));
        twTime = ((TextView) hasViews.findViewById(id.twTime));
        twDescription = ((TextView) hasViews.findViewById(id.twDescription));
        {
            View view = hasViews.findViewById(id.ibViewPinPhoto);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ANoteDetails_.this.ibViewPinPhoto();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ivCloseInfo);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ANoteDetails_.this.ivCloseInfo();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ivCreatedDateAndTime);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ANoteDetails_.this.ivCreatedDateAndTime();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibViewPinOnMap);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ANoteDetails_.this.ibViewPinOnMap();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ivInfo);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ANoteDetails_.this.ivInfo();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ivAlarm);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ANoteDetails_.this.ivAlarm();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ibViewDraw);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ANoteDetails_.this.ibViewDraw();
                    }

                }
                );
            }
        }
        afterViews();
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, ANoteDetails_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, ANoteDetails_.class);
        }

        public Intent get() {
            return intent_;
        }

        public ANoteDetails_.IntentBuilder_ flags(int flags) {
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
