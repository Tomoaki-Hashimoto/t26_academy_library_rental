package jp.co.metateam.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.service.AccountService;
import jp.co.metateam.library.service.StockService;
import jp.co.metateam.library.service.RentalManageService;
import jp.co.metateam.library.values.RentalStatus;
import lombok.extern.log4j.Log4j2;

/**
 * 貸出管理関連クラスß
 */
@Log4j2
@Controller
public class RentalManageController {

    /**
     * 貸出一覧画面初期表示
     * 
     * @param model
     * @return
     */

    private final StockService stockService;
    private final AccountService accountService;
    private final RentalManageService rentalService;

    @Autowired
    public RentalManageController(
            AccountService accountService,
            StockService stockService,
            RentalManageService rentalService) {

        this.accountService = accountService;
        this.stockService = stockService;
        this.rentalService = rentalService;
    }

    @GetMapping("/rental/index")
    public String index(Model model) {
        model.addAttribute("rentalManageList", rentalService.findAll());
        return "/rental/index";
        // 貸出管理テーブルから全件取得
        // 貸出一覧画面に渡すデータをmodelに追加
        // 貸出一覧画面へ遷移
    }

    /**
     * 貸出登録画面初期表示
     */
    @GetMapping("/rental/add")
    public String add(Model model) {

        // DTOを画面に渡す
        model.addAttribute("rentalManageDto", new RentalManageDto());

        // タイトル
        model.addAttribute("title", "貸出登録");

        // アカウント一覧取得（プルダウン用）
        model.addAttribute("accounts", accountService.findAll());

        // 在庫一覧取得（プルダウン用）
        model.addAttribute("stockList", stockService.findAll());

        // 貸出状態を渡す
        model.addAttribute("rentalStatus", RentalStatus.values());

        // 登録画面へ遷移
        return "/rental/add";
    }

    @PostMapping("/rental/add")
    public String add(
            @Valid @ModelAttribute RentalManageDto dto,
            BindingResult bindingResult,
            Model model) {

        // バリデーションをサービスに委譲
        rentalService.validate(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("rentalManageDto", dto);
            model.addAttribute("title", "貸出登録");
            model.addAttribute("accounts", accountService.findAll());
            model.addAttribute("stockList", stockService.findAll());
            model.addAttribute("rentalStatus", RentalStatus.values());
            return "/rental/add";
        }

        rentalService.save(dto);

        return "redirect:/rental/index";
    }

    // 編集画面の表示
    @GetMapping("/rental/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        RentalManage rentalManage = rentalService.findById(id);

        RentalManageDto dto = new RentalManageDto();
        dto.setId(rentalManage.getId());
        dto.setEmployeeId(rentalManage.getEmployeeId());
        dto.setExpectedRentalOn(rentalManage.getExpectedRentalOn());
        dto.setExpectedReturnOn(rentalManage.getExpectedReturnOn());
        dto.setStockId(rentalManage.getStockId());
        dto.setStatus(rentalManage.getStatus());

        model.addAttribute("rentalManageDto", dto);
        model.addAttribute("title", "貸出編集");
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("stockList", stockService.findAll());
        model.addAttribute("rentalStatus", RentalStatus.values());

        return "/rental/edit";
    }

    // 編集内容の保存
    @PostMapping("/rental/{id}/edit")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute RentalManageDto dto,
            BindingResult bindingResult,
            Model model) {
        try {
            rentalService.validate(dto, bindingResult);

            if (bindingResult.hasErrors()) {
                throw new Exception("Validation error.");
            }

            rentalService.update(id, dto);
            return "redirect:/rental/index";

        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("rentalManageDto", dto);
            model.addAttribute("title", "貸出編集");
            model.addAttribute("accounts", accountService.findAll());
            model.addAttribute("stockList", stockService.findAll());
            model.addAttribute("rentalStatus", RentalStatus.values());
            return "/rental/edit";
        }
    }
}