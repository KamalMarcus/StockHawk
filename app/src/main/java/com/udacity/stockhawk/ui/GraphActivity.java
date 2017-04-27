package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udacity.stockhawk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        String symbol = getIntent().getStringExtra("symbol");
        setTitle(symbol);

        String history = getIntent().getStringExtra("history");
        ArrayList<String> historyList = new ArrayList<String>(Arrays.asList(history.split(",|\\n")));
        Collections.reverse(historyList);

        ArrayList<String> dates = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();

        for (String entry : historyList) {
            if (entry.contains(".")) { //Then it's a value not date
                values.add(entry);
            } else { //Otherwise, it's a date
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(entry));
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
                dates.add(formattedDate);
            }
        }

        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            entries.add(new Entry(Float.parseFloat(values.get(i)), i));
        }

        LineDataSet dataset = new LineDataSet(entries, getString(R.string.graph_description));
        LineData data = new LineData(dates, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        lineChart.setData(data);
        lineChart.animateY(5000);
    }
}
