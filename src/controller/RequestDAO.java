package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.RequestBook;

public class RequestDAO {

	// 자료요청 테이블 전체보기
	public ArrayList<RequestBook> getRequestTbl() {
		ArrayList<RequestBook> arrayList = new ArrayList<RequestBook>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			String query = "select * from RequestTBL;";
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				arrayList.add(new RequestBook(rs.getString("title"), rs.getString("content"), rs.getString("name"),
						rs.getString("date"), rs.getInt("No")));
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

	// 자료요청 삭제메소드
	public int deleteRequest(RequestBook selectRequest) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = DBUtil.getConnection();
			String query = "delete from RequestTBL where No=?";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, selectRequest.getNo());
			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("요청 삭제");
				alert.setHeaderText("삭제 완료");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("요청 삭제");
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

	// 자료 신청
	public int requestBook(RequestBook r) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {

			con = DBUtil.getConnection();
			String query = "Insert into RequestTBL(No,title,name,content,date) values(NULL, ?, ?, ?, ?)";
			preparedStatement = con.prepareStatement(query);

			preparedStatement.setString(1, r.getTitle());
			preparedStatement.setString(2, r.getName());
			preparedStatement.setString(3, r.getContent());
			preparedStatement.setString(4, r.getDate());

			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("도서 신청");
				alert.setHeaderText("도서 신청완료");
				alert.setContentText("신청을 성공적으로 진행하였습니다");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("도서 신청");
				alert.setHeaderText("등록 실패");
				alert.showAndWait();
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("등록 실패 : DB에러");
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

}
