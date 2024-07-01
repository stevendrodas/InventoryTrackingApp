package com.example.cs360finalproject;
// Developer: Steven Rodas (contact@stevenrodas.com)
public class RecyclerData {

    // initialize data variables
    private String name;
    private String quantity;


    public String getName() { return name; } // GETTER method for the name

    public void setName(String name) { this.name = name; } // SETTER method for the name
    public String getQuantity() { return quantity; } // GETTER method for the quantity
    public void setQuantity(String quantity) { this.quantity = quantity; } // SETTER method for the quantity

    // constructor to initialize the RecyclerData object
    public RecyclerData(String name, String quantity, String id) {
        this.name = name;
        this.quantity = quantity;
    }

}
