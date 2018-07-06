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

public class CoursesInTermAdapter extends RecyclerView.Adapter<CoursesInTermAdapter.CoursesInTermViewHolder> {
    private CoursesInTermAdapter.OnItemClickListener mListener;
    private Context mContext;
    private Cursor mCursor;

    public CoursesInTermAdapter (Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CoursesInTermAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class CoursesInTermViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mDesignator;
        public TextView mName;
        public TextView mCourseCUs;


        public CoursesInTermViewHolder(View itemView, final CoursesInTermAdapter.OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.courses_in_term_imageView);
            mDesignator = itemView.findViewById(R.id.courses_in_term_crse_designator);
            mName = itemView.findViewById(R.id.courses_in_term_crse_name);
            mCourseCUs = itemView.findViewById(R.id.courses_in_term_crse_cus);

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
    public CoursesInTermAdapter.CoursesInTermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_in_term_item, parent, false);
        CoursesInTermAdapter.CoursesInTermViewHolder citvh = new CoursesInTermAdapter.CoursesInTermViewHolder(v, mListener);
        return citvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesInTermAdapter.CoursesInTermViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String courseDesignator = mCursor.getString(mCursor.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR));
        String courseName = mCursor.getString(mCursor.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_NAME));
        String courseCUs = mCursor.getString(mCursor.getColumnIndex(DBTables.courseTable.COLUMN_CU));

        long id = mCursor.getLong(mCursor.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_ID));

        holder.mImageView.setImageResource(R.drawable.notebook);
        holder.mDesignator.setText(courseDesignator);
        holder.mName.setText(courseName);
        holder.mCourseCUs.setText(courseCUs);
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





























