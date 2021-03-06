package com.example.boardchanger.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardchanger.R;
import com.example.boardchanger.model.posts.Board;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BoardsListAdapter extends RecyclerView.Adapter<BoardsListAdapter.MyViewHolder> {
    private List<Board> boards = new ArrayList<>();
    private OnItemClickListener listener;
    private Boolean editMode = false;

    public void setEditMode(Boolean editMode) {
        this.editMode = editMode;
    }

    public BoardsListAdapter() { }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        TextView editWatermark = view.findViewById(R.id.list_row_edit_watermark);
        if(!editMode) {
            editWatermark.setVisibility(View.GONE);
        }
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Board board = boards.get(position);
        holder.bind(board);
    }

    public void updateBoards(List<Board> boards) {
        this.boards.clear();
        this.boards = boards;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (boards == null) {
            return 0;
        }
        return boards.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView boardName;
        TextView boardPrice;
        TextView boardYear;
        ImageView boardImage;

        public MyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            boardName = itemView.findViewById(R.id.listrow_board_name);
            boardYear = itemView.findViewById(R.id.listrow_board_year);
            boardPrice = itemView.findViewById(R.id.listrow_board_price);
            boardImage = itemView.findViewById(R.id.listrow_image_v);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v, pos);
                }
            });
        }

        public void bind(Board board) {
            boardName.setText(board.getName());
            boardYear.setText(board.getYear());
            boardPrice.setText(board.getPrice());
            boardImage.setImageResource(R.drawable.board);
            Picasso.get().load(board.getImageUrl()).into(boardImage);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

}