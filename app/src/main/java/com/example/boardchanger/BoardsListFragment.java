package com.example.boardchanger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boardchanger.model.Board;
import com.example.boardchanger.model.Model;

import org.w3c.dom.Text;

import java.util.List;

public class BoardsListFragment extends Fragment {

    List<Board> data;

    public BoardsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boards_list, container, false);
        data = Model.instance.getAllBoards();
        RecyclerView boardsList = view.findViewById(R.id.boards_list_rv);
        boardsList.setHasFixedSize(true);

        boardsList.setLayoutManager(new LinearLayoutManager(getContext()));

        MyAdapter adapter = new MyAdapter();
        boardsList.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String boardName = data.get(position).getName();
                Navigation.findNavController(v).navigate(
                        BoardsListFragmentDirections.actionBoardsListFragmentToBoardDetailsFragment(boardName));
            }
        });
        ImageButton add = view.findViewById(R.id.boards_add_btn);
        add.setOnClickListener(Navigation.createNavigateOnClickListener(
                BoardsListFragmentDirections.actionBoardsListFragmentToAddBoardFragment()));

        return view;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView boardName;
        TextView boardPrice;
        TextView boardDesc;
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
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Board board = data.get(position);
            holder.boardName.setText(board.getName());
            holder.boardYear.setText(board.getYear());
            holder.boardPrice.setText(board.getPrice());
        }
        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}