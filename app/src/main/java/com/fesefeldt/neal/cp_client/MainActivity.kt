package com.fesefeldt.neal.cp_client

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val firstColorRequestCode = 100
    private val secondColorRequestCode = 101
    private val intentReturnColor = "myDesign.intent.ACTION_COLOR"
    private var color1 = ColorRGB("rVal", "gVal", "bVal")
    private var color2 = ColorRGB("rVal", "gVal", "bVal")
    private var blendArr = intArrayOf(0,1,2)
    private var gotColor1 = false
    private var gotColor2 = false

    class ColorRGB(val rVal: String, val gVal: String, val bVal: String)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { this
            sendMessage(it, firstColorRequestCode)
        }

        button2.setOnClickListener { this
            sendMessage(it, secondColorRequestCode)
        }

        blendButton.setOnClickListener { this
            if(gotColor1 && gotColor2){
                textView3.visibility = View.VISIBLE
                seekBar.visibility = View.VISIBLE
                blendButton.visibility = View.INVISIBLE
            }
            else{
                Toast.makeText(this,"Please select 2 colors above before blending", Toast.LENGTH_SHORT).show()
                blendButton.visibility = View.VISIBLE
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                               fromUser: Boolean) {

                    blendArr = blendColors(color1, color2)
                    blenderWindow.setBackgroundColor(Color.rgb(blendArr[0], blendArr[1], blendArr[2]))

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    fun blendColors(color1: ColorRGB, color2: ColorRGB): IntArray {

        var tempArr = intArrayOf(0,1,2)
        var c1r = color1.rVal.toFloat() * ((100.00 - seekBar.progress.toFloat())/100.00)
        var c1g = color1.gVal.toFloat() * ((100.00 - seekBar.progress.toFloat())/100.00)
        var c1b = color1.bVal.toFloat() * ((100.00 - seekBar.progress.toFloat())/100.00)
        var c2r = color2.rVal.toFloat() * ((seekBar.progress.toFloat())/100.00)
        var c2g = color2.gVal.toFloat() * ((seekBar.progress.toFloat())/100.00)
        var c2b = color2.bVal.toFloat() * ((seekBar.progress.toFloat())/100.00)

        tempArr[0] = (c1r + c2r).toInt()
        tempArr[1] = (c1g + c2g).toInt()
        tempArr[2] = (c1b + c2b).toInt()

        return tempArr
    }

    fun sendMessage(view: View, i: Int){

        val intent = Intent(intentReturnColor)
        intent.addCategory(Intent.CATEGORY_DEFAULT)

        startActivityForResult(intent, i)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       // super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            firstColorRequestCode -> {
                when(resultCode){
                    68 ->{
                        val red = data!!.getIntExtra("Red Value", 255)
                        val green = data.getIntExtra("Green Value", 255)
                        val blue = data.getIntExtra("Blue Value", 255)

                        textView.setBackgroundColor(Color.rgb(red, green, blue))
                        color1 = ColorRGB(red.toString(), green.toString(), blue.toString())
                        gotColor1 = true

                    }
                }
            }

            secondColorRequestCode -> {
                when(resultCode){
                    68 -> {
                        val red = data!!.getIntExtra("Red Value", 255)
                        val green = data.getIntExtra("Green Value", 255)
                        val blue = data.getIntExtra("Blue Value", 255)

                        textView2.setBackgroundColor(Color.rgb(red, green, blue))
                        color2 = ColorRGB(red.toString(), green.toString(), blue.toString())
                        gotColor2 = true
                    }
                }
            }
        }
    }
}
