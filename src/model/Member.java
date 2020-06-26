package model;

public class Member {
	private String name;
	private String id;
	private String pass;
	private String phoneNumber;
	private String birth;
	private String rentalBook;
	private String fileimg;
	private String etc;
	
	// 1. 
	public Member(String name, String id, String pass, String phoneNumber, String birth, String rentalBook,
			 String etc) {
		super();
		this.name = name;
		this.id = id;
		this.pass = pass;
		this.phoneNumber = phoneNumber;
		this.birth = birth;
		this.rentalBook = rentalBook;
		this.etc = etc;
	}
	
	public Member(String name, String id, String pass, String phoneNumber, String birth, String etc) {
		super();
		this.name = name;
		this.id = id;
		this.pass = pass;
		this.phoneNumber = phoneNumber;
		this.birth = birth;
		this.etc = etc;
	}

	// 2.
	public Member(String name, String id, String pass, String phoneNumber, String birth) {
		super();
		this.name = name;
		this.id = id;
		this.pass = pass;
		this.phoneNumber = phoneNumber;
		this.birth = birth;
	}

	// 3.
	public Member(String id, String pass) {
		super();
		this.id = id;
		this.pass = pass;
	}
	
	

	// getter , setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getRentalBook() {
		return rentalBook;
	}

	public void setRentalBook(String rentalBook) {
		this.rentalBook = rentalBook;
	}

	public String getFileimg() {
		return fileimg;
	}

	public void setFileimg(String fileimg) {
		this.fileimg = fileimg;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	@Override
	public String toString() {
		return "Member [name=" + name + ", id=" + id + ", pass=" + pass + ", phoneNumber=" + phoneNumber + ", birth="
				+ birth + ", rentalBook=" + rentalBook + ", fileimg=" + fileimg + ", etc=" + etc + "]";
	}
	
	
	
	
	
}
