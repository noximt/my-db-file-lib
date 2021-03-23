package my.wrapper;

import java.util.Comparator;

public class RowComparator implements Comparator<Row> {
    @Override
    public int compare(Row o1, Row o2) {
        Long id = (Long) o1.getColumns().get("id");
        Long aLong = (Long) o2.getColumns().get("id");
        return Long.compare(id, aLong);
    }
}
