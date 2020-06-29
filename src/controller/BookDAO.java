package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Book;
import model.Statistical;

public class BookDAO {
	public static ObservableList<String> categoryList = FXCollections.observableArrayList("���� �濵", "��ȭ", "��ȭ", "�Ҽ�",
			"�丮", "�ι�", "�ڱ���", "����");

	// ���� ���̺� ��ü����
	public HashSet<Book> getBookTbl() {
		HashSet<Book> hashSet = new HashSet<Book>();
		// ArrayList<Book> arrayList = new ArrayList<Book>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			String query = "select * from BookTBL;";
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				hashSet.add(new Book(rs.getString("ISBN"), rs.getString("title"), rs.getString("category"),
						rs.getString("writer"), rs.getString("company"), rs.getString("date"), rs.getString("fileimg"),
						rs.getString("information"), rs.getBoolean("rental")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (con != null)
					con.close();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		}

		return hashSet;

	}

	// ���� �߰� �޼ҵ�
	public int addBook(Book book1) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {

			con = DBUtil.getConnection();
			String query = "insert into BookTBL (ISBN,title ,writer,category,company,date,fileimg,information) values(?,?,?,?,?,?,?,?);";
			preparedStatement = con.prepareStatement(query);

			preparedStatement.setString(1, book1.getIsbn());
			preparedStatement.setString(2, book1.getTitle());
			preparedStatement.setString(3, book1.getWriter());
			preparedStatement.setString(4, book1.getCategory());
			preparedStatement.setString(5, book1.getCompany());
			preparedStatement.setString(6, book1.getDate());
			preparedStatement.setString(7, book1.getFileimg());
			preparedStatement.setString(8, book1.getInformation());
			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("��� �Ϸ�");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("��� ����");
				alert.showAndWait();
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("��� ���� : DB����");
			alert.setContentText(e1.getMessage());
			alert.showAndWait();

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (con != null)
					con.close();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		}
		return returnValue;
	}

	// ���� �˻� �޼ҵ�
	public ArrayList<Book> searchBook(String searchText, String type) {
		ArrayList<Book> arrayList = new ArrayList<Book>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = null;
		try {
			con = DBUtil.getConnection();
			if (type.equals("title")) {
				query = "select * from BookTBL where title like ?;";
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, "%" + searchText + "%");

			} else if (type.equals("category")) {
				query = "select * from BookTBL where category like ?;";
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, "%" + searchText + "%");
			} else if (type.equals("ISBN")) {
				query = "select * from BookTBL where ISBN like ?;";
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, "%" + searchText + "%");
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				arrayList.add(new Book(rs.getString("ISBN"), rs.getString("title"), rs.getString("category"),
						rs.getString("writer"), rs.getString("company"), rs.getString("date"), rs.getString("fileimg"),
						rs.getString("information"), rs.getBoolean("rental")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (con != null)
					con.close();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		}
		return arrayList;
	}

	// ���� ���� �޼ҵ�
	public int deleteBook(Book selectBook) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {

			con = DBUtil.getConnection();
			String query = "delete from BookTBL where ISBN=?";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, selectBook.getIsbn());

			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("���� �Ϸ�");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("���� ����");
				alert.showAndWait();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return returnValue;
	}

	// ���� �̿�� (���� ��)
	public ArrayList<Statistical> getAllRentalCount(String month) {
		ArrayList<Statistical> arrayList = new ArrayList<Statistical>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			// String query = "select * from StatisticalTBL where date like ?;";
			String query = "select Rentaldate,category,count(category) from StatisticalTBL A left join BookTBL B on A.Book_ISBN=B.ISBN group by category having Rentaldate like ?;";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, "%" + month + "%");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				arrayList.add(new Statistical(rs.getString(1), rs.getString(2), rs.getInt(3)));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (con != null)
					con.close();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		}
		return arrayList;
	}

	// �������� å Ȯ�� �޼ҵ� (���� -> �뿩����å)
	public ArrayList<Book> searchRentalBook(String searchText) {
		ArrayList<Book> arrayList = new ArrayList<Book>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = null;
		try {
			con = DBUtil.getConnection();
			query = "select * from BookTBL where title like ?;";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, "%" + searchText + "%");

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				arrayList.add(new Book(rs.getString("ISBN"), rs.getString("title"), rs.getString("category"),
						rs.getString("writer"), rs.getString("company"), rs.getString("date"), rs.getString("fileimg"),
						rs.getString("information"), rs.getBoolean("rental")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (con != null)
					con.close();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		}
		return arrayList;
	}

	

}
