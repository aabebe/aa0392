package service;

import domain.RentalAgreement;
import domain.Tool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ToolRentalServiceTest {
    private ToolRentalService toolRentalService;

    @BeforeEach
    public void setUp() {
        Map<String, Tool> toolInventory = new HashMap<>();
        toolInventory.put("CHNS", Tool.builder()
                .code("CHNS")
                .type("Chainsaw")
                .brand("Stihl")
                .DailyRentalCharge(1.49)
                .isWeekdayRentalCharge(true)
                .isWeekendRentalCharge(false)
                .isHolidayRentalCharge(true).build());
        toolInventory.put("LADW", Tool.builder()
                .code("LADW")
                .type("Ladder")
                .brand("Werner")
                .DailyRentalCharge(1.99)
                .isWeekdayRentalCharge(true)
                .isWeekendRentalCharge(true)
                .isHolidayRentalCharge(false).build());
        toolInventory.put("JAKD", Tool.builder()
                .code("JAKD")
                .type("Jackhammer")
                .brand("DeWalt")
                .DailyRentalCharge(2.99)
                .isWeekdayRentalCharge(true)
                .isWeekendRentalCharge(false)
                .isHolidayRentalCharge(false).build());
        toolInventory.put("JAKR", Tool.builder()
                .code("JAKR")
                .type("Jackhammer")
                .brand("Ridgid")
                .DailyRentalCharge(2.99)
                .isWeekdayRentalCharge(true)
                .isWeekendRentalCharge(false)
                .isHolidayRentalCharge(false).build());

        toolRentalService = new ToolRentalService(toolInventory);
    }

    @Test
    public void testInvalidDiscountPercent() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            toolRentalService.checkout("JAKR", 5, 101, LocalDate.of(2015, 9,3));
        });

        assertEquals("Discount percent must be between 0 and 100.", ex.getMessage());
    }

    @Test
    public void testCheckoutScenario2(){
        RentalAgreement rentalAgreement = toolRentalService.checkout("LADW", 3, 10, LocalDate.of(2020, 7,2));
        assertEquals("LADW", rentalAgreement.getTool().getCode());
        assertEquals(LocalDate.of(2020, 7, 2), rentalAgreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 5), rentalAgreement.getDueDate());
        assertEquals(1.99, rentalAgreement.getDailyCharge());
        assertEquals(2, rentalAgreement.getChargeDays());
        assertEquals(3.98, rentalAgreement.getPreDiscountCharge());
        assertEquals(10, rentalAgreement.getDiscountPercent());
        assertEquals(0.40, rentalAgreement.getDiscountAmount());
        assertEquals(3.58, rentalAgreement.getFinalCharge());

        rentalAgreement.printToolRentalAgreement();
    }

    @Test
    public void testCheckoutScenario3(){
        RentalAgreement rentalAgreement = toolRentalService.checkout("CHNS", 5, 25, LocalDate.of(2015, 7,2));
        assertEquals("CHNS", rentalAgreement.getTool().getCode());
        assertEquals(LocalDate.of(2015, 7, 2), rentalAgreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 7), rentalAgreement.getDueDate());
        assertEquals(1.49, rentalAgreement.getDailyCharge());
        assertEquals(3, rentalAgreement.getChargeDays());
        assertEquals(4.47, rentalAgreement.getPreDiscountCharge());
        assertEquals(25, rentalAgreement.getDiscountPercent());
        assertEquals(1.12, rentalAgreement.getDiscountAmount());
        assertEquals(3.35, rentalAgreement.getFinalCharge());

        rentalAgreement.printToolRentalAgreement();
    }
    @Test
    public void testCheckoutScenario4(){
        RentalAgreement rentalAgreement = toolRentalService.checkout("JAKD", 6, 0, LocalDate.of(2015, 9,3));
        assertEquals("JAKD", rentalAgreement.getTool().getCode());
        assertEquals(LocalDate.of(2015, 9, 3), rentalAgreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 9, 9), rentalAgreement.getDueDate());
        assertEquals(2.99, rentalAgreement.getDailyCharge());
        assertEquals(3, rentalAgreement.getChargeDays());
        assertEquals(8.97, rentalAgreement.getPreDiscountCharge());
        assertEquals(0, rentalAgreement.getDiscountPercent());
        assertEquals(0.00, rentalAgreement.getDiscountAmount());
        assertEquals(8.97, rentalAgreement.getFinalCharge());

        rentalAgreement.printToolRentalAgreement();
    }
    @Test
    public void testCheckoutScenario5(){
        RentalAgreement rentalAgreement = toolRentalService.checkout("JAKR", 9, 0, LocalDate.of(2015, 7,2));
        assertEquals("JAKR", rentalAgreement.getTool().getCode());
        assertEquals(LocalDate.of(2015, 7, 2), rentalAgreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 11), rentalAgreement.getDueDate());
        assertEquals(2.99, rentalAgreement.getDailyCharge());
        assertEquals(5, rentalAgreement.getChargeDays());
        assertEquals(14.95, rentalAgreement.getPreDiscountCharge());
        assertEquals(0, rentalAgreement.getDiscountPercent());
        assertEquals(0.00, rentalAgreement.getDiscountAmount());
        assertEquals(14.95, rentalAgreement.getFinalCharge());

        rentalAgreement.printToolRentalAgreement();
    }
    @Test
    public void testCheckoutScenario6(){
        RentalAgreement rentalAgreement = toolRentalService.checkout("JAKR", 4, 50, LocalDate.of(2020, 7,2));
        assertEquals("JAKR", rentalAgreement.getTool().getCode());
        assertEquals(LocalDate.of(2020, 7, 2), rentalAgreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 6), rentalAgreement.getDueDate());
        assertEquals(2.99, rentalAgreement.getDailyCharge());
        assertEquals(1, rentalAgreement.getChargeDays());
        assertEquals(2.99, rentalAgreement.getPreDiscountCharge());
        assertEquals(50, rentalAgreement.getDiscountPercent());
        assertEquals(1.50, rentalAgreement.getDiscountAmount());
        assertEquals(1.49, rentalAgreement.getFinalCharge());

        rentalAgreement.printToolRentalAgreement();
    }
}
