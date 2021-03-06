[![Build Status](https://travis-ci.org/jingyuyao/tactical-adventure.svg?branch=master)](https://travis-ci.org/jingyuyao/tactical-adventure)

# Set up
Required dev tools:
- JDK 7+
- Android SDK
- Tiled
- Piskel

## Android set up:
1. Download commandline tools from https://developer.android.com/studio/index.html#downloads
2. Unpack it to somewhere like /opt/android
3. Create an environment variable that points to the previous location by adding this line to `/etc/environment`
    `ANDROID_HOME="/opt/android"` (need to re-login for it to take effect)
4. Use /opt/android/tools/bin/sdkmanager to download the following tools

```
sudo ./sdkmanager "platforms;android-27"
sudo ./sdkmanager "platforms-tools"
sudo ./sdkmanager "build-tools;27.0.3"
```

## Run and Test
IDEs are unreliable as shit breaks consistently between versions. We will rely purely on Gradle
commands to build, run and test our code. Shortcuts to these Gradle commands can be added to your
IDE but we should not rely on their build system at all. For more information see:
https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline#running-the-android-project

- Running desktop: `./gradlew desktop:run`
- Running android: `./gradlew android:installDebug android:run`
- Testing: `./gradlew core:cleanTest core:test`
- Packaging desktop: `./gradlew desktop:dist`
- Packaging android: `./gradlew android:assembleRelease`

## Intellij quirks 
- Make sure "Android Support" plugin is enabled for Intellij Idea
- Make sure the Android module uses the Android SDK in Project Structure...->Modules->android
- For your sanity don't use the Android emulator and run it on a real phone instead
- Make sure USB mode is set to file transfer when running on your android phone
- Make sure Java language level is 7 in project settings since 8 isn't fully supported by Android

# Game scripting & map editing
The game code is designed so that new levels can be added without the need to change any Java code.
Level data are located within `android/assets/data/{#}/` and `android/assets/i18n/model/script/dialogue/Level{#}`.
Levels are composed of four data files: `script.conf, world.conf, terrains.json and Level#.properties`.
`script.conf` contains the win/lose conditions and various ship spawn/removal events. See
`com.jingyuyao.tactical.model.script` package for all the available script logic. `world.conf` contains
the initial ship data and their position in the level, player spawn coordinates, and any inactive
ships that can be spawned by the script later. `terrains.json` is generated by Tiled and contains a
grid map of the level. `Level#.properties` contains a turn by turn dialogue trigger. See
`com.jingyuyao.tactical.data.DialogueLoader` on how the keys are parsed.

# Assets
The game contains two primary graphic assets: tilesets and textures. Tilesets are used to render
terrains and textures are used to render everything else (e.g. animations and overlays). Textures
are created from numerically labelled image files in the `assets_raw` folder. These texture/animation
files can be generated from a tool such as Piskel. After these "raw" assets are generated and placed
in the `assets_raw` folder they need to be packed using the `TexturePackerRunner` class into a
format used by libGdx.

# Project structure & guidelines
The game follows MVC and uses Guice to share objects between components. We follow Google's Java
style guide for all Java files.

### Packages
- `android/`: Android launcher + packed and unpacked assets
- `desktop/`: Desktop launcher
- `core/`: Game code

## Guava
### Collections
Avoid using Guava's collection classes since they cannot be serialized by Kryo (without additional
work).

### EventBus
We use EventBus to communicate model events to the view. We should NOT use EventBus to communicate
between model components. This is due to the order sensitive nature of model logic. We also should
not use EventBus for things that can be cheaply polled per frame by the view.

## Serialization
Our initial game data are saved in the Hocon format: https://github.com/lightbend/config/blob/master/HOCON.md
These data files are deserialized using Gson to create the initial game objects. We make use of the
runtime type adapter from gson/extra to add type information to any generic list of objects. Each
concrete classes of the generic types should be registered to in a `RuntimeTypeAdapterFactory`.
Nulls are checked during deserialization of Json objects. See `NullCheckAdapterFactory` for more
information. Kryo is used to serialize game state after initial data is loaded.

## UI
We currently use a library called VisUI for development. It comes with its own skin files that are
loaded from classpath by invoking a static method. This means we need to enable headless environment
to be able to call that method during test.

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
