package jp.co.metateam.library.model;

import java.time.LocalDateTime;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 貸出登録
 */
@Entity
@Table(name = "rental_manage")
public class RentalManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "expected_rental_on")
    private LocalDate expectedRentalOn;

    @Column(name = "expected_return_on")
    private LocalDate expectedReturnOn;

    @Column(name = "stock_id", nullable = false)
    private String stockId;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "rentaled_at")
    private LocalDate rentaledAt;

    @Column(name = "returned_at")
    private LocalDate returnedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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

    public LocalDate getRentaledAt() {
        return rentaledAt;
    }

    public LocalDate getReturnedAt() {
        return returnedAt;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public LocalDateTime GetCreatedAt() {
        return createdAt;
    }

    public LocalDateTime GetUpdatedAt() {
        return updatedAt;
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

    public void setRentaledAt(LocalDate rentaledAt) {
        this.rentaledAt = rentaledAt;
    }

    public void setReturnedAt(LocalDate returnedAt) {
        this.returnedAt = returnedAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}