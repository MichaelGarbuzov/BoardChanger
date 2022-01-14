package com.example.boardchanger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boardchanger.model.Board;
import com.example.boardchanger.model.Model;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardDetailsFragment extends Fragment {
    private static final String ARG_BOARD_NAME = "ARG_BOARD_NAME";

    String boardName;
    String boardPrice;
    String boardYear;
    String boardDesc;
    ImageView boardImage;

    public BoardDetailsFragment() {
    }

    public static BoardDetailsFragment newInstance(String studentId) {
        BoardDetailsFragment fragment = new BoardDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BOARD_NAME, fragment.boardName );
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            boardName = getArguments().getString(ARG_BOARD_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board_details, container, false);
        Board board = Model.instance.getBoardByName(boardName);

        TextView boardName = view.findViewById(R.id.board_details_name);
        TextView boardYear = view.findViewById(R.id.board_details_year);
        TextView boardPrice = view.findViewById(R.id.board_details_price);
        TextView boardDesc = view.findViewById(R.id.board_details_desc);
        ImageView boardImage = view.findViewById(R.id.board_details_image);

        boardName.setText(board.getName());
        boardYear.setText(board.getYear());
        boardPrice.setText(board.getPrice());
        boardDesc.setText(board.getDescription());
        return view;
    }
}