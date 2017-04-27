package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_ABSOLUTE_CHANGE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_HISTORY;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_PERCENTAGE_CHANGE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_PRICE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_SYMBOL;

/**
 * Created by KiMoo on 27/04/2017.
 */

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockRVFactory(this.getApplicationContext(), intent);
    }

    private class StockRVFactory implements RemoteViewsFactory {
        Context context;
        Cursor cursor;
        private int appWidgetId;

        public StockRVFactory(Context context, Intent intent) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            cursor = context.getContentResolver().query(Contract.Quote.URI,
                    new String[]{COLUMN_SYMBOL,
                            COLUMN_ABSOLUTE_CHANGE,
                            COLUMN_PERCENTAGE_CHANGE,
                            COLUMN_PRICE,
                            COLUMN_HISTORY}, null, null, COLUMN_SYMBOL);
        }

        @Override
        public void onDestroy() {
            if (this.cursor != null)
                this.cursor.close();
        }

        @Override
        public int getCount() {
            return (this.cursor != null) ? this.cursor.getCount() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(this.context.getPackageName(), R.layout.list_item_quote);

            if (this.cursor.moveToPosition(position)) {

                String symbol = cursor.getString(0);
                String absoluteChange = cursor.getString(1);
                String percentageChange = cursor.getString(2);
                String price = cursor.getString(3);
                String history = cursor.getString(4);

                remoteViews.setTextViewText(R.id.symbol, symbol);
                remoteViews.setTextViewText(R.id.price, "$"+price);
                remoteViews.setTextViewText(R.id.change,percentageChange);
                if(Float.parseFloat(percentageChange)<0){
                    remoteViews.setInt(R.id.change, "setBackgroundResource",
                            R.drawable.percent_change_pill_red);
                }
                else {
                    remoteViews.setInt(R.id.change, "setBackgroundResource",
                            R.drawable.percent_change_pill_green);
                }

                Bundle extras = new Bundle();
                extras.putString(StockWidget.selected_symbol, symbol);
                extras.putString(StockWidget.selected_history,history);

                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                remoteViews.setOnClickFillInIntent(R.id.quote_linear_layout, fillInIntent);

            }


            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
