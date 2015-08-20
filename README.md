# Pixelate
Android library to pixelate images

## Usage

Use the Pixelate imageview in your layout XML.

```XML
<nl.dionsegijn.pixelate
        android:id="@+id/pixelate"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_centerInParent="true"
        android:scaleType="centerCrop"
        android:src="your image.."/>
```

Call the pixelate method on the view to render the pixels with the preferred density
```Java
pixelate.pixelate(10);
```


Download
--------

Download via Gradle:
```groovy
compile 'nl.dionsegijn:pixelate:1.0.0'
```

License
-------

    Copyright 2015 Dion Segijn

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
