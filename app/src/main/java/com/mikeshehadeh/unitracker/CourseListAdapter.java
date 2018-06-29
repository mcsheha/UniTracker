
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


public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {
    private OnItemClickListener mListener;
    private Context mContext;
    private Cursor mCursor;


    public CourseListAdapter (Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class CourseListViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public CourseListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.crs_imageView);
            mTextView1 = itemView.findViewById(R.id.crs_textView);
            mTextView2 = itemView.findViewById(R.id.crs_textView2);
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
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        CourseListViewHolder clvh = new CourseListViewHolder(v, mListener);
        return clvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String courseID = mCursor.getString(mCursor.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR));
        String courseTitle = mCursor.getString(mCursor.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_NAME));
        long id = mCursor.getLong(mCursor.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_ID));

        holder.mImageView.setImageResource(R.drawable.wgu_logo_cropped);
        holder.mTextView1.setText(courseID);
        holder.mTextView2.setText(courseTitle);
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

