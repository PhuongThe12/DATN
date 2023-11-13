package luckystore.datn.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TestRequest implements Serializable {

    List<String> data;

}
