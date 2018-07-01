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

public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.AssessmentListViewHolder>{

    private AssessmentListAdapter.OnItemClickListener mListener;
    private Context mContext;
    private Cursor mCursor;

    public AssessmentListAdapter (Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AssessmentListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class AssessmentListViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public AssessmentListViewHolder(View itemView, final AssessmentListAdapter.OnItemClickListener listener) {
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
    public AssessmentListAdapter.AssessmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_item, parent, false);
        AssessmentListAdapter.AssessmentListViewHolder alvh = new AssessmentListAdapter.AssessmentListViewHolder(v, mListener);
        return alvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentListAdapter.AssessmentListViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String assmtName = mCursor.getString(mCursor.getColumnIndex(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE));
        String assmtType = mCursor.getString(mCursor.getColumnIndex(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE));

        long id = mCursor.getLong(mCursor.getColumnIndex(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID));

        holder.mImageView.setImageResource(R.drawable.wgu_logo_cropped);
        holder.mTextView1.setText(assmtName);
        holder.mTextView2.setText(assmtType);
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
