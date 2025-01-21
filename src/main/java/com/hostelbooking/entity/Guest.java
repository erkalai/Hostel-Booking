package com.hostelbooking.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Guest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String idType;

	private String idNumber;

	private String programName;

	private String mobileNumber;

	private String kid;

	private String foodType;

	private String coffeeTime;

	@ManyToOne
	@JoinColumn(name = "room_id", nullable = false)
	private Room room;

	@ManyToOne
	@JoinColumn(name = "program_id", nullable = true)
	private Program program;

	@OneToMany(mappedBy = "guest")
	@JsonIgnore // Prevents serialization loop
	private List<Booking> bookings;

	public String getProgramName() {
		return program != null ? program.getName() : null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getFoodType() {
		return foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public String getCoffeeTime() {
		return coffeeTime;
	}

	public void setCoffeeTime(String coffeeTime) {
		this.coffeeTime = coffeeTime;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	@Override
	public String toString() {
		return "Guest [id=" + id + ", name=" + name + ", idType=" + idType + ", idNumber=" + idNumber + ", programName="
				+ programName + ", mobileNumber=" + mobileNumber + ", kid=" + kid + ", foodType=" + foodType
				+ ", coffeeTime=" + coffeeTime + ", room=" + room + ", program=" + program + "]";
	}

	public Guest(Long id, String name, String idType, String idNumber, String programName, String mobileNumber,
			String kid, String foodType, String coffeeTime, Long createdBy, Room room, Program program) {
		super();
		this.id = id;
		this.name = name;
		this.idType = idType;
		this.idNumber = idNumber;
		this.programName = programName;
		this.mobileNumber = mobileNumber;
		this.kid = kid;
		this.foodType = foodType;
		this.coffeeTime = coffeeTime;
		this.room = room;
		this.program = program;
	}

	public Guest() {
	}

}