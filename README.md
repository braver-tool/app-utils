# App Utils

# Description

Library provides commonly used sources in Android development.


# Getting Started

To add this library to your project, please follow below steps

Add this in your root `build.gradle` file (project level gradle file):

```gradle
allprojects {
    repositories {
        maven { url "https://www.jitpack.io" }
    }
}

buildscript {
    repositories {
        maven { url "https://www.jitpack.io" }
    }
}
```

Then, Add this in your root `build.gradle` file (app level gradle file):

  add implementation 'implementation 'com.github.braver-tool:app-utils:1.1.1' to your build.gradle dependencies block.

  for example:

  ```
  dependencies {
    implementation 'com.github.braver-tool:app-utils:1.1.1'
  }
  ```

You can find demo project code [here](https://github.com/braver-tool/app-utils)
