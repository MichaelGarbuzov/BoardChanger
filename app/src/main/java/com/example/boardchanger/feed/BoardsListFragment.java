package com.example.boardchanger.feed;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boardchanger.R;
import com.example.boardchanger.model.posts.Board;
import com.example.boardchanger.model.Model;
import com.squareup.picasso.Picasso;

public class BoardsListFragment extends Fragment {
    BoardsListViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;

    public BoardsListFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(BoardsListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boards_list, container, false);

        swipeRefresh = view.findViewById(R.id.boards_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshBoardsList());

        RecyclerView boardsList = view.findViewById(R.id.boards_list_rv);
        boardsList.setHasFixedSize(true);
        boardsList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        boardsList.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String boardName = viewModel.getData().getValue().get(position).getName();
            Navigation.findNavController(v).navigate(
                    BoardsListFragmentDirections.actionBoardsListFragmentToBoardDetailsFragment(boardName));
        });
        ImageButton add = view.findViewById(R.id.boards_add_btn);
        add.setOnClickListener(Navigation.createNavigateOnClickListener(
                BoardsListFragmentDirections.actionBoardsListFragmentToAddBoardFragment()));

        viewModel.getData().observe(getViewLifecycleOwner(), boardList -> refresh());
        swipeRefresh.setRefreshing(Model.instance.getBoardListLoadingState().getValue() ==
                Model.BoardListLoadingState.loading);
        Model.instance.getBoardListLoadingState().observe(getViewLifecycleOwner(), boardListLoadingState -> {
            if (boardListLoadingState == Model.BoardListLoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }
        });
        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
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

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Board board = viewModel.getData().getValue().get(position);
            holder.bind(board);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData().getValue() == null) {
                return 0;
            }
            return viewModel.getData().getValue().size();
        }
    }
}
