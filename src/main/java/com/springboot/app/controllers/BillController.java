package com.springboot.app.controllers;

import com.springboot.app.entities.Bill;
import com.springboot.app.entities.Client;
import com.springboot.app.entities.ItemBill;
import com.springboot.app.entities.Product;
import com.springboot.app.services.IBillService;
import com.springboot.app.services.IClientService;
import com.springboot.app.services.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/bill")
@SessionAttributes("bill")
public class BillController {

    @Autowired
    private IClientService clientService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IBillService billService;

    @Autowired
    private MessageSource messageSource;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/see/{id}")
    public String see(@PathVariable(value="id") Long id, Model model, RedirectAttributes flash, Locale locale){

        Bill bill = billService.fetchByIdWithClientWhitItemBillWithProduct(id); // billService.findBillById(id);

        if(bill == null){
            flash.addFlashAttribute("error", messageSource.getMessage("text.bill.flash.db.error", null, locale));
            return "redirect:/list";
        }

        model.addAttribute("bill", bill);
        model.addAttribute("title", String.format(messageSource.getMessage("text.bill.see.title", null, locale), bill.getDescription()));

        return "bill/see";
    }

    @GetMapping("/form/{clientId}")
    public String create(@PathVariable(value="clientId") Long clientId, Map<String, Object> model, RedirectAttributes flash, Locale locale){

        Client client = clientService.findOne(clientId);

        if(client == null){
            flash.addFlashAttribute("error", messageSource.getMessage("text.client.flash.db.error", null, locale));
            return "redirect:/list";
        }

        Bill bill = new Bill();
        bill.setClient(client);

        model.put("bill", bill);
        model.put("title", messageSource.getMessage("text.bill.form.title", null, locale));

        return "bill/form";
    }

    @GetMapping(value = "/load-products/{term}", produces = {"application/json"})
    public @ResponseBody List<Product> loadProducts(@PathVariable(value="term") String term){

        return productService.findByName(term);
    }

    @PostMapping("/form")
    public String save(@Valid Bill bill,
                       BindingResult result,
                       Model model,
                       @RequestParam(name="item_id[]", required = false) Long[] itemId,
                       @RequestParam(name="quantity[]", required = false) Integer[] quantity,
                       RedirectAttributes flash,
                       SessionStatus status,
                       Locale locale){

        if(result.hasErrors()){
            model.addAttribute("title", messageSource.getMessage("text.bill.form.title", null, locale));
            return "bill/form";
        }

        if(itemId == null || itemId.length == 0){
            model.addAttribute("title", messageSource.getMessage("text.bill.form.title", null, locale));
            model.addAttribute("error", messageSource.getMessage("text.bill.flash.lines.error", null, locale));
            return "bill/form";
        }

        for(int i = 0; i < itemId.length; i++){

            Product product = productService.findProductById(itemId[i]);

            ItemBill line = new ItemBill();
            line.setQuantity(quantity[i]);
            line.setProduct(product);

            bill.addItemBill(line);
            //log.info("ID: " + itemId[i].toString() + ", quantity: " + quantity[i].toString());
        }
        billService.saveBill(bill);
        status.setComplete();

        flash.addFlashAttribute("success", messageSource.getMessage("text.bill.flash.create.success", null, locale));

        return "redirect:/see/" + bill.getClient().getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value="id") Long id, RedirectAttributes flash, Locale locale){

        Bill bill = billService.findBillById(id);

        if(bill != null){
            billService.deleteBill(id);
            flash.addFlashAttribute("success", messageSource.getMessage("text.bill.flash.delete.success", null, locale));
            return "redirect:/see/" + bill.getClient().getId();
        }

        flash.addFlashAttribute("error", messageSource.getMessage("text.bill.flash.db.error", null, locale));

        return "redirect:/list";
    }
}
