package uab.bogra.farmdashboard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.shape.Rectangle;

public class Container {
    private String name;
    private int locationX, locationY, dimensionX, dimensionY;
    private double price;
    private Random random = new Random();
    private int rand = random.nextInt(500);
    private Rectangle box;
    private ArrayList<Item> children;

    public Container(String name) {
        this.name = name;
        this.locationX = rand;
        this.locationY = rand;
        this.dimensionX = 100;
        this.dimensionY = 100;
        this.price = 0.00;
        this.box = new Rectangle();
        this.children = new ArrayList<Item>();
    }

    public Rectangle getBox() {
        return this.box;
    }

    public void setBox(Rectangle box) {
        this.box = box;
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

    public void addChild(Item item) {
        this.children.add(item);
    }

    public ArrayList<Item> getChildrenList() {
        return this.children;
    }

}
