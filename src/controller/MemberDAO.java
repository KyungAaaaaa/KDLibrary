package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import model.Member;

public class MemberDAO {
	public static Member m; 


	// 유저 로그인정보 기억 메소드
	public Member loginUser(String name) {
		Member member = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			String query = "select * from memberTBL where Id = ?;";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, name);
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
}
