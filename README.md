# Internet Of Things
### Built in the summer of 2017 at The University of Texas at Austin

This repository contains two builds of our app. 

The one titled ‘WeatherReader’ is the build which is not integrated with the Philips Hue API. This app pulls data from the RSS server of a weather website and displays the information we chose for it to display. It obtains the string which summarizes the day in a paragraph for example: “The day will be partly cloudy…” after this, it searches the string for words such as ‘cloudy’ or ‘sunny’ or ‘clear’ or ‘rainy’ and if there is an instance of said words in the string; it returns a specific color to the screen.

Our idea was almost exactly what I mentioned above, with the difference being that the instance of words in the string was supposed to push a command to the Philips Hue Lamp and change its color. 

The one titled ‘QuickStartApp’ is the one which tries to address the aspiration mentioned above. It is the file we were directed to download in our canvas tutorial (RESTful programming with Hue) with the code from our main app integrated into it.
