package com.testcase.emuchat.dataProviders.dummyProvider;

import android.content.Context;

import com.testcase.emuchat.dataProviders.AbstractProvider;
import com.testcase.emuchat.model.ChatItem;

import java.util.concurrent.TimeUnit;

/**
 * Created by AlexShredder on 06.07.2016.
 */
public class DummyProvider extends AbstractProvider {

    private Thread mThread;
    private static final String[] DUMMIES_MESS = {"Hi there!","How are you?","Look this - #cooltag","Bebebe","Who are you?","It's sunny today","Popka durak :P","I am the best!","Moon light is cool..","Goodbye!","I See you"};
    private static final String[] BOT_NAMES = {"Vasya","Petya","Anka","Egor"};

    public DummyProvider(Context context) {
        super(context);
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long random = (long) (1000 + Math.random() * 10000);
                    try {
                        TimeUnit.MILLISECONDS.sleep(random);
                        int numMess = (int) (1 + Math.random() * DUMMIES_MESS.length);
                        int numBot = (int) (1 + Math.random() * BOT_NAMES.length);

                        ChatItem chatItem = new ChatItem(BOT_NAMES[numBot-1], DUMMIES_MESS[numMess - 1], false);
                        sendMessage(chatItem);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        mThread.start();
    }

    @Override
    public void dismiss() {
        mThread.interrupt();
    }
}
