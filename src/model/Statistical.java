package model;

public class Statistical {
	private String isbn;
	private String date;
	private String id;

	public Statistical(String isbn, String date, String id) {
		super();
		this.isbn = isbn;
		this.date = date;
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
