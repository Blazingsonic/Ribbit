package com.saier.sebastian.ribbit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Sebastian on 28.04.2015.
 */
public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    private List<ParseObject> mMessages;
    private Context mContext;

    public InboxAdapter(List<ParseObject> messages, Context context) {
        mMessages = messages;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mSenderLabel;
        public ImageView mMessageIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            mSenderLabel = (TextView) itemView.findViewById(R.id.senderLabel);
            mMessageIcon = (ImageView) itemView.findViewById(R.id.messageIcon);
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ParseObject message = mMessages.get(position);

        if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
            holder.mMessageIcon.setImageResource(R.mipmap.ic_action_picture);
        }
        else {
            holder.mMessageIcon.setImageResource(R.mipmap.ic_action_camera);
        }
        holder.mSenderLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.inbox_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
}
