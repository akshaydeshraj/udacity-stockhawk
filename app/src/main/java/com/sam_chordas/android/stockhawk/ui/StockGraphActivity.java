package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * @author akshay
 * @since 2/8/16
 */

public class StockGraphActivity extends AppCompatActivity {

    private static final String STOCK_SYMBOL = "stock_symbol";

    public static Intent getStockGraphActivityIntent(Context context, String symbol) {
        Intent intent = new Intent(context, StockGraphActivity.class);
        intent.putExtra(StockGraphActivity.STOCK_SYMBOL, symbol);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String symbol = getIntent().getExtras().getString(STOCK_SYMBOL);
        Toast.makeText(this, symbol, Toast.LENGTH_SHORT).show();
    }
}
