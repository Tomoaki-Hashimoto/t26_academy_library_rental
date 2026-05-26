package jp.co.metateam.library.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RentalManageDto {

    /** 社員番号 */
    @NotBlank(message = "社員番号は必須です")
    @Size(max = 50)
    private String employeeId;

    /** 貸出予定日 */
    @NotNull(message = "貸出予定日は必須項目です")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedRentalOn;

    /** 返却予定日 */
    @NotNull(message = "返却予定日は必須項目です")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedReturnOn;

    /** 在庫管理番号 */
    @NotBlank(message = "在庫管理番号は必須項目です")
    @Size(max = 20)
    private String stockId;

    /** 貸出ステータス */
    @NotNull(message = "貸出ステータスの選択は必須項目です")
    private Integer status;

    /** Getters */

    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDate getExpectedRentalOn() {
        return expectedRentalOn;
    }

    public LocalDate getExpectedReturnOn() {
        return expectedReturnOn;
    }

    public String getStockId() {
        return stockId;
    }

    public Integer getStatus() {
        return status;
    }

    /** Setters */

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setExpectedRentalOn(LocalDate expectedRentalOn) {
        this.expectedRentalOn = expectedRentalOn;
    }

    public void setExpectedReturnOn(LocalDate expectedReturnOn) {
        this.expectedReturnOn = expectedReturnOn;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}