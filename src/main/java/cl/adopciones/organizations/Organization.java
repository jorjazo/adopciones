package cl.adopciones.organizations;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "organizations")
@Data
public class Organization  implements Serializable {

	private static final long serialVersionUID = 8092413415834577827L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "organizationid")
	@SequenceGenerator(name = "organizationid", sequenceName = "organizationid")
	private Long id;
	
	private String name;
	private String contactEmail;
	
	@Enumerated(EnumType.STRING)
	private OrganizationType type;
	
}
