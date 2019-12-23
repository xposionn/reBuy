package com.buildproject.rebuy;

import android.content.Context;
import android.media.Image;
import android.renderscript.RenderScript;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;

import java.util.List;

public class ItemsRecyclerView_Config {
    private Context mContext;
    private ItemsAdapter mItemAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<ItemInList> items, List<String> keys){
        mContext = context;
        mItemAdapter = new ItemsAdapter(items, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mItemAdapter);
    }

    class ItemItemView extends RecyclerView.ViewHolder{
        private TextView mItemTitleName;
        private TextView mItemQuantity;
        private TextView mItemNote;
        private ImageView mItemPriority;
        private CheckBox mItemBought;

        private String key; //item id record

        public ItemItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext) //parent constructor
                    .inflate(R.layout.layout_listitem, parent,false));
            mItemTitleName = (TextView) itemView.findViewById(R.id.InList_item_name);
            mItemQuantity = (TextView) itemView.findViewById(R.id.InList_quantity);
            mItemNote = (TextView) itemView.findViewById(R.id.InList_note);
            mItemBought = (CheckBox) itemView.findViewById(R.id.InList_isbought);
            //TODO update database on click
            mItemPriority = (ImageView) itemView.findViewById(R.id.InList_priority);

        }
        public void bind(ItemInList item, String key){
            mItemTitleName.setText(item.getItemName());
            mItemQuantity.setText(item.getQuantity());
            mItemNote.setText(item.getNotes());
            mItemBought.setChecked(item.isBought());
            switch (item.getPriority()) {

                case LOW:
                    mItemPriority.setImageResource(R.drawable.green_priority_btn);
                    break;
                case MID:
                    mItemPriority.setImageResource(R.drawable.yellow_priority_btn);
                    break;
                case HIGH:
                    mItemPriority.setImageResource(R.drawable.red_priority_btn);
                    break;
            }
            this.key = key;
        }
    }

    class ItemsAdapter extends RecyclerView.Adapter<ItemItemView>{
        private List<ItemInList> mItem;
        private List<String> mKeys;

        public ItemsAdapter(List<ItemInList> mItem, List<String> mKeys) {
            this.mItem = mItem;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public ItemItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemItemView holder, int position) {
            holder.bind(mItem.get(position), mKeys.get(position));

        }

        @Override
        public int getItemCount() {
            return mItem.size();
        }
    }
}
