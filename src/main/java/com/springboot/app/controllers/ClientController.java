package com.springboot.app.controllers;

import com.springboot.app.entities.Client;
import com.springboot.app.services.IClientService;
import com.springboot.app.services.IIUploadFileService;
import com.springboot.app.util.paginator.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

@Controller
@RequestMapping("")
@SessionAttributes("client")    // se guarda el objeto client mapeado al formulario. cada vez que se invoca el crear/editar con una peticion get. va a obtener el objeto cliente, lo guarda en los atributos de la sesion y lo pasa a la vista.
public class ClientController {

    @Autowired
    private IClientService clientService;

    @Autowired
    private IIUploadFileService uploadFileService;

    @GetMapping("/uploads/{filename:.+}")       // filename:.+ = expresion regular permite que spring no borre o trunque la extension del archivo.
    public ResponseEntity<Resource> seePhoto(@PathVariable String filename) {

        Resource resource = null;

        try {
            resource = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok() // la imagen se pasa a la respuesta, se anexa el recurso al body.
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"") // par de cabecera para juntar la imagen en la respuesta
                .body(resource);
    }

    @GetMapping("/see/{id}")
    public String see(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Client client = clientService.findOne(id);

        if (client == null) {
            flash.addFlashAttribute("error", "Client not exists");
            return "redirect:/list";
        }

        model.put("client", client);
        model.put("title", "Client Detail" + client.getFirstName());

        return "see";
    }

    @GetMapping("/list")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        Pageable pageRequest = PageRequest.of(page, 5);

        Page<Client> clients = clientService.findAll(pageRequest);

        PageRender<Client> pageRender = new PageRender<>("/list", clients);

        model.addAttribute("title", "Client List");
        model.addAttribute("clients", clients);
        model.addAttribute("page", pageRender);

        return "list";
    }

    @GetMapping("/form")
    public String create(Map<String, Object> model) {

        Client client = new Client();

        model.put("client", client);
        model.put("title", "Client Form");

        return "form";
    }

    @GetMapping("/form/{id}")
    public String update(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        Client client = null;

        if (id > 0) {
            client = clientService.findOne(id);
            if (client == null) {
                flash.addFlashAttribute("error", "Client ID doesn't exist in the BD!");
                return "redirect:/list";
            }
        } else {
            flash.addFlashAttribute("error", "Client ID can't be 0!");
            return "redirect:/list";
        }

        model.put("client", client);
        model.put("title", "Update Client");

        return "form";
    }

    @PostMapping("/form")
    public String save(@Valid Client client, BindingResult result, Model model, @RequestParam("file") MultipartFile photo, SessionStatus status, RedirectAttributes flash) {

        if (result.hasErrors()) {
            model.addAttribute("title", "Client Form");
            return "form";
        }

        if (!photo.isEmpty()) {

            if (client.getId() != null && client.getId() > 0 && client.getPhoto() != null && client.getPhoto().length() > 0) {

                uploadFileService.delete(client.getPhoto());             // para eliminar la foto antigua cuando se edita
            }

            String uniqueFileName = null;

            try {
                uniqueFileName = uploadFileService.copy(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }

            flash.addFlashAttribute("info", "You have uploaded correctly '" + uniqueFileName + "'");

            client.setPhoto(uniqueFileName);
        }

        String messageFlash = (client.getId() != null) ? "Client updated successfully!" : "Client created successfully!";

        clientService.save(client);
        status.setComplete(); // elimina el objeto cliente de la sesion.
        flash.addFlashAttribute("success", messageFlash);

        return "redirect:list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

        if (id > 0) {
            Client client = clientService.findOne(id);

            clientService.delete(id);
            flash.addFlashAttribute("success", "Client successfully removed!");

            if (uploadFileService.delete(client.getPhoto())) {
                flash.addFlashAttribute("info", "Photo " + client.getPhoto() + " successfully removed!");
            }
        }
        return "redirect:/list";
    }


}
