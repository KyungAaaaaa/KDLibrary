package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Book;
import model.Member;
import model.RequestBook;

public class AdminManagement implements Initializable {
	@FXML
	TableView tblUser;
	@FXML
	TableView tblBook;
	@FXML
	TableView tblRequest;
///////////////////////////
	@FXML
	TabPane tabContainer;
	@FXML
	Tab memberTab;
	@FXML
	AnchorPane memberContainer;
	@FXML
	Tab bookTab;
	@FXML
	AnchorPane bookContainer;
	@FXML
	Tab requestTab;
	@FXML
	AnchorPane requestContainer;

///////////////////////////
	@FXML
	Button btnBookAdd;
	@FXML
	Button btnBack;
	@FXML
	Button btnBookDelete;
	@FXML
	Button btnBookEdit;
	@FXML
	Button btnUserEdit;
	@FXML
	Button btnUserDelete;
	@FXML
	Button btnUserSearch;
	@FXML
	Button btnBookSearch;
	@FXML
	Button btnRequestDelete;
	@FXML
	Button btnBookCategory;
	@FXML
	Button btnBarChart;
	@FXML
	Button btnPieChart;
	@FXML
	TextField txtBookSearch;
	@FXML
	TextField txtUserSearch;
	@FXML
	private NumberAxis xAxis;
	@FXML
	public Stage stage;
	private double tabWidth = 90.0;
	public static int lastSelectedTabIndex = 0;
	private File selectFile;
	private File directorySave;
	private int bookTableSelectIndex = -1;
	private int requestTableSelectIndex = -1;
	private int userTableSelectIndex = -1;
	private String localUrl;
	private Image localImage;
	private String selectFileName;
	private Image image = null;
	private ObservableList<Book> obLBook = FXCollections.observableArrayList();
	private ObservableList<RequestBook> obLRequest = FXCollections.observableArrayList();
	private ObservableList<Member> obLMember = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnBookCategory.setOnAction(e -> handelBtnBarChartAction(e));
		// ���� �̹��� ���� ���丮 ���� �̺�Ʈ
		setDirectorySaveImage();
		// ȸ�� ���̺� ���� �̺�Ʈ
		tblUserColumnSetting();
		// ���� ���̺� ���� �̺�Ʈ
		tblBookColumnSetting();
		// �ڷ��û ���̺� ���� �̺�Ʈ
		tblRequestColumnSetting();

		// ���� ���̺� ���� �ε��� ��ȯ �̺�Ʈ
		tblBook.setOnMousePressed(e -> bookTableSelectIndex = tblBook.getSelectionModel().getSelectedIndex());
		// ���� ���̺� �߰� ��ư �̺�Ʈ
		btnBookAdd.setOnAction(e -> handleBtnBookAddAction(e));
		// �������̺� ���� ��ư �̺�Ʈ
		btnBookEdit.setOnAction(e -> handleBtnBookEditAction(e));
		// ���� ���̺� å ����Ŭ���� ����â
		// tblBook.setOnMouseClicked(e -> handelTblBookDoubleClickAction(e));
		// ���� ���̺� ���� ��ư �̺�Ʈ
		btnBookDelete.setOnAction(e -> handleBtnBookDeleteAction(e));
		// ���� ���̺� �˻� ��ư �̺�Ʈ
		btnBookSearch.setOnAction(e -> handleBtnBookSearchAction(e));

		// ȸ�� ���̺� ���� �ε��� ��ȯ �̺�Ʈ
		tblUser.setOnMousePressed(e -> userTableSelectIndex = tblUser.getSelectionModel().getSelectedIndex());
		// ȸ�� ���̺� �˻� ��ư �̺�Ʈ
		btnUserSearch.setOnAction(e -> handleBtnUserSearchAction(e));
		// ȸ�� ���̺� ���� ��ư �̺�Ʈ
		btnUserEdit.setOnAction(e -> handleBtnUserEditAction(e));
		// ȸ�� ���̺� ���� ��ư �̺�Ʈ
		btnUserDelete.setOnAction(e -> handleBtnUserDeleteAction(e));

		// �ڷ��û ���̺� ���� �ε��� ��ȯ �̺�Ʈ
		tblRequest.setOnMousePressed(e -> requestTableSelectIndex = tblRequest.getSelectionModel().getSelectedIndex());
		// ��û ���̺� å ����Ŭ���� ����â
		tblRequest.setOnMouseClicked(e -> handelTblRequestDoubleClickAction(e));
		// ��û���̺� ���� ��ư �̺�Ʈ
		btnRequestDelete.setOnAction(e -> handleBtnRequestDeleteAction(e));

		// ���������� �ڷΰ���
		btnBack.setOnAction(e -> handleBtnBackAction(e));

		configureView();
	}

////////////////////////////////////////////////////////////////////////////////////////////////////

	// ������ ������ �޼ҵ�
	private void configureView() {
		// memberTab.setStyle("-fx-background-color:pink;");
		tabContainer.setTabMinWidth(tabWidth);
		tabContainer.setTabMaxWidth(tabWidth);
		tabContainer.setTabMinHeight(tabWidth);
		tabContainer.setTabMaxHeight(tabWidth);
		tabContainer.setRotateGraphic(true);
	}

	// �ڷΰ��� ��ư �ڵ鷯 �̺�Ʈ
	private void handleBtnBackAction(ActionEvent e) {
		try {

			Stage adminMain = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/admin_Main.fxml"));
			Parent root = fxmlLoader.load();
			AdminController adminController = fxmlLoader.getController();
			adminController.stage = adminMain;
			adminMain.initOwner(stage);

			Scene scene = new Scene(root);
			adminMain.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			scene.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			adminMain.setTitle("KD Library-Admin");
			adminMain.setScene(scene);
			adminMain.setResizable(true);
			((Stage) btnBack.getScene().getWindow()).close();
			adminMain.show();
		} catch (IOException e1) {
		}
	}

	/* ========================ȸ������====================== */

	// ���� ���̺� �˻� ��ư �̺�Ʈ
	private void handleBtnUserSearchAction(ActionEvent e) {
		MemberDAO dao = new MemberDAO();
		// ArrayList<Member> arrayList = dao.searchUser(txtUserSearch.getText());
		ArrayList<Member> arrayList = dao.searchUser(txtUserSearch.getText());
		obLMember.clear();
		for (Member m : arrayList) {
			obLMember.add(m);

		}
	}

	// ���� ���̺� ���� ��ư �̺�Ʈ
	private void handleBtnUserDeleteAction(ActionEvent e) {
		try {
			if (userTableSelectIndex == -1)
				throw new Exception();
			Member selectUser = obLMember.get(userTableSelectIndex);
			MemberDAO dao = new MemberDAO();

			int returnValue = dao.deleteUser(selectUser);
			if (returnValue != 0) {
				obLMember.remove(userTableSelectIndex);

			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("������ �̼���");
			alert.setContentText("������ �����͸� �����ϼ���.");
			alert.showAndWait();
			userTableSelectIndex = -1;
		}

	}

	// �������̺� ���� ��ư �̺�Ʈ
	private void handleBtnUserEditAction(ActionEvent e) {

		try {
			if (userTableSelectIndex == -1)
				throw new Exception();
			Parent root = FXMLLoader.load(getClass().getResource("/view/admin_MemberEdit.fxml"));
			Scene s = new Scene(root);
			Stage addPopup = new Stage();
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			addPopup.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			addPopup.setScene(s);
			addPopup.setTitle("ȸ�� ���� ����");

			Button btnOk = (Button) root.lookup("#btnOk");
			Button btnCancel = (Button) root.lookup("#btnCancel");
			TextField txtId = (TextField) root.lookup("#txtId");
			TextField txtName = (TextField) root.lookup("#txtName");
			TextField txtPass = (TextField) root.lookup("#txtPass");
			TextField txtPhoneNumber = (TextField) root.lookup("#txtPhoneNumber");
			// DatePicker dpBirth = (DatePicker) root.lookup("#dpBirth");
			ComboBox cmbEtc = (ComboBox) root.lookup("#cmbEtc");
			ComboBox<String> cmbYear = (ComboBox) root.lookup("#cmbYear");
			ComboBox<String> cmbMonth = (ComboBox) root.lookup("#cmbMonth");
			ComboBox<String> cmbDay = (ComboBox) root.lookup("#cmbDay");
			Label lbRentalBook = (Label) root.lookup("#lbRentalBook");
			cmbEtc.setItems(FXCollections.observableArrayList("����", "��ü"));

			Member selectUser = obLMember.get(userTableSelectIndex);

			btnCancel.setOnAction(eve -> addPopup.close());

			txtId.setText(selectUser.getId());
			txtName.setText(selectUser.getName());
			txtPass.setText(selectUser.getPass());
			txtPhoneNumber.setText(selectUser.getPhoneNumber());
			if (selectUser.getRentalBook() == null)
				lbRentalBook.setText("�뿩���� å ����.");
			else
				lbRentalBook.setText(selectUser.getRentalBook());
			String[] birth = selectUser.getBirth().split("-");
			cmbYear.setValue(birth[0]);
			cmbMonth.setValue(birth[1]);
			cmbDay.setValue(birth[2]);
			cmbEtc.setValue(selectUser.getEtc());
			cmbYear.setItems(FXCollections.observableArrayList("1940", "1941", "1942", "1943", "1944", "1945", "1946",
					"1947", "1948", "1949", "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957", "1958",
					"1959", "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967", "1968", "1969", "1970",
					"1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982",
					"1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994",
					"1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006",
					"2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018",
					"2019", "2020"));
			cmbMonth.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09",
					"10", "11", "12"));

			cmbDay.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09",
					"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
					"26", "27", "28", "29", "30", "31"));

			addPopup.show();

			btnOk.setOnAction(eve -> {
				Connection con1 = null;
				PreparedStatement preparedStatement = null;
				try {
					if (txtName.getText().trim().equals("") || txtPass.getText().trim().equals("")
							|| txtPhoneNumber.getText().trim().equals(""))
						throw new Exception();
					con1 = DBUtil.getConnection(); //
					String query = "update memberTBL set name=?,pass=?,phoneNumber=?,birth=?,etc=? where Id=?";
					preparedStatement = con1.prepareStatement(query);

					preparedStatement.setString(1, txtName.getText());
					preparedStatement.setString(2, txtPass.getText());
					preparedStatement.setString(3, txtPhoneNumber.getText());
					preparedStatement.setString(4,
							cmbYear.getValue() + "-" + cmbMonth.getValue() + "-" + cmbDay.getValue());
					preparedStatement.setString(5, cmbEtc.getValue().toString());
					preparedStatement.setString(6, selectUser.getId());
					selectUser.setName(txtName.getText());
					selectUser.setPass(txtPass.getText());
					selectUser.setPhoneNumber(txtPhoneNumber.getText());
					selectUser.setEtc(cmbEtc.getValue().toString());

					if (preparedStatement.executeUpdate() != 0) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("ȸ������ ����");
						alert.setHeaderText("���� �Ϸ�");
						alert.showAndWait();
						addPopup.close();
						obLMember.set(userTableSelectIndex, selectUser);
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("����");
						alert.setHeaderText("��� ����");
						alert.showAndWait();
						throw new Exception();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("��� �׸��� �Է��ϼ���.");
					alert.showAndWait();
				} finally {
					try {
						if (preparedStatement != null)
							preparedStatement.close();
						if (con1 != null)
							con1.close();
					} catch (SQLException e1) {
						System.out.println("RootController edit-save : " + e1.getMessage());
					}
				}
				tblUser.getSelectionModel().clearSelection();
				userTableSelectIndex = -1;
			});
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("������ �̼���");
			alert.setContentText("������ �����͸� �����ϼ���.");
			alert.showAndWait();
		}

	}

	// ���������� ���̺����� �޼ҵ�
	private void tblUserColumnSetting() {

		TableColumn colName = new TableColumn("�̸�");
		colName.setMaxWidth(150);
		colName.setCellValueFactory(new PropertyValueFactory("name"));
		TableColumn colId = new TableColumn("ID");
		colId.setMaxWidth(100);
		colId.setCellValueFactory(new PropertyValueFactory("id"));
		TableColumn colPhoneNumber = new TableColumn("��ȭ��ȣ");
		colPhoneNumber.setMaxWidth(200);
		colPhoneNumber.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
		TableColumn colBirth = new TableColumn("�������");
		colBirth.setMaxWidth(150);
		colBirth.setCellValueFactory(new PropertyValueFactory("birth"));
		TableColumn colRentalBook = new TableColumn("�뿩���� ����");
		colRentalBook.setMaxWidth(300);
		colRentalBook.setCellValueFactory(new PropertyValueFactory("rentalBook"));
		TableColumn colEtc = new TableColumn("���");
		colEtc.setMaxWidth(100);
		colEtc.setCellValueFactory(new PropertyValueFactory("etc"));

		tblUser.getColumns().addAll(colName, colId, colPhoneNumber, colBirth, colRentalBook, colEtc);

		MemberDAO dao = new MemberDAO();
		ArrayList<Member> userTBL = dao.getUserTbl();
		for (Member m : userTBL) {
			obLMember.add(m);
		}

		tblUser.setItems(obLMember);

	}

	/* ========================��������====================== */

	// ������ ���� ��ư �ڵ鷯�̺�Ʈ
	private void handleBtnBookEditAction(ActionEvent e) {

		try {

			if (bookTableSelectIndex == -1)
				throw new Exception("������ �����͸� �����ϼ���.");
			Parent root = FXMLLoader.load(getClass().getResource("/view/admin_BookAdd.fxml"));
			Scene s = new Scene(root);
			Stage addPopup = new Stage();
			addPopup.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			addPopup.setTitle("���� ����");
			addPopup.setScene(s);
			addPopup.show();
			Button btnOk = (Button) root.lookup("#btnOk");
			Button btnCancel = (Button) root.lookup("#btnCancel");
			Button btnFileSelect = (Button) root.lookup("#btnFileSelect");
			TextField txtISBN = (TextField) root.lookup("#txtISBN");
			TextField txtTitle = (TextField) root.lookup("#txtTitle");
			TextField txtWriter = (TextField) root.lookup("#txtWriter");
			ComboBox cmbCategory = (ComboBox) root.lookup("#cmbCategory");
			TextField txtCompany = (TextField) root.lookup("#txtCompany");
			TextField txtDate = (TextField) root.lookup("#txtDate");
			TextArea txaInformation = (TextArea) root.lookup("#txaInformation");
			ImageView imgV = (ImageView) root.lookup("#imgV");
			btnCancel.setOnAction(eve -> addPopup.close());
			//BookDAO dao = new BookDAO();
			cmbCategory.setItems(BookDAO.categoryList);
			Book book0 = obLBook.get(bookTableSelectIndex);
			selectFileName = book0.getFileimg();
			localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
			image = new Image(localUrl);
			imgV.setImage(image);

			txtISBN.setText(book0.getIsbn());
			txtISBN.setDisable(true);
			txtTitle.setText(book0.getTitle());
			txtWriter.setText(book0.getWriter());
			cmbCategory.getSelectionModel().select(book0.getCategory());
			txtCompany.setText(book0.getCompany());
			txtDate.setText(book0.getDate());
			txaInformation.setText(book0.getInformation());
			String fileName = "Book_" + book0.getIsbn() + "_" + book0.getTitle() + ".jpg";

			selectFile = new File(directorySave.getAbsolutePath() + "\\" + fileName.trim());

			btnFileSelect.setOnAction(eve1 -> {
				image = handleBtnImageFileAction(addPopup);
				imgV.setImage(image);
			});

			btnOk.setOnAction(eve -> {
				// String fileName1= null;
				Connection con1 = null;
				PreparedStatement preparedStatement = null;
				try {

					con1 = DBUtil.getConnection(); //
					String query = "update BookTBL set ISBN=?,title=?,writer=?,category=?,company=?,date=?,information=?,fileimg=? where ISBN=?";
					preparedStatement = con1.prepareStatement(query);

					preparedStatement.setString(1, txtISBN.getText());
					preparedStatement.setString(2, txtTitle.getText());
					preparedStatement.setString(3, txtWriter.getText());
					preparedStatement.setString(4, cmbCategory.getValue().toString());
					preparedStatement.setString(5, txtCompany.getText());
					preparedStatement.setString(6, txtDate.getText());
					preparedStatement.setString(7, txaInformation.getText());
					preparedStatement.setString(8, fileName);
					preparedStatement.setString(9, book0.getIsbn());
					book0.setIsbn(txtISBN.getText());
					book0.setTitle(txtTitle.getText());
					book0.setWriter(txtWriter.getText());
					book0.setCategory(cmbCategory.getValue().toString());
					book0.setCompany(txtCompany.getText());
					book0.setDate(txtDate.getText());
					book0.setInformation(txaInformation.getText());

					if (preparedStatement.executeUpdate() != 0) {
						// imageDelete(selectFileName);

						BufferedInputStream bis = null;// ������ ������ ����ϴ� Ŭ����
						BufferedOutputStream bos = null;// ������ ���� ����ϴ� Ŭ����
						try {
							book0.setFileimg(fileName);
							bis = new BufferedInputStream(new FileInputStream(selectFile));
							bos = new BufferedOutputStream(
									new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
							int data = -1;
							while ((data = bis.read()) != -1) {
								bos.write(data);
								bos.flush();
							}
						} catch (Exception e1) {
							System.out.println("���� ���翡�� : " + e1.getMessage());
							return;
						} finally {
							try {
								book0.setFileimg(fileName);

								if (bis != null)
									bis.close();
								if (bos != null)
									bos.close();
							} catch (IOException e1) {
							}
						}

						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("��� �Ϸ�");
						alert.showAndWait();
						addPopup.close();
						obLBook.set(bookTableSelectIndex, book0);
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("��� ���� : DB����");
					alert.setContentText(e1.getMessage());
					alert.showAndWait();
				} finally {
					try {
						if (preparedStatement != null)
							preparedStatement.close();
						if (con1 != null)
							con1.close();
					} catch (SQLException e1) {
						e1.getMessage();
					}
				}
				tblBook.getSelectionModel().clearSelection();
				bookTableSelectIndex = -1;
			});
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("������ �̼���");
			alert.setContentText("������ �����͸� �����ϼ���.");
			alert.showAndWait();
		}

	}

	// ������ �߰� ��ư �ڵ鷯�̺�Ʈ
	private void handleBtnBookAddAction(ActionEvent e) {
		try {
			BookDAO dao = new BookDAO();
			Parent root = FXMLLoader.load(getClass().getResource("/view/admin_BookAdd.fxml"));
			Stage addPopup = new Stage();
			addPopup.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			Scene s = new Scene(root);
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			addPopup.setScene(s);
			addPopup.setTitle("���� �߰�");
			addPopup.show();

			Button btnOk = (Button) s.lookup("#btnOk");
			Button btnCancel = (Button) s.lookup("#btnCancel");
			Button btnFileSelect = (Button) s.lookup("#btnFileSelect");
			TextField txtISBN = (TextField) s.lookup("#txtISBN");
			TextField txtTitle = (TextField) s.lookup("#txtTitle");
			TextField txtWriter = (TextField) s.lookup("#txtWriter");
			ComboBox cmbCategory = (ComboBox) s.lookup("#cmbCategory");
			TextField txtCompany = (TextField) s.lookup("#txtCompany");
			TextField txtDate = (TextField) s.lookup("#txtDate");
			TextArea txaInformation = (TextArea) s.lookup("#txaInformation");
			ImageView imgV = (ImageView) s.lookup("#imgV");
			cmbCategory.setItems(dao.categoryList);

			btnFileSelect.setOnAction(eve1 -> {
				Image image = handleBtnImageFileAction(addPopup);
				imgV.setImage(image);
			});

			btnOk.setOnAction(eve -> {
				Book book1 = null;
				String fileName = null;
				try {
					book1 = new Book(txtISBN.getText(), txtTitle.getText(), cmbCategory.getValue().toString(),
							txtWriter.getText(), txtCompany.getText(), txtDate.getText(), null,
							txaInformation.getText(), false);
					if (txtISBN.getText().trim().equals("") || txtTitle.getText().trim().equals("")
							|| txtWriter.getText().trim().equals("") || txtCompany.getText().trim().equals("")
							|| txtDate.getText().trim().equals("") || txaInformation.getText().trim().equals(""))
						throw new Exception();
					fileName = "Book_" + book1.getIsbn() + "_" + book1.getTitle() + ".jpg";
					book1.setFileimg(fileName);
				//	BookDAO dao = new BookDAO();
					if (selectFile == null) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("�����߻�");
						alert.setHeaderText("�̹��� ������ �����ϼ���");
						alert.setContentText("�������� �����ϼ���.");
						alert.showAndWait();
						return;
					}
					int returnValue = dao.addBook(book1);
					if (returnValue != 0) {

						obLBook.add(book1);

						BufferedInputStream bis = null;// ������ ������ ����ϴ� Ŭ����
						BufferedOutputStream bos = null;// ������ ���� ����ϴ� Ŭ����
						try {

							bis = new BufferedInputStream(new FileInputStream(selectFile));
							bos = new BufferedOutputStream(
									new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
							int data = -1;
							while ((data = bis.read()) != -1) {
								bos.write(data);
								bos.flush();
							}
						} catch (Exception e1) {
							System.out.println("���� ���翡�� : " + e1.getMessage());
							return;
						} finally {
							try {
								book1.setFileimg(fileName);
								selectFile = null;
								if (bis != null)
									bis.close();
								if (bos != null)
									bos.close();
							} catch (IOException e1) {
							}
						}

						addPopup.close();
					}
				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("����");
					alert.setHeaderText("��� �׸��� �ۼ��ϼ���");
					alert.showAndWait();
					return;
				}

			});
			btnCancel.setOnAction(eve -> addPopup.close());
		} catch (Exception e1) {
		}
	}

	// ������ ���� ��ư �ڵ鷯�̺�Ʈ
	private void handleBtnBookDeleteAction(ActionEvent e) {

		if (bookTableSelectIndex == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("������ �̼���");
			alert.setContentText("������ �����͸� �����ϼ���.");
			alert.showAndWait();
			return;
		}
		Book selectBook = obLBook.get(bookTableSelectIndex);
		selectFileName = selectBook.getFileimg();
		localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
		localImage = new Image(localUrl, false);
		BookDAO dao = new BookDAO();
		int returnValue = dao.deleteBook(selectBook);
		if (returnValue != 0) {
			imageDelete(selectFileName);
			obLBook.remove(bookTableSelectIndex);
			bookTableSelectIndex = -1;
		}
	}

	// ���� ���̺� �˻� ��ư �̺�Ʈ
	private void handleBtnBookSearchAction(ActionEvent e) {
		BookDAO dao = new BookDAO();
		ArrayList<Book> arrayList = dao.searchBook(txtBookSearch.getText(), "title");
		obLBook.clear();
		for (Book b : arrayList) {
			obLBook.add(b);

		}

	}

	// ���� ���̺� ���� �޼ҵ�
	private void tblBookColumnSetting() {
		TableColumn colISBN = new TableColumn("ISBN");
		colISBN.setMaxWidth(150);
		colISBN.setCellValueFactory(new PropertyValueFactory("isbn"));

		TableColumn colTitle = new TableColumn("����");
		colTitle.setMaxWidth(200);
		colTitle.setCellValueFactory(new PropertyValueFactory("title"));

		TableColumn colCategory = new TableColumn("�帣");
		colCategory.setMaxWidth(80);
		colCategory.setCellValueFactory(new PropertyValueFactory("category"));

		TableColumn colWriter = new TableColumn("����");
		colWriter.setMaxWidth(200);
		colWriter.setCellValueFactory(new PropertyValueFactory("writer"));

		TableColumn colCompany = new TableColumn("���ǻ�");
		colCompany.setMaxWidth(2000);
		colCompany.setCellValueFactory(new PropertyValueFactory("company"));

		TableColumn colDate = new TableColumn("������");
		colDate.setMaxWidth(150);
		colDate.setCellValueFactory(new PropertyValueFactory("date"));

		TableColumn colRental = new TableColumn("�뿩����");
		colRental.setMaxWidth(90);
		colRental.setCellValueFactory(new PropertyValueFactory("rental"));

		TableColumn colInformation = new TableColumn("å �Ұ�");
		colInformation.setMaxWidth(400);
		colInformation.setCellValueFactory(new PropertyValueFactory("information"));

		tblBook.getColumns().addAll(colISBN, colTitle, colCategory, colWriter, colCompany, colDate, colRental,
				colInformation);
		BookDAO dao = new BookDAO();

		HashSet<Book> bookTBL = dao.getBookTbl();
		for (Book b : bookTBL) {
			obLBook.add(b);
		}
		tblBook.setItems(obLBook);

	}

	// ����Ʈ ����׷��� ���
	private void handelBtnBarChartAction(Event e) {
		Parent root;
		try {
			BookDAO dao = new BookDAO();
			root = FXMLLoader.load(getClass().getResource("/view/admin_BookCategoryChart.fxml"));
			Stage addPopup = new Stage();
			Scene s = new Scene(root);
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			addPopup.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			BarChart barChart = (BarChart) root.lookup("#barChart");
			Button btnExit = (Button) root.lookup("#btnExit");
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			addPopup.setTitle("�帣�� ���� �� ��");
			addPopup.setScene(s);
			btnExit.setOnAction(e1 -> addPopup.close());

			XYChart.Series series1 = new XYChart.Series();
			series1.setName(dao.categoryList.get(0));
			XYChart.Series series2 = new XYChart.Series();
			series2.setName(dao.categoryList.get(1));
			XYChart.Series series3 = new XYChart.Series();
			series3.setName(dao.categoryList.get(2));
			XYChart.Series series4 = new XYChart.Series();
			series4.setName(dao.categoryList.get(3));
			XYChart.Series series5 = new XYChart.Series();
			series5.setName(dao.categoryList.get(4));
			XYChart.Series series6 = new XYChart.Series();
			series6.setName(dao.categoryList.get(5));
			XYChart.Series series7 = new XYChart.Series();
			series7.setName(dao.categoryList.get(6));

			series1.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series1.getName(), "category").size())));
			barChart.getData().add(series1);
			series2.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series2.getName(), "category").size())));
			barChart.getData().add(series2);
			series3.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series3.getName(), "category").size())));
			barChart.getData().add(series3);
			series4.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series4.getName(), "category").size())));
			barChart.getData().add(series4);
			series5.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series5.getName(), "category").size())));
			barChart.getData().add(series5);
			series6.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series6.getName(), "category").size())));
			barChart.getData().add(series6);
			series7.setData(FXCollections
					.observableArrayList(new XYChart.Data("", dao.searchBook(series7.getName(), "category").size())));
			barChart.getData().add(series7);
			addPopup.show();
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("������ ����");
			alert.setHeaderText("�����Ͱ� ���������ʽ��ϴ�.");
			alert.setContentText(e1.getMessage());
			alert.showAndWait();
		}

	}

	/* ========================�ڷ��û����====================== */

	// �ڷ��û ���̺� ���� �޼ҵ�
	private void tblRequestColumnSetting() {

		TableColumn colName = new TableColumn("�ۼ���");
		colName.setMaxWidth(80);
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colNo = new TableColumn("No");
		colNo.setMaxWidth(80);
		colNo.setCellValueFactory(new PropertyValueFactory("no"));

		TableColumn colContent = new TableColumn("����");
		colContent.setPrefWidth(300);
		colContent.setCellValueFactory(new PropertyValueFactory("content"));

		TableColumn colTitle = new TableColumn("����");
		colTitle.setPrefWidth(250);
		colTitle.setCellValueFactory(new PropertyValueFactory("title"));
		TableColumn colDate = new TableColumn("��¥");
		colDate.setMaxWidth(200);
		colDate.setCellValueFactory(new PropertyValueFactory("date"));

		tblRequest.getColumns().addAll(colNo, colTitle, colContent, colName, colDate);

		RequestDAO dao = new RequestDAO();
		ArrayList<RequestBook> requestTBL = dao.getRequestTbl();
		for (RequestBook r : requestTBL) {
			obLRequest.add(r);
		}
		tblRequest.setItems(obLRequest);

	}

	// ��û ���̺� å ����Ŭ���� ����â
	private void handelTblRequestDoubleClickAction(MouseEvent e) {
		if (e.getClickCount() != 2)
			return;
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/user_Request.fxml"));
			Stage addPopup = new Stage();
			Scene s = new Scene(root);
			s.getStylesheets().add(getClass().getResource("/application/main.css").toString());
			addPopup.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			Button btnAdd = (Button) root.lookup("#btnAdd");
			Button btnBack = (Button) root.lookup("#btnBack");
			TextField txtTitle = (TextField) root.lookup("#txtTitle");
			Label lbName = (Label) root.lookup("#lbName");
			Label lbDate = (Label) root.lookup("#lbDate");
			TextArea txaContent = (TextArea) root.lookup("#txaContent");
			RequestBook request = obLRequest.get(requestTableSelectIndex);
			txtTitle.setText(request.getTitle());
			lbName.setText(request.getName());
			txaContent.setText(request.getContent());
			txtTitle.setEditable(false);
			lbDate.setText(request.getDate());
			txaContent.setEditable(false);
			addPopup.setTitle("�ڷ� ��û ����");
			addPopup.setScene(s);
			addPopup.show();

			btnAdd.setOnAction(eve -> {
				try {
					//BookDAO dao = new BookDAO();
					Parent root1 = FXMLLoader.load(getClass().getResource("/view/admin_BookAdd.fxml"));
					Stage addPopup1 = new Stage();
					addPopup1.getIcons().add(new Image(getClass().getResource("/image/logo.png").toString()));
					addPopup1.initModality(Modality.NONE);
					addPopup1.initOwner(btnAdd.getScene().getWindow());
					Scene s1 = new Scene(root1);
					s1.getStylesheets().add(getClass().getResource("/application/main.css").toString());
					addPopup1.setTitle("���� �߰�");
					addPopup1.setScene(s1);
					addPopup1.show();
					Button btnOk = (Button) s1.lookup("#btnOk");
					Button btnCancel = (Button) s1.lookup("#btnCancel");
					Button btnFileSelect = (Button) s1.lookup("#btnFileSelect");
					TextField txtISBN = (TextField) s1.lookup("#txtISBN");
					TextField txtTitle1 = (TextField) s1.lookup("#txtTitle");
					TextField txtWriter = (TextField) s1.lookup("#txtWriter");
					ComboBox cmbCategory = (ComboBox) s1.lookup("#cmbCategory");
					TextField txtCompany = (TextField) s1.lookup("#txtCompany");
					TextField txtDate = (TextField) s1.lookup("#txtDate");
					TextArea txaInformation = (TextArea) s1.lookup("#txaInformation");
					ImageView imgV = (ImageView) s1.lookup("#imgV");
					cmbCategory.setItems(BookDAO.categoryList);

					btnFileSelect.setOnAction(eve1 -> {
						Image image = handleBtnImageFileAction(addPopup);
						imgV.setImage(image);
					});

					btnOk.setOnAction(eve1 -> {
						Book book1 = null;
						try {
							if (txtISBN.getText().trim().equals("") || txtTitle1.getText().trim().equals("")
									|| txtWriter.getText().trim().equals("") || txtCompany.getText().trim().equals("")
									|| txtDate.getText().trim().equals("")
									|| txaInformation.getText().trim().equals(""))
								throw new Exception();
							book1 = new Book(txtISBN.getText(), txtTitle1.getText(), cmbCategory.getValue().toString(),
									txtWriter.getText(), txtCompany.getText(), txtDate.getText(), null,
									txaInformation.getText(), false);
						} catch (Exception e1) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("�׸� ����");
							alert.setHeaderText("��� �׸��� �Է��ϼ���.");
							alert.showAndWait();
							return;
						}
						if (selectFile == null) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("�̹��� ���� �̼���");
							alert.setHeaderText("�̹��� ������ �����ϼ���");
							alert.showAndWait();
							return;
						}
						BufferedInputStream bis = null;// ������ ������ ����ϴ� Ŭ����
						BufferedOutputStream bos = null;// ������ ���� ����ϴ� Ŭ����
						String fileName = null;
						try {
							fileName = "Book_" + book1.getIsbn() + "_" + book1.getTitle() + ".jpg";
							bis = new BufferedInputStream(new FileInputStream(selectFile));
							bos = new BufferedOutputStream(
									new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
							int data = -1;
							while ((data = bis.read()) != -1) {
								bos.write(data);
								bos.flush();
							}
						} catch (Exception e1) {
							System.out.println("���� ���翡�� : " + e1.getMessage());
							return;
						} finally {
							try {
								book1.setFileimg(fileName);
								selectFile = null;
								if (bis != null)
									bis.close();
								if (bos != null)
									bos.close();
							} catch (IOException e1) {
							}
						}
						// BookDAO dao = new BookDAO();
						int returnValue = dao.addBook(book1);
						if (returnValue != 0) {
							obLBook.add(book1);
							addPopup.close();
						}
					});
					btnCancel.setOnAction(eve1 -> addPopup.close());

				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
			});

			btnBack.setOnAction(eve -> addPopup.close());

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	// ��û���̺� ���� ��ư �̺�Ʈ
	private void handleBtnRequestDeleteAction(ActionEvent e) {
		try {
			RequestBook selectRequest = obLRequest.get(requestTableSelectIndex);
			RequestDAO dao = new RequestDAO();
			int returnValue = dao.deleteRequest(selectRequest);
			if (returnValue != 0) {
				obLRequest.remove(requestTableSelectIndex);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	/* ========================�����Լ��� ====================== */

	// �̹������� ���� �޼ҵ�
	public boolean imageDelete(String fileName) {
		boolean result = false;
		File fileDelete = null;
		try {
			fileDelete = new File(directorySave.getAbsolutePath() + "\\" + fileName); // �����̹��� ����
			if (fileDelete.exists() && fileDelete.isFile()) {
				result = fileDelete.delete();
			}
		} catch (Exception ie) {
			System.out.println(ie.getMessage());
			result = false;
		}
		return result;
	}

	// �̹������ϼ��� ��ư �̺�Ʈ ��� �ڵ鷯 �Լ� ó��
	private Image handleBtnImageFileAction(Stage stage) {
		Image image = null;
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		// ������ �̹��������� ������������ �����ش�.
		selectFile = fileChooser.showOpenDialog(stage);
		try {
			if (selectFile != null) {
				String localURL = selectFile.toURI().toURL().toString();// ������ ���� ��θ�!!�˾Ƶ���
				image = new Image(localURL, false);

			} else {

			}
		} catch (MalformedURLException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("���� ��������");
			alert.setHeaderText("���� �������� ���� �߻�");
			alert.showAndWait();
		}
		return image;
	}

	// �̹������� ���丮 ���� �޼ҵ�
	private void setDirectorySaveImage() {
		// ���� ����
		directorySave = new File("C:/images/Library_BookData");
		if (!directorySave.exists()) {
			directorySave.mkdir();
			System.out.println("���� ���丮 ����");
		}

	}

}
