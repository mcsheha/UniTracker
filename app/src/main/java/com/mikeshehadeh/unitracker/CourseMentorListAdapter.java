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

public class CourseMentorListAdapter extends RecyclerView.Adapter<CourseMentorListAdapter.CourseMentorListViewHolder>{
    private CourseMentorListAdapter.OnItemClickListener mListener;
    private Context mContext;
    private Cursor mCursor;

    public CourseMentorListAdapter (Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CourseMentorListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class CourseMentorListViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewName;
        public TextView mTextViewEmail;
        public TextView mTextViewPhone;

        public CourseMentorListViewHolder(View itemView, final CourseMentorListAdapter.OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.course_mentor_item_image_view);
            mTextViewName = itemView.findViewById(R.id.course_mentor_item_text_view_name);
            mTextViewEmail = itemView.findViewById(R.id.course_mentor_item_text_view_email);
            mTextViewPhone = itemView.findViewById(R.id.course_mentor_item_text_view_phone);

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
    public CourseMentorListAdapter.CourseMentorListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_mentor_item, parent, false);
        CourseMentorListAdapter.CourseMentorListViewHolder cmlvh = new CourseMentorListAdapter.CourseMentorListViewHolder(v, mListener);
        return cmlvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseMentorListAdapter.CourseMentorListViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String mentorName = mCursor.getString(mCursor.getColumnIndex(DBTables.mentorTable.COLUMN_MENTOR_NAME));
        String mentorEmail = mCursor.getString(mCursor.getColumnIndex(DBTables.mentorTable.COLUMN_MENTOR_EMAIL));
        String mentorPhone = mCursor.getString(mCursor.getColumnIndex(DBTables.mentorTable.COLUMN_MENTOR_PHONE));
        //String mentorID = mCursor.getString(mCursor.getColumnIndex(DBTables.Table.COLUMN_COURSE_DESIGNATOR));
        //String courseTitle = mCursor.getString(mCursor.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_NAME));
        long id = mCursor.getLong(mCursor.getColumnIndex(DBTables.mentorTable.COLUMN_MENTOR_ID));

        holder.mImageView.setImageResource(R.drawable.person);
        holder.mTextViewName.setText(mentorName);
        holder.mTextViewEmail.setText(mentorEmail);
        holder.mTextViewPhone.setText(mentorPhone);
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
























