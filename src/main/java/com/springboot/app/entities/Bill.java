package com.springboot.app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "client") // al ser bidireccional si hago toString de client, tambien me muestra el toString de bill que dentro tiene un client. (bucle infinito)
@Table(name = "bills")
public class Bill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long id;

    @NotEmpty
    private String description;

    private String observation;

    @Column(name="create_at")
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @ManyToOne(fetch = FetchType.LAZY) // muchas facturas, un cliente. fetch = forma de obtener los datos de la relacion. LAZY = carga perezosa, hace la consulta solo cuando se le llama. EAGER = trae toda la consulta de una sola vez. incluyendo al cliente
    @JoinColumn(name = "client_id", referencedColumnName = "client_id") // OPCIONAL, lo hace automaticamente el manyToOne
    @JsonBackReference // parte posterior de la relacion, se omite de la serializacion, no la mostrara
    private Client client;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // una factura y muchos item factura. orphanRemoval = elimina items huerfanos que no estan asociados a ningun bill.
    @JoinColumn(name="bill_id") // indicamos la llave forania que va a relacionar bill con itembill. vamos a tener esta key en la tabla item bill
    private List<ItemBill> items = new ArrayList<>();

    private static final long serialVersionUID = 1L;

    @PrePersist
    public void prePersist(){

        createAt = new Date();
    }

    public void addItemBill(ItemBill item){

        this.items.add(item);
    }

    public Double getTotal(){

        Double total = 0.0;

        total = items.stream().mapToDouble(ItemBill::calculateAmount).sum();
/*
        for(int i=0; i < items.size(); i++){
            total += items.get(i).calculateAmount();
        }*/
        return total;
    }

    @XmlTransient // lo omite en la serializacion, no lo incluye en el xml
    public Client getClient(){
        return this.client;
    }
}
