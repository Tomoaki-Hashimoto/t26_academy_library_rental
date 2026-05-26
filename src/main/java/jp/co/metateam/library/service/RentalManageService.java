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

    public void validate(RentalManageDto dto, BindingResult bindingResult) {
        LocalDate today = LocalDate.now();

        // ステータスチェック（返却済み・キャンセル不可）
        if (dto.getStatus() != null && (dto.getStatus() == 2 || dto.getStatus() == 3)) {
            bindingResult.rejectValue("status", "error.status",
                    "貸出ステータスは「貸出待ち」もしくは「貸出中」を選択してください");
        }

        // 日付・ステータス妥当性チェック
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

        // DB関連チェック（上記でエラーがない場合のみ）
        if (!bindingResult.hasErrors() && dto.getStockId() != null) {

            // 在庫ステータスチェック
            if (!isStockAvailable(dto.getStockId())) {
                bindingResult.rejectValue("stockId", "error.stockId",
                        "選択した在庫は現在貸出できない状態です");
            }

            // 日付重複チェック
            if (dto.getExpectedRentalOn() != null && dto.getExpectedReturnOn() != null) {
                if (isOverlapping(dto)) {
                    bindingResult.rejectValue("expectedRentalOn", "error.date",
                            "指定した期間は既存の期間と重複しています。");
                }
            }
        }
    }

    // 在庫が貸出可能か確認（在庫ステータス0=貸出可）
    private boolean isStockAvailable(String stockId) {
        return stockRepository.findById(Long.parseLong(stockId))
                .map(stock -> stock.getStatus() == 0)
                .orElse(false);
    }

    // 貸出期間の重複確認
    private boolean isOverlapping(RentalManageDto dto) {
        List<RentalManage> overlapping = rentalRepository.findOverlappingRentals(
                dto.getStockId(),
                dto.getExpectedRentalOn(),
                dto.getExpectedReturnOn());
        return !overlapping.isEmpty();
    }
}