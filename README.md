# NumberMarbleView


<img src="https://github.com/mym0404/NumberMarbleView/blob/master/sampleimage/sample.gif" width = 300>


----

## Usage

### xml

```xml
<happy.mjstudio.widget.numbermarbleview.NumberMarbleView
    android:id="@+id/numberMarbleView"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintBottom_toBottomOf="parent"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"

    app:marble_marble_count="5"
    app:marble_marble_radius="10dp"
    app:marble_line_color="#000"
    app:marble_line_length="12dp"
    app:marble_line_width="2dp"
    app:marble_number_text_color="#fff"
    app:marble_number_text_shadow_color="#f00"
    app:marble_number_text_size="12sp"
    app:marble_orientation_vertical="false"
    />
```

### code

```kotlin
//Marble Touch Listener
numberMarbleView.setOnMarbleTouchListener {
    Toast.makeText(this,"you clicked $it marble!",Toast.LENGTH_SHORT).show()
}

//Custom Color at position, Default = Random
numberMarbleView.setMarbleColor(0, Color.RED)

//Set Marble Count
numberMarbleView.marbleCount = 12

//Set Marble Radius to 12DP
numberMarbleView.marbleRadius = resources.displayMetrics.density * 12

//Set Line Color to RED
numberMarbleView.lineColor = Color.RED

//Set Line Length to 12DP
numberMarbleView.lineLength = resources.displayMetrics.density * 12

//Set Number Text Color to WHITE
numberMarbleView.numberTextColor = Color.WHITE

//set Number Text Shadow Color to BLACK
numberMarbleView.numberTextShadowColor = Color.BLACK

//set Number Text Size to 12SP
numberMarbleView.numberTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,12f,resources.displayMetrics)

//Set Marble Orientation to VERTICAL
numberMarbleView.isVertical = true
```

----

## Download

### Project-level build.gradle

```gradle
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://mjstudio.bintray.com/MJStudio"
        }
    }
}
```

### Module-level build.gradle

```gradle
implementation 'mjstudio:NumberMarbleView:1.0'
```

----

Thank you
