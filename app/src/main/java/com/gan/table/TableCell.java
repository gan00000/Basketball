package com.gan.table;

public class TableCell {


    private TableCellType tableCellType = TableCellType.TableCellTypeString;

    private String data;
    private int width;
    private int height;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public TableCellType getTableCellType() {
        return tableCellType;
    }

    public void setTableCellType(TableCellType tableCellType) {
        this.tableCellType = tableCellType;
    }
}
