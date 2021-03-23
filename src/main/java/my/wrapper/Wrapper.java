package my.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wrapper implements Serializable {
    private static final long serialVersionUID = -6085527809815570539L;
    private Map<String, Table> tables;

    public Table getTable(String table) {
        return tables.get(table);
    }

    public void createTable(String tableName, String... columns) {
        Map<String, Column> stringColumnMap = new HashMap<>();
        for (String column : columns) {
            stringColumnMap.put(column, new Column(new ArrayList<>()));
        }
        tables.put(tableName, new Table(tableName, stringColumnMap, columns, 0, 0, 1));
    }

    public void deleteTable(String tableName) {
        tables.remove(tableName);
    }

    public List<Table> getAllTables() {
        Collection<Table> values = tables.values();
        return new ArrayList<>(values);
    }
}
