package com.algonquincollege.lelesandroidlabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.algonquincollege.lelesandroidlabs.databinding.ActivityChatRoomBinding;
import com.algonquincollege.lelesandroidlabs.databinding.ReceiveMessageBinding;
import com.algonquincollege.lelesandroidlabs.databinding.SentMessageBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages;
    ChatRoomViewModel chatModel ;
    ChatMessageDAO cmDAO;
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        cmDAO = db.cmDAO();

        messages = chatModel.messages.getValue();



        if(messages == null) {
            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll(cmDAO.getAllMessages()); //Once you get the data from database

                runOnUiThread(() -> binding.recyclerView.setAdapter(myAdapter)); //You can then load the RecyclerView
            });
        }

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.sendButton.setOnClickListener(click -> {
            String message = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(message, currentDateandTime, true);
            messages.add(chatMessage);
            myAdapter.notifyDataSetChanged();
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click -> {
            String message = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(message, currentDateandTime, false);
            messages.add(chatMessage);
            myAdapter.notifyDataSetChanged();
            binding.textInput.setText("");
        });

        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder( binding.getRoot() );
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder( binding.getRoot() );
                }

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.getMessage());
                holder.timeText.setText(chatMessage.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage chatMessage = messages.get(position);
                if (chatMessage.SendOrReceive()) {
                    return 0;
                } else {
                    return 1;
                }
            }



        });

        chatModel.selectedMessage.observe(this, newValue -> {
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newValue);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, chatFragment)
                    .commit();
        });

    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
//                int position = getAbsoluteAdapterPosition();
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
//
//                builder.setMessage("Do you want to delete this message: \"" + messageText.getText() + "\"?")
//                        .setTitle("Delete Message")
//                        .setNegativeButton("No", (dialog, id) -> {
//                            dialog.dismiss();
//                        })
//                        .setPositiveButton("Yes", (dialog, id) -> {
//                            ChatMessage m = messages.get(position);
//
//                            Snackbar.make(itemView, "Deleted message #" + position, Snackbar.LENGTH_SHORT)
//                                    .setAction("Undo", clk2 -> {
//                                        messages.add(position, m);
//                                        myAdapter.notifyItemInserted(position);
//                                        Executor thread = Executors.newSingleThreadExecutor();
//                                        thread.execute(() ->
//                                                cmDAO.insertMessage(m));
//                                    })
//                                    .show();
//
//                            Executor thread = Executors.newSingleThreadExecutor();
//                            thread.execute(() ->
//                                    cmDAO.deleteMessage(m));
//                            messages.remove(position);
//                            myAdapter.notifyItemRemoved(position);
//                        })
//                        .create().show();
//            });
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);

                chatModel.selectedMessage.postValue(selected);

            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        });


    }
}
}