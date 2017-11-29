package cl.adopciones.pets;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import cl.adopciones.users.User;
import io.rebelsouls.chile.Comuna;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "petid")
    @SequenceGenerator(name = "petid", sequenceName = "petid")
    private Long id;

    private String name;
    
    @Enumerated(EnumType.STRING)
    private PetType type;

    @Enumerated(EnumType.STRING)
    private PetAgeCategory ageCategory;

    @Enumerated(EnumType.STRING)
    private PetSizeCategory sizeCategory;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Enumerated(EnumType.ORDINAL)
    private Comuna location;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="owner")
    private User owner;
    
}
