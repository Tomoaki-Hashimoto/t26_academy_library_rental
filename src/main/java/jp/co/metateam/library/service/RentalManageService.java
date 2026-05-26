package jp.co.metateam.library.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.repository.RentalManageRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class RentalManageService {

    private final RentalManageRepository rentalRepository;

    @Autowired
    public RentalManageService(RentalManageRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Transactional
    public void save(RentalManageDto dto) {

        System.out.println("SAVE START"); // ←追加

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

        System.out.println("SAVE END"); // ←追加
    }
}