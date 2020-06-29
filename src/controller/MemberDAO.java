package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Book;
import model.Member;

public class MemberDAO {
	public static Member m;


	
	// 대여중인 도서 정보(반납)
	public boolean getRentalBookInformationPopup(Book b) {
		int returnValue1 = 0;
		int returnValue2 = 0;
		boolean returnValue = false;
		Connection con = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		try {
			con = DBUtil.getConnection();
			String query1 = "update memberTBL set rentalBook=? where Id=?";
			String query2 = "update BookTBL set rental=? where ISBN=?";
			preparedStatement1 = con.prepareStatement(query1);
			preparedStatement2 = con.prepareStatement(query2);
			preparedStatement1.setString(1, null);
			preparedStatement1.setString(2, MemberDAO.m.getId());
			preparedStatement2.setBoolean(1, false);
			preparedStatement2.setString(2, b.getIsbn());
			returnValue1 = preparedStatement1.executeUpdate();
			returnValue2 = preparedStatement1.executeUpdate();
			if (preparedStatement1.executeUpdate() != 0 && preparedStatement2.executeUpdate() != 0)
				returnValue = true;
			if (returnValue) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("반납 완료");
				alert.showAndWait();

			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("반납 실패");
				alert.setContentText("관리자에게 문의하세요.");
				alert.showAndWait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement1 != null)
					preparedStatement1.close();
				if (preparedStatement2 != null)
					preparedStatement2.close();
				if (con != null)
					con.close();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		}

		return returnValue;

	}

	
	// 유저 로그인정보 기억 메소드
	public Member loginUser(String id,String pass) {
		Member member = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			String query = "select * from memberTBL where Id = ? and pass = ?";
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, id);
			preparedStatement.setString(2,pass);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				member = new Member(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7));
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
		return member;

	}

	// 유저 테이블 전체보기
	public ArrayList<Member> getUserTbl() {
		ArrayList<Member> arrayList = new ArrayList<Member>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			String query = "select * from memberTBL;";
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				arrayList.add(new Member(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7)));
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

	// 유저 검색 메소드
	public ArrayList<Member> searchUser(String userName) {
		ArrayList<Member> arrayList = new ArrayList<Member>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			String query = "select * from memberTBL where name like ?;";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, "%" + userName + "%");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				arrayList.add(new Member(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7)));
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

	// 유저 삭제메소드
	public int deleteUser(Member selectUser) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = DBUtil.getConnection();
			String query = "delete from memberTBL where Id=?";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, selectUser.getId());
			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("삭제 완료");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("삭제 실패");
				alert.showAndWait();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
}
