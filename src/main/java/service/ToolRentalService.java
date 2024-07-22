package service;

import domain.RentalAgreement;
import domain.Tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

public class ToolRentalService {
    private final Map<String, Tool> toolInventory;
    private static final double MAX_DISCOUNT =100.0;
    private static final double MIN_DISCOUNT =0.0;

    public ToolRentalService(Map<String, Tool> toolInventory) {
        this.toolInventory = toolInventory;
    }

    public RentalAgreement checkout (String toolCode, int rentalDays, int discountPercent,
                                     LocalDate checkoutDate) {
        if(rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if(discountPercent < MIN_DISCOUNT || discountPercent > MAX_DISCOUNT) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        Tool tool = toolInventory.get(toolCode);
        if(tool == null) {
            throw new IllegalArgumentException("Invalid tool code;");
        }

        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        int chargeDays = calculateChargeDays(tool, checkoutDate, dueDate);
        BigDecimal dailyCharge = BigDecimal.valueOf(tool.getDailyRentalCharge());
        BigDecimal preDiscountCharge = dailyCharge.multiply(BigDecimal.valueOf(chargeDays));
        BigDecimal discountPercentRounded = BigDecimal.valueOf(discountPercent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal discountAmount = preDiscountCharge
                .multiply(discountPercentRounded).setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalCharge = preDiscountCharge
                .subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);


        return RentalAgreement.builder()
                .tool(tool)
                .rentalDays(rentalDays)
                .checkoutDate(checkoutDate)
                .dueDate(dueDate)
                .dailyCharge(dailyCharge.doubleValue())
                .chargeDays(chargeDays)
                .preDiscountCharge(preDiscountCharge.doubleValue())
                .discountPercent(discountPercent)
                .discountAmount(discountAmount.doubleValue())
                .finalCharge(finalCharge.doubleValue()).build();
    }

    private int calculateChargeDays(Tool tool, LocalDate checkoutDate, LocalDate dueDate) {
        int chargeDays = 0;
        for(LocalDate date = checkoutDate.plusDays(1); !date.isAfter(dueDate);
        date = date.plusDays(1)){
            boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.SUNDAY;

            boolean isHoliday = isHoliday(date);

            if((isWeekend && tool.isWeekendRentalCharge()) ||
                    (!isWeekend && !isHoliday && tool.isWeekdayRentalCharge()) ||
            (isHoliday && tool.isHolidayRentalCharge())) {
                chargeDays++;
            }
        }
        return chargeDays;
    }

    private boolean isHoliday(LocalDate date) {
        int year = date.getYear();
        LocalDate independenceDay = LocalDate.of(year, 7, 4);
        if (independenceDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
            independenceDay = independenceDay.minusDays(1);
        } else if (independenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            independenceDay = independenceDay.plusDays(1);
        }

        LocalDate laborDay = LocalDate.of(year, 9, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

        return date.equals(independenceDay) || date.equals(laborDay);
    }
}
