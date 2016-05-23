package com.orhanobut.dialogplus;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.csware.ee.R;

/**
 * @author Orhan Obut
 */
public class ViewHolder implements Holder {

    private static final int INVALID = -1;

    private int backgroundColor;

    private ViewGroup headerContainer;
    private ViewGroup footerContainer;
    private View.OnKeyListener keyListener;

    private View contentView;
    private int viewResourceId = INVALID;

    public ViewHolder(int viewResourceId) {
        this.viewResourceId = viewResourceId;
    }

    public ViewHolder(View contentView) {
        this.contentView = contentView;
    }

    @Override
    public void addHeader(View view) {
        if (view == null) {
            return;
        }
        headerContainer.addView(view);
    }

    @Override
    public void addFooter(View view) {
        if (view == null) {
            return;
        }
        footerContainer.addView(view);
    }

    @Override
    public void setBackgroundColor(int colorResource) {
        this.backgroundColor = colorResource;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.dialog_view, parent, false);
        ViewGroup contentContainer = (ViewGroup) view.findViewById(R.id.view_container);
        contentContainer.setBackgroundColor(parent.getResources().getColor(backgroundColor));
        contentContainer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyListener == null) {
                    throw new NullPointerException("keyListener should not be null");
                }
                return keyListener.onKey(v, keyCode, event);
            }
        });
        addContent(inflater, parent, contentContainer);
        headerContainer = (ViewGroup) view.findViewById(R.id.header_container);
        footerContainer = (ViewGroup) view.findViewById(R.id.footer_container);
        return view;
    }

    private void addContent(LayoutInflater inflater, ViewGroup parent, ViewGroup container) {
        if (viewResourceId != INVALID) {
            contentView = inflater.inflate(viewResourceId, parent, false);
        } else {
            ViewGroup parentView = (ViewGroup) contentView.getParent();
            if (parentView != null) {
                parentView.removeView(contentView);
            }
        }

        container.addView(contentView);
    }

    @Override
    public void setOnKeyListener(View.OnKeyListener keyListener) {
        this.keyListener = keyListener;
    }

    @Override
    public View getInflatedView() {
        return contentView;
    }


    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        if (view != null)
            view.setText(text);
        return this;
    }
    /**
     * 为ImageView设置资源文件
     * */
    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        if (view != null)
            view.setImageResource(resId);
        return this;
    }
    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     * 如果将来有需要可以加入View的缓存
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
//        View view = mViews.get(viewId);
//        if (view == null) {
//            view = contentView.findViewById(viewId);
//            mViews.put(viewId, view);
//        }
        View view = contentView.findViewById(viewId);
        if (view != null)
            return (T) view;
        return null;
    }
}
