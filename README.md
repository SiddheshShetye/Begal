# Begal
Android library to get random dog images with caching fetched data in memory.

![MicrosoftTeams-image (2)](https://github.com/SiddheshShetye/Begal/assets/3103872/39670dcf-6106-4a1a-ae8f-0d342e384d30)

Download
--------
Download the latest AAR file from or grab via gradle
```kotlin
implementation(files("/libs/app-debug.aar"))
```

Usage
-----
Begal must be initialized before using any functionality
```kotlin
BegalInit.init(application, BegalConfig(enableMemCache, memCacheConfig)
```
Begal configurations
```kotlin
BegalConfig(true, MemCacheConfig(20))
```

to get new single image from network
```kotlin

Begal.getImage() { data:BegalEntityData<List<Data>> ->
}

```
to  getMultiple images from network
```kotlin
Begal.getImages(count) { data:BegalEntityData<List<Data>> ->

}
```

to get previous image (if memory cache enabled)
```kotlin
 Begal.getPreviousImage() { data:BegalEntityData<List<Data>> ->

}
```

to get next image (if memory cache enabled)
```kotlin
Begal.getNexImage() { data:BegalEntityData<List<Data>> ->

}
```

to get the current index
```kotlin
Begal.getCurrentIndex()
```

License
-------

    Copyright 2013 Siddhesh Shetye.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


