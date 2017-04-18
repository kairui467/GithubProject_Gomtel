package com.awesomedroidapps.inappstoragereader.entities;

import java.util.List;

/**
 * Created by anshul on 1/3/17.
 */

public class TableDataResponse {

  private int recyclerViewWidth;
  private List<Integer> recyclerViewColumnsWidth;
  private List<List<String>> tableData;

  public int getRecyclerViewWidth() {
    return recyclerViewWidth;
  }

  public void setRecyclerViewWidth(int recyclerViewWidth) {
    this.recyclerViewWidth = recyclerViewWidth;
  }

  public List<Integer> getRecyclerViewColumnsWidth() {
    return recyclerViewColumnsWidth;
  }

  public void setRecyclerViewColumnsWidth(List<Integer> recyclerViewColumnsWidth) {
    this.recyclerViewColumnsWidth = recyclerViewColumnsWidth;
  }

  public List<List<String>> getTableData() {
    return tableData;
  }

  public void setTableData(List<List<String>> tableData) {
    this.tableData = tableData;
  }
}
