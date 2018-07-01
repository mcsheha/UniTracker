package com.mikeshehadeh.unitracker;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertListAdapter extends RecyclerView.Adapter<AlertListAdapter.AlertListViewHolder>{

    private AlertListAdapter.OnItemClickListener mListener;
    private Context mContext;
    private Cursor mCursor;

    public AlertListAdapter (Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AlertListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class AlertListViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public AlertListViewHolder(View itemView, final AlertListAdapter.OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.alert_imageView);
            mTextView1 = itemView.findViewById(R.id.alert_textView);
            mTextView2 = itemView.findViewById(R.id.assmt_textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }

                }
            });

        }
    }

    @NonNull
    @Override
    public AlertListAdapter.AlertListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_item, parent, false);
        AlertListAdapter.AlertListViewHolder alvh = new AlertListAdapter.AlertListViewHolder(v, mListener);
        return alvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AlertListAdapter.AlertListViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String alertName = mCursor.getString(mCursor.getColumnIndex(DBTables.alertTable.COLUMN_ALERT_TYPE));
        String alertDateTime = mCursor.getString(mCursor.getColumnIndex(DBTables.alertTable.COLUMN_ALERT_DATETIME));

        long id = mCursor.getLong(mCursor.getColumnIndex(DBTables.alertTable.COLUMN_ALERT_ID));

        holder.mImageView.setImageResource(R.drawable.wgu_logo_cropped);
        holder.mTextView1.setText(alertName);
        holder.mTextView2.setText(alertDateTime);
        holder.itemView.setTag(id);
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}





















