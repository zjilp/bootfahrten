package de.thd.pms.model;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Josef Schneeberger (josef.schneeberger@th-deg.de)
 */
@Entity
@Table(name = "tbl_boot")
public class Boot {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pk_boot")
	@Schema(description = "Die ID des Boots", example = "2")
	private Long id;
	@Schema(description = "Wann das Boot angelegt wurde", example = "2017-07-21T17:32:28Z")
	private LocalDateTime created;
	@Schema(description = "Name des Bootes", example = "Boaty McBoatface")
    private String name;
	@Schema(description = "Anzahl der Sitzplätze", example = "3")
    private int sitze;
	@Schema(description = "Bootstyp", example = "A")
	private String klasse;

    public Boot() {
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Boot other = (Boot) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boot(String n, String k, int s) {
    	name = n;
    	klasse = k;
    	sitze = s;
	}

	public Boot(String n) {
    	name = n;
    	klasse = "";
    	sitze = 0;
	}

	/**
	 * Added to simplify thymeleaf input.
	 * @return an array of size sitze filled with integers starting from 0
	 */
	@Hidden
	public Integer[] getSitzeArray() {
		Integer[] result = new Integer[sitze];
		for (int i = 0; i < sitze; i++) {
			result[i] = i;
		}
		return result;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSitze() {
		return sitze;
	}

	public void setSitze(int sitze) {
		this.sitze = sitze;
	}

	public String getKlasse() {
		return klasse;
	}

	public void setKlasse(String klasse) {
		this.klasse = klasse;
	}

}
