package uab.bogra.farmdashboard;

import javafx.event.EventHandler;

// Root
// Barn[container] > Livestock Area[container] > Cow[item]
// Barn[container] > Milk Storage[container]
// Command Center[container] > Drone[item]

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    AnchorPane containerCommandsPane;

    @FXML
    AnchorPane itemCommandsPane;

    @FXML
    TreeView<String> treeView;

    @FXML
    Alert alert = new Alert(Alert.AlertType.ERROR, "You must select an item from the tree.");

    @FXML
    Pane shapesPane;

    ArrayList<Container> containerArrayList = new ArrayList<>();
    ArrayList<Item> itemsArrayList = new ArrayList<>();
    TextInputDialog newPropertyDialog = new TextInputDialog();
    TextInputDialog renameDialog = new TextInputDialog();
    TextInputDialog newItemDialog = new TextInputDialog();
    TextInputDialog priceDialog = new TextInputDialog();

    Image folderImage = new Image(getClass().getResourceAsStream("/folder.png"));
    Image fileImage = new Image(getClass().getResourceAsStream("/file.png"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        containerCommandsPane.setVisible(true);
        containerCommandsPane.setDisable(false);
        itemCommandsPane.setVisible(false);
        itemCommandsPane.setDisable(true);

        TreeItem<String> rootNode = new TreeItem<>("Root", new ImageView(folderImage));
        Container rootContainer = new Container("Root");
        containerArrayList.add(rootContainer);
        treeView.setRoot(rootNode);
        rootNode.setExpanded(true);

        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                    if (isContainer(selectedItem.getValue())) {
                        showContainerControls(containerCommandsPane, itemCommandsPane);
                    } else if (!isContainer(selectedItem.getValue())) {
                        showItemControls(itemCommandsPane, containerCommandsPane);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addContainer() {
        try {
            TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
            newPropertyDialog.setTitle("Add Item Container");
            newPropertyDialog.setHeaderText("Enter the name of the new item container:");
            newPropertyDialog.showAndWait().ifPresent(response -> {
                selectedItem.getChildren().add(new TreeItem<String>(response, new ImageView(folderImage)));
                containerArrayList.add(new Container(response));
                newPropertyDialog.getEditor().clear();
                drawContainers();
            });
        } catch (NullPointerException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("You must select an item from the tree.");
            alert.show();
        }
    }

    public void addItem() {
        try {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (isContainer(selectedItem.getValue())) {
                newPropertyDialog.setTitle("Add Item");
                newPropertyDialog.setHeaderText("Enter the name of the new container:");
                newPropertyDialog.showAndWait().ifPresent(response -> {
                    selectedItem.getChildren()
                            .add(new TreeItem<String>(response, new ImageView(fileImage)));
                    Item item = new Item(response);
                    itemsArrayList.add(item);
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(selectedItem.getValue())) {
                            // Test -> comment out when not needed
                            System.out.println(container.getName() + " has " + container.getChildrenList());
                            container.addChild(item);
                            // Test -> comment out when nopt needed
                            System.out.println(container.getName() + " has " + container.getChildrenList());
                        }
                    }
                    newPropertyDialog.getEditor().clear();
                    drawItemShapes();
                });
            } else {
                newPropertyDialog.setTitle("Add item");
                newPropertyDialog.setHeaderText("Enter the name of the new item:");
                newPropertyDialog.showAndWait().ifPresent(response -> {
                    selectedItem.getParent().getChildren()
                            .add(new TreeItem<String>(response, new ImageView(fileImage)));
                    Item item = new Item(response);
                    itemsArrayList.add(item);
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(selectedItem.getParent().getValue())) {
                            // Test -> comment out when not needed
                            System.out.println(container.getName() + " has " + container.getChildrenList());
                            container.addChild(item);
                            // Test -> comment out when not needed
                            System.out.println(container.getName() + " has " + container.getChildrenList());
                        }
                    }
                    newPropertyDialog.getEditor().clear();
                    drawItemShapes();
                });
            }
        } catch (NullPointerException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("You must select an item from the tree.");
            alert.show();
        }
    }

    public void changePrice() {
        try {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (isContainer(selectedItem.getValue())) {
                priceDialog.setTitle("Change Price");
                priceDialog.setHeaderText("Enter the price of the container:");
                priceDialog.showAndWait().ifPresent(response -> {
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(selectedItem.getValue())) {
                            // Test -> cooment out when not needed
                            // System.out.println(container.getPriceToString());
                            container.setPrice(Double.parseDouble(response));
                            // System.out.println(container.getPriceToString());
                        }
                    }
                });
                priceDialog.getEditor().clear();
            } else {
                priceDialog.setTitle("Change Price");
                priceDialog.setHeaderText("Enter the price of the item:");
                priceDialog.showAndWait().ifPresent(response -> {
                    for (Item item : itemsArrayList) {
                        if (item.getName().equals(selectedItem.getValue())) {
                            // Test -> comment out when not needed
                            // System.out.println(item.getPriceToString());
                            item.setPrice(Double.parseDouble(response));
                            // System.out.println(item.getPriceToString());
                        }
                    }
                });
                priceDialog.getEditor().clear();
            }
        } catch (NullPointerException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("You must select an item from the tree.");
            alert.show();
        }
    }

    public void rename() {
        try {
            TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
            if (selectedItem.getValue().equals("Root")) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("You cannot rename the Root directory.");
                alert.show();
            } else {
                renameDialog.setTitle("Rename");
                renameDialog.setHeaderText("Enter the new name of the property: ");
                renameDialog.showAndWait().ifPresent(response -> {
                    if (isContainer(selectedItem.getValue())) {
                        for (Container container : containerArrayList) {
                            if (container.getName().equals(selectedItem.getValue())) {
                                container.setName(response);
                                // Test -> comment out when not needed
                                // System.out.println(container.getName());
                            }
                        }
                    } else {
                        for (Item item : itemsArrayList) {
                            if (item.getName().equals(selectedItem.getValue())) {
                                item.setName(response);
                                // Test -> comment out when not needed
                                // System.out.println(item.getName());
                            }
                        }
                    }
                    selectedItem.setValue(response);
                    renameDialog.getEditor().clear();
                    removeContainerAndItemShapes();
                });
            }
        } catch (NullPointerException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("You must select an item from the tree.");
            alert.show();
        }
    }

    public void delete() {
        try {
            TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
            if (selectedItem.getValue().equals("Root")) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("You cannot delete the Root directory.");
                alert.show();
            } else {
                if (isContainer(selectedItem.getValue())) {
                    for (Iterator<Container> iterator = containerArrayList.iterator(); iterator.hasNext();) {
                        Container container = iterator.next();
                        if (container.getChildrenList().isEmpty()) {
                            iterator.remove();
                        } else {
                            for (Iterator<Item> iterator2 = container.getChildrenList().iterator(); iterator
                                    .hasNext();) {
                                Item item = iterator2.next();
                                // Test -> comment out when not needed
                                System.out.println(container.getName() + " has " + container.getChildrenList());
                                container.getChildrenList().remove(item);
                                // Test -> comment out when not needed
                                System.out.println(container.getName() + " has " + container.getChildrenList());
                            }
                            iterator.remove();
                        }
                    }
                    // Test -> comment out when not needed
                    // System.out.println(containerArrayList.toString());
                } else {
                    TreeItem<String> parentOfSelectedItem = selectedItem.getParent();
                    for (Iterator<Item> iterator = itemsArrayList.iterator(); iterator.hasNext();) {
                        Item item = iterator.next();
                        if (item.getName().equals(selectedItem.getValue())) {
                            for (Container container : containerArrayList) {
                                if (container.getName().equals(parentOfSelectedItem.getValue())) {
                                    container.getChildrenList().remove(item);
                                }
                            }
                            iterator.remove();
                        }
                    }
                    // Test -> comment out when not needed
                    // System.out.println(itemsArrayList.toString());
                }
                selectedItem.getParent().getChildren().remove(selectedItem);
                removeContainerAndItemShapes();
            }
        } catch (NullPointerException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("You must select an item to delete.");
            alert.show();
        }
    }

    public void changeLocation() {
        String selectedItemValue = treeView.getSelectionModel().getSelectedItem().getValue();

        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Change Location");
        dialog.setHeaderText("Enter the new location:");

        ButtonType buttonOk = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonOk, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField x = new TextField();
        TextField y = new TextField();

        gridPane.add(new Label("Location X"), 0, 0);
        gridPane.add(x, 1, 0);
        gridPane.add(new Label("Location Y"), 0, 1);
        gridPane.add(y, 1, 1);

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonOk) {
                return new Pair<Integer, Integer>(Integer.parseInt(x.getText()), Integer.parseInt(y.getText()));
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();

        result.ifPresent(xAndY -> {
            if (isContainer(selectedItemValue)) {
                for (Container container : containerArrayList) {
                    if (container.getName().equals(selectedItemValue)) {
                        // Test -> comment out when not needed
                        // System.out.println(container.getName());
                        // System.out.println(container.getLocationX());
                        // System.out.println(container.getLocationY());
                        container.setLocationX(xAndY.getKey());
                        container.setLocationY(xAndY.getValue());
                        // Test -> comment out when not needed
                        // System.out.println(container.getName());
                        // System.out.println(container.getLocationX());
                        // System.out.println(container.getLocationY());
                    }
                }
            } else {
                for (Item item : itemsArrayList) {
                    if (item.getName().equals(selectedItemValue)) {
                        // Test -> comment out when not needed
                        // System.out.println(item.getName());
                        // System.out.println(item.getLocationX());
                        // System.out.println(item.getLocationY());
                        item.setLocationX(xAndY.getKey());
                        item.setLocationY(xAndY.getValue());
                        // Test -> comment out when not needed
                        // System.out.println(item.getName());
                        // System.out.println(item.getLocationX());
                        // System.out.println(item.getLocationY());
                    }
                }
            }
            shapesPane.getChildren().clear();
            drawContainers();
            drawItemShapes();
        });
    }

    public void changeDimension() {
        String selectedItemValue = treeView.getSelectionModel().getSelectedItem().getValue();

        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Change Dimension");
        dialog.setHeaderText("Enter the new dimensions:");

        ButtonType buttonOk = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonOk, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField x = new TextField();
        TextField y = new TextField();

        gridPane.add(new Label("Dimension X"), 0, 0);
        gridPane.add(x, 1, 0);
        gridPane.add(new Label("Dimension Y"), 0, 1);
        gridPane.add(y, 1, 1);

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonOk) {
                return new Pair<Integer, Integer>(Integer.parseInt(x.getText()), Integer.parseInt(y.getText()));
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();

        result.ifPresent(xAndY -> {
            if (isContainer(selectedItemValue)) {
                for (Container container : containerArrayList) {
                    if (container.getName().equals(selectedItemValue)) {
                        // Test -> comment out when not needed
                        // System.out.println(container.getName());
                        // System.out.println(container.getDimensionX());
                        // System.out.println(container.getDimensionY());
                        container.setDimensionX(xAndY.getKey());
                        container.setDimensionY(xAndY.getValue());
                        // Test -> comment out when not needed
                        // System.out.println(container.getName());
                        // System.out.println(container.getDimensionX());
                        // System.out.println(container.getDimensionY());
                    }
                }
            } else {
                for (Item item : itemsArrayList) {
                    if (item.getName().equals(selectedItemValue)) {
                        // Test -> comment out when not needed
                        // System.out.println(item.getName());
                        // System.out.println(item.getDimensionX());
                        // System.out.println(item.getDimensionY());
                        item.setDimensionX(xAndY.getKey());
                        item.setDimensionY(xAndY.getValue());
                        // Test -> comment out when not needed
                        // System.out.println(item.getName());
                        // System.out.println(item.getDimensionX());
                        // System.out.println(item.getDimensionY());
                    }
                }
            }
            shapesPane.getChildren().clear();
            drawContainers();
            drawItemShapes();
        });
    }

    public void drawContainers() {
        for (Container container : containerArrayList) {
            if (container.getName() != "Root") {
                Rectangle box = new Rectangle();
                box.setX(container.getLocationX());
                box.setY(container.getLocationY());
                box.setWidth(container.getDimensionX());
                box.setHeight(container.getDimensionY());
                box.setFill(Color.WHITE);
                box.setStroke(Color.BLACK);
                box.setStrokeWidth(1);
                Text text = new Text(container.getName());
                text.setX(container.getLocationX() + 5);
                text.setY(container.getLocationY() + 15);
                shapesPane.getChildren().addAll(box, text);
            }
        }
    }

    public void drawItemShapes() {
        for (Item item : itemsArrayList) {
            Rectangle itemBox = new Rectangle();
            itemBox.setX(item.getLocationX());
            itemBox.setY(item.getLocationY());
            itemBox.setWidth(item.getDimensionX());
            itemBox.setHeight(item.getDimensionY());
            itemBox.setFill(Color.WHITE);
            itemBox.setStroke(Color.BLACK);
            itemBox.setStrokeWidth(1);
            Text text = new Text(item.getName());
            text.setX(item.getLocationX() + 5);
            text.setY(item.getLocationY() + 15);
            shapesPane.getChildren().addAll(itemBox, text);
        }
    }

    public void removeContainerAndItemShapes() {
        shapesPane.getChildren().clear();
        drawContainers();
        drawItemShapes();
    }

    public boolean isContainer(String value) {
        boolean isContainer = false;

        for (Container container : containerArrayList) {
            if (container.getName().equals(value)) {
                isContainer = true;
            }
        }
        return isContainer;
    }

    public void showItemControls(AnchorPane items, AnchorPane containers) {
        items.setVisible(true);
        items.setDisable(false);
        containers.setVisible(false);
        containers.setDisable(true);
    }

    public void showContainerControls(AnchorPane containers, AnchorPane items) {
        containers.setVisible(true);
        containers.setDisable(false);
        items.setVisible(false);
        items.setDisable(true);
    }
}