package eu.cec.digit.comref.interview.persistent.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

@Entity
@Table(name = "WATCH")
public class Watch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 349533338490577533L;

	public Watch() {
	}

	public Watch(String name, Integer value, Integer sold, Boolean available) {

		this.name = name;
		this.value = value;
		this.sold = sold;
		this.available = available;
	}

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue
	private UUID id;

	@Column(name = "NAME", length = 128)
	private String name;

	@Column(name = "value")
	private Integer value;

	@Column(name = "sold")
	private Integer sold;

	@Column(name = "available")
	private Boolean available;

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getSold() {
		return sold;
	}

	public void setSold(Integer sold) {
		this.sold = sold;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

}
