## This is a Gravity Simulation

2D gravity simulation. I would say it's self-explanatory but it's really not. Usage instructions upcoming. Just download the jar or something

## Roadmap

Future features will include:
- Optimization of sorts
- Bug fixes (Maybe)

Features that have been added:
- Adjustable speed
- Two-body analysis
- Change frame of reference
- Config file
- Save planetary systems
- Statistics for selected body
- Ability to add/edit new celestial bodies


# Running the program
Download the GravitySim.jar file. This program can be run by double-clicking the downloaded file, or by running `java -jar GravitySim.jar` from the folder it is located. This file was compiled using JDK-17, meaning that you will need this version or newer to run this file. Upon startup, a file called config.txt will be created. This can be edited to change startup values, and deleting it will cause the program to regenerate a new one, in the case of an error.

## Compiling the program
If you have an older version of Java installed, you can compile this on your own machine. Simply navigate in the terminal/console to the src folder of the downloaded zip, and run `javac Start.java`. Simply run `java Start` afterwards to run the program


# How is the program actually used
By default, the program starts with a model of the solar system, including all the planets and our moon. Here are the basic controls:
* Click and drag to pan
* Scroll to zoom
* Spacebar to pause/unpause

Clicking on a celestial body selects it, providing information on the body. Using the directional arrow keys also allows you to cycle through all the celestial bodies, as actually clicking the planets is not always easy. 

Various actions can be done by pressing the indicated keys on the controls list on the left side of the window. They should be hopefully self-explanatory, except for the reference frame thing which will be looked at later.

Six of the actions that have keybinds also have corresponding buttons on the top of the window, for ease of access. There is no difference between pressing the button and pressing the key. Buttons that are greyed out can only be used if a celestial body is selected, and pressing it otherwise will not perform the action.


Contact me at ahartry87@gmail.com if you want to insult my program
