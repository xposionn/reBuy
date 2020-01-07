package com.buildproject.rebuy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buildproject.rebuy.Modules.ListOfItems;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private ListsAdapter mListAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<ListOfItems> lists, List<String> keys){
        mContext = context;
        mListAdapter = new ListsAdapter(lists, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mListAdapter);
    }

    class ListItemView extends RecyclerView.ViewHolder{
        private TextView mListTitleName;
        private TextView mListOwnerName;
        private TextView mListPriority;

        private String key; //list id record

        public ListItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext) //parent constructor
                    .inflate(R.layout.listofitems_minified_for_recycleviews, parent,false));
            mListTitleName = (TextView) itemView.findViewById(R.id.list_titleName);
            mListOwnerName = (TextView) itemView.findViewById(R.id.list_ownerName);
            mListPriority = (TextView) itemView.findViewById(R.id.list_priorityText);

        }
        public void bind(ListOfItems list, String key){
            mListTitleName.setText(list.getTitleName());
            mListOwnerName.setText(list.getOwner());
            mListPriority.setText(list.getPriority());
            this.key = key;


        }

        public void bind(ListOfItems list, String key, View.OnClickListener listener){
            mListTitleName.setText(list.getTitleName());
            mListOwnerName.setText(list.getOwner());
            mListPriority.setText(list.getPriority());
            itemView.setOnClickListener(listener);
            this.key = key;


        }
    }

    class ListsAdapter extends RecyclerView.Adapter<ListItemView>{
        private List<ListOfItems> mList;
        private List<String> mKeys;

        public ListsAdapter(List<ListOfItems> mList, List<String> mKeys) {
            this.mList = mList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public ListItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ListItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ListItemView holder, final int position) {
            holder.bind(mList.get(position), mKeys.get(position), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newIntent = new Intent(view.getContext(),ItemsActivity.class);
                    newIntent.putExtra("list_id",mKeys.get(position));
                    newIntent.putExtra("list_title",mList.get(position).getTitleName());
                    newIntent.putExtra("permission", mList.get(position).getAccountPermission().toString());
                    view.getContext().startActivity(newIntent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
