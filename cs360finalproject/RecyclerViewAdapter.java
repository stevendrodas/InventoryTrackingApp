package com.example.cs360finalproject;
// Developer: Steven Rodas (contact@stevenrodas.com)
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList nameArray, quantityArray;
    private Context context;
    InventoryDB db;
    String user;
    private AdapterCallback adapterCallback;

    // constructor for RecyclerViewAdapter
    public RecyclerViewAdapter(Context context, ArrayList nameArray, ArrayList quantityArray, String user) {
        this.context = context;
        this.nameArray = nameArray;
        this.quantityArray = quantityArray;
        this.db = new InventoryDB(context);
        this.user = user;

        // ensures the context implements the AdapterCallback interface
        try {
            adapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback");
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflates the item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // sets item information to the UI components
        holder.textViewName.setText(String.valueOf(nameArray.get(position)));
        holder.textViewQuantity.setText(String.valueOf(quantityArray.get(position)));
        holder.position = position;
    }

    // AdapterCallback interface for sending SMS
    public static interface AdapterCallback {
        void sendSMSCallback(String itemName);
    }

    @Override
    public int getItemCount() {
        return nameArray.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewQuantity;
        int position;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewItemName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);

            // sets click listener for the add button to increment item quantity
            itemView.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v){
                   // increments item quantity
                   String newQuantity = db.incrementQuantity(nameArray.get(getAdapterPosition()).toString());
                   textViewQuantity.setText(newQuantity);
               }
            });

            // sets click listener for the subtract button to decrement item quantity
            itemView.findViewById(R.id.buttonSubtract).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // checks if item quantity is low
                    if(db.checkItemQuantityLow(nameArray.get(getAdapterPosition()).toString())) {
                        return;
                    }

                    // decrements item quantity and sends SMS if low
                    String newQuantity = db.decrementQuantity(nameArray.get(getAdapterPosition()).toString());
                    if(db.checkItemQuantityLow(nameArray.get(getAdapterPosition()).toString())) {
                        adapterCallback.sendSMSCallback(nameArray.get(getAdapterPosition()).toString());
                    }

                    textViewQuantity.setText(newQuantity);
                }
            });

            // sets click listener for the delete button to delete the item
            itemView.findViewById(R.id.imageButton4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.deleteItem(nameArray.get(getAdapterPosition()).toString()); // deletes the item from the database
                    nameArray.remove(getAdapterPosition()); // removes the item from the lists
                    nameArray.trimToSize();
                    quantityArray.remove(getAdapterPosition());
                    quantityArray.trimToSize();
                    notifyItemRemoved(getAdapterPosition()); // notifies the adapter that the item has been removed
                }
            });
        }
    }

}
