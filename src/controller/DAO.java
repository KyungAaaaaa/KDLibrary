package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Notice;
import model.Schedule;

public class DAO {
	// 일정 전체보기
	public ArrayList<Schedule> getSchedule(String date) {
		ArrayList<Schedule> schduleList=new ArrayList<Schedule>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			String query = "select * from ScheduleTBL where date=?;";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, date);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				schduleList.add(new Schedule(rs.getString("content"), rs.getString("date"),rs.getInt("no")));
			}

		} catch (Exception e31) {
			e31.printStackTrace();
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
		return schduleList;
	}
	
	//공지 전체보기
	public ArrayList<Notice> getNotice() {
		ArrayList<Notice> arrayList = new ArrayList<Notice>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();

			String query = "select * from noticeTBL";

			pstmt = con.prepareStatement(query);

			rs = pstmt.executeQuery();

			
			while (rs.next()) {
				Notice notice = new Notice(rs.getString("title"), rs.getString("content"), rs.getString("date"),
						rs.getInt("No"));
				arrayList.add(notice);
			}

			for (int i = 0; i < arrayList.size(); i++) {
				Notice n = arrayList.get(i);
			}

		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러발생");
			alert.setHeaderText("공지사항 불러오기 오류");
			alert.setContentText(e1.getMessage());
			alert.showAndWait();
		}
		return arrayList;
	}

	
}
