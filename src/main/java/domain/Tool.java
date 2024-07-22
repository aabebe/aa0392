package domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Tool {
    private String code;
    private String type;
    private String brand;
    private boolean isHolidayRentalCharge;
    private double DailyRentalCharge;
    private boolean isWeekdayRentalCharge;
    private boolean isWeekendRentalCharge;
}
