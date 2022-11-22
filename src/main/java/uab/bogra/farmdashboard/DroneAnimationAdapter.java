package uab.bogra.farmdashboard;

import java.io.IOException;

public class DroneAnimationAdapter extends DroneAnimation{

    public DroneAnimationAdapter(){
        this.controller = new DroneAnimation();
    }

    @Override
    public void rotDrone(int oldDir, int newDir) {
        this.controller.rotDrone(oldDir, newDir);
    }

    @Override
    public void moveTrDir(Double toX, Double toY) {
        this.controller.moveTrDir(toX, toY);
    }

    @Override
    public void goHome() {
        this.controller.goHome();
    }

    @Override
    public void coverFarm() {
        this.controller.coverFarm();
    }

    @Override
    public void moveTr(int xval, int yval) {
        this.controller.moveTr(xval, yval);
    }

    @Override
    public void moveDir() {
        moveTrDir(200.0, 200.0);
    }

    @Override
    public void moveLeft() {
        this.controller.moveLeft();
    }
    @Override
    public void moveRight() {
        this.controller.moveRight();
    }
    @Override
    public void moveUp() {
        this.controller.moveUp();
    }
    @Override
    public void moveDown() {
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

    @Override
	public void flyForward(int front) throws IOException{
        this.controller.flyForward(front);
    }

    @Override
	public void flyLeft(int left) throws IOException{
        this.controller.flyLeft(left);
    }

    @Override
	public void flyRight(int right) throws IOException{
        this.controller.flyRight(right);  
    }

    @Override
	public void turnCW(int degrees) throws IOException{
        this.controller.turnCW(degrees);
    }
    

    @Override
	public void turnCCW(int degrees) throws IOException{
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
