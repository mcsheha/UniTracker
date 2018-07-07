
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


public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.TermListViewHolder> {
    private OnItemClickListener mListener;
    private Context mContext;
    private Cursor mCursor;


    public TermListAdapter (Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class TermListViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public TermListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
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
    public TermListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_item, parent, false);
        TermListViewHolder tlvh = new TermListViewHolder(v, mListener);
        return tlvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TermListViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String termID = "Term " + mCursor.getString(mCursor.getColumnIndex(DBTables.termTable.COLUMN_TERM_ID));
        String termDates = mCursor.getString(mCursor.getColumnIndex(DBTables.termTable.COLUMN_TERM_STARTDATE)) + " - " +
                mCursor.getString((mCursor.getColumnIndex(DBTables.termTable.COLUMN_TERM_ENDDATE)));
        long id = mCursor.getLong(mCursor.getColumnIndex(DBTables.termTable.COLUMN_TERM_ID));

        holder.mImageView.setImageResource(R.drawable.cap);
        holder.mTextView1.setText(termID);
        holder.mTextView2.setText(termDates);
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

