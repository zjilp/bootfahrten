package de.thd.pms.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author josef.schneeberger@th-deg.de
 */
@Entity
@Table(name = "tbl_person")
public class Person {
	@Id
	@GeneratedValue
	@Column(name = "pk_person")
	@Schema(description = "Die ID der Person", example = "2")
	private Long id;
	@Schema(description = "Wann die Person angelegt wurde", example = "2017-07-21T17:32:28Z")
	private LocalDateTime created;
	@Schema(description = "Vorname der Person", example = "Peter")
	private String vorname;
	@Schema(description = "Nachname der Person", example = "Meier")
	private String nachname;
	@Schema(description = "Telefonnummer der Person", example = "0991 12345")
	private String telefon;

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

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

}
