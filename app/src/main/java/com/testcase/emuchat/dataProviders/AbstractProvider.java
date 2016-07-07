package com.testcase.emuchat.dataProviders;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.testcase.emuchat.dataProviders.Sql.ChatBase;
import com.testcase.emuchat.model.ChatItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by AlexShredder on 06.07.2016.
 */
public abstract class AbstractProvider{

    private static ChatBase mChatBase;
    private List<OnEventListener> onEventListeners;
    public static final int NEW_MESSAGE = 0;
    public static final int BASE_CLEARED = 1;

    public AbstractProvider(Context context) {
        if (mChatBase==null) mChatBase = new ChatBase(context);
    }

    public boolean sendMessage(ChatItem chatItem){
        boolean result = mChatBase.insert(chatItem);
        //network access simulation
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result) notifyOnEventListeners(NEW_MESSAGE);
        return result;
    };

    public void setOnEventListener(OnEventListener listener) {
        if (onEventListeners==null) onEventListeners=new ArrayList<>();
        if (!onEventListeners.contains(listener)) onEventListeners.add(listener);
    }

    public void removeOnEventListener(OnEventListener listener) {
        if (onEventListeners==null) onEventListeners=new ArrayList<>();
        if (onEventListeners.contains(listener)) onEventListeners.remove(listener);
    }

    private void notifyOnEventListeners(final int eventType){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (onEventListeners==null) onEventListeners=new ArrayList<>();
                for (int i = 0; i < onEventListeners.size(); i++) {
                    OnEventListener o = onEventListeners.get(i);
                    o.onEvent(eventType);
                }
            }
        });
    }

    public boolean getMessages(long fromTime, List<ChatItem> toList) {
        if (toList==null) toList = new ArrayList<>();
        try {
            toList.addAll(mChatBase.getMessagesFromTime(fromTime));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean clearBase() {
        boolean result = mChatBase.dropBase();
        if (result) notifyOnEventListeners(BASE_CLEARED);
        return result;
    }

    public interface OnEventListener{
        void onEvent(int eventType);
    }

    public void dismiss(){
        mChatBase.release();
        onEventListeners=null;
    }
}
