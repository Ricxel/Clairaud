package com.omasba.clairaud.ui.components

import android.content.res.Configuration
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun PresetGraph(presetName:String, bands: ArrayList<Pair<Int, Short>>){
    val labelColor = MaterialTheme.colorScheme.primary.toArgb()
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        factory = { context ->
            LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                val entries = bands.map { Entry(it.first.toFloat(), it.second.toFloat()) }

                val dataSet = LineDataSet(entries, presetName).apply {
                    color = ColorTemplate.getHoloBlue()
                    lineWidth = 2f
                    circleRadius = 4f
                    setDrawValues(false)
                    setDrawCircles(true)
                    setDrawFilled(true)
                }
                // per y = 0
                val zeroDbLine = LimitLine(0f, "").apply {
                    lineColor = android.graphics.Color.MAGENTA
                    lineWidth = 2f
                    textSize = 12f
                    labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
                }

                axisLeft.addLimitLine(zeroDbLine)

                data = LineData(dataSet)
                description.isEnabled = false
                axisRight.isEnabled = false

                // asse X
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawLabels(true)
                xAxis.setDrawGridLines(false)
                xAxis.textColor = labelColor

                // asse Y
                axisLeft.setDrawLabels(true)
                axisLeft.setDrawGridLines(true)
                axisLeft.axisMinimum = -15f
                axisLeft.axisMaximum = 15f
                axisLeft.textColor = labelColor
                invalidate()
            }
        }
    )
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun PresetGraphPreview(){
    val sampleBands = arrayListOf(
        Pair<Int,Short>(60,4),
        Pair<Int,Short>(250,-2),
        Pair<Int,Short>(1000,1),
        Pair<Int,Short>(4000,3),
        Pair<Int,Short>(14000,4),
    )
    PresetGraph(presetName = "Jazz", bands = sampleBands)

}