package com.codamasters.ryp.utils.adapter.list.university;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.codamasters.ryp.model.Degree;
import com.codamasters.ryp.model.Professor;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is a generic way of backing an Android ListView with a Firebase location.
 * It handles all of the child events at the given Firebase location. It marshals received data into the given
 * class type. Extend this class and provide an implementation of <code>populateView</code>, which will be given an
 * instance of your list item mLayout and an instance your class that holds your data. Simply populate the view however
 * you like and this class will handle updating the list as the data changes.
 *
 * @param <T> The class type to use as a model for the data contained in the children of the given Firebase location
 */
public abstract class FirebaseUniversityListAdapter<T> extends BaseAdapter {

    private Query mRef;
    private Class<T> mModelClass;
    private int mLayout;
    public LayoutInflater mInflater;
    private ChildEventListener mListener;
    private ValueEventListener valueEventListener;


    private Map<String, T> objects;
    private ArrayList<T> mValues;

    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mModelClass Firebase will marshall the data at a location into an instance of a class that you provide
     * @param mLayout     This is the mLayout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of mModelClass.
     * @param activity    The activity containing the ListView
     */
    public FirebaseUniversityListAdapter(Query mRef, final Class<T> mModelClass, final int mLayout, Activity activity) {

        Log.d("Firebase", "NEW LIST ADAPTER");

        this.mRef = mRef;
        this.mModelClass = mModelClass;
        this.mLayout = mLayout;
        mInflater = activity.getLayoutInflater();
        objects = new LinkedHashMap<>();
        mValues = new ArrayList<>();
        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        mListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                Log.d("Firebase", "CHILD ADDED");

                String key = dataSnapshot.getKey();

                T model = dataSnapshot.getValue(FirebaseUniversityListAdapter.this.mModelClass);

                objects.put(key, model);

                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // One of the mModels changed. Replace it in our list and name mapping
                String key = dataSnapshot.getKey();
                T newModel = dataSnapshot.getValue(FirebaseUniversityListAdapter.this.mModelClass);
                objects.put(key, newModel);
                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // A model was removed from the list. Remove it from our list and the name mapping
                String key = dataSnapshot.getKey();

                objects.remove(key);

                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }
        });


        valueEventListener = mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Firebase", "Done loading " + dataSnapshot.getChildrenCount());

                List<Map.Entry<String, T>> list = new LinkedList<>( objects.entrySet() );
                Collections.sort( list, new Comparator<Map.Entry<String, T>>() {
                    @Override
                    public int compare( Map.Entry<String,T> lhs, Map.Entry<String, T> rhs ){

                        if(lhs.getValue() instanceof  Professor){
                            Professor p1 = (Professor) lhs.getValue();
                            Professor p2 = (Professor) rhs.getValue();
                            return p1.getElo() - p2.getElo();
                        }else{
                            Degree d1 = (Degree) lhs.getValue();
                            Degree d2 = (Degree) rhs.getValue();
                            return (int) (d1.getSumElo() - d2.getSumElo());
                        }
                    }
                } );

                Map<String, T> result = new LinkedHashMap<>();
                for (Map.Entry<String, T> entry : list) {
                    result.put( entry.getKey(), entry.getValue() );
                }
                objects.clear();
                objects = result;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cleanup() {
        // We're being destroyed, let go of our mListener and forget about all of the mModels
        mRef.removeEventListener(mListener);
        objects.clear();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);

    /**
     * Each time the data at the given Firebase location changes, this method will be called for each item that needs
     * to be displayed. The arguments correspond to the mLayout and mModelClass given to the constructor of this class.
     * <p/>
     * Your implementation should populate the view using the data contained in the model.
     *
     * @param v     The view to populate
     * @param model The object containing the data used to populate the view
     */
    protected abstract void populateView(View v, T model, String key, int i);

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
        List<Map.Entry<K, V>> list =
                new LinkedList<>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return ( o1.getValue() ).compareTo( o2.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

}
