package com.buildproject.rebuy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ListOfItems;
import com.buildproject.rebuy.Modules.User;

import java.util.List;

public class EditRecyclerView_Config {
    private Context mContext;
    private UsersAdapter mItemAdapter;
    private String list_id;
    private ListOfItems.Permission permission;


    public void setConfig(RecyclerView recyclerView, Context context, List<User> users, List<String> keys, String list_id){
        mContext = context;
        mItemAdapter = new UsersAdapter(users, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mItemAdapter);
        this.list_id = list_id;
//        mItemAdapter.setList_id(list_id);
    }


    class EditorView extends RecyclerView.ViewHolder{
        private TextView mUserName;
        private Button mDeleteItem;

        private String key; //item id record

        public EditorView(ViewGroup parent){
            super(LayoutInflater.from(mContext) //parent constructor
                    .inflate(R.layout.partner, parent,false));
            mUserName = (TextView) itemView.findViewById(R.id.InList_item_name);
            mDeleteItem = (Button) itemView.findViewById(R.id.InList_delete);

        }

        private void setElements(final User user) {
            mUserName.setText("adasdas");

            mDeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (permission== ListOfItems.Permission.VIEWER || permission== ListOfItems.Permission.NOTHING) {
                        //TODO make toast
//                        makeText(this, mContext.getString(R.string.cannot_delete_item), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        new FirebaseDBadapterUsers(list_id).deleteItem(key,
                                new FirebaseDBadapterUsers.DataStatus() {
                                    @Override
                                    public void DataIsLoaded(List<User> lists, List< String > keys) {

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


        public void bind(final User user, final String key, View.OnClickListener listener){
            setElements(user);
            itemView.setOnClickListener(listener);
            this.key = key;
        }

        public void bind(final User user, final String key){
            setElements(user);
            this.key = key;
        }
    }

    class UsersAdapter extends RecyclerView.Adapter<EditorView>{
        private List<User> mItem;
        private List<String> mKeys;
//        private String list_id;
//
//        public void setList_id(String list_id) {
//            this.list_id = list_id;
//        }

        public UsersAdapter(List<User> mItem, List<String> mKeys) {
            this.mItem = mItem;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public EditorView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EditorView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EditorView holder, final int position) {
            holder.bind(mItem.get(position), mKeys.get(position), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent newIntent = new Intent(view.getContext(), EditItemActivity.class);
                   // newIntent.putExtra("item_info",mItem.get(position));
                    newIntent.putExtra("permission",permission);
                    newIntent.putExtra("item",mItem.get(position));
                    newIntent.putExtra("list_id",list_id);
                    newIntent.putExtra("itemKey",mKeys.get(position));

                    view.getContext().startActivity(newIntent);*/
                    Toast.makeText(mContext, "pressed on user.", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return mItem.size();
        }
    }
}
