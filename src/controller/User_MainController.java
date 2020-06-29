
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
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
	public Stage userStage;
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
	private ObservableList<Schedule> obsListS = FXCollections.observableArrayList();
	private int tableViewselectedIndex;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

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

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// �뿩���� ���� �ݳ� ��ư �ڵ鷯 �Լ�
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
			Stage userModifyStage = new Stage();
			userModifyStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
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
			userModifyStage.initModality(Modality.WINDOW_MODAL);
			userModifyStage.initOwner(this.userStage);
			userModifyStage.setScene(scene);
			userModifyStage.setResizable(false);
			userModifyStage.setTitle("å ����");
			userModifyStage.show();
			btnClose.setOnAction(e -> userModifyStage.close());
			btnRental.setOnAction(e -> {

				if (memberDao.getRentalBookInformationPopup(b)) {
					memberDao.m.setRentalBook(null);
					userModifyStage.close();
					bookList.clear();
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

	// �ڷ�˻� : â ��ȯ
	private void handlerBtnBookSearch(ActionEvent event) {

		try {
			Stage mainStage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/user_BookSearch.fxml"));
			Parent root = fxmlLoader.load();
			User_BookSearchController userBookSearchController = fxmlLoader.getController();
			userBookSearchController.stage = mainStage;

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			mainStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			mainStage.setTitle("KD Library");
			mainStage.setScene(scene);
			mainStage.initOwner(userStage);
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

	// �α׾ƿ�
	private void handlerBtnOut(ActionEvent event) {
		try {
			Stage mainStage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
			Parent root = fxmlLoader.load();
			RootController rootController = fxmlLoader.getController();
			rootController.stage = mainStage;
			mainStage.initOwner(this.userStage);

			Scene scene = new Scene(root);
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
			Stage userModifyStage = new Stage();
			userModifyStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			PasswordField txtPass = (PasswordField) scene.lookup("#txtPass");
			Button btnUserModifyNo = (Button) scene.lookup("#btnNo");
			Button btnUserModifyOk = (Button) scene.lookup("#btnOk");

			btnUserModifyOk.setOnAction(event1 -> {
				if (txtPass.getText().equals("")) {
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
						Stage userModifyStage2 = new Stage();
						userModifyStage2.getIcons()
								.add(new Image(getClass().getResource("/image/logo.png").toString()));

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
							if (txtPass2.getText().equals("") || txtPhone2.getText().equals("")) {
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
						userModifyStage2.initOwner(userStage);
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
			userModifyStage.initOwner(userStage);
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
		try {
			obsListS.clear();
			schList.clear();
			DAO dao = new DAO();
			Parent userScheduleView = FXMLLoader.load(getClass().getResource("/view/user_schedule2.fxml"));
			Scene scene = new Scene(userScheduleView);
			Button btnSchExit = (Button) scene.lookup("#btnExit");
			Stage userScheduleStage = new Stage();
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			userScheduleStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));

			ArrayList<Button> dateBtnList = new ArrayList<Button>();
			for (int i = 1; i <= 30; i++) {
				dateBtnList.add((Button) scene.lookup("#btn" + i));
			}

			for (Button b : dateBtnList) {
				b.setStyle("-fx-background-color:#00ff0000;-fx-text-fill:#00ff0000;");
				b.setDisable(true);
			}

			schList = dao.getScheduleMember();
			for (Schedule s : schList) {
				for (Button b : dateBtnList) {
					if (s.getDate().equals(b.getText())) {
						b.setDisable(false);
						b.setStyle("-fx-text-fill:#00ff0000;-fx-background-color:#F7AA97;");
					}
				}
			}

			for (Button b : dateBtnList) {
				b.setOnAction(e -> {
					ObservableList<String> obSchdule = FXCollections.observableArrayList();
					ArrayList<Schedule> schduleList = new ArrayList<Schedule>();
					Popup popup = new Popup();
					Parent root = null;
					try {
						root = FXMLLoader.load(getClass().getResource("/view/popup.fxml"));
					} catch (IOException e1) {
					}
					Label lbDate = (Label) root.lookup("#lbDate");
					ListView list = (ListView) root.lookup("#list");
					obSchdule.clear();
					schduleList = dao.getSchedule(b.getText());
					for (int i = 0; i < schduleList.size(); i++) {
						obSchdule.add(schduleList.get(i).getContent());
					}
					list.setItems(obSchdule);
					lbDate.setText(b.getText());
					popup.getContent().add(root);
					popup.setAutoHide(true);
					popup.show(userScheduleStage);
				});

			}
			userScheduleStage.initModality(Modality.WINDOW_MODAL);
			userScheduleStage.initOwner(userStage);
			userScheduleStage.setScene(scene);
			userScheduleStage.setResizable(false);
			userScheduleStage.setTitle("����ǥ");
			userScheduleStage.show();
			btnSchExit.setOnAction(eve-> userScheduleStage.close());
		} catch (Exception e) {
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
			obsList.clear();
			Parent userNoticeView = FXMLLoader.load(getClass().getResource("/view/user_notice.fxml"));
			Scene scene = new Scene(userNoticeView);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			Stage userNoticeStage = new Stage();
			userNoticeStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
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

			tblUserNotice.setOnMousePressed(event1 -> {
				tableViewselectedIndex = tblUserNotice.getSelectionModel().getSelectedIndex();
			});

			tblUserNotice.setOnMouseClicked(event1 -> {
				if (event1.getClickCount() > 1) {
					try {
						Parent userNotView = FXMLLoader.load(getClass().getResource("/view/user_NoticeView.fxml"));
						Scene scene1 = new Scene(userNotView);
						Stage userNotStage = new Stage();
						scene1.getStylesheets().add(getClass().getResource("/application/main.css").toString());

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

						btnNoNotView.setOnAction(event2 -> userNotStage.close());

					} catch (IOException e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("�����߻�");
						alert.setHeaderText("�������׺� �θ��� ����");
						alert.setContentText(e.getMessage());
						alert.showAndWait();
					}
				}
			});

			DAO dao = new DAO();
			ArrayList<Notice> arrayList = new ArrayList<Notice>();
			arrayList = dao.getNotice();

			for (Notice n : arrayList) {
				obsList.add(n);
			}

			userNoticeStage.initModality(Modality.WINDOW_MODAL);
			userNoticeStage.initOwner(userStage);
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
			Stage userRequestPopUpStage = new Stage();
			userRequestPopUpStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));

			Button btnUserPopUpAdd = (Button) scene.lookup("#btnAdd");
			Button btnUserPopUpBack = (Button) scene.lookup("#btnBack");
			Label lblUserPopUpDate = (Label) scene.lookup("#lbDate");
			Label lblUserPopUpName = (Label) scene.lookup("#lbName");
			TextField txfUserPopUpTitle = (TextField) scene.lookup("#txtTitle");
			TextArea txaUserPopUpContent = (TextArea) scene.lookup("#txaContent");
			lblUserPopUpName.setText(memberDao.m.getName());
			btnUserPopUpAdd.setText("�ڷ� ��û");

			btnUserPopUpAdd.setOnAction(event1 -> {
				if (txfUserPopUpTitle.getText().trim().equals("") || txaUserPopUpContent.getText().trim().equals("")) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("�����߻�");
					alert.setHeaderText("���� ����� ������ �Է��ϼ���");
					alert.showAndWait();
					return;
				}
				RequestBook r = new RequestBook(txfUserPopUpTitle.getText(), txaUserPopUpContent.getText(),
						lblUserPopUpName.getText(), lblUserPopUpDate.getText());
				RequestDAO dao = new RequestDAO();

				int bookApply = dao.requestBook(r);
				if (bookApply != 0) {
					userRequestPopUpStage.close();
				}
			});

			userRequestPopUpStage.initModality(Modality.WINDOW_MODAL);
			userRequestPopUpStage.initOwner(userStage);
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
