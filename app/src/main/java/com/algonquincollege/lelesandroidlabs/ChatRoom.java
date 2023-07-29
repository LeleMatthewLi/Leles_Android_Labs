package com.algonquincollege.lelesandroidlabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    ChatRoomViewModel chatModel;

    ChatMessageDAO cmDAO;

    private RecyclerView.Adapter myAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        ChatMessage selectedMessage = chatModel.selectedMessage.getValue();

        if (itemId == R.id.item_1 && selectedMessage != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
            builder.setMessage("Do you want to delete this message: \"" + selectedMessage.getMessage() + "\"?")
                    .setTitle("Delete Message")
                    .setNegativeButton("No", (dialog, id) -> dialog.dismiss())
                    .setPositiveButton("Yes", (dialog, id) -> {
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            cmDAO.deleteMessage(selectedMessage);
                            runOnUiThread(() -> {
                                messages.remove(selectedMessage);
                                myAdapter.notifyDataSetChanged();
                                Toast.makeText(ChatRoom.this, "Message deleted", Toast.LENGTH_SHORT).show();
                            });
                        });
                    })
                    .create().show();
        } else if (itemId == R.id.item_2) {
            Toast.makeText(ChatRoom.this, "Version 1.0, created by Lele Li", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = binding.myToolbar;
        setSupportActionBar(myToolbar);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").fallbackToDestructiveMigration().build();

        cmDAO = db.cmDAO();

        messages = chatModel.messages.getValue();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (messages == null) {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll(cmDAO.getAllMessages()); //Once you get the data from database

                runOnUiThread(() -> binding.recyclerView.setAdapter(myAdapter)); //You can then load the RecyclerView
            });
        }

        binding.sendMessage.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", getResources().getConfiguration().locale);
            String currentDateAndTime = sdf.format(new Date());
            messages.add(new ChatMessage(binding.messageInput.getText().toString(), currentDateAndTime, true));

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
                    cmDAO.insertMessage(new ChatMessage(binding.messageInput.getText().toString(), currentDateAndTime, true)));

            myAdapter.notifyItemInserted(messages.size() - 1);

            binding.messageInput.setText("");
        });

        binding.receiveMessage.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", getResources().getConfiguration().locale);
            String currentDateAndTime = sdf.format(new Date());
            messages.add(new ChatMessage(binding.messageInput.getText().toString(), currentDateAndTime, false));

            Executor thread = Executors.newSingleThreadExecutor();

            thread.execute(() ->
                    cmDAO.insertMessage(new ChatMessage(binding.messageInput.getText().toString(), currentDateAndTime, false)));


            myAdapter.notifyItemInserted(messages.size() - 1);

            binding.messageInput.setText("");
        });

        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());

                    return new MyRowHolder(binding.getRoot());
                } else {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());

                    return new MyRowHolder(binding.getRoot());
                }

            }



            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.message.setText("");
                holder.time.setText("");
                ChatMessage obj = messages.get(position);
                holder.message.setText(obj.getMessage());
                holder.time.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            public int getItemViewType(int position) {
                if (messages.get(position).isSentButton()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        chatModel.selectedMessage.observe(this, (newMessageValue) -> {
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_location, chatFragment).addToBackStack("").commit();
        });
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView time;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();

                ChatMessage selected = messages.get(position);

                chatModel.selectedMessage.postValue(selected);

//                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
//
//                builder.setMessage("Do you want to delete this message: \"" + message.getText() + "\"?")
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
            });

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
        }
    }
}