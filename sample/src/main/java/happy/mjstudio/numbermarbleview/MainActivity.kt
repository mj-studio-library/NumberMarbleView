package happy.mjstudio.numbermarbleview

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Marble Touch Listener
        numberMarbleView.setOnMarbleTouchListener {
            Toast.makeText(this,"you clicked $it marble!",Toast.LENGTH_SHORT).show()
        }

        //Custom Color, Default = Random
        numberMarbleView.setMarbleColor(0, Color.RED)
    }
}
