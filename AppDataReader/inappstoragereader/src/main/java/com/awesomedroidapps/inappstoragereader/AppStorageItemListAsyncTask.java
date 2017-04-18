package com.awesomedroidapps.inappstoragereader;

import android.app.Activity;
import android.os.AsyncTask;

import com.awesomedroidapps.inappstoragereader.entities.AppDataStorageItem;
import com.awesomedroidapps.inappstoragereader.interfaces.ListDataView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by anshul on 1/3/17.
 *
 * A async task for fetching AppStorageItem.
 *
 */

public class AppStorageItemListAsyncTask extends AsyncTask<String, Void, List<AppDataStorageItem>> {

  private final WeakReference<Activity> activtyWeakReference;
  private final ListDataView listDataView;
  private final StorageType storageType;

  public AppStorageItemListAsyncTask(WeakReference activtyWeakReference,
                                     ListDataView listDataView, StorageType storageType) {
    this.activtyWeakReference = activtyWeakReference;
    this.listDataView = listDataView;
    this.storageType = storageType;
  }

  @Override
  protected List<AppDataStorageItem> doInBackground(String... params) {
    if (activtyWeakReference.get() != null) {
      switch (storageType) {
        case ALL:
          return AppDataReader.readAppDataStorageList(activtyWeakReference.get());
        case SHARED_PREFERENCE:
          return SharedPreferenceReader.getSharedPreferencesTags(activtyWeakReference.get());
        case TABLE:
          if (params == null) {
            return null;
          }
          String databaseName = params[Constants.ZERO_INDEX];
          if (Utils.isEmpty(databaseName)) {
            return null;
          }
          return SqliteDatabaseReader.readTablesList(activtyWeakReference.get(), databaseName);
      }
    }
    return null;
  }


  protected void onPostExecute(List<AppDataStorageItem> appDataList) {
    if (listDataView != null && activtyWeakReference.get() != null) {
      listDataView.onDataFetched(appDataList);
    }
  }
}
