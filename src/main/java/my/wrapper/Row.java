package my.wrapper;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Row implements Serializable {
    private static final long serialVersionUID = 5878848279317453973L;
    private Map<String, Object> columns;

    @Override
    public String toString() {
        return "Row "+" {" + columns +
                '}' + "\n";
    }
}
