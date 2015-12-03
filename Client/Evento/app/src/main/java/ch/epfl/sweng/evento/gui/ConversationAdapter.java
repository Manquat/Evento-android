package ch.epfl.sweng.evento.gui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.Conversation;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.gui.CommentActivity;

/**
 * List view adapter for a conversation
 */
public class ConversationAdapter extends BaseAdapter {
    private Conversation mConversation;
    private Context mContext;

    public ConversationAdapter(Context context, Conversation conversation) {
        mContext = context;
        mConversation = conversation;
    }

    @Override
    public int getCount() {
        return mConversation.size();
    }

    @Override
    public Object getItem(int position) {
        return mConversation.getComment(position);
    }

    @Override
    public long getItemId(int position) {
        return mConversation.getComment(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater.from(mContext));
            convertView = inflater.inflate(R.layout.list_comment, parent, false);
        }

        Comment comment = mConversation.getComment(position);

        TextView owner = (TextView) convertView.findViewById(R.id.list_comment_owner);
        owner.setText(comment.getOwner().getName());

        TextView message = (TextView) convertView.findViewById(R.id.list_comment_message);
        message.setText(comment.getMessage());

        return convertView;
    }
}