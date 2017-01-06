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

## Big picture
Data package dispatches an event with necessary information to set up the model and view.
The model and the view are initialized at the same time. Model changes are dispatched as
events to the view. Controllers are attached to the view which translate user input to model
input via visitor pattern. Controllers also listen for new model set up to be initialized.

## Models
- Model classes should self-contained, it should not reference libgdx, controller or views
- Receive commands via method invocation from controllers
- Changes in model are notified to interested components via the observer pattern using EventBus.
  Generally separated into the following three types:
  - Broadcast: events that alert subscribers about change in model state e.g. StateChanged
  - Command: events that request specific action to be performed e.g. RemoveCharacter
  - Request: events that tries to gather info from other components (no example yet)

## Views
- Only views should hold references to assets
- Glues model and controller together in the form of Actor
- Subscribes to various model events to update itself.

## Controllers
- Listen to user input
- Dispatches input to models via visitor pattern, this minimizes logic in controllers

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