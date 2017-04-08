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
- Set Java language level to 7 in project settings since 8 isn't fully supported by Android

# Project structure & guidelines
The game follows MVC and uses Guice to share objects between components.
We follow Google's Java style guide for all Java files. Install the style guide for Intellij
then enable format code and optimize import on commit.

### Note for EventBus
We use EventBus to communicate model events to the view. We should NOT use EventBus to communicate
between model components. This is due to the order sensitive nature of model logic. We also should
not use EventBus for things that can be cheaply polled per frame by the view. Our EventBus' are also
scoped. We have ONE model scoped EventBus. Each character also have its own EventBus which is used
to send events to its associated actor.

### Note for serialization
We use Gson to serialize/deserialize game objects (except for terrains). We make use of the
runtime type adapter from gson/extra to add type information to our generic lists of characters
and items. Each concrete classes of the generic types should be registered to the corresponding
type adapter. Each concrete class that require Guice injection should also be registered in the
data module. The gson deserialization reduce the need for factories as we no longer need to create
instances by hand. All the data fields and injected by gson. Since gson requires an instance before
injections, all model objects either need a no-args constructor or a Guice injectable constructor
(with no assisted injections).

## Models
- Model classes should self-contained, it should not reference libgdx, controller or views
- Receive commands via method invocation from controllers
- Changes in model are notified to interested components via the observer pattern using EventBus
- Only model should fire events

## Views
- Follows the Entity-Component design pattern
- Only views should hold references to assets
- Subscribes to various model events to update itself.
- Should not change the model except to complete future events

## Controllers
- Listen to user input
- Dispatches input to models

## Data
- The game objects are dynamically saved and loaded with the class information intact
