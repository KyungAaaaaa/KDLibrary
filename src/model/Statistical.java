package model;

public class Statistical {
	private String isbn;
	private String date;
	private String id;
	private int no;
	private int count;
	private String cartgory;

	public Statistical(String isbn, String date, String id,int no) {
		super();
		this.isbn = isbn;
		this.date = date;
		this.id = id;
		this.no= no;
	}

	public Statistical(String date,String cartgory,int count) {
		super();
		this.count = count;
		this.date = date;
		this.cartgory = cartgory;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCartgory() {
		return cartgory;
	}

	public void setCartgory(String cartgory) {
		this.cartgory = cartgory;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
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
