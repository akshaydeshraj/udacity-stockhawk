package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;

/**
 * @author akshay
 * @since 2/8/16
 */

public class StockGraphActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String STOCK_SYMBOL = "stock_symbol";
    public static final String ACTIVITY_SOURCE = "activity_source";

    private static final int SINGLE_STOCK_LOADER = 100;

    private String symbol;
    private LineChart lineChart;

    public static Intent getStockGraphActivityIntent(Context context, String symbol) {
        Intent intent = new Intent(context, StockGraphActivity.class);
        intent.putExtra(StockGraphActivity.STOCK_SYMBOL, symbol);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        symbol = getIntent().getExtras().getString(STOCK_SYMBOL);
        getLoaderManager().initLoader(SINGLE_STOCK_LOADER, null, this);

        lineChart = (LineChart) findViewById(R.id.line_chart);
        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.setDescription("Stock Price variation for " + symbol);
        lineChart.setDescriptionTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light.ttf"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(SINGLE_STOCK_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns.BIDPRICE},
                QuoteColumns.SYMBOL + " = ?",
                new String[]{symbol},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("Cursor", DatabaseUtils.dumpCursorToString(data));

        LineDataSet stockPriceSet = new LineDataSet(getPriceArrayList(data), symbol);
        stockPriceSet.setDrawValues(false);
        stockPriceSet.setDrawCircles(false);
        stockPriceSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        stockPriceSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        stockPriceSet.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(stockPriceSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (getIntent().getExtras().getString(ACTIVITY_SOURCE) == null) {
            finish();
        } else {
            Intent intent = new Intent(this, MyStocksActivity.class);
            startActivity(intent);
        }
    }

    private ArrayList<Entry> getPriceArrayList(Cursor data) {

        ArrayList<Entry> stockPriceList = new ArrayList<>();

        int temp = 0;

        try {
            while (data.moveToNext()) {
                String price = data.getString(data.getColumnIndexOrThrow(QuoteColumns.BIDPRICE));
                Float amount = Float.parseFloat(price);
                Entry entry = new Entry(temp, amount);
                temp++;
                stockPriceList.add(entry);
            }
        } finally {
            data.close();
        }

        return stockPriceList;
    }
}
