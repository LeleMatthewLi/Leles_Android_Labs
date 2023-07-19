package com.algonquincollege.lelesandroidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.algonquincollege.lelesandroidlabs.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {

    ChatMessage selected;

    public MessageDetailsFragment(ChatMessage m){
        selected = m;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.messageText.setText(selected.getMessage());
        binding.timeText.setText(selected.getTimeSent());
        binding.databaseText.setText( "Id = " + selected.id );

        return binding.getRoot();
    };
}
