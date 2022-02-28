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

import java.util.List;

public class BoardsListAdapter extends RecyclerView.Adapter<BoardsListAdapter.MyViewHolder> {
    private LiveData<List<Board>> boards;
    private OnItemClickListener listener;

    public BoardsListAdapter(LiveData<List<Board>> boards) {
        this.boards = boards;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Board board = boards.getValue().get(position);
        holder.bind(board);
    }

    @Override
    public int getItemCount() {
        if (boards.getValue() == null) {
            return 0;
        }
        return boards.getValue().size();
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