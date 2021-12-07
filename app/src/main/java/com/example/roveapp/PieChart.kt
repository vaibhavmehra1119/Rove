package com.example.roveapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class PieChart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)

        val pieChart=findViewById<PieChart>(R.id.pieChart)
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(30f,"Level 1"))
        entries.add(PieEntry(20f, "Level 2"))
        entries.add(PieEntry(50f, "Level 3"))

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = PieData(pieDataSet)
        pieChart.data = data
        pieChart.centerText = "Crime Level Probability"
        pieChart.animate()
    }
}