package uab.bogra.farmdashboard;

import javafx.event.Event;
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

    Image folderImage = new Image(getClass().getResourceAsStream("/folder.png"));
    Image fileImage = new Image(getClass().getResourceAsStream("/file.png"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        containerCommandsPane.setVisible(true);
        containerCommandsPane.setDisable(false);
        itemCommandsPane.setVisible(false);
        itemCommandsPane.setDisable(true);

        TreeItem<String> rootNode = new TreeItem<>("Root", new ImageView(folderImage));
        Container root = new Container("Root");
        root.setDimensionX(800);
        root.setDimensionY(600);
        containerArrayList.add(root);
        treeView.setRoot(rootNode);
        rootNode.setExpanded(true);

        treeView.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                // TODO Auto-generated method stub
                try {
                    TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                    if (selectedItem.getGraphic().equals(fileImage)) {
                        itemCommandsPane.setVisible(true);
                        itemCommandsPane.setDisable(false);
                        containerCommandsPane.setVisible(true);
                        containerCommandsPane.setDisable(true);
                    } else if (selectedItem.getGraphic().equals(folderImage)) {
                        itemCommandsPane.setVisible(false);
                        itemCommandsPane.setDisable(true);
                        containerCommandsPane.setVisible(false);
                        containerCommandsPane.setDisable(true);
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
            TreeItem<String> selectedIteam = treeView.getSelectionModel().getSelectedItem();
            newPropertyDialog.setTitle("Add Item");
            newPropertyDialog.setHeaderText("Enter the name of the new item:");
            newPropertyDialog.showAndWait().ifPresent(response -> {
                selectedIteam.getChildren().add(new TreeItem<String>(response, new ImageView(fileImage)));
                itemsArrayList.add(new Item(response));
                newPropertyDialog.getEditor().clear();
            });
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
                    for (Container c : containerArrayList) {
                        if (c.getName().equals(selectedItem.getValue())) {
                            c.setName(response);
                        }
                    }
                    selectedItem.setValue(response);
                    renameDialog.getEditor().clear();
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
                for (Iterator<Container> iterator = containerArrayList.iterator(); iterator.hasNext();) {
                    Container container = iterator.next();
                    if (container.getName().equals(selectedItem.getValue())) {
                        iterator.remove();
                        removeContainerShapes();
                    }
                }
                selectedItem.getParent().getChildren().remove(selectedItem);
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
            for (Container container : containerArrayList) {
                if (container.getName().equals(selectedItemValue)) {
                    // System.out.println(container.getName());
                    // System.out.println(container.getLocationX());
                    // System.out.println(container.getLocationY());
                    container.setLocationX(xAndY.getKey());
                    container.setLocationY(xAndY.getValue());
                    // System.out.println(container.getName());
                    // System.out.println(container.getLocationX());
                    // System.out.println(container.getLocationY());
                }
            }
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
            for (Container container : containerArrayList) {
                if (container.getName().equals(selectedItemValue)) {
                    // System.out.println(container.getName());
                    // System.out.println(container.getDimensionX());
                    // System.out.println(container.getDimensionY());
                    container.setDimensionX(xAndY.getKey());
                    container.setDimensionY(xAndY.getValue());
                    // System.out.println(container.getName());
                    // System.out.println(container.getDimensionX());
                    // System.out.println(container.getDimensionY());
                }
            }
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
                text.setX(container.getLocationX() + 35);
                text.setY(container.getLocationY() + 25);
                shapesPane.getChildren().addAll(box, text);
            }
        }
    }

    public void removeContainerShapes() {
        shapesPane.getChildren().clear();
        drawContainers();
    }
}