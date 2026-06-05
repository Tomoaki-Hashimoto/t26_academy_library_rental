package jp.co.metateam.library.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.repository.RentalManageRepository;
import jp.co.metateam.library.repository.StockRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class RentalManageService {

    private final RentalManageRepository rentalRepository;
    private final StockRepository stockRepository;

    @Autowired
    public RentalManageService(RentalManageRepository rentalRepository, StockRepository stockRepository) {
        this.rentalRepository = rentalRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void save(RentalManageDto dto) {
        RentalManage entity = new RentalManage();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setExpectedRentalOn(dto.getExpectedRentalOn());
        entity.setExpectedReturnOn(dto.getExpectedReturnOn());
        entity.setStockId(dto.getStockId());
        entity.setStatus(dto.getStatus());

        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        this.rentalRepository.save(entity);
    }

    public List<RentalManage> findAll() {
        return rentalRepository.findAll();
    }

    public void validate(RentalManageDto dto, BindingResult bindingResult) {
        LocalDate today = LocalDate.now();

        if (dto.getStatus() != null && (dto.getStatus() == 2 || dto.getStatus() == 3)) {
            bindingResult.rejectValue("status", "error.status",
                    "貸出ステータスは「貸出待ち」もしくは「貸出中」を選択してください");
        }

        if (dto.getExpectedRentalOn() != null && dto.getExpectedReturnOn() != null && dto.getStatus() != null) {

            if (dto.getExpectedRentalOn().isAfter(dto.getExpectedReturnOn())) {
                bindingResult.rejectValue("expectedReturnOn", "error.date",
                        "返却予定日は貸出予定日以降の日付を入力してください");
            }

            if (dto.getStatus() == 1 && !dto.getExpectedRentalOn().isBefore(today.plusDays(1))) {
                bindingResult.rejectValue("status", "error.status",
                        "未来日付では「貸出待ち」を選択してください");
            }

            if (dto.getStatus() == 0 && dto.getExpectedRentalOn().isBefore(today.plusDays(1))) {
                bindingResult.rejectValue("status", "error.status",
                        "過去日付では「貸出中」を選択してください");
            }
        }

        if (!bindingResult.hasErrors() && dto.getStockId() != null) {

            if (!isStockAvailable(dto.getStockId())) {
                bindingResult.rejectValue("stockId", "error.stockId",
                        "選択した在庫は現在貸出できない状態です");
            }

            if (dto.getExpectedRentalOn() != null && dto.getExpectedReturnOn() != null) {
                if (isOverlapping(dto)) {
                    bindingResult.rejectValue("expectedRentalOn", "error.date",
                            "指定した期間は既存の期間と重複しています。");
                }
            }
        }
    }

    private boolean isStockAvailable(String stockId) {
        return stockRepository.findById(stockId)
                .map(stock -> stock.getStatus() == 0)
                .orElse(false);
    }

    private boolean isOverlapping(RentalManageDto dto) {
        List<RentalManage> list = rentalRepository.findOverlappingRentals(
                dto.getStockId(),
                dto.getExpectedRentalOn(),
                dto.getExpectedReturnOn());

        for (RentalManage existing : list) {
            if (existing.getExpectedRentalOn().isBefore(dto.getExpectedReturnOn()) &&
                    existing.getExpectedReturnOn().isAfter(dto.getExpectedRentalOn())) {
                return true;
            }
        }
        return false;
    }

    public RentalManage findById(Long id) {
        return rentalRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void update(Long id, RentalManageDto dto) throws Exception {
        try {
            RentalManage entity = rentalRepository.findById(id).orElse(null);
            if (entity == null) {
                throw new Exception("RentalManage record not found.");
            }

            entity.setEmployeeId(dto.getEmployeeId());
            entity.setExpectedRentalOn(dto.getExpectedRentalOn());
            entity.setExpectedReturnOn(dto.getExpectedReturnOn());
            entity.setStockId(dto.getStockId());
            entity.setStatus(dto.getStatus());
            entity.setUpdatedAt(LocalDateTime.now());

            rentalRepository.save(entity);
        } catch (Exception e) {
            throw e;
        }
    }
}