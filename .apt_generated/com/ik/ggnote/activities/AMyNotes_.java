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

public final class AMyNotes_
    extends AMyNotes
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_my_notes);
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

    public static AMyNotes_.IntentBuilder_ intent(Context context) {
        return new AMyNotes_.IntentBuilder_(context);
    }

    public static AMyNotes_.IntentBuilder_ intent(Fragment supportFragment) {
        return new AMyNotes_.IntentBuilder_(supportFragment);
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
        twAmoutNotes = ((TextView) hasViews.findViewById(id.twAmoutNotes));
        twMyNotes = ((TextView) hasViews.findViewById(id.twMyNotes));
        twDate = ((TextView) hasViews.findViewById(id.twDate));
        ivPrevDay = ((ImageView) hasViews.findViewById(id.ivPrevDay));
        ivNextDay = ((ImageView) hasViews.findViewById(id.ivNextDay));
        twAmoutFinished = ((TextView) hasViews.findViewById(id.twAmoutFinished));
        twCompleted = ((TextView) hasViews.findViewById(id.twCompleted));
        llMyNotes = ((LinearLayout) hasViews.findViewById(id.llMyNotes));
        {
            View view = hasViews.findViewById(id.ivNextDay);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AMyNotes_.this.ivNextDay();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.ivPrevDay);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AMyNotes_.this.ivPrevDay();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.twDate);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AMyNotes_.this.twDate();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.twCompleted);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AMyNotes_.this.twCompleted();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.twMyNotes);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AMyNotes_.this.twMyNotes();
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
            intent_ = new Intent(context, AMyNotes_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, AMyNotes_.class);
        }

        public Intent get() {
            return intent_;
        }

        public AMyNotes_.IntentBuilder_ flags(int flags) {
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
