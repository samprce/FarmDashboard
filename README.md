# farm-dashboard
Readme
github located at:
https://github.com/samprce/app-farm-dashboard

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
Change Dimensions - will allow the user to specify new dimensions of the I
Add Item - will allow the user to add an Item to the IC that the current Item belongs to 
Delete - allows user to delete an I

The Drone starts at the command center in the upper left corner and from there has 3 options:
Scan Farm, Visit Item/Item Container
Scan Farm:
Scan Farm command will run the drone across the farm and then return it back to the Command Center
Visit Item/Item Container:
This will take the drone from its current location to the selected Item/Item Container then return to its original location

