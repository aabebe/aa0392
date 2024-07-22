package domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@Builder
public class RentalAgreement {
    private Tool tool;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private double dailyCharge;
    private int chargeDays;
    private double preDiscountCharge;
    private int discountPercent;
    private double discountAmount;
    private double finalCharge;

    public void printToolRentalAgreement () {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        DecimalFormat currencyFormatter = new DecimalFormat("$#,##0.00");
        DecimalFormat percentFormatter = new DecimalFormat("#0%");

        StringBuilder rentalAgreement = new StringBuilder();
        rentalAgreement.append("Tool code: ").append(tool.getCode()).append("\n");
        rentalAgreement.append("Tool type: ").append(tool.getType()).append("\n");
        rentalAgreement.append("Tool brand: ").append(tool.getBrand()).append("\n");
        rentalAgreement.append("Rental days: ").append(rentalDays).append("\n");
        rentalAgreement.append("Check out date: ").append(checkoutDate.format(dateFormatter)).append("\n");
        rentalAgreement.append("Due date: ").append(dueDate.format(dateFormatter)).append("\n");
        rentalAgreement.append("Daily rental charge: ").append(currencyFormatter.format(dailyCharge)).append("\n");
        rentalAgreement.append("Charge days: ").append(chargeDays).append("\n");
        rentalAgreement.append("Pre-discount charge: ").append(currencyFormatter.format(preDiscountCharge)).append("\n");
        rentalAgreement.append("Discount percent: ").append(percentFormatter.format(discountPercent/100.0)).append("\n");
        rentalAgreement.append("Discount amount: ").append(currencyFormatter.format(discountAmount)).append("\n");
        rentalAgreement.append("Final charge: ").append(currencyFormatter.format(finalCharge)).append("\n");

        System.out.println(rentalAgreement);

    }
}
