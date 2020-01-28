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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/see/{id}")
    public String see(@PathVariable(value="id") Long id, Model model, RedirectAttributes flash){

        Bill bill = billService.fetchByIdWithClientWhitItemBillWithProduct(id); // billService.findBillById(id);

        if(bill == null){
            flash.addFlashAttribute("error", "The bill not exists!");
            return "redirect:/list";
        }

        model.addAttribute("bill", bill);
        model.addAttribute("title", "Bill: " + bill.getDescription());

        return "bill/see";
    }

    @GetMapping("/form/{clientId}")
    public String create(@PathVariable(value="clientId") Long clientId, Map<String, Object> model, RedirectAttributes flash){

        Client client = clientService.findOne(clientId);

        if(client == null){
            flash.addFlashAttribute("error", "Client doesn't exists");
            return "redirect:/list";
        }

        Bill bill = new Bill();
        bill.setClient(client);

        model.put("bill", bill);
        model.put("title", "Create Bill");

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
                       SessionStatus status){

        if(result.hasErrors()){
            model.addAttribute("title", "Create Bill");
            return "bill/form";
        }

        if(itemId == null || itemId.length == 0){
            model.addAttribute("title", "Create Bill");
            model.addAttribute("error", "Error: The Bill can't be not have lines!");
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

        flash.addFlashAttribute("success", "Bill created successfully!");

        return "redirect:/see/" + bill.getClient().getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value="id") Long id, RedirectAttributes flash){

        Bill bill = billService.findBillById(id);

        if(bill != null){
            billService.deleteBill(id);
            flash.addFlashAttribute("success", "Bill successfully removed!");
            return "redirect:/see/" + bill.getClient().getId();
        }

        flash.addFlashAttribute("error", "The Bill not exists, couldn't be deleted!");

        return "redirect:/list";
    }
}
