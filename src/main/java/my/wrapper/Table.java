package my.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Table implements Serializable {
    private static final long serialVersionUID = 1110659918049323646L;

    private String tableName;
    private Map<String, Column> columns;
    private String[] columnNames;
    private int index;
    private int count;
    private long id;

    public void save(Object ob) {
        try {
            Class<?> aClass = ob.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                String name = declaredField.getName();
                Column col = columns.get(name);
                if (name.equalsIgnoreCase("id")) {
                    col.getObject().add(index, autoInc());
                    continue;
                }
                declaredField.setAccessible(true);
                col.getObject().add(index, declaredField.get(ob));
            }
            index++;
            count++;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void updateName(String tableName) {
        this.tableName = tableName;
    }

    public void updateColumnName(String oldColumnName, String newColumnName) {
        String[] columnNames = this.columnNames;
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(oldColumnName)) {
                columnNames[i] = newColumnName;
                break;
            }
        }
        Map<String, Column> columns = this.columns;
        Column column = columns.get(oldColumnName);
        columns.remove(oldColumnName);
        columns.put(newColumnName, column);
    }

    public void addColumn(String columnName) {
        String[] strings = Arrays.copyOf(columnNames, columnNames.length + 1);
        strings[strings.length - 1] = columnName;
        this.columnNames = strings;
    }

    public void removeColumn(String columnName) {
        if (columnName.equalsIgnoreCase("id")){
            throw new Error();
        }
        String[] columnNames = this.columnNames;
        String[] newColumnNames = new String[columnNames.length - 1];
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(columnName)) {
                columnNames[i] = null;
            }
        }
        for (int i = 0; i < newColumnNames.length; i++) {
            if (columnNames[i] == null){
                continue;
            }
            newColumnNames[i] = columnNames[i];
        }
        this.columnNames = newColumnNames;
        columns.remove(columnName);
    }

    public List<Row> getAllRows() {
        ArrayList<Row> objects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Map<String, Object> forRow = new HashMap<>();
            for (String columnName : columnNames) {
                Column column = columns.get(columnName);
                forRow.put(columnName, column.getObject().get(i));
            }
            Row row = new Row();
            row.setColumns(forRow);
            objects.add(row);
        }
        objects.sort(new RowComparator());
        return objects;
    }

    public void deleteById(long id) {
        List<Row> allRows = getAllRows();
        Row row = new Row();
        row = findRowById(id, allRows, row);
        for (String columnName : columnNames) {
            columns.get(columnName).getObject().remove(row.getColumns().get(columnName));
        }
        count--;
    }

    private long autoInc() {
        return id++;
    }

    private Row findRowById(long id, List<Row> allRows, Row row) {
        for (Row ro : allRows) {
            long id1 = (long) ro.getColumns().get("id");
            if (id1 == id) {
                row = ro;
            }
        }
        return row;
    }

    @Override
    public String toString() {
        return "tableName='" + tableName + '\'' +
                ", columns=" + columns +
                '}';
    }
}
