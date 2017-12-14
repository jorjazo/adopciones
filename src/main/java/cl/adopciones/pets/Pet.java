package cl.adopciones.pets;

import java.time.LocalDateTime;

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

import cl.adopciones.organizations.Organization;
import cl.adopciones.users.Role;
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
    
    private LocalDateTime creationDateTime;
    
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
    
    private String description;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="owner")
    private User owner;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="organization")
    private Organization organization;
    
    public boolean isOwner(User user) {
    	return user != null && user.getId() == owner.getId();
    }
    
    public boolean isInOrganization(Organization other) {
    	return other != null && getOrganization().getId() == other.getId();
    }
    
    public boolean canUploadPhotos(User user) {
    	return user != null 
    			&& (user.getRoles().contains(Role.ADMIN)
    					|| isInOrganization(user.getOrganization()));
    }
}
