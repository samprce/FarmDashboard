package uab.bogra.farmdashboard;

import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class DroneAnimation extends Pane {
    Image image = new Image(
            "/drone.png");
    Rectangle rectangle = new Rectangle(0, 0, 50, 50);
    int recOr = 90;
    int newOr = 0;
    ImagePattern imagePattern = new ImagePattern(image);
    Path path = new Path();

    DroneAnimation() {
        rectangle.setFill(imagePattern);
        recOr = 90;
        getChildren().add(rectangle);
    }

    // rotate drone takes old orientation oldDir and new Dir and rototes image
    // accordingly
    // if buttons pressed too fast can cause wonky alignment
    // down to right configuration causes spin first, could improve
    public void rotDrone(int oldDir, int newDir) {
        RotateTransition rotate = new RotateTransition();
        rotate.setDuration(Duration.millis(1000));
        rotate.setByAngle(oldDir - newDir); // could optimize so doesnt spin as much later
        rotate.setCycleCount(1);
        rotate.setNode(rectangle);
        rotate.play();
    }

    public void moveTrDir(Double toX, Double toY) {
        Path path = new Path();
        path.getElements().add(new MoveTo(0f, 0f));

        LineTo lineT = new LineTo();
        lineT.setX(toX);
        lineT.setY(toY);
        path.getElements().add(lineT);

        //path to
        /* //this code is for horizontal then verticle movement
        LineTo lineH = new LineTo();
        lineH.setX(toX);
        lineH.setY(0);
        path.getElements().add(lineH);

        LineTo lineV = new LineTo();
        lineV.setX(toX);
        lineV.setY(toY);
        path.getElements().add(lineV);
        */

        //rotate

        //path back

        LineTo lineB = new LineTo();
        lineB.setX(0);
        lineB.setY(0);
        path.getElements().add(lineB);

        /* 
        LineTo lineVR = new LineTo();
        lineVR.setX(toX);
        lineVR.setY(0);
        path.getElements().add(lineVR);

        LineTo lineHR = new LineTo();
        lineHR.setX(0);
        lineHR.setY(0);
        path.getElements().add(lineHR);
        */

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(15000));
        pathTransition.setNode(rectangle);
        pathTransition.setPath(path);
        pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(1);
        pathTransition.play();


    }

    //original move to direct code
    /* 

    // move to direct spot
    // takes doubles as input for where to move X and Y
    // uses current loc as starting point
    // need to implement to pick up location from an object isntead of direct x,y
    public void moveTrDir(Double toX, Double toY) {
        // implement with list, extract out
        TranslateTransition moveTrans = new TranslateTransition();
        // DO NOT use these
        // moveTrans.setFromX(rectangle.getX());
        // moveTrans.setFromY(rectangle.getY());
        moveTrans.setFromX(moveTrans.getFromX());
        moveTrans.setFromY(moveTrans.getFromY());
        moveTrans.setToX(toX);
        moveTrans.setToY(toY);
        // moveTrans.setByX(xval);
        // moveTrans.setByY(yval);
        moveTrans.setDuration(Duration.millis(5000));
        moveTrans.setCycleCount(1);
        moveTrans.setNode(rectangle);
        moveTrans.play();
    }
    */

    // go to origin
    // need to implement to pick up 'home base' location instead
    public void goHome() {
        moveTrDir(0.0, 0.0);
        RotateTransition rHome = new RotateTransition();
        rHome.setDuration(Duration.millis(1000));
        rHome.setByAngle(90); // could optimize so doesnt spin as much later
        rHome.setCycleCount(1);
        rHome.setNode(rectangle);
        rHome.play();
    }

    // cover enture farm
    // in progress
    // should be run after running go home
    public void coverFarm() {
        // Double fWidth, Double fHeight
        Double fWidth = 800.0;
        // Double fHeight = 600.0;
        // start at top

        Path path = new Path();
        path.getElements().add(new MoveTo(0f, 0f));

        // doesnt work, unsure why
        LineTo lineSt = new LineTo();
        lineSt.setX(0);
        lineSt.setY(0);
        path.getElements().add(lineSt);

        // Double xval = 0.0;
        Double yval = 0.0;
        Double incrY = 30.0;

        while (yval < fWidth) {
            LineTo lineRt = new LineTo();
            lineRt.setX(fWidth);
            lineRt.setY(yval);
            path.getElements().add(lineRt);
            LineTo lineDn = new LineTo();
            lineDn.setX(fWidth);
            yval = yval + incrY;
            lineDn.setY(yval);
            path.getElements().add(lineDn);
            LineTo lineLf = new LineTo();
            lineLf.setX(0);
            lineLf.setY(yval);
            path.getElements().add(lineLf);
            LineTo lineD2 = new LineTo();
            lineD2.setX(0);
            yval = yval + incrY;
            lineD2.setY(yval);
            path.getElements().add(lineD2);
        }
        LineTo lineHome = new LineTo();
        lineHome.setX(0.0);
        lineHome.setY(0.0);
        path.getElements().add(lineHome);

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(30000));
        pathTransition.setNode(rectangle);
        pathTransition.setPath(path);
        pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(1);
        pathTransition.play();

    }

    // here down not currently used

    // used for individual movements
    // adjusts by given x and y amounts
    // used for the UDLR commands, which are a combo of 0 and 10
    public void moveTr(int xval, int yval) {
        TranslateTransition moveTransS = new TranslateTransition();
        moveTransS.setByX(xval);
        moveTransS.setByY(yval);
        moveTransS.setDuration(Duration.millis(500));
        moveTransS.setCycleCount(1);
        moveTransS.setNode(rectangle);
        moveTransS.play();
    }
    // move to specific spot
    // mostly for testing
    public void moveDir() {
        moveTrDir(200.0, 200.0);
    }

    // move left
    // check if orientation has changed, rotate accordingly
    public void moveLeft() {

        if (recOr != 180) {
            rotDrone(recOr, 180);
            recOr = 180;
        }
        // rectangle.setX(rectangle.getX() - 10);
        moveTr(-10, 0);
    }

    public void moveRight() {
        if (recOr != 0) {
            rotDrone(recOr, 0);
            recOr = 0;
        }
        // rectangle.setX(rectangle.getX() + 10);
        moveTr(10, 0);
    }

    public void moveUp() {
        if (recOr != 90) {
            rotDrone(recOr, 90);
            recOr = 90;
        }
        // rectangle.setY(rectangle.getY() - 10);
        moveTr(0, -10);
    }

    public void moveDown() {
        if (recOr != 270) {
            rotDrone(recOr, 270);
            recOr = 270;
        }
        // rectangle.setY(rectangle.getY() + 10);
        moveTr(0, 10);
    }

}