package com.algonquincollege.lelesandroidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.algonquincollege.lelesandroidlabs.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.detailsMessage.setText(selected.message);
        binding.detailsTime.setText(selected.timeSent);
        binding.detailsId.setText("Id = " + selected.id);
        binding.detailsIsSend.setText("Sent = " + selected.isSentButton);

        return binding.getRoot();
    }

    ChatMessage selected;

    public MessageDetailsFragment(ChatMessage m) {
        selected = m;
    }
}