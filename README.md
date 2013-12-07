Team18
======

SimCity201 Project Repository for CS 201 students

###Run Instructions

1. Download the project to the computer using either
"git clone git@github.com:usc-csci201-fall2013/team18.git" or 
https://github.com/usc-csci201-fall2013/team18/archive/master.zip

2. Open eclipse and create a new java project.
Remember to include JRE System Library (JavaSE-1.7), and JUnit 4 Library

3. Import "source" folder into the project. Right click the "source" folder.
In "Bulid Path", choose "Use as a Source Folder"

4. Choose MainGui.java under source/gui. Click "Run as..." and choose 
"Java Application" to run the program.

5. To run scenarios, choose the Configuration tab at the top right corner, and select a configuration. Descriptions of each scenario will be displayed.  Make sure to click the start button once you have selected a scenario.  To run a second
 scenario, we recommend you quit the program and restart so as to assure they work correctly.

###V1 Deliverable Information

####Work Done by Each Person

- Eric Gauderman (Integration Lead/Git Lead)
  - Person Agent
  - Home Scenarios (most importantly, HomeOccupantRole)
  - Integration, major contributor (with Omar and Yixin)
  - Instantiation of Person Agent with the correct job and house; use of factory methods (with Yixin)
  - Design of program's architecture (i.e. packages and class hierarchy)
  - General helping with questions
- Omar Khulusi (Team Leader)
  - Bank Scenarios
  - Animation Panels (Building interior animation panels and worldview specifically)
  - Integration, major contributor (with Eric and Yixin)
  - Aided Eric with integration of Person Agent and Roles
  - Aided with Person Agent scheduler
  - Design of program's architecture (i.e. packages and class hierarchy)
  - Integrated his Restaurant (including revolving stand, market interaction, bank interaction)
  - Time class
  - Discussing major decisions (particularly with Eric and Yixin)
- Ryan Hsu
  - Transportation (TruckAgent, BusAgent, CommuterRole)
  - Transportation Tests (Bus, Commuter, Truck)
  - Integrating Truck and Market/Truck and Restaurant scenarios
  - Truck Gui
  - Integrated his Restaurant (integrated but without revolving stand/market interaction/bank interaction)
- Tanner Zigrang
  - Gui Panels (more specifically, creation/configuration buttons/forms, organization of frames and panels)
  - Bank unit testing
  - Attempted to integrate his restaurant, but ran out of time (partially complete)
- Yixin Cai (Restaurant Integration Lead, Largest Code Contributor)
  - First to fully integrate his Restaurant, and assisted Omar/Ryan/Tanner in integrating restaurants
  - Market scenarios
  - Extensive Testing of his scenarios
  - Instantiation of Person Agent with the correct job and house; use of factory methods (with Eric)
  - Integration, major contributor (with Eric and Omar)

####Known Issues
  - Buses and Cars are not fully implemented. Bus travels around the city but commuter role does not take account of it
  - Collision detection is not implemented (i.e. no AStar, people just go straight to their destinations)
  - Not all five restaurants updated and integrated (2 fully integrated (Omar/Yixin), 1 semi-integrated (Ryan), 1 added but not integrated (Tanner), 1 not added (Eric))
  - Apartments can be occupied but owners do not pay rent.
  - City is set up to run specific scenarios, and therefore does not run well freely beyond these scenarios.  It is possible to continuously run it and add more people, but actions will be limited.
  - No parties and friend scenarios because we are a group of 5. 
