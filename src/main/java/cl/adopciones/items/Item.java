package cl.adopciones.items;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "itemid")
    @SequenceGenerator(name = "itemid", sequenceName = "itemid")
    private Long id;

    @NonNull
    private String petName;
    
    @NonNull
    private PetType petType;

    @NonNull
    private PetAgeCategory petAgeCategory;

}
