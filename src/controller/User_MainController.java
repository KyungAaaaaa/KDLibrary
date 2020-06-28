
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Book;
import model.Member;
import model.Notice;
import model.RequestBook;
import model.Schedule;

public class User_MainController implements Initializable {
	
	@FXML
	private Button btnUserModify;
	@FXML
	private Button btnOut;
	@FXML
	private Button btnBookSearch;
	@FXML
	private Button btnBookApply;
	@FXML
	private Button btnBookRental;
	@FXML
	private Button btnNotice;
	@FXML
	private Button btnCalendar;
	public Stage stage;
	@FXML
	private ImageView imgV;
	@FXML
	private Label lblName;
	@FXML
	private AnchorPane A;
	MemberDAO memberDao = new MemberDAO();
	SimpleDateFormat format1 = new SimpleDateFormat("yyyy�� MM�� dd��");
	Date time = new Date();
	String BookApplytime = format1.format(time);
	ArrayList<Book> bookList;
	ArrayList<Schedule> schList = new ArrayList<Schedule>();
	private ObservableList<Notice> obsList = FXCollections.observableArrayList();
	private ObservableList<Member> obsList2;
	private ObservableList<Schedule> obsListS= FXCollections.observableArrayList();
	private int tableViewselectedIndex;
	public Stage stage2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//lblName.setText(memberDao.m.getName());
		
		// �α���â���� ���ư�
		btnOut.setOnAction(event -> handlerBtnOut(event));

		// �뿩���� ���� Ȯ��(�ݳ�)
		btnBookRental.setOnAction(e -> setReturnRentalBook());

		// ȸ������ ����
		btnUserModify.setOnAction(event -> handlerBtnUserModify(event));

		// �ڷ� �˻�
		btnBookSearch.setOnAction(event -> handlerBtnBookSearch(event));

		// ����
		btnCalendar.setOnAction(event -> handlerBtnCalendar(event));

		// ��������
		btnNotice.setOnAction(event -> handlerBtnNotice(event));

		// �ڷ��û
		btnBookApply.setOnAction(event -> handlerBtnBookApply(event));

	}

	private void setReturnRentalBook() {
		try {
			BookDAO dao = new BookDAO();
			bookList = dao.searchBook(memberDao.m.getRentalBook(), "ISBN");
			getRentalBookInformationPopup(bookList.get(0));

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("�뿩���� ������ �����ϴ�.");
			alert.showAndWait();
		}

	}



	// �뿩���� ���� ����(�ݳ�)
	private void getRentalBookInformationPopup(Book b) {
		try {
			Parent userModifyView = FXMLLoader.load(getClass().getResource("/view/user_bookInformation.fxml"));
			Scene scene = new Scene(userModifyView);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			Stage userModifyStage = new Stage(StageStyle.UTILITY);
			Label lbTitle = (Label) scene.lookup("#lbTitle");
			Label lbISBN = (Label) scene.lookup("#lbISBN");
			Label lbWriter = (Label) scene.lookup("#lbWriter");
			Label lbCompany = (Label) scene.lookup("#lbCompany");
			Label lbDate = (Label) scene.lookup("#lbDate");
			TextArea txaInformation = (TextArea) scene.lookup("#txaInformation");
			Button btnClose = (Button) scene.lookup("#btnClose");
			Button btnRental = (Button) scene.lookup("#btnRental");
			ImageView imgV = (ImageView) scene.lookup("#imgV");
			btnRental.setText("�ݳ�");
			lbTitle.setText(b.getTitle());
			lbISBN.setText(b.getIsbn());
			lbWriter.setText(b.getWriter());
			lbCompany.setText(b.getCompany());
			lbDate.setText(b.getDate());
			txaInformation.setText(b.getInformation());
			String selectFileName = b.getFileimg();
			String localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
			imgV.setImage(new Image(localUrl));
			// userModifyStage.initModality(Modality.WINDOW_MODAL);
			// userModifyStage.initOwner(stage2);
			userModifyStage.initOwner(this.stage);
			userModifyStage.setScene(scene);
			userModifyStage.setResizable(false);
			userModifyStage.setTitle("å ����");
			userModifyStage.show();
			btnClose.setOnAction(e -> userModifyStage.close());
			btnRental.setOnAction(e -> {
				MemberDAO dao = new MemberDAO();
				Connection con1 = null;
				PreparedStatement preparedStatement1 = null;
				PreparedStatement preparedStatement2 = null;
				try {
					con1 = DBUtil.getConnection(); //
					String query1 = "update memberTBL set rentalBook=? where Id=?";
					String query2 = "update BookTBL set rental=? where ISBN=?";
					preparedStatement1 = con1.prepareStatement(query1);
					preparedStatement2 = con1.prepareStatement(query2);
					preparedStatement1.setString(1, null);
					preparedStatement1.setString(2, dao.m.getId());
					preparedStatement2.setBoolean(1, false);
					preparedStatement2.setString(2, b.getIsbn());

					if (preparedStatement1.executeUpdate() != 0 && preparedStatement2.executeUpdate() != 0) {
						memberDao.m.setRentalBook(null);
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("�ݳ� �Ϸ�");
						alert.showAndWait();
						userModifyStage.close();
						bookList.clear();
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("�ݳ� ����");
						alert.showAndWait();
						throw new Exception();
					}
				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("��� ���� DB����");
					alert.showAndWait();
				}
			});
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("�����߻�");
			alert.setHeaderText("��������â �θ��� ����");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	// �ڷ�˻�
	private void handlerBtnBookSearch(ActionEvent event) {

		Parent mainView = null;
		Stage mainStage = null;
		try {
			mainView = FXMLLoader.load(getClass().getResource("/view/user_BookSearch.fxml"));
			Scene scene = new Scene(mainView);
			scene.getStylesheets().add(getClass().getResource("/css/user_Main.css").toString());
			mainStage = new Stage();
			mainStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			mainStage.setTitle("KD Library");
			mainStage.setScene(scene);
			mainStage.setResizable(true);
			((Stage) btnOut.getScene().getWindow()).close();
			mainStage.show();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("�����߻�");
			alert.setHeaderText("�ڷ�˻�â �θ��� ����");
			alert.showAndWait();
		}

	}

	// �α���â���� ���ư�
	private void handlerBtnOut(ActionEvent event) {
		Parent mainView = null;
		Stage mainStage = null;
		try {
			mainView = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
			Scene scene = new Scene(mainView);

			mainStage = new Stage();
			mainStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			mainStage.setTitle("KD Library");
			mainStage.setScene(scene);
			mainStage.setResizable(true);
			((Stage) btnOut.getScene().getWindow()).close();
			mainStage.show();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("�����߻�");
			alert.setHeaderText("����â �θ��� ����");
			alert.showAndWait();
		}
	}

	// �������� ����
	private void handlerBtnUserModify(ActionEvent event) {
		try {

			Parent userModifyView = FXMLLoader.load(getClass().getResource("/view/user_changingInformation.fxml"));
			Scene scene = new Scene(userModifyView);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			Stage userModifyStage = new Stage(StageStyle.UTILITY);
			
			PasswordField txtPass = (PasswordField) scene.lookup("#txtPass");
			Button btnUserModifyNo = (Button) scene.lookup("#btnNo");
			Button btnUserModifyOk = (Button) scene.lookup("#btnOk");

			btnUserModifyOk.setOnAction(event1 -> {
				if(txtPass.getText().equals("")) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("��й�ȣ �̱���");
					alert.setHeaderText("��й�ȣ�� �Է��ϼ���.");
					alert.showAndWait();
					return;
				}
				if (txtPass.getText().equals(memberDao.m.getPass())) {
					userModifyStage.close();
					try {
						Parent userModifyView2 = FXMLLoader
								.load(getClass().getResource("/view/user_changingInformation2.fxml"));
						Scene scene1 = new Scene(userModifyView2);
						scene1.getStylesheets().add(getClass().getResource("/application/main.css").toString());
						Stage userModifyStage2 = new Stage(StageStyle.UTILITY);

						Label lblId2 = (Label) scene1.lookup("#lblId");
						Label lblBirth2 = (Label) scene1.lookup("#lblBirth");
						Label lbName = (Label) scene1.lookup("#lbName");
						TextField txtPass2 = (TextField) scene1.lookup("#txtPass");
						TextField txtPhone2 = (TextField) scene1.lookup("#txtPhone");
						Button btnModifyNo2 = (Button) scene1.lookup("#btnNo");
						Button btnModifyAdd2 = (Button) scene1.lookup("#btnAdd");
						
						lblId2.setText(memberDao.m.getId());
						lblBirth2.setText(memberDao.m.getBirth());
						txtPass2.setText(memberDao.m.getPass());
						lbName.setText(memberDao.m.getName());
						txtPhone2.setText(memberDao.m.getPhoneNumber());

						btnModifyAdd2.setOnAction(event2 -> {
							if(txtPass2.getText().equals("")||txtPhone2.getText().equals("")) {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("���� �̱���");
								alert.setHeaderText("��� �׸��� �Է��ϼ���.");
								alert.showAndWait();
								return;
							}
							Connection con = null;
							PreparedStatement pstmt = null;
							try {
								con = DBUtil.getConnection();
								String query = "update memberTBL set pass = ?, phoneNumber = ? where id = ?";
								pstmt = con.prepareStatement(query);
								pstmt.setString(1, txtPass2.getText());
								pstmt.setString(2, txtPhone2.getText());
								pstmt.setString(3, memberDao.m.getId());

								int userModify = pstmt.executeUpdate();

								if (userModify != 0) {
									memberDao.m.setPass(txtPass2.getText());
									memberDao.m.setPhoneNumber(txtPhone2.getText());
									Alert alert = new Alert(AlertType.INFORMATION);
									alert.setTitle("��������");
									alert.setHeaderText("ȸ������ ������ �Ϸ�Ǿ����ϴ�");
									alert.showAndWait();
									userModifyStage2.close();
								} else {
									throw new Exception();
								}
							} catch (Exception e) {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("�����߻�");
								alert.setHeaderText("����â ����");
								alert.setContentText(e.getMessage());
								alert.showAndWait();
							}
						});

						userModifyStage2.initModality(Modality.WINDOW_MODAL);
						userModifyStage2.initOwner(stage);
						userModifyStage2.setScene(scene1);
						userModifyStage2.setResizable(false);
						userModifyStage2.setTitle("��������");
						userModifyStage2.show();

						btnModifyNo2.setOnAction(event2 -> userModifyStage2.close());

					} catch (IOException e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("�����߻�");
						alert.setHeaderText("��������â �θ��� ����");
						alert.setContentText(e.getMessage());
						alert.showAndWait();
					}
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ȸ������ ���ԺҰ�");
					alert.setHeaderText("��й�ȣ�� Ȯ���ϼ���");
					alert.showAndWait();
				}
			});
			userModifyStage.initModality(Modality.WINDOW_MODAL);
			userModifyStage.initOwner(stage);
			userModifyStage.setScene(scene);
			userModifyStage.setResizable(false);
			userModifyStage.setTitle("����Ȯ��");
			userModifyStage.show();
			btnUserModifyNo.setOnAction(event1 -> userModifyStage.close());
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("�����߻�");
			alert.setHeaderText("��������â �θ��� ����");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	// ����ǥ
	private void handlerBtnCalendar(ActionEvent event) {
		Stage userScheduleStage=null;
		try {
			Parent userScheduleView = FXMLLoader.load(getClass().getResource("/view/user_schedule2.fxml"));
			Scene scene = new Scene(userScheduleView);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			userScheduleStage = new Stage(StageStyle.UTILITY);
			//TableView tblUserSchedule = (TableView) scene.lookup("#tblSchedule");
			//Button btnUserScheduleNo = (Button) scene.lookup("#btnNo");
			/*
			 * TableColumn colDate = new TableColumn("�� ¥"); colDate.setMaxWidth(115);
			 * colDate.setStyle("-fx-allignment: CENTER"); colDate.setCellValueFactory(new
			 * PropertyValueFactory("date"));
			 * 
			 * TableColumn colContent = new TableColumn("�� ��");
			 * colContent.setPrefWidth(550); colContent.setStyle("-fx-allignment: CENTER");
			 * colContent.setCellValueFactory(new PropertyValueFactory("content"));
			 * tblUserSchedule.getColumns().addAll(colDate,colContent);
			 * 
			 * Connection con = null; PreparedStatement pstmt = null; ResultSet rs = null;
			 * try { con=DBUtil.getConnection(); String query = "SELECT * FROM ScheduleTBL";
			 * pstmt = con.prepareStatement(query); rs=pstmt.executeQuery();
			 * 
			 * while(rs.next()) { Schedule schedule = new
			 * Schedule(rs.getString("content"),rs.getString("date"));
			 * schList.add(schedule); }
			 * 
			 * for(int i=0; i<schList.size(); i++) { Schedule s = schList.get(i);
			 * obsListS.add(s); }
			 * 
			 * tblUserSchedule.setItems(obsListS);
			 */
				
			userScheduleStage.initModality(Modality.WINDOW_MODAL);
			userScheduleStage.initOwner(stage);
			userScheduleStage.setScene(scene);
			userScheduleStage.setResizable(false);
			userScheduleStage.setTitle("����ǥ");
			userScheduleStage.show();
			//btnUserScheduleNo.setOnAction(event1 -> userScheduleStage.close());
			/*
			 * } catch (Exception e) { Alert alert = new Alert(AlertType.ERROR);
			 * alert.setTitle("�����߻�"); alert.setHeaderText("����ǥ �ҷ����� ����");
			 * alert.setContentText(e.getMessage()); alert.showAndWait(); }
			 */
			
			
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("�����߻�");
			alert.setHeaderText("����ǥ �θ��� ����");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	// ��������
	private void handlerBtnNotice(ActionEvent event) {
		try {
			Parent userNoticeView = FXMLLoader.load(getClass().getResource("/view/user_notice.fxml"));
			Scene scene = new Scene(userNoticeView);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			Stage userNoticeStage = new Stage(StageStyle.UTILITY);

			TableView tblUserNotice = (TableView) scene.lookup("#tblNotice");
			Button btnUserNoticeNo = (Button) scene.lookup("#btnNo");
			Button btnUserNoticeAdd = (Button) scene.lookup("#btnAdd");
			Button btnUserNoticeDelete = (Button) scene.lookup("#btnDelete");

			TableColumn colNo = new TableColumn("No");
			colNo.setMaxWidth(30);
			colNo.setStyle("-fx-allignment: CENTER");
			colNo.setCellValueFactory(new PropertyValueFactory("no"));

			TableColumn colTitle = new TableColumn("�� ��");
			colTitle.setPrefWidth(90);
			colTitle.setStyle("-fx-allignment: CENTER");
			colTitle.setCellValueFactory(new PropertyValueFactory("title"));

			TableColumn colContent = new TableColumn("�� ��");
			colContent.setPrefWidth(400);
			colContent.setStyle("-fx-allignment: CENTER");
			colContent.setCellValueFactory(new PropertyValueFactory("content"));

			TableColumn colDate = new TableColumn("�ۼ���¥");
			colDate.setPrefWidth(115);
			colDate.setStyle("-fx-allignment: CENTER");
			colDate.setCellValueFactory(new PropertyValueFactory("date"));

			tblUserNotice.getColumns().addAll(colNo, colTitle, colContent, colDate);
			tblUserNotice.setItems(obsList);
			
			tblUserNotice.setOnMousePressed(event1->{
				tableViewselectedIndex=tblUserNotice.getSelectionModel().getSelectedIndex();
			});
			
			tblUserNotice.setOnMouseClicked(event1-> {
				if(event1.getClickCount()>1) {
					try {
						Parent userNotView = FXMLLoader.load(getClass().getResource("/view/user_NoticeView.fxml"));
						Scene scene1 = new Scene(userNotView);
						Stage userNotStage = new Stage();
						
						TextField txtTitleNotView = (TextField) scene1.lookup("#txtTitle");
						Label lblDateNotView = (Label) scene1.lookup("#lblDate");
						TextArea txaContentNotView = (TextArea) scene1.lookup("#txaContent");
						Button btnNoNotView = (Button) scene1.lookup("#btnNo");
						
						Notice noti = obsList.get(tableViewselectedIndex);
						txtTitleNotView.setText(noti.getTitle());
						lblDateNotView.setText(noti.getDate());
						txaContentNotView.setText(noti.getContent());
						
						
						userNotStage.setScene(scene1);
						userNotStage.setResizable(false);
						userNotStage.setTitle("��������");
						userNotStage.show();
						
						btnNoNotView.setOnAction(event2-> userNotStage.close());
						
					} catch (IOException e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("�����߻�");
						alert.setHeaderText("�������׺� �θ��� ����");
						alert.setContentText(e.getMessage());
						alert.showAndWait();
					}
				}
			});

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				con = DBUtil.getConnection();

				String query = "select * from noticeTBL";

				pstmt = con.prepareStatement(query);

				rs = pstmt.executeQuery();

				ArrayList<Notice> arrayList = new ArrayList<Notice>();
				while (rs.next()) {
					Notice notice = new Notice(rs.getString("title"), rs.getString("content"), rs.getString("date"),
							rs.getInt("No"));
					arrayList.add(notice);
				}

				for (int i = 0; i < arrayList.size(); i++) {
					Notice n = arrayList.get(i);
					obsList.add(n);
				}

			} catch (Exception e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("�����߻�");
				alert.setHeaderText("DB �����ϼ���");
				alert.setContentText(e1.getMessage());
				alert.showAndWait();
			}

			userNoticeStage.initModality(Modality.WINDOW_MODAL);
			userNoticeStage.initOwner(stage);
			userNoticeStage.setScene(scene);
			userNoticeStage.setResizable(false);
			userNoticeStage.setTitle("��������");
			userNoticeStage.show();
			btnUserNoticeNo.setOnAction(event1 -> userNoticeStage.close());

			btnUserNoticeAdd.setVisible(false);
			btnUserNoticeAdd.setDisable(true);
			btnUserNoticeDelete.setVisible(false);
			btnUserNoticeDelete.setDisable(true);
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("�����߻�");
			alert.setHeaderText("�������� �θ��� ����");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	// �ڷ��û
	private void handlerBtnBookApply(ActionEvent event) {
		try {
			Parent userRequestPopUpView = FXMLLoader.load(getClass().getResource("/view/requestPopUp.fxml"));
			Scene scene = new Scene(userRequestPopUpView);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			Stage userRequestPopUpStage = new Stage(StageStyle.UTILITY);

			Button btnUserPopUpAdd = (Button) scene.lookup("#btnAdd");
			Button btnUserPopUpBack = (Button) scene.lookup("#btnBack");
			Label lblUserPopUpDate = (Label) scene.lookup("#lbDate");
			Label lblUserPopUpName = (Label) scene.lookup("#lbName");
			TextField txfUserPopUpTitle = (TextField) scene.lookup("#txtTitle");
			TextArea txaUserPopUpContent = (TextArea) scene.lookup("#txaContent");
			lblUserPopUpName.setText(memberDao.m.getName());
			btnUserPopUpAdd.setText("�ڷ� ��û");

			btnUserPopUpAdd.setOnAction(event1 -> {
				Connection con = null;
				PreparedStatement pstmt = null;

				try {
					if (txfUserPopUpTitle.getText().trim().equals("")
							|| txaUserPopUpContent.getText().trim().equals(""))
						throw new Exception();
					con = DBUtil.getConnection();
					String query = "Insert into RequestTBL(No,title,name,content,date) values(NULL, ?, ?, ?, ?)";
					pstmt = con.prepareStatement(query);

					RequestBook r = new RequestBook(txfUserPopUpTitle.getText(), txaUserPopUpContent.getText(),
							lblUserPopUpName.getText(), lblUserPopUpDate.getText());
					pstmt.setString(1, r.getTitle());
					pstmt.setString(2, r.getName());
					pstmt.setString(3, r.getContent());
					pstmt.setString(4, r.getDate());

					int BookApply = pstmt.executeUpdate();
					if (BookApply != 0) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("������û");
						alert.setHeaderText("���� ��û�Ϸ�");
						alert.setContentText("��û�� ���������� �����Ͽ����ϴ�");
						alert.showAndWait();
						userRequestPopUpStage.close();
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("�����߻�");
						alert.setHeaderText("���Ի����� �����ϼ���");
						alert.showAndWait();
					}

				} catch (Exception e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("�����߻�");
					alert.setHeaderText("���� ����� ������ �Է��ϼ���");
					alert.showAndWait();
				}
			});

			userRequestPopUpStage.initModality(Modality.WINDOW_MODAL);
			userRequestPopUpStage.initOwner(stage);
			userRequestPopUpStage.setScene(scene);
			userRequestPopUpStage.setResizable(false);
			userRequestPopUpStage.setTitle("�ڷ��û");
			userRequestPopUpStage.show();

			btnUserPopUpBack.setOnAction(event1 -> userRequestPopUpStage.close());
			lblUserPopUpDate.setText(BookApplytime);

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("�����߻�");
			alert.setHeaderText("�ڷ��ûâ �θ��� ����");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

}
