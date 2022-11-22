package uab.bogra.farmdashboard;

import java.io.IOException;

public class DroneAnimationAdapter extends DroneAnimation{

    public DroneAnimationAdapter(){
        this.controller = new DroneAnimation();
    }
    public void rotDroneAdapter(int oldDir, int newDir) {
        this.controller.rotDrone(oldDir, newDir);
    }
    public void moveTrDirAdapter(Double toX, Double toY) {
        this.controller.moveTrDir(toX, toY);
    }
    public void goHomeAdapter() {
        this.controller.goHome();
    }
    public void coverFarmAdapter() {
        this.controller.coverFarm();
    }
    public void moveTrAdapter(int xval, int yval) {
        this.controller.moveTr(xval, yval);
    }
    public void moveDirAdapter() {
        moveTrDir(200.0, 200.0);
    }
    public void moveLeftAdapter() {
        this.controller.moveLeft();
    }
    public void moveRightAdapter() {
        this.controller.moveRight();
    }
    public void moveUpAdapter() {
        this.controller.moveUp();
    }
    public void moveDownAdapter() {
        this.controller.moveDown();
    }
    //Refactoring Here down:
    @Override
    public void takeoff() throws IOException{
        //not needed in simulation
    }
    @Override
	public void land() throws IOException{
        //not needed in simulation
    }
    @Override
	public void increaseAltitude(int up) throws IOException{
        //not needed in simulation
    }
    @Override
	public void decreaseAltitude(int down) throws IOException{
        //not needed in simulation
    }
	public void flyForwardAdapter(int front) throws IOException{
        this.controller.flyForward(front);
    }
	public void flyLeftAdapter(int left) throws IOException{
        this.controller.flyLeft(left);
    }
	public void flyRightAdapter(int right) throws IOException{
        this.controller.flyRight(right);  
    }
	public void turnCWAdapter(int degrees) throws IOException{
        this.controller.turnCW(degrees);
    }
	public void turnCCWAdapter(int degrees) throws IOException{
        this.controller.turnCCW(degrees);
    }

    @Override
	public int getFlightTime() throws IOException{
        return 0;
        //not needed in simulation
    }

    @Override
	public int getheight() throws IOException{
        return 0;
        //not needed in simulation
    }

    @Override
	public int getAttitudePitch() throws IOException{
        return 0;
        //not needed in simulation
    }

    @Override
	public int getAttitudeRoll() throws IOException{
        return 0;
        //not needed in simulation
    }

    @Override
	public int getAttitudeYaw() throws IOException{
        return 0;
        //not needed in simulation
    }

    @Override
	public double getAccelerationX() throws IOException{
        return 0.0;
        //not needed in simulation
    }

    @Override
	public double getAccelerationY() throws IOException{
        return 0.0;
        //not needed in simulation
    }

    @Override
	public double getAccelerationZ() throws IOException{
        return 0.0;
        //not needed in simulation
    }

    @Override
	public int getTOF() throws IOException{
        return 0;
        //not needed in simulation
    }
    
}
