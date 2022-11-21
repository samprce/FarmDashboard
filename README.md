# farm-dashboard
Readme
main github located at:
https://github.com/samprce/app-farm-dashboard
Please use this github for Term project–Design and Implementation-Part 2:
@@@ insert here

In app-farm-dashboard/src/main/java/uab/bogra/farmdashboard/
Run MainController.java

Item/Item Containers:
Item Containers (IC) have the following options:
Rename - will rename the IC
Change Location - will allow the user to specify new x,y coordinates for the IC
Change Price - will allow the user to specify a new price of the IC
Change Dimensions - will allow the user to specify new dimensions of the IC
Add Item - will allow the user to add an Item to the IC
Delete - allows user to delete an IC and any associated Items

Items can be added to Item Containers
Items (I) have the following options:
Rename - will rename the I
Change Location - will allow the user to specify new x,y coordinates for the I
Change Price - will allow the user to specify a new price of the I
Change Market Value - will allow the user to specify a new market value of the I
Change Dimensions - will allow the user to specify new dimensions of the I
Add Item - will allow the user to add an Item to the IC that the current Item belongs to 
Delete - allows user to delete an I

The simulated Drone starts at the command center in the upper left corner and from there has 2 options:
Scan Farm, Visit Item/Item Container
Scan Farm:
Scan Farm command will run the drone across the farm and then return it back to the Command Center
Visit Item/Item Container:
This will take the drone from its current location to the selected Item/Item Container then return to its original location

There is an option to switch between running the simulated and actual drone to perform the same action, the user may choose which of the two they would like to perform and then press the Scan Farm or Visit Item/Item Container option to see it be performed.

App.Java and AppRunner.java run the application and ensure only a single instance of the program can run at a time.

The visitor Pattern is implemented through Visitor.java

Adapter Pattern is implemented in DroneAnimation.java and TelloDrone.java, which extends MultiRotorDrone.java

The following java classes were provided from the professor for flying the drone:
Constants.java
DroneController.java
FlightControllable.java
MultiRotorDrone.java
PhysicalDrone.java
TelloDrone.java
