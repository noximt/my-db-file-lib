package my.wrapper;

import java.io.*;
import java.util.*;

public class FileWrapper {
    private final String path = System.getProperty("user.home") + "/db/database.txt";
    private final String directory = System.getProperty("user.home") + "/db";
    private Wrapper wrapper;
    private Map<Object, Object[]> joins = new HashMap<>();
    

    public FileWrapper() {
        initCheck();
    }

    public void createTable(String tableName, String... columns) {
        wrapper.createTable(tableName, columns);
        flush();
    }

    public void updateTableName(String tableName, String newTableName) {
        Table table = wrapper.getTable(tableName);
        table.updateName(newTableName);
        flush();
    }

    public void updateColumnName(String tableName, String oldColumnName, String newColumnName) {
        Table table = wrapper.getTable(tableName);
        table.updateColumnName(oldColumnName, newColumnName);
        flush();
    }

    public void addColumn(String tableName, String columnName) {
        Table table = wrapper.getTable(tableName);
        table.addColumn(columnName);
        flush();
    }


    public void save(String table, Object ob) {
        Table table1 = wrapper.getTable(table);
        table1.save(ob);
        flush();
    }

    public String[] findAllColumns(String tableName) {
        Table table = wrapper.getTable(tableName);
        return table.getColumnNames();
    }

    public void removeColumn(String tableName, String columnName) {
        Table table = wrapper.getTable(tableName);
        table.removeColumn(columnName);
        flush();
    }

    public void saveList(String table, List<?> ob) {
        Table table1 = wrapper.getTable(table);
        for (Object o : ob) {
            table1.save(o);
        }
        flush();
    }

    public void updateInColumn(String tableName, String columnName, Object oldValue, Object newValue) {
        Table table = wrapper.getTable(tableName);
        Map<String, Column> columns = table.getColumns();
        Column column = columns.get(columnName);
        List<Object> object = column.getObject();
        for (int i = 0; i < object.size(); i++) {
            if (object.get(i).equals(oldValue)) {
                object.set(i, newValue);
            }
        }
        flush();
    }

    public void joinTables(String table, String joinTable, long id, long joinTableId) {
        Object byId = getById(table, id);
        Object byId1 = getById(joinTable, joinTableId);
        Object[] objectList = new Object[]{byId, byId1};
        joins.put(byId, objectList);
        flush();
    }

    public boolean deleteDB() {
        File file = new File(path);
        return file.delete();
    }

    public List<Object> getAllTables() {
        return Collections.singletonList(wrapper.getAllTables());
    }

    public void deleteById(String tableName, long id) {
        Table table = wrapper.getTable(tableName);
        table.deleteById(id);
        flush();
    }

    public void drop(String tableName) {
        wrapper.deleteTable(tableName);
        flush();
    }

    public List<Row> getAll(String table) {
        Table table1 = wrapper.getTable(table);
        return table1.getAllRows();
    }

    public Object getById(String tableName, long id) {
        Table table = wrapper.getTable(tableName);
        List<Row> allRows = table.getAllRows();
        for (Row row : allRows) {
            long id1 = (long) row.getColumns().get("id");
            if (id1 == id) {
                if (joins != null && joins.containsKey(row)){
                    return joins.get(row);
                }
                return row;
            }
        }
        throw new NoSuchElementException();
    }

    private void initCheck() {
        File file = new File(path);
        File dir = new File(directory);
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path));
                this.wrapper = (Wrapper) objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                dir.mkdir();
                file.createNewFile();
                Wrapper wrapper = new Wrapper(new HashMap<>());
                this.wrapper = wrapper;
                flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void flush() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path));
            objectOutputStream.writeObject(wrapper);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
