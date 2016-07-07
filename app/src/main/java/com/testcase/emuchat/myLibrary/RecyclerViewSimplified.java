package com.testcase.emuchat.myLibrary;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AlexShredder on 30.06.2016.
 */
public abstract class RecyclerViewSimplified extends RecyclerView {
    private static final int INSERTED = 1;
    private static final int REMOVED = 2;
    RVAdapter adapter;


    public RecyclerViewSimplified(Context context) {
        super(context);
    }

    public RecyclerViewSimplified(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewSimplified(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(int listItemID, List<?> itemsList){
        adapter = new RVAdapter(itemsList,listItemID);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        setLayoutManager(llm);
        if (itemsList instanceof RVList) ((RVList) itemsList).setListViewSimplified(this);
        this.setAdapter(adapter);
    }

    protected abstract void processView(ViewHolder viewHolder, List<?> itemsList, int position);

    protected abstract Map<String,Integer> fillListItemWidgetsNameAndID();

    private class RVAdapter extends RecyclerView.Adapter<ViewHolder>{

        private int listItemID;
        private List<?> items;

        public RVAdapter(List<?> items, int listItemID) {
            this.items = items;
            this.listItemID = listItemID;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(listItemID, parent, false);
            ViewHolder vh = new ViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (holder.views==null) initHolder(holder);
            processView(holder, items, position);
        }

        private void initHolder(ViewHolder holder) {
            holder.views = new HashMap<>();
            Map<String, Integer> widgets = fillListItemWidgetsNameAndID();
            for (Map.Entry<String,Integer> entry: widgets.entrySet())
                holder.views.put(entry.getKey(),holder.itemView.findViewById(entry.getValue()));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    public void notifyDataSetChanged(int changeType, int position){
        if (adapter==null) return;
        switch(changeType){
            case INSERTED: adapter.notifyItemInserted(position-1); break;
            case REMOVED: adapter.notifyItemRemoved(position-1); break;
            default: adapter.notifyDataSetChanged();
        };
    }

    public void notifyDataSetChanged(){
        if (adapter==null) return;
        adapter.notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        Map<String,View> views;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public View getView(String name) {
            return views.get(name);
        }

    }

    public static class RVList<T> extends ArrayList<T> {

        private RecyclerViewSimplified listViewSimplified;

        public void setListViewSimplified(RecyclerViewSimplified listViewSimplified) {
            this.listViewSimplified = listViewSimplified;
        }

        private void updateAdapter(int changeType, int position){
            if (listViewSimplified!=null) listViewSimplified.notifyDataSetChanged(changeType,position);
        }
        private void updateAdapter(){
            if (listViewSimplified!=null) listViewSimplified.notifyDataSetChanged();
        }

        @Override
        public boolean add(T object) {
            boolean result = super.add(object);
            updateAdapter(INSERTED,size());

            return result;
        }

        @Override
        public void add(int index, T object) {
            super.add(index, object);
            updateAdapter(INSERTED,index);
        }

        @Override
        public boolean addAll(Collection<? extends T> collection) {
            boolean result = super.addAll(collection);
            updateAdapter();
            return result;
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> collection) {
            boolean result = super.addAll(index, collection);
            updateAdapter();
            return result;
        }

        @Override
        public void clear() {
            super.clear();
            updateAdapter();
        }

        @Override
        public T remove(int index) {
            T result = super.remove(index);
            updateAdapter(REMOVED,index);
            return result;
        }

        @Override
        public boolean remove(Object object) {
            int index = indexOf(object);
            boolean result = super.remove(object);
            updateAdapter(REMOVED,index);
            return result;
        }

        @Override
        protected void removeRange(int fromIndex, int toIndex) {
            super.removeRange(fromIndex, toIndex);
            updateAdapter();
        }

        @Override
        public T set(int index, T object) {
            T result = super.set(index, object);
            updateAdapter();
            return result;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            boolean result = super.removeAll(collection);
            updateAdapter();
            return result;
        }
    }
}
