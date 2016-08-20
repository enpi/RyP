package com.codamasters.ryp.utils.adapter.list.comments;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codamasters.ryp.R;
import com.codamasters.ryp.model.Comment;
import com.codamasters.ryp.utils.adapter.list.FirebaseListAdapter;
import com.google.firebase.database.Query;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class CommentListAdapter extends FirebaseListAdapter<Comment> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUserid;
    private Context context;

    public CommentListAdapter(Context context, Query ref, Activity activity, int layout, String mUserid) {
        super(ref, Comment.class, layout, activity);
        this.context = context;
        this.mUserid = mUserid;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Comment model = mModels.get(i);

        if (model.getAuthor_id().equals(mUserid)) {
            view = mInflater.inflate(R.layout.own_chat_message, viewGroup, false);

        }else{
            view = mInflater.inflate(R.layout.other_chat_message, viewGroup, false);
        }

        // Call out to subclass to marshall this model into the provided view
        populateView(view, model, null, 0);
        return view;

    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Comment chat, String key, int i) {
        // Map a Chat object to an entry in our listview

        if(!chat.getAuthor_id().equals(mUserid)) {
            TextView authorText = (TextView) view.findViewById(R.id.author);
            authorText.setText(chat.getAuthor_name());
        }

        TextView contentText = (TextView) view.findViewById(R.id.message);
        contentText.setText(chat.getMessage());

        TextView timestampText = (TextView) view.findViewById(R.id.timestamp);

        String elapsed_time_string = (String) DateUtils.getRelativeTimeSpanString(context, chat.getTimestamp());

        timestampText.setText(elapsed_time_string);
    }
}
