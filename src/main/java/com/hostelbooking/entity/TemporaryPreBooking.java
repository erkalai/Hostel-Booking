package com.hostelbooking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TemporaryPreBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String kid;

	private String name;

	private String idType;

	private String idNumber;

	private String programName;

	private String mobileNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "program_id", nullable = true)
	private Program program;

	private boolean isConfirmed = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	@Override
	public String toString() {
		return "TemporaryPreBooking [id=" + id + ", kid=" + kid + ", name=" + name + ", idType=" + idType
				+ ", idNumber=" + idNumber + ", programName=" + programName + ", mobileNumber=" + mobileNumber
				+ ", program=" + program + ", isConfirmed=" + isConfirmed + "]";
	}

	public TemporaryPreBooking(Long id, String kid, String name, String idType, String idNumber, String programName,
			String mobileNumber, Program program, boolean isConfirmed) {
		super();
		this.id = id;
		this.kid = kid;
		this.name = name;
		this.idType = idType;
		this.idNumber = idNumber;
		this.programName = programName;
		this.mobileNumber = mobileNumber;
		this.program = program;
		this.isConfirmed = isConfirmed;
	}

	public TemporaryPreBooking() {
	}

}
