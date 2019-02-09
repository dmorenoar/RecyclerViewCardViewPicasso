package com.example.dmorenoar.recyclerview.Models;

public class Pokemon {
    public String name;
    public String description;
    public int img;
    public int icon;
    public int quantity = 0;


    public Pokemon(){ }

    public Pokemon(String name, String description, int img, int icon) {
        this.name = name;
        this.description = description;
        this.img = img;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //Incrementamos al haber atrapado un pokemon
    public void addPokemon(){
        this.quantity += 1;
    }

    //Liberamos un pokemon
    public void releasePokemon(){
        this.quantity -= 1;
    }

}
