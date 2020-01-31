package com.springboot.app.view.xml;

import com.springboot.app.entities.Client;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="clientList")
public class ClientList {

    @XmlElement(name="client")
    public List<Client> clients;

    public ClientList(){}

    public ClientList(List<Client> clients) {
        this.clients = clients;
    }

    public List<Client> getClients() {
        return clients;
    }

}
