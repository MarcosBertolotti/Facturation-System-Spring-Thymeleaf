package com.springboot.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client implements Serializable { // serializable recomendado para almacenar o transmitirlo.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column(name="first_name")
    @NotEmpty
    @Size(min=2,max=20)
    private String firstName;

    @Column(name="last_name")
    @NotEmpty
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @Column(name="create_at")
    @NotNull
    @Temporal(TemporalType.DATE) // indica el formato en que se va a guardar esta fecha de java en la tabla de bd. DATE/TIME/TIMESTAMP
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createAt;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // cascade all = las operaciones delete,persist se van a realizar en cadena. cuando el cliente guarde varias facturas y el cliente se persiste, tambien persiste los elementos hijos.
    private List<Bill> bills;

    private String photo;

    private static final long serialVersionUID = 1L;

    public void addBill(Bill bill){

        bills.add(bill);
    }

}
