package com.buildproject.rebuy.Configs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buildproject.rebuy.ItemsActivity;
import com.buildproject.rebuy.Modules.ListOfItems;
import com.buildproject.rebuy.R;
import com.buildproject.rebuy.Services.FirebaseDBadapterUsers;

import java.util.List;

import static com.buildproject.rebuy.R.*;

public class RecyclerView_Config {
    private Context mContext;
    private ListsAdapter mListAdapter;
    private String displayName;

    public void setConfig(RecyclerView recyclerView, Context context, List<ListOfItems> lists, List<String> keys, String displayName){
        mContext = context;
        mListAdapter = new ListsAdapter(lists, keys);
        this.displayName = displayName;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mListAdapter);
    }

    class ListItemView extends RecyclerView.ViewHolder{
        private TextView mListTitleName;
        private TextView mListOwnerName;
        private ImageView mListPriority;
        private ConstraintLayout mConstraintLayout;

        private String key; //list id record

        public ListItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext) //parent constructor
                    .inflate(R.layout.listofitems_minified_for_recycleviews, parent,false));
            mListTitleName = (TextView) itemView.findViewById(id.list_titleName);
            mListOwnerName = (TextView) itemView.findViewById(id.list_ownerName);
            mListPriority = (ImageView) itemView.findViewById(id.recycleview_lists_priority);
            mConstraintLayout = (ConstraintLayout) itemView.findViewById(id.list_layout);

        }

        private void setPriority(String priority) {
            if (priority.equals("Low")) {
                mListPriority.setImageResource(drawable.green_priority_btn);
            } else if (priority.equals("Normal")) {
                mListPriority.setImageResource(drawable.yellow_priority_btn);
            } else if (priority.equals("High")) {
                mListPriority.setImageResource(drawable.red_priority_btn);
            }
        }

        private void setBackground(ListOfItems list) {
            if (list.isAllBought()) {
                mConstraintLayout.setBackgroundColor(Color.rgb(197,230,171));
            }
            else {
                mConstraintLayout.setBackgroundColor(0);
            }
        }

        public void bind(ListOfItems list, String key){
            mListTitleName.setText(list.getTitleName());
            new FirebaseDBadapterUsers().setNameByUesrID(list.getOwner(), mListOwnerName);
            setPriority(list.getPriority());
            setBackground(list);
            this.key = key;
        }

        public void bind(ListOfItems list, String key, View.OnClickListener listener){
            mListTitleName.setText(list.getTitleName());
            new FirebaseDBadapterUsers().setNameByUesrID(list.getOwner(), mListOwnerName);
            setPriority(list.getPriority());
            setBackground(list);
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
                    Intent newIntent = new Intent(view.getContext(), ItemsActivity.class);
                    newIntent.putExtra("list_id",mKeys.get(position));
                    newIntent.putExtra("list_title",mList.get(position).getTitleName());
                    newIntent.putExtra("display_name",displayName);
                    newIntent.putExtra("permission", mList.get(position).getAccountPermission());
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
