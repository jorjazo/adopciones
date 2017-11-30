package io.rebelsouls.chile;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

public enum Region {

	Arica_y_Parinacota(1, "Arica y Parinacota","XV"),
	Tarapacá(2, "Tarapacá","I"),
	Antofagasta(3, "Antofagasta","II"),
	Atacama(4, "Atacama","III"),
	Coquimbo(5, "Coquimbo","IV"),
	Valparaiso(6, "Valparaiso","V"),
	Metropolitana_de_Santiago(7, "Metropolitana de Santiago","RM"),
	Libertador_General_Bernardo_Ohiggins(8, "Libertador General Bernardo O\"Higgins","VI"),
	Maule(9, "Maule","VII"),
	Biobío(10, "Biobío","VIII"),
	La_Araucanía(11, "La Araucanía","IX"),
	Los_Ríos(12, "Los Ríos","XIV"),
	Los_Lagos(13, "Los Lagos","X"),
	Aisén_del_General_Carlos_Ibáñez_del_Campo(14, "Aisén del General Carlos Ibáñez del Campo","XI"),
	Magallanes_y_de_la_Antártica_Chilena(15, "Magallanes y de la Antártica Chilena","XII");
	
	@Getter
	private int id;
	
	@Getter
	private String nombre;
	
	@Getter
	private String ordinal;
	
	private Region(int id, String nombre, String ordinal) {
		this.id = id;
		this.nombre = nombre;
		this.ordinal = ordinal;
	}
	
	public Set<Provincia> getProvincias() {
		return Stream.of(Provincia.values()).filter(p -> p.getIdRegion() == id).collect(Collectors.toSet());
	}
	
	public Set<Comuna> getComunas() {
		return getProvincias().stream().flatMap(p -> p.getComunas().stream()).collect(Collectors.toSet());
	}
	
}
