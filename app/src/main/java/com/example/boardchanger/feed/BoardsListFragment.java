package com.example.boardchanger.feed;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.boardchanger.R;
import com.example.boardchanger.adapters.BoardsListAdapter;
import com.example.boardchanger.model.Model;
import com.example.boardchanger.model.posts.Board;
import com.example.boardchanger.model.posts.BoardsListViewModel;

public class BoardsListFragment extends Fragment {
    BoardsListViewModel viewModel;
    BoardsListAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    Boolean isOnlyUserBoards = false;
    ImageButton add;


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
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_boards_list, container, false);

        swipeRefresh = view.findViewById(R.id.boards_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshBoardsList());

        RecyclerView boardsList = view.findViewById(R.id.boards_list_rv);
        boardsList.setHasFixedSize(true);
        boardsList.setLayoutManager(new LinearLayoutManager(getContext()));

        isOnlyUserBoards = BoardsListFragmentArgs.fromBundle(getArguments()).getOnlyUserBoard();
        viewModel.setIsUserBoardsOnly(isOnlyUserBoards);
        adapter = new BoardsListAdapter(viewModel.getData());
        boardsList.setAdapter(adapter);
        adapter.setEditMode(isOnlyUserBoards);

        adapter.setOnItemClickListener((v, position) -> {
            if(isOnlyUserBoards) {
                String boardID = viewModel.getData().getValue().get(position).getId();
                Navigation.findNavController(v).navigate(
                        BoardsListFragmentDirections.actionBoardsListFragmentToEditBoardFragment(boardID));
            } else {
                String boardID = viewModel.getData().getValue().get(position).getId();
                Navigation.findNavController(v).navigate(
                        BoardsListFragmentDirections.actionBoardsListFragmentToBoardDetailsFragment(boardID));
            }
        });
        add = view.findViewById(R.id.boards_add_btn);

        add.setOnClickListener(Navigation.createNavigateOnClickListener(
                BoardsListFragmentDirections.actionBoardsListFragmentToAddBoardFragment(null)));

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(isOnlyUserBoards) {
            add.setVisibility(View.GONE);
            MainFeedActivity activity = (MainFeedActivity) getActivity();
            activity.toolbar.setTitle(R.string.user_boards_title);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(isOnlyUserBoards) {
            menu.findItem(R.id.menuProfileFragment).setVisible(false);
        }
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }
}
