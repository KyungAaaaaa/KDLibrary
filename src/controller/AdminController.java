package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.stage.StageStyle;
import model.Notice;
import model.Schedule;

public class AdminController implements Initializable {
	public Stage stage;
	int schduleCount=0;
	@FXML
	Button btnLogout;
	@FXML
	Button btnManagement;
	@FXML
	Button btnNotice;
	@FXML
	Button btnSchedule;
	@FXML
	Button btnAd;
	ArrayList<Schedule> schduleList = new ArrayList<Schedule>();
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
								schduleCount+=1;
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
