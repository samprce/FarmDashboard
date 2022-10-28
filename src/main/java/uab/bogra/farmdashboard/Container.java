package uab.bogra.farmdashboard;

import java.text.NumberFormat;

public class Container {
    private String name;
    private int locationX, locationY, dimensionX, dimensionY;
    private double price;

    public Container(String name) {
        this.name = name;
        this.locationX = 300;
        this.locationY = 400;
        this.dimensionX = 25;
        this.dimensionY = 50;
        this.price = 0.00;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLocationX() {
        return this.locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return this.locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public int getDimensionX() {
        return this.dimensionX;
    }

    public void setDimensionX(int dimensionX) {
        this.dimensionX = dimensionX;
    }

    public int getDimensionY() {
        return this.dimensionY;
    }

    public void setDimensionY(int dimensionY) {
        this.dimensionY = dimensionY;
    }

    public String getPriceToString() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        return format.format(this.price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
