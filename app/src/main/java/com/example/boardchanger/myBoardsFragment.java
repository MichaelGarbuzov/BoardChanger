package com.example.boardchanger;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.boardchanger.adapters.BoardsListAdapter;
import com.example.boardchanger.feed.BoardsListFragmentDirections;
import com.example.boardchanger.model.Model;
import com.example.boardchanger.model.posts.BoardsListViewModel;

public class myBoardsFragment extends Fragment {
    BoardsListViewModel viewModel; // TODO change view model to MyBoardsListViewModel
    BoardsListAdapter adapter;
    SwipeRefreshLayout swipeRefresh;

    public myBoardsFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(BoardsListViewModel.class); // TODO: change BoardsListViewModel.class to MyBoardsViewModel.class
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_boards, container, false);

        swipeRefresh = view.findViewById(R.id.boards_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshBoardsList()); // TODO: get my boards

        RecyclerView boardsList = view.findViewById(R.id.boards_list_rv);
        boardsList.setHasFixedSize(true);
        boardsList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BoardsListAdapter(viewModel.getData());
        boardsList.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String boardName = viewModel.getData().getValue().get(position).getName();
            Navigation.findNavController(v).navigate(
                    BoardsListFragmentDirections.actionBoardsListFragmentToBoardDetailsFragment(boardName)); // TODO update here also
        });
//        ImageButton add = view.findViewById(R.id.boards_add_btn);
//        add.setOnClickListener(Navigation.createNavigateOnClickListener(
//                BoardsListFragmentDirections.actionBoardsListFragmentToAddBoardFragment()));

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

}