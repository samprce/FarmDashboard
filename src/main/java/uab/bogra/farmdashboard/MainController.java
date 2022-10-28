package uab.bogra.farmdashboard;

// Root
// Barn[container] > Livestock Area[container] > Cow[item]
// Barn[container] > Milk Storage[container]
// Command Center[container] > Drone[item]

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    TextInputDialog newContainerDialogue = new TextInputDialog();
    TextInputDialog renameDialogue = new TextInputDialog();

    Image folderImage = new Image(getClass().getResourceAsStream("/folder.png"));
    // Image file = new Image(getClass().getResourceAsStream("icons/file.png"));

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
            newContainerDialogue.setTitle("Add Item Container");
            newContainerDialogue.setHeaderText("Enter the name of the new item container:");
            newContainerDialogue.showAndWait().ifPresent(response -> {
                selectedItem.getChildren().add(new TreeItem<String>(response, new ImageView(folderImage)));
                propertyArrayList.add(new Container(response));
                newContainerDialogue.getEditor().clear();
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
                renameDialogue.setTitle("Rename");
                renameDialogue.setHeaderText("Enter the new name of the property: ");
                renameDialogue.showAndWait().ifPresent(response -> {
                    for (Container c : propertyArrayList) {
                        if (c.getName().equals(selectedItem.getValue())) {
                            c.setName(response);
                        }
                    }
                    selectedItem.setValue(response);
                    renameDialogue.getEditor().clear();
                });
                printArrayList(propertyArrayList);
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

    public void printArrayList(ArrayList<Container> arr) {
        for (Container c : arr) {
            System.out.println(c.getName());
        }
    }
}