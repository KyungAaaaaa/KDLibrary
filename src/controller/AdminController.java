package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import com.sun.javafx.scene.control.skin.DatePickerSkin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Book;
import model.Notice;
import model.Schedule;
import model.Statistical;

public class AdminController implements Initializable {
	public Stage stage;
	int schduleCount = 0;
	@FXML
	Button btnLogout;
	@FXML
	Button btnManagement;
	@FXML
	Button btnNotice;
	@FXML
	Button btnSchedule;
	@FXML
	Button btnReantalList;
	ArrayList<Schedule> schduleList = new ArrayList<Schedule>();
	private ObservableList<Statistical> obsListRentalList = FXCollections.observableArrayList();
	private ObservableList<Notice> obsListN = FXCollections.observableArrayList();
	private ObservableList<Schedule> obsListS = FXCollections.observableArrayList();
	ArrayList<Notice> arrayList = null;
	ArrayList<Schedule> arrayList2 = null;
	private int tableViewselectedIndex;
	private int tableViewselectedIndex2;
	SimpleDateFormat format2 = new SimpleDateFormat("yyyy�� MM�� dd��");
	Date time = new Date();
	String Noticetime = format2.format(time);
	String date = LocalDate.now().toString();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// �α׾ƿ� ��ư : �α���ȭ������ ���ư���
		btnLogout.setOnAction(e -> handleBtnLogoutAction(e));

		// ���� ��ư : �̿ϼ�
		btnManagement.setOnAction(e -> handleBtnManagementAction(e));

		// ������ �������׹�ư
		btnNotice.setOnAction(e -> handleBtnNoticeAction(e));

		// ������ ���� ��ư
		btnSchedule.setOnAction(e -> handleBtnScheduleAction(e));

		// �뿩���Ȯ�� ��ư
		btnReantalList.setOnAction(e -> handleBtnRentalListAction(e));

	}

	private void handleBtnRentalListAction(ActionEvent e) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/adminRentalListPopUp.fxml"));
			Scene scene = new Scene(root);
			Stage adminRentalListPopUp = new Stage();
			adminRentalListPopUp.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			adminRentalListPopUp.setTitle("������ �̿� ��Ȳ");
			adminRentalListPopUp.setScene(scene);
			adminRentalListPopUp.setResizable(true);
			Button btnSearch = (Button) root.lookup("#btnSearch");
			Button btnAll = (Button) root.lookup("#btnAll");
			Button btnExit = (Button) root.lookup("#btnExit");
			TableView tblRentalList = (TableView) root.lookup("#tblRentalList");
			BarChart barChart = (BarChart) root.lookup("#barChart");

			ComboBox<String> cmbYear1 = (ComboBox) root.lookup("#cmbYear1");
			ComboBox<String> cmbMonth1 = (ComboBox) root.lookup("#cmbMonth1");
			ComboBox<String> cmbDay1 = (ComboBox) root.lookup("#cmbDay1");
			ComboBox<String> cmbYear2 = (ComboBox) root.lookup("#cmbYear2");
			ComboBox<String> cmbMonth2 = (ComboBox) root.lookup("#cmbMonth2");
			ComboBox<String> cmbDay2 = (ComboBox) root.lookup("#cmbDay2");

			cmbYear1.setItems(FXCollections.observableArrayList("1940", "1941", "1942", "1943", "1944", "1945", "1946",
					"1947", "1948", "1949", "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957", "1958",
					"1959", "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967", "1968", "1969", "1970",
					"1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982",
					"1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994",
					"1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006",
					"2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018",
					"2019", "2020"));
			cmbMonth1.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09",
					"10", "11", "12"));

			cmbDay1.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09",
					"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
					"26", "27", "28", "29", "30", "31"));
			cmbYear2.setItems(FXCollections.observableArrayList("1940", "1941", "1942", "1943", "1944", "1945", "1946",
					"1947", "1948", "1949", "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957", "1958",
					"1959", "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967", "1968", "1969", "1970",
					"1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982",
					"1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994",
					"1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006",
					"2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018",
					"2019", "2020"));
			cmbMonth2.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09",
					"10", "11", "12"));

			cmbDay2.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09",
					"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
					"26", "27", "28", "29", "30", "31"));

			TableColumn colNo = new TableColumn("No");
			colNo.setMaxWidth(30);
			colNo.setStyle("-fx-allignment: CENTER");
			colNo.setCellValueFactory(new PropertyValueFactory("no"));

			TableColumn colDate = new TableColumn("��¥");
			colDate.setPrefWidth(90);
			colDate.setStyle("-fx-allignment: CENTER");
			colDate.setCellValueFactory(new PropertyValueFactory("date"));

			TableColumn colId = new TableColumn("���̵�");
			colId.setPrefWidth(80);
			colId.setStyle("-fx-allignment: CENTER");
			colId.setCellValueFactory(new PropertyValueFactory("id"));

			TableColumn colIsbn = new TableColumn("ISBN");
			colIsbn.setPrefWidth(120);
			colIsbn.setStyle("-fx-allignment: CENTER");
			colIsbn.setCellValueFactory(new PropertyValueFactory("isbn"));

			TableColumn colTitle = new TableColumn("����");
			colTitle.setPrefWidth(200);
			colTitle.setStyle("-fx-allignment: CENTER");
			colTitle.setCellValueFactory(new PropertyValueFactory("title"));

			TableColumn colCartgory = new TableColumn("�帣");
			colCartgory.setPrefWidth(80);
			colCartgory.setStyle("-fx-allignment: CENTER");
			colCartgory.setCellValueFactory(new PropertyValueFactory("cartgory"));

			tblRentalList.getColumns().addAll(colNo, colDate, colId, colIsbn, colTitle, colCartgory);
			
			btnAll.setOnAction(event -> {
				obsListRentalList.clear();
				ArrayList<Statistical> arrayList = new ArrayList<Statistical>();
				Connection con = null;
				PreparedStatement preparedStatement = null;
				ResultSet rs = null;
				String query = null;
				try {
					con = DBUtil.getConnection();

					query = "select No,Rentaldate,Id,ISBN,title,category from StatisticalTBL A inner join memberTBL B on A.Member_Id=B.Id\r\n"
							+ "inner join BookTBL C on A.Book_ISBN=C.ISBN;";
					preparedStatement = con.prepareStatement(query);
					// preparedStatement.setString(1, "%" + searchText + "%");

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
				for (Statistical s : arrayList) {
					obsListRentalList.add(s);
				}

				tblRentalList.setItems(obsListRentalList);


			});
			btnSearch.setOnAction(e3 -> {
				int month1 = Integer.parseInt(cmbMonth1.getValue());
				int day1 = Integer.parseInt(cmbDay1.getValue());
				System.out.println(cmbYear1.getValue() + "-" + month1 + "-" + day1);
				
				
				
				obsListRentalList.clear();
				ArrayList<Statistical> arrayList = new ArrayList<Statistical>();
				Connection con = null;
				PreparedStatement preparedStatement = null;
				ResultSet rs = null;
				String query = null;
				try {
					con = DBUtil.getConnection();

					query = "select No,Rentaldate,Id,ISBN,title,category from StatisticalTBL A inner join memberTBL B on A.Member_Id=B.Id\r\n"
							+ "inner join BookTBL C on A.Book_ISBN=C.ISBN where Rentaldate like '?';";
					preparedStatement = con.prepareStatement(query);
					//preparedStatement.setString(1, "%" + searchText + "%");

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
				for (Statistical s : arrayList) {
					obsListRentalList.add(s);
				}

				tblRentalList.setItems(obsListRentalList);

				
				
				
			});
			adminRentalListPopUp.show();
			btnExit.setOnAction(e3 -> adminRentalListPopUp.close());
		} catch (IOException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("������-���� ȭ�� ��ȯ ���� Ȯ���ϼ���");
			alert.showAndWait();
		}

	}

	// ���� ��ư : �̿ϼ�
	private void handleBtnManagementAction(ActionEvent e) {
		Stage adminMain = null;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/managementView.fxml"));
			Scene scene = new Scene(root);
			adminMain = new Stage();
			adminMain.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			adminMain.setTitle("Management");
			adminMain.setScene(scene);
			adminMain.setResizable(true);
			((Stage) btnLogout.getScene().getWindow()).close();
			adminMain.show();
		} catch (IOException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("������-���� ȭ�� ��ȯ ���� Ȯ���ϼ���");
			alert.showAndWait();
		}
	}

	// ������â �α׾ƿ� ��ư �ڵ鷯�̺�Ʈ
	private void handleBtnLogoutAction(ActionEvent e) {

		Parent mainView = null;
		Stage mainStage = null;
		try {
			mainView = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
			Scene scene = new Scene(mainView);
			mainStage = new Stage();
			mainStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			mainStage.setTitle("KD Library");
			mainStage.setScene(scene);
			mainStage.setResizable(true);
			((Stage) btnLogout.getScene().getWindow()).close();
			mainStage.show();
		} catch (IOException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("������ �α׾ƿ� (�α���â���� ���ư���) ���� Ȯ���ϼ���");
			alert.showAndWait();
		}

	}

	// ������ �������׹�ư
	private void handleBtnNoticeAction(ActionEvent e) {
		try {
			obsListN.clear();
			Parent adminNoticeView = FXMLLoader.load(getClass().getResource("/view/user_notice.fxml"));
			Scene scene = new Scene(adminNoticeView);
			Stage adminNoticeStage = new Stage(StageStyle.UTILITY);

			TableView tbladminNotice = (TableView) scene.lookup("#tblNotice");
			Button btnadminAdd = (Button) scene.lookup("#btnAdd");
			Button btnadminDelete = (Button) scene.lookup("#btnDelete");
			Button btnadminNo = (Button) scene.lookup("#btnNo");
			Button btnadminEdit = (Button) scene.lookup("#btnEdit");

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

			tbladminNotice.getColumns().addAll(colNo, colTitle, colContent, colDate);
			DAO dao = new DAO();
			arrayList = dao.getNotice();
			for (Notice n : arrayList) {
				obsListN.add(n);
			}
			tbladminNotice.setItems(obsListN);

			// ������ư
			btnadminDelete.setOnAction(event3 -> {
				Connection con2 = null;
				PreparedStatement pstmt2 = null;
				try {
					if (tbladminNotice.getSelectionModel().getSelectedItem() == null)
						throw new Exception();
					con2 = DBUtil.getConnection();
					String query = "delete from noticeTBL where no = ?";
					pstmt2 = con2.prepareStatement(query);
					Notice not2 = obsListN.get(tableViewselectedIndex);
					pstmt2.setInt(1, not2.getNo());
					int notxx = pstmt2.executeUpdate();
					if (notxx != 0) {
						obsListN.remove(tableViewselectedIndex);
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("�������� ����");
						alert.setHeaderText("���������� ���� �Ϸ�Ǿ����ϴ�.");
						alert.showAndWait();

					} else {
						throw new Exception();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("�����߻�");
					alert.setHeaderText("���������� �����ϼ���");
					alert.setContentText(e1.getMessage());
					alert.showAndWait();
				}

			});

			// ���â ��ư
			btnadminAdd.setOnAction(event -> {
				try {
					Parent adminNotAddView = FXMLLoader.load(getClass().getResource("/view/admin_NoticeAdd.fxml"));
					Scene scene2 = new Scene(adminNotAddView);
					Stage adminNotAddStage = new Stage();

					TextField txtAdminAddTitle = (TextField) scene2.lookup("#txtTitle");
					Label lblAdminAddDate = (Label) scene2.lookup("#lblDate");
					TextArea txaAdminAddContent = (TextArea) scene2.lookup("#txaContent");
					Button btnAdminAddOk = (Button) scene2.lookup("#btnOk");
					Button btnAdminAddNo = (Button) scene2.lookup("#btnNo");

					adminNotAddStage.setResizable(false);
					adminNotAddStage.setScene(scene2);
					adminNotAddStage.setTitle("�������� ���");
					adminNotAddStage.show();

					btnAdminAddOk.setOnAction(e2 -> {
						Connection con1 = null;
						PreparedStatement pstmt1 = null;
						try {
							if (txtAdminAddTitle.getText().trim().equals("")
									|| txaAdminAddContent.getText().trim().equals(""))
								throw new Exception();
							con1 = DBUtil.getConnection();

							String query = "Insert into noticeTBL (No, title, content,date) values(NULL, ?, ?, ?)";

							pstmt1 = con1.prepareStatement(query);

							Notice n = new Notice(txtAdminAddTitle.getText(), txaAdminAddContent.getText(),
									lblAdminAddDate.getText());
							pstmt1.setString(1, n.getTitle());
							pstmt1.setString(2, n.getContent());
							pstmt1.setString(3, n.getDate());

							int v = pstmt1.executeUpdate();

							if (v != 0) {
								obsListN.clear();
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("�˸�");
								alert.setHeaderText("�������� ��� �Ϸ�");
								alert.showAndWait();
								adminNotAddStage.close();
								arrayList = dao.getNotice();
								for (Notice n1 : arrayList) {
									obsListN.add(n1);
								}
								tbladminNotice.setItems(obsListN);
							} else {
								throw new Exception();
							}

						} catch (Exception e1) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("�����߻�");
							alert.setHeaderText("���������� ��� �Է��Ͻñ� �ٶ��ϴ�.");
							alert.showAndWait();
							return;
						}
					});

					btnAdminAddNo.setOnAction(event2 -> adminNotAddStage.close());
					lblAdminAddDate.setText(Noticetime);

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("���� �����߻�");
					alert.setHeaderText(e1.getMessage());
				}

			});

			// ���̺�信�� ������ �ε����� ���������� �޾ƿ��� �̺�Ʈ
			tbladminNotice.setOnMousePressed(event -> {
				tableViewselectedIndex = tbladminNotice.getSelectionModel().getSelectedIndex();
			});

			// ������ư
			// ���̺�� ������ ���� Ŭ������ �ٲپ��ִ� �̺�Ʈ
			tbladminNotice.setOnMouseClicked(event -> {
				if (event.getClickCount() > 1) {
					try {
						Parent adminNotMfView = FXMLLoader
								.load(getClass().getResource("/view/admin_NoticeModified.fxml"));
						Scene scene3 = new Scene(adminNotMfView);
						Stage adminNotMfStage = new Stage();
						adminNotMfStage.setScene(scene3);
						adminNotMfStage.setResizable(false);
						adminNotMfStage.setTitle("�������� ����â");
						adminNotMfStage.show();

						TextField txtAdminMfTitle = (TextField) scene3.lookup("#txtTitle");
						Label lblAdminMfDate = (Label) scene3.lookup("#lblDate");
						TextArea txaAdminMfContent = (TextArea) scene3.lookup("#txaContent");
						Button btnAdminMfOk = (Button) scene3.lookup("#btnOk");
						Button btnAdminMfNo = (Button) scene3.lookup("#btnNo");

						// Notice�� �ִ� obsListN�� �����ͼ� not�� �־���
						Notice not = obsListN.get(tableViewselectedIndex);
						txtAdminMfTitle.setText(not.getTitle());
						txaAdminMfContent.setText(not.getContent());

						btnAdminMfOk.setOnAction(event3 -> {
							if (!(txtAdminMfTitle.getText().trim().equals("")
									|| txaAdminMfContent.getText().trim().equals(""))) {

								Connection con2 = null;
								PreparedStatement pstmt2 = null;
								try {
									con2 = DBUtil.getConnection();

									String query = "update noticeTBL set title = ?, content = ?, date = ? where No = ?";

									not.setTitle(txtAdminMfTitle.getText());
									not.setContent(txaAdminMfContent.getText());
									not.setDate(lblAdminMfDate.getText());

									pstmt2 = con2.prepareStatement(query);
									pstmt2.setString(1, not.getTitle());
									pstmt2.setString(2, not.getContent());
									pstmt2.setString(3, not.getDate());
									pstmt2.setInt(4, not.getNo());

									int mf = pstmt2.executeUpdate();

									if (mf != 0) {
										obsListN.set(tableViewselectedIndex, not);
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("�˸�");
										alert.setHeaderText("������ ���������� �����Ͽ����ϴ�");
										alert.showAndWait();
										adminNotMfStage.close();
									} else {
										throw new Exception();
									}

								} catch (Exception e1) {
									Alert alert = new Alert(AlertType.CONFIRMATION);
									alert.setTitle("�����߻�");
									alert.setHeaderText("�������� ���� �����߻�");
									alert.setContentText(e1.getMessage());
									alert.showAndWait();
								}
							} else {
								Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("�����߻�");
								alert.setHeaderText("���������� ��� �ۼ����ֽñ� �ٶ��ϴ�.");
								alert.showAndWait();
							}

						});
						btnAdminMfNo.setOnAction(event3 -> adminNotMfStage.close());
						lblAdminMfDate.setText(Noticetime);
					} catch (IOException e1) {
					}
				}
			});

			adminNoticeStage.initModality(Modality.WINDOW_MODAL);
			adminNoticeStage.initOwner(stage);
			adminNoticeStage.setScene(scene);
			adminNoticeStage.setResizable(false);
			adminNoticeStage.setTitle("������ ��������");
			adminNoticeStage.show();

			btnadminNo.setOnAction(event1 -> adminNoticeStage.close());

		} catch (IOException e1) {

		}
	}

	// ������ ���� ��ư
	private void handleBtnScheduleAction(ActionEvent e) {
		try {

			HBox tab3 = FXMLLoader.load(getClass().getResource("/view/calender.fxml"));

			Button btnAdd = (Button) tab3.lookup("#btnAdd");
			Button btnEdit = (Button) tab3.lookup("#btnEdit");
			Button btnDelete = (Button) tab3.lookup("#btnDelete");
			Button btnClose = (Button) tab3.lookup("#btnClose");
			ListView listV = (ListView) tab3.lookup("#listV");
			DatePicker datePicker = (DatePicker) tab3.lookup("#datePicker");
			datePicker.setValue(LocalDate.now());
			Label lbDate1 = (Label) tab3.lookup("#lbDate");
			DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
			Pane popupContent = (Pane) datePickerSkin.getPopupContent();
			popupContent.setPrefWidth(250);
			VBox vboxBtn = (VBox) tab3.lookup("#vboxBtn");
			VBox vBoxList = (VBox) tab3.lookup("#vBoxList");
			vBoxList.getChildren().setAll(lbDate1, listV);
			vboxBtn.getChildren().setAll(btnAdd, btnEdit, btnDelete, btnClose);
			tab3.getChildren().setAll(popupContent, vBoxList, vboxBtn);
			Scene s = new Scene(tab3);
			Stage arg0 = new Stage();
			arg0.setResizable(false);
			arg0.setScene(s);
			arg0.setTitle("����");
			arg0.show();
			ObservableList<String> obSchdule = FXCollections.observableArrayList();
			DAO dao = new DAO();
			schduleList = dao.getSchedule(date);
//			schduleCount=schduleList.size();
//			System.out.println(schduleCount);
			if (schduleList.size() != 0) {
				for (int i = 0; i < schduleList.size(); i++) {
					obSchdule.add(schduleList.get(i).getContent());
				}
			}
			listV.setItems(obSchdule);
			System.out.println(schduleCount);
			datePicker.setOnAction(e3 -> {
				obSchdule.clear();
				schduleList.clear();
				try {

					date = datePicker.getValue().toString();

					schduleList = dao.getSchedule(date);

					if (schduleList.size() != 0) {
						for (int i = 0; i < schduleList.size(); i++) {
							obSchdule.add(schduleList.get(i).getContent());
						}
					}
					listV.setItems(obSchdule);

				} catch (Exception e31) {
				}
			});

			btnAdd.setOnAction(eve -> {
				Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource("/view/scheduleAddPopup.fxml"));
					Scene s1 = new Scene(root);
					Label lbDate = (Label) s1.lookup("#lbDate");
					TextArea txaContent = (TextArea) s1.lookup("#txaContent");
					Button btnAdd2 = (Button) s1.lookup("#btnAdd");
					Stage popup = new Stage();
					popup.setResizable(false);
					popup.setScene(s1);
					popup.setTitle("����");
					popup.show();

					lbDate.setText(date);
					btnAdd2.setOnAction(eve2 -> {
						Connection con1 = null;
						PreparedStatement pstmt = null;
						try {
							if (txaContent.getText().trim().equals(""))
								throw new Exception();
							con1 = DBUtil.getConnection();
							String query = "Insert into ScheduleTBL(`content`,`date`,`No`) values(?,?,?);";
							pstmt = con1.prepareStatement(query);

							Schedule schedule = new Schedule(txaContent.getText(), date);
							pstmt.setString(1, schedule.getContent());
							pstmt.setString(2, schedule.getDate());
							pstmt.setInt(3, schduleCount);

							int resultValue = pstmt.executeUpdate();
							if (resultValue != 0) {
								schduleCount += 1;
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("��� �Ϸ�");
								alert.setHeaderText("���� ��� �Ϸ�");
								alert.showAndWait();
								popup.close();
							} else {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("�����߻�");
								alert.setHeaderText("���Ի����� �����ϼ���");
								alert.showAndWait();
							}

						} catch (Exception e4) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("�����߻�");
							alert.setHeaderText("������ �Է��ϼ���");
							alert.showAndWait();
						}

					});
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			});

			//
			listV.setOnMousePressed(event -> {
				tableViewselectedIndex2 = listV.getSelectionModel().getSelectedIndex();
			});

			// ������ư
			btnEdit.setOnAction(eve -> {
				try {
					Parent adminScheduleView = FXMLLoader.load(getClass().getResource("/view/scheduleEditPopup.fxml"));
					Scene scene = new Scene(adminScheduleView);
					Stage adminScheduleStage = new Stage();

					Button btnEdit2 = (Button) scene.lookup("#btnEdit");
					Label lbDate2 = (Label) scene.lookup("#lbDate");
					TextArea txaContent2 = (TextArea) scene.lookup("#txaContent");

					lbDate2.setText(date);

					// No�� �ε������̶� ��ġ�ȵ� �̿�
					btnEdit2.setOnAction(eve2 -> {
						Connection con2 = null;
						PreparedStatement pstmt = null;
						try {
							con2 = DBUtil.getConnection();
							String query = "update ScheduleTBL set content = ? where No = ?";
							pstmt = con2.prepareStatement(query);

							Schedule sh = schduleList.get(tableViewselectedIndex2);

							pstmt.setString(1, sh.getContent());
							// pstmt.setInt(2, sh.getNo());
							pstmt.setInt(2, 1);
							int ase = pstmt.executeUpdate();
							if (ase != 0) {
								sh.setContent(txaContent2.getText());
								sh.setNo(tableViewselectedIndex2);
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("����ǥ ����â");
								alert.setHeaderText("������ �Ϸ�Ǿ����ϴ�");
								alert.showAndWait();
								adminScheduleStage.close();
							} else {
								throw new Exception();
							}

						} catch (Exception e1) {
							// TODO Auto-generated catch block
							System.out.println(e1.getMessage());
						}
					});

					adminScheduleStage.setScene(scene);
					adminScheduleStage.setResizable(false);
					adminScheduleStage.setTitle("����ǥ ����");
					adminScheduleStage.show();
				} catch (IOException e1) {

				}
			});

			btnClose.setOnAction(e3 -> arg0.close());

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
