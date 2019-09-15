package happy.mjstudio.numbermarbleview

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switch_orientation.setOnCheckedChangeListener { compoundButton, b ->
            numberMarbleView.isVertical = !b
        }

        seek_bar_marble_count.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                numberMarbleView.marbleCount = p1
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        seek_bar_line_length.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                numberMarbleView.lineLength = resources.displayMetrics.density * p1
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        //Marble Touch Listener
        numberMarbleView.setOnMarbleTouchListener {
            Toast.makeText(this, "you clicked $it marble!", Toast.LENGTH_SHORT).show()
        }

        //Custom Color at position, Default = Random
        numberMarbleView.setMarbleColor(0, Color.RED)

        //Set Marble Count
        numberMarbleView.marbleCount = 5

        //Set Marble Radius to 12DP
        numberMarbleView.marbleRadius = resources.displayMetrics.density * 12

        //Set Line Color to RED
        numberMarbleView.lineColor = Color.BLACK

        //Set Line Length to 12DP
        numberMarbleView.lineLength = resources.displayMetrics.density * 12

        //Set Number Text Color to WHITE
        numberMarbleView.numberTextColor = Color.WHITE

        //set Number Text Shadow Color to BLACK
        numberMarbleView.numberTextShadowColor = Color.BLACK

        //set Number Text Size to 12SP
        numberMarbleView.numberTextSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)

        //Set Marble Orientation to VERTICAL
        numberMarbleView.isVertical = true
    }
}
