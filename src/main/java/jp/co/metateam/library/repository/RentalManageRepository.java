package jp.co.metateam.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jp.co.metateam.library.model.RentalManage;

import java.util.List;
import java.util.Optional;

public interface RentalManageRepository extends JpaRepository<RentalManage, Long> {
    List<RentalManage> findAll();

    Optional<RentalManage> findById(Long id);

}