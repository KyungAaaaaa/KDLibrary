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


	
	// �뿩���� ���� ����(�ݳ�)
	public boolean getRentalBookInformationPopup(Book b) {
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
			if (preparedStatement1.executeUpdate() != 0 && preparedStatement2.executeUpdate() != 0)
				returnValue = true;
			if (returnValue) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("���� �ݳ�");
				alert.setHeaderText("�ݳ� �Ϸ�");
				alert.showAndWait();

			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("���� �ݳ�");
				alert.setHeaderText("�ݳ� ����");
				alert.setContentText("�����ڿ��� �����ϼ���.");
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

	
	// ���� �α������� ��� �޼ҵ�
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

	// ���� ���̺� ��ü����
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

	// ���� �˻� �޼ҵ�
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

	// ���� �����޼ҵ�
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
				alert.setTitle("ȸ�� ����");
				alert.setHeaderText("���� �Ϸ�");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ȸ�� ����");
				alert.setHeaderText("���� ����");
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
	
	// ���� ���� �޼ҵ�
	public int editUser(Member selectUser) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = DBUtil.getConnection();
			String query = "update memberTBL set name=?,pass=?,phoneNumber=?,birth=?,etc=? where Id=?";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, selectUser.getName());
			preparedStatement.setString(2, selectUser.getPass());
			preparedStatement.setString(3, selectUser.getPhoneNumber());
			preparedStatement.setString(4,selectUser.getBirth());
			preparedStatement.setString(5,selectUser.getEtc());
			preparedStatement.setString(6, selectUser.getId());
			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("ȸ�� ����");
				alert.setHeaderText("���� �Ϸ�");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ȸ�� ����");
				alert.setHeaderText("���� ����");
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
	
	// �������� ���� �޼ҵ�
		public int editUser2() {
			int returnValue = 0;
			Connection con = null;
			PreparedStatement preparedStatement = null;
			try {
				con = DBUtil.getConnection();
				String query = "update memberTBL set pass = ?, phoneNumber = ? where id = ?";
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, MemberDAO.m.getName());
				preparedStatement.setString(2, MemberDAO.m.getPass());
				preparedStatement.setString(3, MemberDAO.m.getId());

				returnValue = preparedStatement.executeUpdate();
				if (returnValue != 0) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("���� ���� ����");
					alert.setHeaderText("���� �Ϸ�");
					alert.showAndWait();
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("���� ���� ����");
					alert.setHeaderText("���� ����");
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
