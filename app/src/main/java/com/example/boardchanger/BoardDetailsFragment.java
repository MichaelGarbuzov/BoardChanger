package com.example.boardchanger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board_details, container, false);
        String boardNameData = BoardDetailsFragmentArgs.fromBundle(getArguments()).getBoardName();
        Board board = Model.instance.getBoardByName(boardNameData);

        TextView boardName = view.findViewById(R.id.board_details_name);
        TextView boardYear = view.findViewById(R.id.board_details_year);
        TextView boardPrice = view.findViewById(R.id.board_details_price);
        TextView boardDesc = view.findViewById(R.id.board_details_desc);
        ImageView boardImage = view.findViewById(R.id.board_details_image);

        boardName.setText(board.getName());
        boardYear.setText(board.getYear());
        boardPrice.setText(board.getPrice());
        boardDesc.setText(board.getDescription());

        Button backBtn = view.findViewById(R.id.board_details_back_btn);
        backBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigateUp();

        });

        Button sendMsg = view.findViewById(R.id.board_details_sendmessage_btn);
        sendMsg.setOnClickListener(Navigation.createNavigateOnClickListener(
                BoardDetailsFragmentDirections.actionBoardDetailsFragmentToSendMessageFragment()));
        return view;
    }
}