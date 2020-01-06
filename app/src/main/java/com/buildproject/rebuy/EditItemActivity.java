package com.buildproject.rebuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.buildproject.rebuy.Modules.ItemInList;
import com.buildproject.rebuy.Modules.ItemInList.Priority;
import com.buildproject.rebuy.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EditItemActivity extends AppCompatActivity {

    //item data
    ItemInList current_item;
    String list_id;
    //TODO initilize current_user
    String userId;

    //components
    EditText item_name;

    EditText quantity;
    ImageButton down;
    ImageButton up;

    ImageButton green_priority;
    ImageButton yellow_priority;
    ImageButton red_priority;
    ItemInList.Priority priority; //temp data about chosen priority

    CheckBox is_bought;

    TextView added_by;
    TextView added_at;
    EditText notes;

    pl.droidsonroids.gif.GifImageView barcode_button;

    Button apply_button;

    //TODO put this static variable in another class
    static DateFormat date_time_format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        //init components
        item_name = findViewById(R.id.edit_item_name);
        quantity = findViewById(R.id.quantity);
        down = findViewById(R.id.imageButton_down);
        up = findViewById(R.id.imageButton_up);

        green_priority = findViewById(R.id.imageButton_green);
        yellow_priority = findViewById(R.id.imageButton_yellow);
        red_priority = findViewById(R.id.imageButton_red);

        is_bought = findViewById(R.id.isbought);
        added_by = findViewById(R.id.add_by);
        added_at = findViewById(R.id.changed_at);
        notes = findViewById(R.id.edit_notes);
        barcode_button = findViewById(R.id.barcode);
        apply_button = findViewById(R.id.edit_item_apply);

        //get from previous intent
        Bundle bundle = getIntent().getExtras();
//        list_id = Objects.requireNonNull(getIntent().getExtras()).getString("list_id");
        list_id = bundle.getString("list_id");
        //current_item = (ItemInList)bundle.get("item_info");

        //if is new item
        if (current_item == null) {
            current_item = new ItemInList();
            current_item.setUserId(userId);
        }

        //get data about exists item
        else {
            item_name.setText(current_item.getItemName());
            added_at.setText(date_time_format.format(new Date()));
            if (!current_item.getNotes().isEmpty())
                notes.setText(current_item.getNotes());
        }

        quantity.setText(Integer.toString(current_item.getQuantity()));
        setPriority(current_item.getPriority());
        is_bought.setChecked(current_item.isBought());

        String user_added_by = current_item.getUserId();
//        added_by.setText(String.format("%s %s", user_added_by.getFirstName(), user_added_by.getLastName()));
//        added_by.setText("ADDED BY..");

        //If current user is viewer

/*        current_list = (ListOfItems)bundle.get("list_info");
        if (current_list.getViewers().contains(current_user.getUserId())) {
            item_name.setEnabled(false);
            quantity.setEnabled(false);
            down.setEnabled(false);
            up.setEnabled(false);
            green_priority.setEnabled(false);
            yellow_priority.setEnabled(false);
            red_priority.setEnabled(false);
            is_bought.setEnabled(false);
            notes.setEnabled(false);
            apply_button.setEnabled(false);
            //enable to press barcode_button!
        }*/
    }

    public void clickDown(View view) {
        int i = Integer.parseInt(quantity.getText().toString());
        if (i>0) //minimum quantity is 0
            quantity.setText(Integer.toString(i-1));
    }

    public void clickUp(View view) {
        int i = Integer.parseInt(quantity.getText().toString());
        quantity.setText(Integer.toString(++i));
    }

    public void clickGreen(View view) {
        setPriority(Priority.LOW);
    }

    public void clickYellow(View view) {
        setPriority(Priority.MID);
    }

    public void clickRed(View view) {
        setPriority(Priority.HIGH);
    }

    private void setPriority(Priority prio) {
        priority = prio;

        int not_chosen = 0xffffffff;
        int chosen = 0x88888888;
        green_priority.setBackgroundColor(not_chosen);
        yellow_priority.setBackgroundColor(not_chosen);
        red_priority.setBackgroundColor(not_chosen);

        switch (priority) {
            case LOW:
                green_priority.setBackgroundColor(chosen);
                break;
            case MID:
                yellow_priority.setBackgroundColor(chosen);
                break;
            case HIGH:
                red_priority.setBackgroundColor(chosen);
                break;
        }
    }

    public void onBarcodeClick(View view) {
        //TODO in future: open scanner activity and delete this stupid toast
        CharSequence text = "Thank you!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

    public void saveItem (View view) {
//        if (current_item.getAddedTime() == null)
//            current_item.setAddedTime(new Date());
        current_item.setBought(is_bought.isChecked());
        current_item.setItemName(item_name.getText().toString());
        current_item.setNotes(notes.getText().toString());
        current_item.setPriority(priority);
        current_item.setQuantity(Integer.parseInt(quantity.getText().toString()));
        //TODO push current_item()
        new FirebaseDBadapterItems(list_id).addItem(current_item, new FirebaseDBadapterItems.DataStatus() {
            @Override
            public void DataIsLoaded(List<ItemInList> lists, List<String> keys) {

            }

            @Override
            public void DataIsInserted() {
                Toast.makeText(EditItemActivity.this, "Item has been added successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

//        //go to list activity
//        Intent toList = new Intent(getBaseContext(), .class);
//        startActivity(toList);
        super.onBackPressed();
    }
}