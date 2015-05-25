package pl.pbednarz.systemyrozproszone; /**
 * @author Piotr Bednarz
 * @date 25.05.2015
 */
import com.google.common.collect.Multiset;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ZipfTableModel extends AbstractTableModel {

    private List<Multiset.Entry> entries;

    public ZipfTableModel(List<Multiset.Entry> entries) {
        this.entries = entries;
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return entries.get(rowIndex).getElement();
            case 2:
                return entries.get(rowIndex).getCount();

        }
        return null;
    }
}
