package luckystore.datn.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component
public class ConvertDateToLong {

    private long currentTimeMillis = System.currentTimeMillis();
    private java.sql.Date currentDate = new java.sql.Date(currentTimeMillis);
    public Long dateToLong(String date) {
        long milliseconds = -1;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setTimeZone(TimeZone.getDefault());
        try {
            Date d = format.parse(date);
            milliseconds = d.getTime();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return milliseconds;
    }


    public String longToDate(Long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    public Long getStartOfToday() {
        // Tạo đối tượng Calendar và đặt ngày là hôm nay
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeZone(TimeZone.getTimeZone("UTC")); // Để tránh ảnh hưởng của múi giờ
        calendarStart.setTime(currentDate);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);

        // Lấy thời điểm đầu hôm nay dưới dạng currentTimeMillis
        return calendarStart.getTimeInMillis();
    }

    public Long getEndOfToday() {
        // Tạo đối tượng Calendar và đặt ngày là hôm nay
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeZone(TimeZone.getTimeZone("UTC")); // Để tránh ảnh hưởng của múi giờ
        calendarEnd.setTime(currentDate);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);

        // Lấy thời điểm cuối hôm nay dưới dạng currentTimeMillis
        return calendarEnd.getTimeInMillis();
    }

    public Long getStartMonth() {
        // Tạo đối tượng Calendar và đặt ngày trong tháng thành 1
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeZone(TimeZone.getTimeZone("UTC")); // Để tránh ảnh hưởng của múi giờ
        calendarStart.setTime(currentDate);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);

        // Lấy thời điểm đầu tháng dưới dạng currentTimeMillis
        return calendarStart.getTimeInMillis();
    }

    public Long getEndMonth() {
        // Tạo đối tượng Calendar và đặt ngày trong tháng thành ngày cuối cùng
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeZone(TimeZone.getTimeZone("UTC")); // Để tránh ảnh hưởng của múi giờ
        calendarEnd.setTime(currentDate);
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);

        // Lấy thời điểm cuối tháng dưới dạng currentTimeMillis
        return calendarEnd.getTimeInMillis();
    }

    public Long getStartOfYear() {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarStart.setTime(currentDate);
        calendarStart.set(Calendar.MONTH, Calendar.JANUARY);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);

        return calendarStart.getTimeInMillis();
    }

    public Long getEndOfYear() {
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarEnd.setTime(currentDate);
        calendarEnd.set(Calendar.MONTH, Calendar.DECEMBER);
        calendarEnd.set(Calendar.DAY_OF_MONTH, 31);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);

        return calendarEnd.getTimeInMillis();
    }

    public Long getStartOfYesterday() {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarStart.setTime(currentDate);
        calendarStart.add(Calendar.DAY_OF_MONTH, -1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);

        return calendarStart.getTimeInMillis();
    }

    public Long getEndOfYesterday() {
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarEnd.setTime(currentDate);
        calendarEnd.add(Calendar.DAY_OF_MONTH, -1);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);

        return calendarEnd.getTimeInMillis();
    }
    public Long getStartPreviousMonth() {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarStart.setTime(currentDate);
        calendarStart.add(Calendar.MONTH, -1);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);

        return calendarStart.getTimeInMillis();
    }
    public Long getEndPreviousMonth() {
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarEnd.setTime(currentDate);
        calendarEnd.add(Calendar.MONTH, -1);
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);

        return calendarEnd.getTimeInMillis();
    }

    public Long getStartOfPreviousYear() {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarStart.setTime(currentDate);
        calendarStart.add(Calendar.YEAR, -1);
        calendarStart.set(Calendar.MONTH, Calendar.JANUARY);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);

        return calendarStart.getTimeInMillis();
    }

    public Long getEndOfPreviousYear() {
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarEnd.setTime(currentDate);
        calendarEnd.add(Calendar.YEAR, -1);
        calendarEnd.set(Calendar.MONTH, Calendar.DECEMBER);
        calendarEnd.set(Calendar.DAY_OF_MONTH, 31);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);

        return calendarEnd.getTimeInMillis();
    }


}
