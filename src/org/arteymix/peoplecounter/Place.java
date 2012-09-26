/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arteymix.peoplecounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author guillaume
 */
public class Place<T> implements TableModel {

    private HashMap<T, Boolean> mIndividuals = new HashMap<>();
    public HashSet<T> mMatchAgainst = new HashSet<>();

    private void updateListeners(int code) {
        final TableModelEvent event = new TableModelEvent(this, code);
        for (TableModelListener tml : mTableModelListener) {
            tml.tableChanged(event);
        }

    }
    private ArrayList<TableModelListener> mTableModelListener = new ArrayList<>();

    public void scan(T id) {
        if (!mMatchAgainst.contains(id)) {
            return;
        }

        if (!mIndividuals.containsKey(id)) {
            mIndividuals.put(id, true);
            updateListeners(TableModelEvent.INSERT);

        } else {
            // Reverse state+
            mIndividuals.put(id, !mIndividuals.get(id));
            updateListeners(TableModelEvent.UPDATE);


        }
    }

    public void scanIn(T id) {
        if (!mMatchAgainst.contains(id)) {
            return;
        }

        mIndividuals.put(id, true);
        updateListeners(TableModelEvent.UPDATE);

    }

    public void scanOut(T id) {
        if (!mMatchAgainst.contains(id)) {
            return;
        }

        mIndividuals.put(id, false);
        updateListeners(TableModelEvent.UPDATE);

    }

    public boolean isIn(T id) {

        return mIndividuals.containsKey(id) & mIndividuals.get(id);
    }

    public int getCount() {
        return mIndividuals.size();
    }

    public int getCountIn() {
        int count = 0;
        for (Boolean b : mIndividuals.values()) {
            if (b) {
                count++;
            }
        }
        return count;
    }

    public int getCountOut() {
        return getCount() - getCountIn();
    }

    ///////////////
    // Singleton stuff...
    private Place() {
    }

    public static Place instance() {
        return PlaceHolder.INSTANCE;
    }

    @Override
    public int getRowCount() {
        return getCount();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "ID";
        } else {
            return "State";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        } else {
            return Boolean.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        final T temp = (T) mIndividuals.keySet().toArray()[rowIndex];
        if (columnIndex == 0) {
            return temp;

        } else {
            return mIndividuals.get(temp);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        final T temp = (T) mIndividuals.keySet().toArray()[rowIndex];


        mIndividuals.put(temp, (Boolean) aValue);
        updateListeners(TableModelEvent.UPDATE);

    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        mTableModelListener.add(l);

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        mTableModelListener.remove(l);
    }

    private static class PlaceHolder {

        private static final Place INSTANCE = new Place();
    }
}
