package controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
import model.Notice;
import model.Schedule;
import model.Statistical;

public class AdminController implements Initializable {
	public Stage stage;
	@FXML
	private Button btnLogout;
	@FXML
	private Button btnManagement;
	@FXML
	private Button btnNotice;
	@FXML
	private Button btnSchedule;
	@FXML
	private Button btnReantalList;
	private ArrayList<Schedule> schduleList = new ArrayList<Schedule>();
	private ArrayList<Notice> noticeList = new ArrayList<Notice>();
	private ObservableList<Notice> obsListN = FXCollections.observableArrayList();
	private ObservableList<Statistical> obsListRentalList = FXCollections.observableArrayList();
	private int noticeTableViewSelectedIndex = -1;
	private int schduleTableViewSelectedIndex = -1;
	private String noticeTime = new SimpleDateFormat("yyyy�� MM�� dd��").format(new Date());
	private String date = LocalDate.now().toString();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// �α׾ƿ� ��ư
		btnLogout.setOnAction(e -> handleBtnLogoutAction(e));

		// ���� ��ư
		btnManagement.setOnAction(e -> handleBtnManagementAction(e));

		// �������� ��ư
		btnNotice.setOnAction(e -> handleBtnNoticeAction(e));

		// ���� ��ư
		btnSchedule.setOnAction(e -> handleBtnScheduleAction(e));

		// �뿩���Ȯ�� ��ư
		btnReantalList.setOnAction(e -> handleBtnRentalListAction(e));

	}

	// �뿩��� Ȯ�� ��ư �ڵ鷯 �Լ�
	private void handleBtnRentalListAction(ActionEvent e) {

		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/admin_RentalList.fxml"));
			Scene scene = new Scene(root);
			Stage adminRentalListPopUp = new Stage();
			adminRentalListPopUp.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			adminRentalListPopUp.setTitle("�뿩���Ȯ��");
			adminRentalListPopUp.setScene(scene);
			adminRentalListPopUp.setResizable(true);
			Button btnSearch = (Button) root.lookup("#btnSearch");
			Button btnAll = (Button) root.lookup("#btnAll");
			Button btnExit = (Button) root.lookup("#btnExit");
			TableView tblRentalList = (TableView) root.lookup("#tblRentalList");
			BarChart barChart = (BarChart) root.lookup("#barChart");
			LineChart lineChart = (LineChart) root.lookup("#lineChart");
			DatePicker dp1 = (DatePicker) root.lookup("#dp1");
			DatePicker dp2 = (DatePicker) root.lookup("#dp2");

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
				dp1.setValue(null);
				dp2.setValue(null);
				obsListRentalList.clear();
				ArrayList<Statistical> arrayList = new ArrayList<Statistical>();
				DAO dao = new DAO();
				arrayList = dao.getRentalList();
				for (Statistical s : arrayList) {
					obsListRentalList.add(s);
				}

				tblRentalList.setItems(obsListRentalList);

				///////////////////////////////////////////////////////////////////////////////////////////
				// 2020�� ���� �뿩��� ��Ʈ
				try {
					// BookDAO dao = new BookDAO();
					XYChart.Series series1 = new XYChart.Series();
					series1.setName("���� ��");
					series1.getData().add(new XYChart.Data(1, dao.searchRentalBookList("01").size()));
					series1.getData().add(new XYChart.Data(2, dao.searchRentalBookList("02").size()));
					series1.getData().add(new XYChart.Data(3, dao.searchRentalBookList("03").size()));
					series1.getData().add(new XYChart.Data(4, dao.searchRentalBookList("04").size()));
					series1.getData().add(new XYChart.Data(5, dao.searchRentalBookList("05").size()));
					series1.getData().add(new XYChart.Data(6, dao.searchRentalBookList("06").size()));
					series1.getData().add(new XYChart.Data(7, dao.searchRentalBookList("07").size()));
					series1.getData().add(new XYChart.Data(8, dao.searchRentalBookList("08").size()));
					series1.getData().add(new XYChart.Data(9, dao.searchRentalBookList("09").size()));
					series1.getData().add(new XYChart.Data(10, dao.searchRentalBookList("10").size()));
					series1.getData().add(new XYChart.Data(11, dao.searchRentalBookList("11").size()));
					series1.getData().add(new XYChart.Data(11, dao.searchRentalBookList("12").size()));

					ObservableList<XYChart.Series<Number, Number>> list = FXCollections.observableArrayList();
					list.addAll(series1);
					lineChart.setData(list);

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("������ ����");
					alert.setHeaderText("�����Ͱ� ���������ʽ��ϴ�.");
					alert.setContentText(e1.getMessage());
					alert.showAndWait();
				}
				////////////////////////////////////////////////////////////////////////////////////////////////

			});
			btnSearch.setOnAction(e3 -> {
				if (dp1.getValue() == null || dp2.getValue() == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("��¥�� �����ϼ���.");
					alert.showAndWait();
					return;
				}
				String[] date1 = dp1.getValue().toString().split("-");
				String[] date2 = dp2.getValue().toString().split("-");
				int searchDate1 = Integer.parseInt((date1[0] + date1[1] + date1[2]).trim());
				int searchDate2 = Integer.parseInt((date2[0] + date2[1] + date2[2]).trim());

				obsListRentalList.clear();
				ArrayList<Statistical> arrayList = new ArrayList<Statistical>();
				DAO dao = new DAO();
				arrayList = dao.searchRentalBookList(searchDate1, searchDate2);
				for (Statistical s : arrayList) {
					obsListRentalList.add(s);
				}
				tblRentalList.setItems(obsListRentalList);
			});
			adminRentalListPopUp.initModality(Modality.WINDOW_MODAL);
			adminRentalListPopUp.initOwner(stage);
			adminRentalListPopUp.show();
			btnExit.setOnAction(e3 -> adminRentalListPopUp.close());
		} catch (IOException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("������-���� ȭ�� ��ȯ ���� Ȯ���ϼ���");
			alert.showAndWait();
		}
	}

	// ���� ��ư �ڵ鷯 �Լ� : ������������ ��ȯ
	private void handleBtnManagementAction(ActionEvent e) {

		try {

			Stage adminMain = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/admin_Management.fxml"));
			Parent root = fxmlLoader.load();
			AdminManagement adminManagement = fxmlLoader.getController();
			adminManagement.stage = adminMain;
			adminMain.initOwner(this.stage);

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
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

	// ������â �α׾ƿ� ��ư �ڵ鷯�̺�Ʈ : �α���ȭ������ ��ȯ
	private void handleBtnLogoutAction(ActionEvent e) {
		try {
			Stage mainStage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
			Parent root1 = fxmlLoader.load();
			RootController rootController = fxmlLoader.getController();
			rootController.stage = mainStage;
			mainStage.initOwner(this.stage);
			Scene scene = new Scene(root1);
			mainStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
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
			Parent adminNoticeView = FXMLLoader.load(getClass().getResource("/view/notice.fxml"));
			Scene scene = new Scene(adminNoticeView);
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			Stage adminNoticeStage = new Stage();
			adminNoticeStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));

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
			noticeList = dao.getNotice();
			for (Notice n : noticeList) {
				obsListN.add(n);
			}
			tbladminNotice.setItems(obsListN);
			// ���̺�信�� ������ �ε����� ���������� �޾ƿ��� �̺�Ʈ
			tbladminNotice.setOnMousePressed(event -> {
				noticeTableViewSelectedIndex = tbladminNotice.getSelectionModel().getSelectedIndex();
			});
			// ������ư
			btnadminDelete.setOnAction(event3 -> {
				try {
					if (noticeTableViewSelectedIndex == -1) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("���� ����");
						alert.setHeaderText("������ �����͸� �����ϼ���.");
						alert.showAndWait();
						return;
					}
					if (tbladminNotice.getSelectionModel().getSelectedItem() == null)
						throw new Exception();
					Notice selectNotice = obsListN.get(noticeTableViewSelectedIndex);
					int notxx = dao.deleteNotice(selectNotice);
					if (notxx != 0) {
						obsListN.remove(noticeTableViewSelectedIndex);
						noticeTableViewSelectedIndex = -1;
					}
				} catch (Exception e1) {
				}
			});

			// ���â ��ư
			btnadminAdd.setOnAction(event -> {
				try {
					Parent adminNotAddView = FXMLLoader.load(getClass().getResource("/view/admin_NoticeAdd.fxml"));
					Scene scene2 = new Scene(adminNotAddView);
					Stage adminNotAddStage = new Stage();
					scene2.getStylesheets().add(getClass().getResource("/application/main.css").toString());
					adminNotAddStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
					TextField txtAdminAddTitle = (TextField) scene2.lookup("#txtTitle");
					Label lblAdminAddDate = (Label) scene2.lookup("#lblDate");
					TextArea txaAdminAddContent = (TextArea) scene2.lookup("#txaContent");
					Button btnAdminAddOk = (Button) scene2.lookup("#btnOk");
					Button btnAdminAddNo = (Button) scene2.lookup("#btnNo");
					adminNotAddStage.initModality(Modality.WINDOW_MODAL);
					adminNotAddStage.initOwner(adminNoticeStage);
					adminNotAddStage.setResizable(false);
					adminNotAddStage.setScene(scene2);
					adminNotAddStage.setTitle("�������� ���");
					adminNotAddStage.show();

					btnAdminAddOk.setOnAction(e2 -> {
						if (txtAdminAddTitle.getText().trim().equals("")
								|| txaAdminAddContent.getText().trim().equals("")) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("����");
							alert.setHeaderText("���������� ��� �Է��Ͻñ� �ٶ��ϴ�.");
							alert.showAndWait();
							return;
						}
						Notice n = new Notice(txtAdminAddTitle.getText(), txaAdminAddContent.getText(),
								lblAdminAddDate.getText());

						int v = dao.addNotice(n);
						if (v != 0) {
							obsListN.clear();
							adminNotAddStage.close();

							noticeList = dao.getNotice();
							for (Notice n1 : noticeList) {
								obsListN.add(n1);
							}
							tbladminNotice.setItems(obsListN);
						}

					});
					btnAdminAddNo.setOnAction(event2 -> adminNotAddStage.close());
					lblAdminAddDate.setText(noticeTime);

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("��� �����߻�");
					alert.setHeaderText(e1.getMessage());
				}
			});

			// ������ư
			tbladminNotice.setOnMouseClicked(event -> {
				// ���̺�� ������ ���� Ŭ������ �ٲپ��ִ� �̺�Ʈ
				if (event.getClickCount() > 1) {
					try {
						if (noticeTableViewSelectedIndex == -1) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("�������� ����â");
							alert.setHeaderText("������ �����͸� �����ϼ���.");
							alert.showAndWait();
							return;
						}
						Parent adminNotMfView = FXMLLoader.load(getClass().getResource("/view/admin_NoticeAdd.fxml"));
						Scene scene3 = new Scene(adminNotMfView);
						Stage adminNotMfStage = new Stage();
						scene3.getStylesheets().add(getClass().getResource("/application/main.css").toString());
						adminNotMfStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
						adminNotMfStage.initModality(Modality.WINDOW_MODAL);
						adminNotMfStage.initOwner(adminNoticeStage);
						adminNotMfStage.setScene(scene3);
						adminNotMfStage.setResizable(false);
						adminNotMfStage.setTitle("�������� ����â");
						adminNotMfStage.show();

						TextField txtAdminMfTitle = (TextField) scene3.lookup("#txtTitle");
						Label lblAdminMfDate = (Label) scene3.lookup("#lblDate");
						Label lbTitle = (Label) scene3.lookup("#lbTitle");
						TextArea txaAdminMfContent = (TextArea) scene3.lookup("#txaContent");
						Button btnAdminMfOk = (Button) scene3.lookup("#btnOk");
						Button btnAdminMfNo = (Button) scene3.lookup("#btnNo");
						lbTitle.setText("�������� ����");
						// Notice�� �ִ� obsListN�� �����ͼ� not�� �־���
						Notice not = obsListN.get(noticeTableViewSelectedIndex);
						txtAdminMfTitle.setText(not.getTitle());
						txaAdminMfContent.setText(not.getContent());

						btnAdminMfOk.setOnAction(event3 -> {
							if (!(txtAdminMfTitle.getText().trim().equals("")
									|| txaAdminMfContent.getText().trim().equals(""))) {

								not.setTitle(txtAdminMfTitle.getText());
								not.setContent(txaAdminMfContent.getText());
								not.setDate(lblAdminMfDate.getText());

								int mf = dao.editNotice(not);
								if (mf != 0) {
									obsListN.set(noticeTableViewSelectedIndex, not);
									noticeTableViewSelectedIndex = -1;
									adminNotMfStage.close();
								}

							} else {
								Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("���� �߻�");
								alert.setHeaderText("���������� ��� �ۼ����ֽñ� �ٶ��ϴ�.");
								alert.showAndWait();
							}

						});
						btnAdminMfNo.setOnAction(event3 -> adminNotMfStage.close());
						lblAdminMfDate.setText(noticeTime);
					} catch (Exception e1) {
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

			HBox tab3 = FXMLLoader.load(getClass().getResource("/view/admin_Schdule.fxml"));
			Button btnAdd = (Button) tab3.lookup("#btnAdd");
			Button btnEdit = (Button) tab3.lookup("#btnEdit");
			Button btnDelete = (Button) tab3.lookup("#btnDelete");
			Button btnClose = (Button) tab3.lookup("#btnClose");
			ListView listV = (ListView) tab3.lookup("#listV");
			DatePicker datePicker = (DatePicker) tab3.lookup("#datePicker");

			datePicker.setValue(LocalDate.now());

			DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
			Pane popupContent = (Pane) datePickerSkin.getPopupContent();
			popupContent.setPrefWidth(250);
			VBox vboxBtn = (VBox) tab3.lookup("#vboxBtn");
			VBox vBoxList = (VBox) tab3.lookup("#vBoxList");
			vBoxList.getChildren().setAll(listV);
			vboxBtn.getChildren().setAll(btnAdd, btnEdit, btnDelete, btnClose);
			tab3.getChildren().setAll(popupContent, vBoxList, vboxBtn);
			Scene s = new Scene(tab3);
			Stage arg0 = new Stage();
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			arg0.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			arg0.setResizable(false);
			arg0.initModality(Modality.WINDOW_MODAL);
			arg0.initOwner(stage);
			arg0.setScene(s);
			arg0.setTitle("����");
			arg0.show();
			ObservableList<String> obSchdule = FXCollections.observableArrayList();
			DAO dao = new DAO();
			schduleList = dao.getSchedule(date);
			if (schduleList.size() != 0) {
				for (int i = 0; i < schduleList.size(); i++) {
					obSchdule.add(schduleList.get(i).getContent());
				}
			}
			listV.setItems(obSchdule);
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

			// ���� �߰�
			btnAdd.setOnAction(eve -> {
				Parent root;
				try {
					obSchdule.clear();
					root = FXMLLoader.load(getClass().getResource("/view/admin_ScheduleAdd.fxml"));
					Scene s1 = new Scene(root);
					Label lbDate = (Label) s1.lookup("#lbDate");
					TextArea txaContent = (TextArea) s1.lookup("#txaContent");
					Button btnAdd2 = (Button) s1.lookup("#btnAdd");
					Stage popup = new Stage();
					s1.getStylesheets().add(getClass().getResource("/application/main.css").toString());
					popup.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
					popup.setResizable(false);
					popup.setScene(s1);
					popup.setTitle("����");
					popup.initModality(Modality.WINDOW_MODAL);
					popup.initOwner(arg0);
					popup.show();

					lbDate.setText(date);
					btnAdd2.setOnAction(eve2 -> {
						if (txaContent.getText().trim().equals("")) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("�����߻�");
							alert.setHeaderText("���� ������ �Է��Ͻñ� �ٶ��ϴ�.");
							alert.showAndWait();
							return;
						}
						Schedule schedule = new Schedule(txaContent.getText(), date);
						int resultValue = dao.addSchedule(schedule);
						if (resultValue != 0) {
							schduleList = dao.getSchedule(date);
							if (schduleList.size() != 0) {
								for (int i = 0; i < schduleList.size(); i++) {
									obSchdule.add(schduleList.get(i).getContent());
								}
							}
							listV.setItems(obSchdule);
							popup.close();
						}
					});
				} catch (IOException e1) {
				}
			});

			listV.setOnMousePressed(event -> {
				schduleTableViewSelectedIndex = listV.getSelectionModel().getSelectedIndex();
			});

			// ������ư
			btnEdit.setOnAction(eve -> {
				try {
					if (schduleTableViewSelectedIndex == -1) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("����ǥ ����â");
						alert.setHeaderText("������ �����͸� �����ϼ���.");
						alert.showAndWait();
						return;
					}
					Parent adminScheduleView = FXMLLoader.load(getClass().getResource("/view/admin_ScheduleAdd.fxml"));
					Scene scene = new Scene(adminScheduleView);
					Stage adminScheduleStage = new Stage();
					adminScheduleStage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
					scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());

					Button btnEdit2 = (Button) scene.lookup("#btnAdd");
					Label lbDate2 = (Label) scene.lookup("#lbDate");
					TextArea txaContent2 = (TextArea) scene.lookup("#txaContent");
					Schedule sh = schduleList.get(schduleTableViewSelectedIndex);
					lbDate2.setText(sh.getDate());
					btnEdit2.setText("����");
					txaContent2.setText(sh.getContent());
					adminScheduleStage.initModality(Modality.WINDOW_MODAL);
					adminScheduleStage.initOwner(arg0);
					adminScheduleStage.setScene(scene);
					adminScheduleStage.setResizable(false);
					adminScheduleStage.setTitle("����ǥ ����");
					adminScheduleStage.show();
					btnEdit2.setOnAction(eve2 -> {
						if (txaContent2.getText().trim().equals("")) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("�����߻�");
							alert.setHeaderText("���� ������ �Է��Ͻñ� �ٶ��ϴ�.");
							alert.showAndWait();
							return;
						}
						sh.setContent(txaContent2.getText());
						int ase = dao.editSchedule(sh);
						if (ase != 0) {
							obSchdule.set(schduleTableViewSelectedIndex, sh.getContent());
							schduleTableViewSelectedIndex = -1;
						}
					});
				} catch (Exception e1) {

				}
			});

			// ���� ����
			btnDelete.setOnAction(event -> {
				try {
					if (schduleTableViewSelectedIndex == -1) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("���� ����");
						alert.setHeaderText("������ �����͸� �����ϼ���.");
						alert.showAndWait();
						return;
					}
					Schedule sh = schduleList.get(schduleTableViewSelectedIndex);
					int notxx = dao.deleteSchedule(sh);
					if (notxx != 0) {
						obSchdule.remove(schduleTableViewSelectedIndex);
						schduleTableViewSelectedIndex = -1;
					}

				} catch (Exception e1) {
				}

			});

			// �ݱ�
			btnClose.setOnAction(e3 -> arg0.close());

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
}
