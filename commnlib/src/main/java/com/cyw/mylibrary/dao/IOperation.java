package com.cyw.mylibrary.dao;

import java.util.ArrayList;

/**
 * author： cyw
 */
public abstract class IOperation<T> {
    public abstract boolean insetData(T t);
    public abstract boolean deleteData(T t);
    //public abstract boolean deleteById(int id);
    public abstract boolean updateData(T t);
    public abstract ArrayList<T> queryData();
    public abstract ArrayList<T> queryDataById(int id);
    public abstract ArrayList<T> queryDataByName(String name);
}
