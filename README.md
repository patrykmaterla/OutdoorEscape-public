# OutdoorEscape

Class referenced in the layout file, com.google.android.gms.maps.SupportMapFragment, was not found in the project or the libraries
Unresolved class 'SupportMapFragment'
`android:name="com.google.android.gms.maps.SupportMapFragment"`

https://stackoverflow.com/questions/45692460/failed-to-resolve-com-google-android-gmsplay-services-in-intellij-idea-with-gr

Add this to your project-level build.gradle file:

```
repositories {
    maven {
        url "https://maven.google.com"
    }
}
```

---

Uporządkować navigation fragment i maps fragment jakos je zlaczyc w jedno calosc

---

    Rendering Problems
    A tag <fragment> allows a layout file to dynamically include different layouts at runtime. At layout editing time the specific layout to be used is not known. You can choose which layout you would like previewed while editing the layout...

This is just the preview window telling you that it can't display a preview for the <Fragment.../> tag, because it doesn't know what kind of fragment you're going to put in it. You can safely ignore that message - your actual app will render the fragment fine when you run it (as long as you code it up correctly!).

---

