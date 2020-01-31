package com.springboot.app.view.csv;

import com.springboot.app.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component("list.csv")
public class ClientCsvView extends AbstractView {

    public ClientCsvView(){
        setContentType("text/csv");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"clients.csv\"");
        httpServletResponse.setContentType(getContentType());

        Page<Client> clients = (Page<Client>) map.get("clients");

        ICsvBeanWriter beanWriter = new CsvBeanWriter(httpServletResponse.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"id", "firstName", "lastName", "email", "createAt"};
        beanWriter.writeHeader(header);
/*
        for(Client client: clients){
            beanWriter.write(client,header);
        }*/

        clients.stream().forEach(client -> {
            try {
                beanWriter.write(client,header);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        beanWriter.close();
    }
}
