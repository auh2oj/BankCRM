package com.bank.crm.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="Customers")
@DynamicUpdate
public class Customer {

	@Id
	@Column(name="cid")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int cid;
	
	@Column(length=100)
	private String name;
	
	@Column
	private int age;
	
	@Column(length=100)
	private String address;
	
	@Column(length=100)
	private String contact; //phone number
	
	@Column
	private double loan; //loan amount
	
	@Column(length=20)
	private String ssn;
	
	@Column
	private Date requestDate; // date that request was sent
	
	// status can be Pending, Accepted, or Rejected depending on the discretion of the Bank Manager
	// status can also be Assigned if the Team Leader has assigned the Customer to an Agent
	@Column(length=10)
	private String status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public double getLoan() {
		return loan;
	}
	public void setLoan(double loan) {
		this.loan = loan;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCid() {
		return cid;
	}
	@Override
	public String toString() {
		return "Customer [cid=" + cid + ", name=" + name + ", age=" + age + ", address=" + address + ", contact="
				+ contact + ", loan=" + loan + ", ssn=" + ssn + ", requestDate=" + requestDate + ", status=" + status
				+ "]";
	}
	
}

