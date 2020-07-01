package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Notice;
import model.Schedule;
import model.Statistical;

public class DAO {

	// 일정 전체보기(유저)
	public ArrayList<Schedule> getScheduleMember() {
		ArrayList<Schedule> schduleList = new ArrayList<Schedule>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			String query = "SELECT * FROM ScheduleTBL";
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				schduleList.add(new Schedule(rs.getString("content"), rs.getString("date")));
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

	// 일정 보기(관리자)
	public ArrayList<Schedule> getSchedule(String date) {
		ArrayList<Schedule> schduleList = new ArrayList<Schedule>();
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
				schduleList.add(new Schedule(rs.getString("content"), rs.getString("date"), rs.getInt("no")));
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

	// 대여기록 전체보기
	public ArrayList<Statistical> getRentalList() {
		ArrayList<Statistical> arrayList = new ArrayList<Statistical>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();

			String query = "select No,Rentaldate,Id,ISBN,title,category from StatisticalTBL A Left join memberTBL B on A.Member_Id=B.Id\r\n"
					+ "Left join BookTBL C on A.Book_ISBN=C.ISBN;";
			pstmt = con.prepareStatement(query);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				arrayList.add(new Statistical(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6)));
			}

		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("대여기록 불러오기 오류");
			alert.setContentText(e1.getMessage());
			alert.showAndWait();
		}
		return arrayList;
	}

	// 월별 대출 기록 메소드
	public ArrayList<Statistical> searchRentalBookList(String searchMonth) {
		ArrayList<Statistical> arrayList = new ArrayList<Statistical>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = null;
		try {
			con = DBUtil.getConnection();

			query = "select No,DATE_FORMAT(Rentaldate,'%Y-%m-%d '),Id,ISBN,title,category from StatisticalTBL A Left join memberTBL B "
					+ "on A.Member_Id=B.Id Left join BookTBL C on A.Book_ISBN=C.ISBN where Rentaldate between date(?) and date(?)+1;";
			preparedStatement = con.prepareStatement(query); //
			if (searchMonth != "02") {
				preparedStatement.setString(1, LocalDate.now().getYear() + searchMonth + "01");
				preparedStatement.setString(2, LocalDate.now().getYear() + searchMonth + "30");
			} else {
				preparedStatement.setString(1, LocalDate.now().getYear() + searchMonth + "01");
				preparedStatement.setString(2, LocalDate.now().getYear() + searchMonth + "28");
			}

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				arrayList.add(new Statistical(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6)));
			}

		} catch (Exception e1) {
			e1.printStackTrace();
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

	// 설정한 날짜별 대출 기록 메소드
	public ArrayList<Statistical> searchRentalBookList(int searchDate1, int searchDate2) {
		ArrayList<Statistical> arrayList = new ArrayList<Statistical>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = null;
		try {
			con = DBUtil.getConnection();

			query = "select No,DATE_FORMAT(Rentaldate,'%Y-%m-%d '),Id,ISBN,title,category from StatisticalTBL A Left join memberTBL B "
					+ "on A.Member_Id=B.Id Left join BookTBL C on A.Book_ISBN=C.ISBN where Rentaldate between date(?) and date(?);";
			preparedStatement = con.prepareStatement(query); //
			preparedStatement.setInt(1, searchDate1);
			preparedStatement.setInt(2, searchDate2);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				arrayList.add(new Statistical(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6)));
			}

		} catch (Exception e1) {
			e1.printStackTrace();
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

	// 공지 전체보기
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

		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("에러");
			alert.setHeaderText("공지사항 불러오기 오류");
			alert.setContentText(e1.getMessage());
			alert.showAndWait();
		}
		return arrayList;
	}

	// 공지 삭제 메소드
	public int deleteNotice(Notice selectNotice) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = DBUtil.getConnection();
			String query = "delete from noticeTBL where no = ?";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, selectNotice.getNo());
			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("공지 삭제");
				alert.setHeaderText("삭제 완료");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("공지 삭제");
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

	// 공지 추가 메소드
	public int addNotice(Notice notice) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {

			con = DBUtil.getConnection();
			String query = "Insert into noticeTBL (No, title, content,date) values(NULL, ?, ?, ?)";
			preparedStatement = con.prepareStatement(query);

			preparedStatement.setString(1, notice.getTitle());
			preparedStatement.setString(2, notice.getContent());
			preparedStatement.setString(3, notice.getDate());
			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("공지 추가");
				alert.setHeaderText("등록 완료");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("공지 추가");
				alert.setHeaderText("등록 실패");
				alert.showAndWait();
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.INFORMATION);
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

	// 공지 수정 메소드
	public int editNotice(Notice selectNotice) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = DBUtil.getConnection();
			String query = "update noticeTBL set title = ?, content = ?, date = ? where No = ?";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, selectNotice.getTitle());
			preparedStatement.setString(2, selectNotice.getContent());
			preparedStatement.setString(3, selectNotice.getDate());
			preparedStatement.setInt(4, selectNotice.getNo());
			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("공지 수정");
				alert.setHeaderText("수정 완료");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("공지 수정");
				alert.setHeaderText("수정 실패");
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
	// 일정 수정 메소드
	public int editSchedule(Schedule selectSchedule) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = DBUtil.getConnection();
			String query = "update ScheduleTBL set content = ? where no = ?";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, selectSchedule.getContent());
			preparedStatement.setInt(2, selectSchedule.getNo());
			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("일정 수정");
				alert.setHeaderText("수정 완료");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("일정 수정");
				alert.setHeaderText("수정 실패");
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

	// 일정 추가 메소드
	public int addSchedule(Schedule schedule) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {

			con = DBUtil.getConnection();
			String query = "Insert into ScheduleTBL(`content`,`date`,`No`) values(?,?,null);";
			preparedStatement = con.prepareStatement(query);

			preparedStatement.setString(1, schedule.getContent());
			preparedStatement.setString(2, schedule.getDate());
			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("일정 추가");
				alert.setHeaderText("등록 완료");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("일정 추가");
				alert.setHeaderText("등록 실패");
				alert.showAndWait();
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.INFORMATION);
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

	// 일정 삭제 메소드
	public int deleteSchedule(Schedule schedule) {
		int returnValue = 0;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		try {
			con = DBUtil.getConnection();
			String query = "delete from ScheduleTBL where no = ?";
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, schedule.getNo());
			returnValue = preparedStatement.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("일정 삭제");
				alert.setHeaderText("삭제 완료");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("일정 삭제");
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
