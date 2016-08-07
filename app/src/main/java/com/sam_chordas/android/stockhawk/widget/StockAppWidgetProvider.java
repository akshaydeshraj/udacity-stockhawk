package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;
import com.sam_chordas.android.stockhawk.ui.StockGraphActivity;

/**
 * @author akshay
 * @since 2/8/16
 */

public class StockAppWidgetProvider extends AppWidgetProvider {

    public static RemoteViews getRemoteView(Context context, AppWidgetManager appWidgetManager) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stockhawk_appwidget);
        views.setTextViewText(R.id.widget_text, context.getString(R.string.appwidget_text));
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, StockAppRemoteViewsService.class));

        //Intent to start MyStocksActivity
        Intent stocksActivityIntent = new Intent(context, MyStocksActivity.class);
        PendingIntent stocksActivityPendingIntent = PendingIntent.getActivity(context, 0,
                stocksActivityIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_text, stocksActivityPendingIntent);

        //Intent to start StockGraphActivity
        Intent stockGraphActivityIntent = new Intent(context, StockGraphActivity.class);
        PendingIntent stockGraphActivityPendingIntent = PendingIntent.getActivity(context, 0,
                stockGraphActivityIntent, 0);
        views.setPendingIntentTemplate(R.id.widget_list, stockGraphActivityPendingIntent);

        return views;
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        appWidgetManager.updateAppWidget(appWidgetId, getRemoteView(context, appWidgetManager));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}