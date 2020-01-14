package com.buildproject.rebuy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;

import java.util.List;

import static android.widget.Toast.makeText;

public class ItemsRecyclerView_Config {
    private Context mContext;
    private ItemsAdapter mItemAdapter;
    private String list_id;
    private ListOfItems.Permission permission;


    public void setConfig(RecyclerView recyclerView, Context context, List<ItemInList> items, List<String> keys, String list_id){
        mContext = context;
        mItemAdapter = new ItemsAdapter(items, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mItemAdapter);
        this.list_id = list_id;
//        mItemAdapter.setList_id(list_id);
    }


    public void setConfig(RecyclerView recyclerView, Context context, List<ItemInList> items, List<String> keys, String list_id, ListOfItems.Permission permission){
        mContext = context;
        mItemAdapter = new ItemsAdapter(items, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mItemAdapter);
        this.list_id = list_id;
//        mItemAdapter.setList_id(list_id);
        this.permission = permission;
    }

    class ItemItemView extends RecyclerView.ViewHolder{
        private TextView mItemTitleName;
        private TextView mItemQuantity;
        private Button mDeleteItem;
        private TextView mItemNote;
        private ImageView mItemPriority;
        private CheckBox mItemBought;

        private String key; //item id record

        public ItemItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext) //parent constructor
                    .inflate(R.layout.layout_listitem, parent,false));
            mItemTitleName = (TextView) itemView.findViewById(R.id.InList_item_name);
            mItemQuantity = (TextView) itemView.findViewById(R.id.InList_quantity);
            mDeleteItem = (Button) itemView.findViewById(R.id.InList_delete);
            mItemNote = (TextView) itemView.findViewById(R.id.InList_note);
            mItemBought = (CheckBox) itemView.findViewById(R.id.InList_isbought);
            //TODO update database on click
            mItemPriority = (ImageView) itemView.findViewById(R.id.InList_priority);

        }

        private void setElements(final ItemInList item) {
            mItemTitleName.setText(item.getItemName());
            mItemQuantity.setText(Integer.toString(item.getQuantity()));
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

            mItemBought.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FirebaseDBadapterItems(list_id).updateItem(key, "bought", mItemBought.isChecked(),
                            new FirebaseDBadapterItems.DataStatus() {
                                @Override
                                public void DataIsLoaded(List<ItemInList> lists, List<String> keys) {

                                }

                                @Override
                                public void DataIsInserted() {

                                }

                                @Override
                                public void DataIsUpdated() {
                                    //TODO write log
                                }

                                @Override
                                public void DataIsDeleted() {

                                }
                            });
                }
            });
            
            
            mDeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (permission== ListOfItems.Permission.VIEWER || permission== ListOfItems.Permission.NOTHING) {
                        //TODO make toast
//                        makeText(this, mContext.getString(R.string.cannot_delete_item), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        new FirebaseDBadapterItems(list_id).deleteItem(key,
                                new FirebaseDBadapterItems.DataStatus() {
                                    @Override
                                    public void DataIsLoaded(List< ItemInList > lists, List< String > keys) {

                                    }

                                    @Override
                                    public void DataIsInserted() {

                                    }

                                    @Override
                                    public void DataIsUpdated() {

                                    }

                                    @Override
                                    public void DataIsDeleted() {
                                        //TODO write log
                                    }
                                });
                    }
                }
            });
        }


        public void bind(final ItemInList item, final String key, View.OnClickListener listener){
            setElements(item);
            itemView.setOnClickListener(listener);
            this.key = key;
        }

        public void bind(final ItemInList item, final String key){
            setElements(item);
            this.key = key;
        }
    }

    class ItemsAdapter extends RecyclerView.Adapter<ItemItemView>{
        private List<ItemInList> mItem;
        private List<String> mKeys;
//        private String list_id;
//
//        public void setList_id(String list_id) {
//            this.list_id = list_id;
//        }

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
        public void onBindViewHolder(@NonNull ItemItemView holder, final int position) {
            holder.bind(mItem.get(position), mKeys.get(position), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newIntent = new Intent(view.getContext(), EditItemActivity.class);
                   // newIntent.putExtra("item_info",mItem.get(position));
                    newIntent.putExtra("permission",permission);
                    newIntent.putExtra("item",mItem.get(position));
                    newIntent.putExtra("list_id",list_id);
                    newIntent.putExtra("itemKey",mKeys.get(position));

                    view.getContext().startActivity(newIntent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mItem.size();
        }
    }
}
