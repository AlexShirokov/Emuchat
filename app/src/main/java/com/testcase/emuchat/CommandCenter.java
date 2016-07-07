package com.testcase.emuchat;

import android.content.Context;
import android.os.Bundle;

import com.testcase.emuchat.dataProviders.AbstractProvider;
import com.testcase.emuchat.dataProviders.dummyProvider.DummyProvider;
import com.testcase.emuchat.model.ChatItem;
import com.testcase.emuchat.myLibrary.ExecutorWithFeedback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlexShredder on 06.07.2016.
 */
public class CommandCenter implements AbstractProvider.OnEventListener{
    private static final String MY_NAME = "Me";
    private static final String RESULT = "result";
    private static final String MESSAGES_LIST = "messages";
     private static CommandCenter ourInstance;
    private static AbstractProvider mDataProvider;
    private static List<OnCommandsListener> onCommandsListeners;

    //commands
    public static final int NEW_MESSAGE = 1;
    public static final int RELOAD_LIST = 2;


    public static CommandCenter getInstance(Context context) {
        if (ourInstance==null) ourInstance = new CommandCenter(context);
        return ourInstance;
    }

    private CommandCenter() {
    }

    private CommandCenter(Context context) {
        if (mDataProvider==null) mDataProvider = new DummyProvider(context);
        if (onCommandsListeners ==null) onCommandsListeners = new ArrayList<>();
        mDataProvider.setOnEventListener(this);
    }

    public void sendMessage(String text, final CallBack callBack) {
        if (text.isEmpty()) return;
        final ChatItem chatItem = new ChatItem(MY_NAME, text, true);
        ExecutorWithFeedback.MyJob job = new ExecutorWithFeedback.MyJob() {
            @Override
            protected Bundle doJob() {
                Bundle bundle = new Bundle();
                boolean resultOk = mDataProvider.sendMessage(chatItem);
                bundle.putBoolean(RESULT,resultOk);
                return bundle;
            }
        };
        if (callBack!=null)
            job.setOnCompleteListener(new ExecutorWithFeedback.OnCompleteListener() {
                @Override
                public void onComplete(Bundle result) {
                    if (result!=null && result.containsKey(RESULT) && result.getBoolean(RESULT)) callBack.onEvent(true,null); else callBack.onEvent(false,null);
                }
            });
        job.execute();
    }

    public void clearBase(final CallBack callBack) {
        ExecutorWithFeedback.MyJob job = new ExecutorWithFeedback.MyJob() {
            @Override
            protected Bundle doJob() {
                Bundle bundle = new Bundle();
                boolean resultOk = mDataProvider.clearBase();
                bundle.putBoolean(RESULT,resultOk);
                return bundle;
            }
        };
        if (callBack!=null)
            job.setOnCompleteListener(new ExecutorWithFeedback.OnCompleteListener() {
                @Override
                public void onComplete(Bundle result) {
                    if (result!=null && result.containsKey(RESULT) && result.getBoolean(RESULT)) callBack.onEvent(true,null); else callBack.onEvent(false,null);
                }
            });
        job.execute();
    }
    public void getMessages(final long fromTime, final CallBack callBack) {

        ExecutorWithFeedback.MyJob job = new ExecutorWithFeedback.MyJob() {
            @Override
            protected Bundle doJob() {
                Bundle bundle = new Bundle();
                List<ChatItem> list = new ArrayList<>();
                boolean resultOk = mDataProvider.getMessages(fromTime,list);
                bundle.putBoolean(RESULT,resultOk);
                bundle.putSerializable(MESSAGES_LIST, (Serializable) list);
                return bundle;
            }
        };
        if (callBack!=null)
            job.setOnCompleteListener(new ExecutorWithFeedback.OnCompleteListener() {
                @Override
                public void onComplete(Bundle result) {
                    if (result!=null && result.containsKey(RESULT) && result.getBoolean(RESULT)) callBack.onEvent(true,result.get(MESSAGES_LIST)); else callBack.onEvent(false,null);
                }
            });
        job.execute();
    }

    private void notifyNewCommandsListeners(int command){
        for (int i = 0; i < onCommandsListeners.size(); i++) {
            OnCommandsListener o = onCommandsListeners.get(i);
            o.onNewCommand(command);
        }
    }

    public void setOnNewCommandsListener(OnCommandsListener listener){
        if (!onCommandsListeners.contains(listener)) onCommandsListeners.add(listener);
    }

    public void removeOnNewCommandsListener(OnCommandsListener listener){
        if (onCommandsListeners.contains(listener)) onCommandsListeners.remove(listener);
    }

    public void dismiss(){
        ExecutorWithFeedback.stop();
        ourInstance = null;
    }

    @Override
    public void onEvent(int eventType) {
        switch (eventType){
            case AbstractProvider.NEW_MESSAGE: notifyNewCommandsListeners(NEW_MESSAGE); break;
            case AbstractProvider.BASE_CLEARED: notifyNewCommandsListeners(RELOAD_LIST); break;
        }
    }

    public interface CallBack{
        void onEvent(boolean result, Object data);
    }

    public interface OnCommandsListener {
        void onNewCommand(int command);
    }


}
