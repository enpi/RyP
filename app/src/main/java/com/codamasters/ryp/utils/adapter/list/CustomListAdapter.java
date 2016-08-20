package com.codamasters.ryp.utils.adapter.list;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Juan on 20/08/2016.
 */
public abstract class CustomListAdapter<T> extends BaseAdapter {

    private Class<T> mModelClass;
    public List<T> mModels;
    public List<String> mKeys;

    public LayoutInflater mInflater;


    public CustomListAdapter(Class<T> mModelClass, List<T> mModels, List<String> mKeys,  Activity activity){
        this.mModelClass = mModelClass;
        this.mModels = mModels;
        this.mKeys = mKeys;

        mInflater = activity.getLayoutInflater();

    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public Object getItem(int i) {
        return mModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
