package io.rebelsouls.chile;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

public enum Provincia {

	Arica(1, "Arica",1),
	Parinacota(2, "Parinacota",1),
	Iquique(3, "Iquique",2),
	El_Tamarugal(4, "El Tamarugal",2),
	Antofagasta(5, "Antofagasta",3),
	El_Loa(6, "El Loa",3),
	Tocopilla(7, "Tocopilla",3),
	Chañaral(8, "Chañaral",4),
	Copiapó(9, "Copiapó",4),
	Huasco(10, "Huasco",4),
	Choapa(11, "Choapa",5),
	Elqui(12, "Elqui",5),
	Limarí(13, "Limarí",5),
	Isla_de_Pascua(14, "Isla de Pascua",6),
	Los_Andes(15, "Los Andes",6),
	Petorca(16, "Petorca",6),
	Quillota(17, "Quillota",6),
	San_Antonio(18, "San Antonio",6),
	San_Felipe_de_Aconcagua(19, "San Felipe de Aconcagua",6),
	Valparaiso(20, "Valparaiso",6),
	Chacabuco(21, "Chacabuco",7),
	Cordillera(22, "Cordillera",7),
	Maipo(23, "Maipo",7),
	Melipilla(24, "Melipilla",7),
	Santiago(25, "Santiago",7),
	Talagante(26, "Talagante",7),
	Cachapoal(27, "Cachapoal",8),
	Cardenal_Caro(28, "Cardenal Caro",8),
	Colchagua(29, "Colchagua",8),
	Cauquenes(30, "Cauquenes",9),
	Curicó(31, "Curicó",9),
	Linares(32, "Linares",9),
	Talca(33, "Talca",9),
	Arauco(34, "Arauco",10),
	Bio_Bío(35, "Bio Bío",10),
	Concepción(36, "Concepción",10),
	Ñuble(37, "Ñuble",10),
	Cautín(38, "Cautín",11),
	Malleco(39, "Malleco",11),
	Valdivia(40, "Valdivia",12),
	Ranco(41, "Ranco",12),
	Chiloé(42, "Chiloé",13),
	Llanquihue(43, "Llanquihue",13),
	Osorno(44, "Osorno",13),
	Palena(45, "Palena",13),
	Aisén(46, "Aisén",14),
	Capitán_Prat(47, "Capitán Prat",14),
	Coihaique(48, "Coihaique",14),
	General_Carrera(49, "General Carrera",14),
	Antártica_Chilena(50, "Antártica Chilena",15),
	Magallanes(51, "Magallanes",15),
	Tierra_del_Fuego(52, "Tierra del Fuego",15),
	Última_Esperanza(53, "Última Esperanza",15);
	
	@Getter
	private int id;
	
	@Getter
	private String nombre;
	
	@Getter
	private int idRegion;
	
	private Provincia(int id, String nombre, int idRegion) {
		this.id = id;
		this.nombre = nombre;
		this.idRegion = idRegion;
	}
	
	public Set<Comuna> getComunas() {
		return Stream.of(Comuna.values()).filter(c -> c.getIdProvincia() == id).collect(Collectors.toSet());
	}
	
	public Region getRegion() {
		return Stream.of(Region.values()).filter(r -> r.getId() == idRegion).findFirst().orElse(null);
	}
}
