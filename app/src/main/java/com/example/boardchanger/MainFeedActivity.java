package com.example.boardchanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boardchanger.model.Board;
import com.example.boardchanger.model.Model;

import java.util.List;

public class MainFeedActivity extends AppCompatActivity {

    RecyclerView boardsList;
    List<Board> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        boardsList = findViewById(R.id.mainfeed_boardslist_recyclerview);

        boardsList.hasFixedSize();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        boardsList.setLayoutManager(layoutManager);

        data = Model.instance.getAllBoards();

        MyAdapater adapter = new MyAdapater();
        boardsList.setAdapter(adapter);

        BoardDetailsFragment boardDetails = new BoardDetailsFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tran = manager.beginTransaction();
        tran.add(R.id.mainfeed_frg_container, boardDetails);
        tran.commit();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public OnItemClickListener listener;
        TextView boardName, boardPrice, boardYear;
        ImageView boardImage;
        int position;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            boardName = itemView.findViewById(R.id.listrow_board_name);
            boardPrice = itemView.findViewById(R.id.listrow_board_price);
            boardYear = itemView.findViewById(R.id.listrow_board_year);
            boardImage = itemView.findViewById(R.id.listrow_image_v);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }

        public void bindData(Board board, int position) {
            boardName.setText(board.name);
            boardYear.setText(board.year);
            boardPrice.setText(board.price);
            this.position = position;
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    class MyAdapater extends RecyclerView.Adapter<MyViewHolder> {
        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.list_row, null);
            MyViewHolder holder = new MyViewHolder(v);
            holder.listener = listener;
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Board board = data.get(position);
            holder.bindData(board, position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}