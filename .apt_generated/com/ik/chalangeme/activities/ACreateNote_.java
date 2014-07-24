//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.ik.chalangeme.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.ik.chalangeme.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ACreateNote_
    extends ACreateNote
    implements HasViews
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_create_note);
    }

    private void init_(Bundle savedInstanceState) {
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

    public static ACreateNote_.IntentBuilder_ intent(Context context) {
        return new ACreateNote_.IntentBuilder_(context);
    }

    public static ACreateNote_.IntentBuilder_ intent(Fragment supportFragment) {
        return new ACreateNote_.IntentBuilder_(supportFragment);
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, ACreateNote_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, ACreateNote_.class);
        }

        public Intent get() {
            return intent_;
        }

        public ACreateNote_.IntentBuilder_ flags(int flags) {
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
