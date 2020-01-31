package com.springboot.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Table(name="items_bills")
public class ItemBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY) // EAGER trae los productos inmediatamente, juntos con los ItemBIll trae los products. poco redundante. En este caso es aceptable usar EAGER porque siempre necesitamos el producto para mostrar la linea
    @JoinColumn(name="product_id", referencedColumnName = "product_id") // por defecto hace esto no es necesario
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // ignora esos atributos
    private Product product;

    private static final long serialVersionUID = 1L;

    public Double calculateAmount(){

        return quantity.doubleValue() * product.getPrice();
    }
}
