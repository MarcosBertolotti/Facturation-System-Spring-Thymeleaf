package com.springboot.app.controllers;

import com.springboot.app.entities.Client;
import com.springboot.app.services.IClientService;
import com.springboot.app.services.IIUploadFileService;
import com.springboot.app.util.paginator.PageRender;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("")
@SessionAttributes("client")    // se guarda el objeto client mapeado al formulario. cada vez que se invoca el crear/editar con una peticion get. va a obtener el objeto cliente, lo guarda en los atributos de la sesion y lo pasa a la vista.
public class ClientController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private IClientService clientService;

    @Autowired
    private IIUploadFileService uploadFileService;

    @Autowired
    private MessageSource messageSource;

    @Secured("ROLE_USER")
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/see/{id}")
    public String see(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {

        Client client = clientService.fetchByIdWithBills(id); //clientService.findOne(id);

        if (client == null) {
            flash.addFlashAttribute("error", messageSource.getMessage("text.client.flash.db.error", null, locale));
            return "redirect:/list";
        }

        model.put("client", client);
        model.put("title", messageSource.getMessage("text.client.detail.title", null, locale));

        return "see";
    }

    @GetMapping({"/list", "/"})
    public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
                       Authentication authentication, HttpServletRequest request, Locale locale) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");

        if(securityContext.isUserInRole("ROLE_ADMIN")){ // o request.isUserInRole("ROLE_ADMIN")
            logger.info("Hello " + auth.getName() + ", you have Admin role access!");
        }else{
            logger.info("Hello " + auth.getName() + ", you haven't Admin role access!");
        }

        Pageable pageRequest = PageRequest.of(page, 5);

        Page<Client> clients = clientService.findAll(pageRequest);

        PageRender<Client> pageRender = new PageRender<>("/list", clients);

        model.addAttribute("title", messageSource.getMessage("text.client.list.title", null, locale));
        model.addAttribute("clients", clients);
        model.addAttribute("page", pageRender);

        return "list";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/form")
    public String create(Map<String, Object> model, Locale locale) {

        Client client = new Client();

        model.put("client", client);
        model.put("title", messageSource.getMessage("text.client.form.title.create", null, locale));

        return "form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/form/{id}")
    public String update(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {

        Client client = null;

        if (id > 0) {
            client = clientService.findOne(id);
            if (client == null) {
                flash.addFlashAttribute("error", messageSource.getMessage("text.client.flash.db.error", null, locale));
                return "redirect:/list";
            }
        } else {
            flash.addFlashAttribute("error", messageSource.getMessage("text.client.flash.id.error", null, locale));
            return "redirect:/list";
        }

        model.put("client", client);
        model.put("title", messageSource.getMessage("text.client.form.title.update", null, locale));

        return "form";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/form")
    public String save(@Valid Client client, BindingResult result, Model model, @RequestParam("file") MultipartFile photo, SessionStatus status, RedirectAttributes flash, Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("title", messageSource.getMessage("text.client.form.title", null, locale));
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

            flash.addFlashAttribute("info", messageSource.getMessage("text.client.flash.photo.upload.success", null, locale) + ' '+ uniqueFileName + "'");

            client.setPhoto(uniqueFileName);
        }

        String messageFlash = (client.getId() != null) ? messageSource.getMessage("text.client.flash.update.success", null, locale) : messageSource.getMessage("text.client.flash.create.success", null, locale);

        clientService.save(client);
        status.setComplete(); // elimina el objeto cliente de la sesion.
        flash.addFlashAttribute("success", messageFlash);

        return "redirect:list";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {

        if (id > 0) {
            Client client = clientService.findOne(id);

            clientService.delete(id);
            flash.addFlashAttribute("success", messageSource.getMessage("text.client.flash.delete.success", null, locale));

            if (uploadFileService.delete(client.getPhoto())) {
                flash.addFlashAttribute("info", messageSource.getMessage("text.client.flash.photo.delete.success", null, locale));
            }
        }
        return "redirect:/list";
    }


}
