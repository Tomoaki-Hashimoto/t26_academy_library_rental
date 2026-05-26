package jp.co.metateam.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jp.co.metateam.library.model.RentalManage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentalManageRepository extends JpaRepository<RentalManage, Long> {
    List<RentalManage> findAll();

    Optional<RentalManage> findById(Long id);

    // 期間重複チェック用（ステータスが貸出待ち=0 か 貸出中=1 のもののみ対象）
    @Query("SELECT r FROM RentalManage r WHERE r.stockId = :stockId " +
            "AND (r.status = 0 OR r.status = 1) " +
            "AND r.expectedRentalOn < :expectedReturnOn " +
            "AND r.expectedReturnOn > :expectedRentalOn")
    List<RentalManage> findOverlappingRentals(
            @Param("stockId") String stockId,
            @Param("expectedRentalOn") LocalDate expectedRentalOn,
            @Param("expectedReturnOn") LocalDate expectedReturnOn);
}