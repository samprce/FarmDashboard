package uab.bogra.farmdashboard;

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

    ArrayList<Container> propertyArrayList = new ArrayList<>();
    TextInputDialog newContainerDialog = new TextInputDialog();
    TextInputDialog renameDialog = new TextInputDialog();
    TextInputDialog newItemDialog = new TextInputDialog();

    Image folderImage = new Image(getClass().getResourceAsStream("/folder.png"));
    Image file = new Image(getClass().getResourceAsStream("/file.png"));

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
        propertyArrayList.add(root);
        treeView.setRoot(rootNode);
        rootNode.setExpanded(true);
    }

    public void addContainer() {
        try {
            TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
            newContainerDialog.setTitle("Add Item Container");
            newContainerDialog.setHeaderText("Enter the name of the new item container:");
            newContainerDialog.showAndWait().ifPresent(response -> {
                selectedItem.getChildren().add(new TreeItem<String>(response, new ImageView(folderImage)));
                propertyArrayList.add(new Container(response));
                newContainerDialog.getEditor().clear();
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
                    for (Container c : propertyArrayList) {
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
                for (Iterator<Container> iterator = propertyArrayList.iterator(); iterator.hasNext();) {
                    Container container = iterator.next();
                    if (container.getName().equals(selectedItem.getValue())) {
                        iterator.remove();
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
            for (Container container : propertyArrayList) {
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
            for (Container container : propertyArrayList) {
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
}