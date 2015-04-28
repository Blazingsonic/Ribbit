package com.saier.sebastian.ribbit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sebastian on 28.04.2015.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private String[] mDataset;
    private Context mContext;

    public FriendsAdapter(String[] dataset, Context context) {
        mDataset = dataset;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public TextView mNew;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.dayNameLabel);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(
                            mContext,
                            "onItemClick - " + getPosition() + " - "
                                    + mTextView.getText().toString() + " - "
                                    + mDataset[getPosition()], 0).show();
                }
            });
            mNew = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mNew.setText("Hellllooo");
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset[position]);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.friends_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

}
