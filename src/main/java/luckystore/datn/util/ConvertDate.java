package luckystore.datn.util;

import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Component
public class ConvertDate {
    public static Date convertStringToSQLDate(String stringDate) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = dateFormat.parse(stringDate);
            return new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
