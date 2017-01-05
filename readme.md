# Set up
Required dev tools:
- JDK 7+
- Intellij Idea
- Android SDK

## Android set up:
1. Download commandline tools from https://developer.android.com/studio/index.html#downloads
2. Unpack it to somewhere like $HOME/Documents/android
3. Create an environment variable that points to the previous location by adding this line to .profile
    `export ANDROID_HOME=$HOME/Documents/android` (need to re-login for it to take effect)
4. Use $ANDROID_HOME/tools/android to download the latest platform, platform-tools and build-tools

## Import & run
Follow this guide for Intellij Idea to import and run the project:
https://github.com/libgdx/libgdx/wiki/Gradle-and-Intellij-IDEA

## Additional quirks:
- Make sure "Android Support" plugin is enabled for Intellij Idea
- Set the SDK for the android module to Android SDK in Project Structure...->Modules->android
- For your sanity don't use the Android emulator and run it on a real phone instead
- Make sure USB mode is set to file transfer when running on your android phone
- Set Java language level to 6 in project settings since that is what libgdx likes

# Project structure & guidelines
The game follows MVC and uses the command pattern to communicate between different components.
We follow Google's Java style guide for all Java files. Install the style guide for Intellij
then enable format code and optimize import on commit.

## Dependency injection
WHAT IF WE DONT CREATE NEW LEVELS?

## Big picture
Model is constructed by the data package.
The model is then used to create the view which attaches observers to the model to listen for changes.
Various controllers are also attached to the view during its creation.
These controllers holds a reference to their model objects and relays user input to be processed by the model.
The controllers are activated right before the screen is set to the view.

## Models
- Model classes should not reference controllers or views
- Receive commands via method invocation from controllers
- Notify state change to the view through the observable pattern
  - Pass details regarding the exact change through the argument of notifyObservers()
  - The argument should be concrete classes that the view can figure out
- "Live" collections should be returned as an iterable, this accidentally storing or changing the backing data
- "Snapshots" should be returned as an immutable collection

## Views
- Only views should hold references to assets
- Add observers to models
- Set up controllers upon view creation

## Controllers
- Listen to user input
- Issue commands to models

# Development plan
- MVC 
- path finding + moving
- creating enemy
- idea of player & enemy teams
- targeting + stub fighting
- idea of a turn
- dumb AI
- map UI overlay
- simple character detail viewer
- saving/loading map state
- create a simple level
- misc games stuff like start screen