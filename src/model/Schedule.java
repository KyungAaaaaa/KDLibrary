package model;

public class Schedule {
private String content;
private String date;
private int no;

public Schedule(String content, String date, int no) {
	super();
	this.content = content;
	this.date = date;
	this.no = no;
}

public Schedule(String content, String date) {
	super();
	this.content = content;
	this.date = date;
}

public Schedule(String content, int no) {
	super();
	this.content = content;
	this.no = no;
}

public String getContent() {
	return content;
}

public void setContent(String content) {
	this.content = content;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public int getNo() {
	return no;
}
public void setNo(int no) {
	this.no = no;
}

}
