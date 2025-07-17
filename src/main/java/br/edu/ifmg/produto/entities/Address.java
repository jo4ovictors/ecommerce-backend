package br.edu.ifmg.produto.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String street;      
    private String number;
    private String complement;
    private String city;
    private String state;       
    private String zipCode;
    private boolean main;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
