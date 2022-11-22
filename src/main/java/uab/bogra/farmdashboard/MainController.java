package uab.bogra.farmdashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Root
// Barn[container] > Livestock Area[container] > Cow[item]
// Barn[container] > Milk Storage[container]
// Command Center[container] > Drone[item]

// addItem() ✅ delete() ✅ rename() ✅ changeLocation() ✅ changeDimension() ✅ changePrice() ✅ changemVal() ✅
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
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

    @FXML
    Rectangle commandShape;

    @FXML
    Text commandText;

    @FXML
    Text dispPrice;

    @FXML
    Text dispmVal;

    @FXML
    RadioButton visitRadioButton;

    @FXML
    RadioButton scanRadioButton;

    @FXML
    Button simButton;

    @FXML
    Button launchButton;

    //kw
    //private static final Logger 		logger = Logger.getGlobal();
	//private static final ConsoleHandler handler = new ConsoleHandler();

    TelloDrone tello;

    DroneAnimationAdapter Square = new DroneAnimationAdapter();

    ArrayList<Container> containerArrayList = new ArrayList<>();
    ArrayList<Item> itemsArrayList = new ArrayList<>();
    TextInputDialog newPropertyDialog = new TextInputDialog();
    TextInputDialog renameDialog = new TextInputDialog();
    TextInputDialog newItemDialog = new TextInputDialog();
    TextInputDialog priceDialog = new TextInputDialog();
    TextInputDialog mValDialog = new TextInputDialog();

    Image folderImage = new Image(getClass().getResourceAsStream("/folder.png"));
    Image fileImage = new Image(getClass().getResourceAsStream("/file.png"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            tello = new TelloDrone();
        } catch (SocketException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (UnknownHostException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (FileNotFoundException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        try {
            tello.activateSDK();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        containerCommandsPane.setVisible(true);
        containerCommandsPane.setDisable(false);
        itemCommandsPane.setVisible(false);
        itemCommandsPane.setDisable(true);

        TreeItem<String> rootNode = new TreeItem<>("Root", new ImageView(folderImage));
        Container rootContainer = new Container("Root");
        containerArrayList.add(rootContainer);
        treeView.setRoot(rootNode);
        rootNode.setExpanded(true);

        shapesPane.getChildren().addAll(Square);

        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                    if (isContainer(selectedItem.getValue())) {
                        showContainerControls(containerCommandsPane, itemCommandsPane);
                        goVisit(selectedItem);
                    } else if (!isContainer(selectedItem.getValue())) {
                        showItemControls(itemCommandsPane, containerCommandsPane);
                        goVisit(selectedItem);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        ToggleGroup radioToggles = new ToggleGroup();
        visitRadioButton.setToggleGroup(radioToggles);
        scanRadioButton.setToggleGroup(radioToggles);
        scanRadioButton.setSelected(true);

        // Code for sim on button press goes here
        simButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event){
                if (visitRadioButton.isSelected()) {
                    try {
                        visitItem();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }      
                } else {
                    scanFarm();
                }
            }
            
        });

        // Code for physical drone logic on button press goes here 
        launchButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (visitRadioButton.isSelected()) {
                    try {
                        launchVisitItem();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }      
                } else {
                    try {
                        launchScanFarm();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    };
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
            newPropertyDialog.setTitle("Add Item Container");
            newPropertyDialog.setHeaderText("Enter the name of the new item container:");
            newPropertyDialog.showAndWait().ifPresent(response -> {
                if (!isContainer(selectedItem.getValue()) && selectedItem.isLeaf()) {
                    // Adds new tree item to the items
                    selectedItem.getParent().getChildren()
                            .add(new TreeItem<String>(response, new ImageView(fileImage)));
                    // Get parent tree item of the selected tree item
                    TreeItem<String> parentToSelectedItem = selectedItem.getParent();
                    // Find Container object for the parent tree item
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(parentToSelectedItem.getValue())) {
                            // Adds an item to the containers ArrayList<Item> that stores all items
                            container.addChild(new Item(response));
                        }
                    }
                    drawItemShapes();
                } else {
                    // Adds new tree item to tree as a child to selected tree item
                    selectedItem.getChildren().add(new TreeItem<String>(response, new ImageView(fileImage)));
                    // Find container object that represents the selected tree item
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(selectedItem.getValue())) {
                            container.addChild(new Item(response));
                        }
                    }
                    drawItemShapes();
                }
            });
        } catch (NullPointerException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("You must select an item from the tree.");
            alert.show();
        }
    }

    public void changePrice() {
        try {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            priceDialog.setTitle("Change Price");
            priceDialog.setHeaderText("Enter the price of the container:");
            priceDialog.showAndWait().ifPresent(response -> {
                if (isContainer(selectedItem.getValue())) {
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(selectedItem.getValue())) {
                            container.setPrice(Double.parseDouble(response));
                            removeContainerAndItemShapes();
                        }
                    }
                } else {
                    TreeItem<String> parentToSelectedItem = selectedItem.getParent();
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(parentToSelectedItem.getValue())) {
                            for (Item item : container.getChildrenList()) {
                                if (item.getName().equals(selectedItem.getValue())) {
                                    item.setPrice(Double.parseDouble(response));
                                    removeContainerAndItemShapes();
                                }
                            }
                        }
                    }
                }
            });
        } catch (NullPointerException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("You must select an item from the tree.");
            alert.show();
        }
    }

    public void changemVal() {
        try {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            mValDialog.setTitle("Change Market Value");
            mValDialog.setHeaderText("Enter the market value of the container:");
            mValDialog.showAndWait().ifPresent(response -> {
                if (isContainer(selectedItem.getValue())) {
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(selectedItem.getValue())) {
                            container.setmVal(Double.parseDouble(response));
                            removeContainerAndItemShapes();
                        }
                    }
                } else {
                    TreeItem<String> parentToSelectedItem = selectedItem.getParent();
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(parentToSelectedItem.getValue())) {
                            for (Item item : container.getChildrenList()) {
                                if (item.getName().equals(selectedItem.getValue())) {
                                    item.setmVal(Double.parseDouble(response));
                                    removeContainerAndItemShapes();
                                }
                            }
                        }
                    }
                }
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
            newPropertyDialog.setTitle("Add Item Container");
            newPropertyDialog.setHeaderText("Enter the name of the new item container:");
            newPropertyDialog.showAndWait().ifPresent(response -> {
                if (isContainer(selectedItem.getValue()) && !selectedItem.getValue().equals("Root")) {
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(selectedItem.getValue())) {
                            container.setName(response);
                            selectedItem.setValue(response);
                            removeContainerAndItemShapes();
                        }
                    }
                } else {
                    TreeItem<String> parentToSelectedItem = selectedItem.getParent();
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(parentToSelectedItem.getValue())) {
                            for (Item item : container.getChildrenList()) {
                                if (item.getName().equals(selectedItem.getValue())) {
                                    item.setName(response);
                                    selectedItem.setValue(response);
                                    removeContainerAndItemShapes();
                                }
                            }
                        }
                    }
                }
            });
        } catch (NullPointerException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("You must select an item from the tree.");
            alert.show();
        }
    }

    public void delete() {
        try {
            TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
            if (isContainer(selectedItem.getValue()) && !selectedItem.getValue().equals("Null")) {
                for (Container container : containerArrayList) {
                    if (container.getName().equals(selectedItem.getValue())) {
                        container.getChildrenList().clear();
                        containerArrayList.remove(container);
                        selectedItem.getParent().getChildren().remove(selectedItem);
                        removeContainerAndItemShapes();
                    }
                }
            } else {
                TreeItem<String> parentToSelectedItem = selectedItem.getParent();
                for (Container container : containerArrayList) {
                    if (container.getName().equals(parentToSelectedItem.getValue())) {
                        for (Item item : container.getChildrenList()) {
                            if (item.getName().equals(selectedItem.getValue())) {
                                container.getChildrenList().remove(item);
                                selectedItem.getParent().getChildren().remove(selectedItem);
                                removeContainerAndItemShapes();
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("You must select an item to delete.");
            alert.show();
        }
    }

    public void changeLocation() {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

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
            if (isContainer(selectedItem.getValue())) {
                for (Container container : containerArrayList) {
                    if (container.getName().equals(selectedItem.getValue())) {
                        container.setLocationX(xAndY.getKey());
                        container.setLocationY(xAndY.getValue());
                    }
                }
                removeContainerAndItemShapes();
            } else {
                TreeItem<String> parentToSelectedItem = selectedItem.getParent();
                for (Container container : containerArrayList) {
                    if (container.getName().equals(parentToSelectedItem.getValue())) {
                        for (Item item : container.getChildrenList()) {
                            if (item.getName().equals(selectedItem.getValue())) {
                                item.setLocationX(xAndY.getKey());
                                item.setLocationY(xAndY.getValue());
                            }
                        }
                    }
                }
                removeContainerAndItemShapes();
            }
        });
    }

    public void changeDimension() {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

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
            if (isContainer(selectedItem.getValue())) {
                for (Container container : containerArrayList) {
                    if (container.getName().equals(selectedItem.getValue())) {
                        container.setDimensionX(xAndY.getKey());
                        container.setDimensionY(xAndY.getValue());
                    }
                }
                removeContainerAndItemShapes();
            } else {
                TreeItem<String> parentToSelectedItem = selectedItem.getParent();
                for (Container container : containerArrayList) {
                    if (container.getName().equals(parentToSelectedItem.getValue())) {
                        for (Item item : container.getChildrenList()) {
                            if (item.getName().equals(selectedItem.getValue())) {
                                item.setDimensionX(xAndY.getKey());
                                item.setDimensionY(xAndY.getValue());
                            }
                        }
                    }
                }
                removeContainerAndItemShapes();
            }
        });
    }

    public void drawContainers() {
        for (Container container : containerArrayList) {
            if (!container.getName().equals("Root")) {
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
        for (Container container : containerArrayList) {
            if (!container.getName().equals("Root")) {
                for (Item item : container.getChildrenList()) {
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
        }
    }

    public void removeContainerAndItemShapes() {
        shapesPane.getChildren().clear();
        drawContainers();
        drawItemShapes();
        shapesPane.getChildren().add(commandShape);
        shapesPane.getChildren().add(commandText);
        shapesPane.getChildren().add(Square);
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


    //drone animation
    public ObservableList<Double> getdLocs() {
 
    ObservableList<Double> wantedvals = FXCollections. observableArrayList();
    TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

    if (isContainer(selectedItem.getValue()) && !selectedItem.getValue().equals("Null")) {
        for (Container container : containerArrayList) {
            if (container.getName().equals(selectedItem.getValue())) {
                wantedvals.addAll(Double.valueOf(container.getLocationX()),Double.valueOf(container.getLocationY()));
            }
        }
    } else {
        TreeItem<String> parentToSelectedItem = selectedItem.getParent();
        for (Container container : containerArrayList) {
            if (container.getName().equals(parentToSelectedItem.getValue())) {
                for (Item item : container.getChildrenList()) {
                    if (item.getName().equals(selectedItem.getValue())) {
                        wantedvals.addAll(Double.valueOf(item.getLocationX()),Double.valueOf(item.getLocationY()));
                    }
                }
            }
        }
    }
    return wantedvals;
    }

    public void visitItem() throws IOException, InterruptedException{
        Square.toFront();
        ObservableList<Double> newDLoc = getdLocs();
        Square.moveTrDir(newDLoc.get(0), newDLoc.get(1));
    }

    public void scanFarm() {
        Square.toFront();
        Square.coverFarm();
    }

    public void goHome() {
        Square.toFront();
        Square.goHome();
    }

    //visitor
    public void goVisit(TreeItem<String> selectedItem){
        //visitor
        Visitor viz = new Visitor();
        
        //System.out.println("here!!!!!!!!!!!!!!!!!!!!!!");
        //System.out.println(selectedItem.getValue());

        if (isContainer(selectedItem.getValue()) && !selectedItem.getValue().equals("Null")) {
            for (Container container : containerArrayList) {
                if (container.getName().equals(selectedItem.getValue())) {
                    //TreeItem<String> p1 = selectedItem.getParent();
                    //System.out.println("Parent value vv");
                    //System.out.println(p1.getValue());
                    //System.out.println("Container");
                    //System.out.println(container.getPrice());
                    viz.visit(container);
                    //System.out.println("final val");
                    //System.out.println(viz.getVal());
                    //second level
                    ObservableList<TreeItem<String>> c1 = selectedItem.getChildren();
                    for (Container cont2 : containerArrayList){
                        for (TreeItem<String> c1i : c1){
                            if (cont2.getName().equals(c1i.getValue())){
                                viz.visit(cont2);
                                //level 3
                                ObservableList<TreeItem<String>> c2 = c1i.getChildren();
                                for (Container cont3 : containerArrayList){
                                    for (TreeItem<String> c2i : c2){
                                        if (cont3.getName().equals(c2i.getValue())){
                                            viz.visit(cont3);
                                            //level 4
                                            ObservableList<TreeItem<String>> c3 = c2i.getChildren();
                                            for (Container cont4 : containerArrayList){
                                                for (TreeItem<String> c3i : c3){
                                                    if (cont4.getName().equals(c3i.getValue())){
                                                        viz.visit(cont4);
                                                        //level 5 would go here
                        
                                                    }
                                                }
                                            } 
                                        }
                                    }
                                }

                            }
                        }
                    }

                }
            }
        } else {
            TreeItem<String> parentToSelectedItem = selectedItem.getParent();
            for (Container container : containerArrayList) {
                if (container.getName().equals(parentToSelectedItem.getValue())) {
                    for (Item item : container.getChildrenList()) {
                        if (item.getName().equals(selectedItem.getValue())) {
                            //System.out.println("Item");
                            //System.out.println(item.getPrice());
                            viz.visit(item);
                            //System.out.println("final val");
                            //System.out.println(viz.getVal());

                        }
                    }
                }
            }
        }
        dispPrice.setText(Double.toString(viz.getPrice()));
        dispmVal.setText(Double.toString(viz.getmVal()));
    }

    public void launchVisitItem() throws IOException, InterruptedException{
        try {
            TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
                if (isContainer(selectedItem.getValue()) && !selectedItem.getValue().equals("Root")) {
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(selectedItem.getValue())) {
                            int selectedItemX = container.getLocationX();
                            int selectedItemY = container.getLocationY();
                            tello.takeoff();
                            tello.hoverInPlace(2);
                            tello.gotoXY(selectedItemX, -(selectedItemY), 1);
                            tello.turnCCW(360);
                            tello.hoverInPlace(2);
                            tello.gotoXY(-(selectedItemX), selectedItemY, 1);
                            tello.hoverInPlace(2);
                            tello.land();
                        }
                    }
                } else {
                    TreeItem<String> parentToSelectedItem = selectedItem.getParent();
                    for (Container container : containerArrayList) {
                        if (container.getName().equals(parentToSelectedItem.getValue())) {
                            for (Item item : container.getChildrenList()) {
                                if (item.getName().equals(selectedItem.getValue())) {
                                    int selectedItemX = item.getLocationX();
                                    int selectedItemY = item.getLocationY();
                                    tello.takeoff();
                                    tello.hoverInPlace(2);
                                    tello.gotoXY(selectedItemX, -(selectedItemY), 1);
                                    tello.turnCCW(360);
                                    tello.hoverInPlace(2);
                                    tello.gotoXY(-(selectedItemX), selectedItemY, 1);
                                    tello.hoverInPlace(2);
                                    tello.land();
                                }
                            }
                        }
                    }
                }
        } catch (NullPointerException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("You must select an item from the tree.");
            alert.show();
        }
    }

    public void launchScanFarm() throws IOException, InterruptedException{
        tello.takeoff();
        tello.hoverInPlace(1);
        tello.flyForward(40);
        tello.turnCCW(90);
        tello.flyForward(20);
        tello.turnCCW(90);
        tello.flyForward(40);
        tello.turnCW(90);
        tello.flyForward(20);
        tello.turnCW(90);
        tello.hoverInPlace(1);
        tello.land();
    }

}