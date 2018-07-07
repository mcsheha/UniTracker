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

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>{

    private NoteListAdapter.OnItemClickListener mListener;
    private Context mContext;
    private Cursor mCursor;

    public NoteListAdapter (Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(NoteListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class NoteListViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;

        public NoteListViewHolder(View itemView, final NoteListAdapter.OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.note_imageView);
            mTextView1 = itemView.findViewById(R.id.note_textView);
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
    public NoteListAdapter.NoteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        NoteListAdapter.NoteListViewHolder alvh = new NoteListAdapter.NoteListViewHolder(v, mListener);
        return alvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.NoteListViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String noteText = mCursor.getString(mCursor.getColumnIndex(DBTables.noteTable.COLUMN_NOTE_TEXT));

        long id = mCursor.getLong(mCursor.getColumnIndex(DBTables.noteTable.COLUMN_NOTE_ID));

        holder.mImageView.setImageResource(R.drawable.note);
        holder.mTextView1.setText(noteText);
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