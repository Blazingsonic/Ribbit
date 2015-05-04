package com.saier.sebastian.ribbit.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.saier.sebastian.ribbit.ParseConstants;
import com.saier.sebastian.ribbit.R;
import com.saier.sebastian.ribbit.ui.ViewImageActivity;

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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mSenderLabel;
        public ImageView mMessageIcon;
        public IMyViewHolderClicks mListener;

        public ViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);

            mListener = listener;
            mSenderLabel = (TextView) itemView.findViewById(R.id.senderLabel);
            mMessageIcon = (ImageView) itemView.findViewById(R.id.messageIcon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                mListener.onPotato(v);
        }

        public static interface IMyViewHolderClicks {
            public void onPotato(View caller);
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
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.inbox_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view, new InboxAdapter.ViewHolder.IMyViewHolderClicks() {
            public void onPotato(View caller) {
                Intent intent = new Intent(mContext, ViewImageActivity.class);
                //intent.setData(fileUri);
                mContext.startActivity(intent);

                /*ParseObject message = mMessages.get(); // or getLayoutPosition
                String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
                ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
                Uri fileUri = Uri.parse(file.getUrl());

                if (messageType.equals(ParseConstants.TYPE_IMAGE)) {
                    // View the image
                    Intent intent = new Intent(mContext, ViewImageActivity.class);
                    intent.setData(fileUri);
                    mContext.startActivity(intent);
                }
                else {
                    // View the video
                }*/
            }
        });
        return holder;
    }
}
