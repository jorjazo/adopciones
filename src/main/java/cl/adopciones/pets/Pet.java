package cl.adopciones.pets;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "itemid")
    @SequenceGenerator(name = "itemid", sequenceName = "itemid")
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
    
}
