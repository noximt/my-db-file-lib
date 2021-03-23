package my.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Column implements Serializable {
    private static final long serialVersionUID = -5404915469632594699L;
    private List<Object> object;

    @Override
    public String toString() {
        return "Column{" +
                "object=" + object +
                '}';
    }
}
