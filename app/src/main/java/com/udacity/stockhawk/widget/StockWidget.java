package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.GraphActivity;

/**
 * Implementation of App Widget functionality.
 */
public class StockWidget extends AppWidgetProvider {

    private static final String INTENT_ACTION ="com.udacity.stockhawk.widget.StockWidgetProvider.INTENT_ACTION" ;
    public static String selected_symbol="com.udacity.stockhawk.widget.StockWidgetProvider.EXTRA_SYMBOL";
    public static String selected_history="com.udacity.stockhawk.widget.StockWidgetProvider.EXTRA_HISTORY";

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        if(intent.getAction().equals(INTENT_ACTION)){

            String symbol = intent.getStringExtra(selected_symbol);
            String history = intent.getStringExtra(selected_history);

            Intent showHistoricalData = new Intent(context, GraphActivity.class);
            showHistoricalData.putExtra("symbol", symbol);
            showHistoricalData.putExtra("history", history);

            showHistoricalData.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(showHistoricalData);
        }
        
        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_widget);
        views.setRemoteAdapter(appWidgetId, R.id.stock_widget_list_view, intent);
        views.setEmptyView(R.id.stock_widget_list_view, R.id.tv_empty_stocks_widget_layout);


        Intent openSymbol = new Intent(context, StockWidget.class);
        openSymbol.setAction(StockWidget.INTENT_ACTION);
        openSymbol.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, openSymbol,
                PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.stock_widget_list_view, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

