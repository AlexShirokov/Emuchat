package com.testcase.emuchat.views.widgets;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.testcase.emuchat.R;
import com.testcase.emuchat.model.ChatItem;
import com.testcase.emuchat.myLibrary.RecyclerViewSimplified;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AlexShredder on 06.07.2016.
 */
public class MyRecyclerView extends RecyclerViewSimplified {

    final String LINE1 = "Line1";
    final String LINE2 = "Line2";
    final String TIME = "time";
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void processView(ViewHolder viewHolder, final List<?> itemsList, int position) {
        final ChatItem listItem = (ChatItem) itemsList.get(position);
        TextView textView1 = (TextView) viewHolder.getView(LINE1);
        TextView textView2 = (TextView) viewHolder.getView(LINE2);
        TextView textView3 = (TextView) viewHolder.getView(TIME);
        textView1.setText(listItem.getName());
        Spanned message = decodeMessage(listItem.getMessage());
        textView2.setText(message);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(listItem.getTimeStamp());
        textView3.setText(dateFormat.format(calendar.getTime()));
        if (listItem.isMine()) textView1.setTextColor(getResources().getColor(R.color.colorAccent)); else textView1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private Spanned decodeMessage(String message) {
        String color = String.format("#%06X", (0xFFFFFF & (getContext().getResources().getColor(R.color.colorAccent))));
        String html = "<font color=\"" + color + "\">%s</font>";
        String result = message.trim();

        Pattern pattern = Pattern.compile("\\#[^ \\.\\,\\=]+");
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            String res1 = matcher.group();
            String res2 = res1.replace("#","&#35;");
            String temp = String.format(html,res2);
            result = result.replaceAll(res1,temp);
        }

        result = result.replaceAll("\\n","<br>");
        return Html.fromHtml(result);
    }

    @Override
    protected Map<String, Integer> fillListItemWidgetsNameAndID() {
        Map<String, Integer> result = new HashMap<>();
        result.put(LINE1, R.id.listItemText1);
        result.put(LINE2, R.id.listItemText2);
        result.put(TIME, R.id.listItemText3);
        return result;
    }

}
