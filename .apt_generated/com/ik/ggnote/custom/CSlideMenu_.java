//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.ik.ggnote.custom;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ik.ggnote.R.id;
import com.ik.ggnote.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;


/**
 * We use @SuppressWarning here because our java code
 * generator doesn't know that there is no need
 * to import OnXXXListeners from View as we already
 * are in a View.
 * 
 */
@SuppressWarnings("unused")
public final class CSlideMenu_
    extends CSlideMenu
    implements HasViews, OnViewChangedListener
{

    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public CSlideMenu_(Context context) {
        super(context);
        init_();
    }

    public static CSlideMenu build(Context context) {
        CSlideMenu_ instance = new CSlideMenu_(context);
        instance.onFinishInflate();
        return instance;
    }

    /**
     * The mAlreadyInflated_ hack is needed because of an Android bug
     * which leads to infinite calls of onFinishInflate()
     * when inflating a layout with a parent and using
     * the <merge /> tag.
     * 
     */
    @Override
    public void onFinishInflate() {
        if (!alreadyInflated_) {
            alreadyInflated_ = true;
            inflate(getContext(), layout.menu_drawing_companies_activity, this);
            onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        filter = ((RelativeLayout) hasViews.findViewById(id.filter));
        logout = ((RelativeLayout) hasViews.findViewById(id.logout));
        twAmoutFinished = ((TextView) hasViews.findViewById(id.twAmoutFinished));
        notes = ((RelativeLayout) hasViews.findViewById(id.notes));
        twAmoutNotes = ((TextView) hasViews.findViewById(id.twAmoutNotes));
        settings = ((RelativeLayout) hasViews.findViewById(id.settings));
        {
            View view = hasViews.findViewById(id.settings);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        CSlideMenu_.this.settings();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.filter);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        CSlideMenu_.this.filter();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.notes);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        CSlideMenu_.this.notes();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.logout);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        CSlideMenu_.this.logout();
                    }

                }
                );
            }
        }
        afterViews();
    }

}
