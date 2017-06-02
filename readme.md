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

### Packages
- `android/`: Android launcher + packed and unpacked assets
- `desktop/`: Desktop launcher
- `core/`: Game code

### Note for Guava
#### Collections
Avoid using Guava's collection classes since they cannot be serialized by Kryo (without additional
work).

#### Optional
Poor man `ifPresent` lambda:
```
for (Object obj : optional.asSet()) {
  // run when optional is present 
}
```

#### EventBus
We use EventBus to communicate model events to the view. We should NOT use EventBus to communicate
between model components. This is due to the order sensitive nature of model logic. We also should
not use EventBus for things that can be cheaply polled per frame by the view.


### Note for serialization
We use Gson to deserialize initial game objects. We make use of the runtime type adapter from
gson/extra to add type information to any generic list of objects. Each concrete classes of the
generic types should be registered to in a `RuntimeTypeAdapterFactory`. Nulls are checked during
deserialization of Json objects. See `NullCheckAdapterFactory` for more information. Kryo is used
to serialize game state after initial data is loaded.

### Note for UI
We currently use a library called VisUI for development. It comes with its own skin files that are
loaded from classpath by invoking a static method. This means we need to enable headless environment
to be able to call that method.

## Models
- Model classes should self-contained, it should not reference libgdx, controller or views
- Receive commands via method invocation from controllers
- Changes in model are notified to interested components via the observer pattern using EventBus
- Only model should fire events

## Views
- Follows the Entity-Component design pattern
- DO NOT HOLD REFERENCE TO ENTITY AND COMPONENTS OUTSIDE OF ENGINE
  - We pool entity and components so holding reference outside of engine can have unintended effects
- Only views should hold references to assets
- Subscribes to various model events to update itself.
- Should not hold reference to model objects (i.e. world)
  - Only react to events from the model to initialize + update itself
- Should not change the model except to complete events

## Controllers
- Listen to user input
- Dispatches input to models
