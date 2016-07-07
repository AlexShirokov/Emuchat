package com.testcase.emuchat.views.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.testcase.emuchat.CommandCenter;
import com.testcase.emuchat.R;
import com.testcase.emuchat.model.ChatItem;
import com.testcase.emuchat.views.widgets.MyRecyclerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment implements CommandCenter.OnCommandsListener {

    private static final String TAG = "RecyclerView fragment 1";
    private MyRecyclerView.RVList<ChatItem> chatItemList;
    private CommandCenter commandCenter;

    //widgets
    private MyRecyclerView listView;
    private EditText text4send;
    private ImageButton buttonSend;
    private ProgressBar progressBar;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        commandCenter = CommandCenter.getInstance(getContext());
        chatItemList = new MyRecyclerView.RVList<>();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView){

        Log.d(TAG, "initViews");
        listView = (MyRecyclerView) rootView.findViewById(R.id.RecyclerView);
        listView.init(R.layout.item_chat, chatItemList);

        text4send = (EditText) rootView.findViewById(R.id.editText_sendMessage);
        buttonSend = (ImageButton) rootView.findViewById(R.id.imageButton_sendMessage);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text4send.getText().toString().isEmpty()) return;
                showProgressBar();
                commandCenter.sendMessage(text4send.getText().toString(), new CommandCenter.CallBack() {
                    @Override
                    public void onEvent(boolean result, Object data) {
                        if (result){
                            text4send.setText("");
                            hideProgressBar();
                        }
                    }
                });
            }
        });
    }

    private void showProgressBar(){
        buttonSend.setEnabled(false);
        text4send.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        buttonSend.setEnabled(true);
        text4send.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        commandCenter.setOnNewCommandsListener(this);
        getPendingMessages(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        commandCenter.removeOnNewCommandsListener(this);
    }

    private void getPendingMessages(final boolean showProgress) {
        Log.d(TAG, "getPendingMessages");
        long lastTime= chatItemList.size()>0?chatItemList.get(chatItemList.size()-1).getTimeStamp():0;
        if (showProgress) showProgressBar();
        commandCenter.getMessages(lastTime, new CommandCenter.CallBack() {
            @Override
            public void onEvent(boolean result, Object data) {
                if (result){
                    Log.d(TAG, "onEvent");
                    boolean needScroll = !listView.canScrollVertically(1);
                    for (int i = 0; i < ((List)data).size(); i++) {
                        ChatItem o = (ChatItem) ((List)data).get(i);
                        if (!chatItemList.contains(o)) chatItemList.add(o);
                    }

                    if (needScroll) listView.scrollToPosition(chatItemList.size()-1);
                }
                if (showProgress) hideProgressBar();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        commandCenter.dismiss();
    }

    @Override
    public void onNewCommand(int command) {
        Log.d(TAG, "onNewCommand: "+command);
        switch (command){
            case CommandCenter.NEW_MESSAGE: getPendingMessages(false); break;
            case CommandCenter.RELOAD_LIST: chatItemList.clear(); getPendingMessages(false); break;
        }
    }
}
