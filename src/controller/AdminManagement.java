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
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
	TextField txtBookSearch;
	@FXML
	TextField txtUserSearch;
	///////////////////////////
	private double tabWidth = 90.0;
	public static int lastSelectedTabIndex = 0;
	////////////////////////////////
	private File selectFile;
	private File directorySave;
	private File directoryMemberSave;
	ObservableList<Book> obLBook = FXCollections.observableArrayList();
	ObservableList<RequestBook> obLRequest = FXCollections.observableArrayList();
	ObservableList<Member> obLMember = FXCollections.observableArrayList();
	private int bookTableSelectIndex = -1;
	private int requestTableSelectIndex;
	private int userTableSelectIndex;
	private String localUrl;
	private Image localImage;
	private String selectFileName;
	BookDAO dao = new BookDAO();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setDirectorySaveImage();
		tblUserColumnSetting();
		tblBookColumnSetting();
		tblRequestColumnSetting();
		tblUser.setOnMousePressed(e -> userTableSelectIndex = tblUser.getSelectionModel().getSelectedIndex());
		tblBook.setOnMousePressed(e -> bookTableSelectIndex = tblBook.getSelectionModel().getSelectedIndex());
		tblRequest.setOnMousePressed(e -> requestTableSelectIndex = tblRequest.getSelectionModel().getSelectedIndex());

		// ��û ���̺� å ����Ŭ���� ����â
		tblRequest.setOnMouseClicked(e -> handelTblRequestDoubleClickAction(e));
		// ��û���̺� ���� ��ư �̺�Ʈ
		btnRequestDelete.setOnAction(e -> handleBtnRequestDeleteAction(e));
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

		// ���� ���̺� �˻� ��ư �̺�Ʈ
		btnUserSearch.setOnAction(e -> handleBtnUserSearchAction(e));
		// �������̺� ���� ��ư �̺�Ʈ
		btnUserEdit.setOnAction(e -> handleBtnUserEditAction(e));
		// ���� ���̺� ���� ��ư �̺�Ʈ
		btnUserDelete.setOnAction(e -> handleBtnUserDeleteAction(e));
		// ���������� �ڷΰ���
		btnBack.setOnAction(e -> handleBtnBackAction(e));

		//configureView();
	}

///////////////////////////
	private void configureView() {
	
		  tabContainer.setTabMinWidth(tabWidth); tabContainer.setTabMaxWidth(tabWidth);
		  tabContainer.setTabMinHeight(tabWidth);
		  tabContainer.setTabMaxHeight(tabWidth); tabContainer.setRotateGraphic(true);
	}
///////////////////////////

	private void handleBtnRequestDeleteAction(ActionEvent e) {

		try {
			RequestBook selectRequest = obLRequest.get(requestTableSelectIndex);
			Connection con = DBUtil.getConnection();
			String query = "delete from RequestTBL where No=?";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, selectRequest.getNo());
			if (preparedStatement.executeUpdate() != 0) {
				obLRequest.remove(requestTableSelectIndex);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("���� �Ϸ�");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("���� ����");
				alert.showAndWait();
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	// �ڷΰ��� ��ư �ڵ鷯 �̺�Ʈ
	private void handleBtnBackAction(ActionEvent e) {
		Stage adminMain = null;
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/adminMain.fxml"));
			Scene scene = new Scene(root);
			adminMain = new Stage();
			adminMain.setTitle("������ ����");
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
		ArrayList<Member> arrayList = dao.searchUser(txtUserSearch.getText());
		obLMember.clear();
		for (Member m : arrayList) {
			obLMember.add(m);

		}
	}

	// ���� ���̺� ���� ��ư �̺�Ʈ
	private void handleBtnUserDeleteAction(ActionEvent e) {
		try {
			Member selectUser = obLMember.get(userTableSelectIndex);
			selectFileName = selectUser.getFileimg();
			localUrl = "file:/C:/images/Library_MemberData/" + selectFileName;
			localImage = new Image(localUrl);

			Connection con = DBUtil.getConnection();
			String query = "delete from memberTBL where Id=?";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, selectUser.getId());
			if (preparedStatement.executeUpdate() != 0) {
				imageDelete(selectFileName, "member");
				obLMember.remove(userTableSelectIndex);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("���� �Ϸ�");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("���� ����");
				alert.showAndWait();
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// �������̺� ���� ��ư �̺�Ʈ
	private void handleBtnUserEditAction(ActionEvent e) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource("/view/adminEditUserPopup.fxml"));
			Stage addPopup = new Stage(StageStyle.UTILITY);
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			addPopup.setScene(new Scene(root));
			addPopup.show();
			Button btnOk = (Button) root.lookup("#btnOk");
			Button btnCancel = (Button) root.lookup("#btnCancel");
			Button btnFileSelect = (Button) root.lookup("#btnFileSelect");
			TextField txtId = (TextField) root.lookup("#txtId");
			TextField txtName = (TextField) root.lookup("#txtName");
			TextField txtPass = (TextField) root.lookup("#txtPass");
			TextField txtPhoneNumber = (TextField) root.lookup("#txtPhoneNumber");
			DatePicker dpBirth = (DatePicker) root.lookup("#dpBirth");
			ComboBox cmbEtc = (ComboBox) root.lookup("#cmbEtc");
			TextField txtRentalBook = (TextField) root.lookup("#txtRentalBook");
			ImageView imgV = (ImageView) root.lookup("#imgV");
			cmbEtc.setItems(FXCollections.observableArrayList("����", "��ü"));
			btnCancel.setOnAction(eve -> addPopup.close());
			Member selectUser = obLMember.get(userTableSelectIndex);
			selectFileName = selectUser.getFileimg();
			localUrl = "file:/C:/images/Library_MemberData/" + selectFileName;
			imgV.setImage(new Image(localUrl));
			txtId.setText(selectUser.getId());
			txtName.setText(selectUser.getName());
			txtPass.setText(selectUser.getPass());
			txtPhoneNumber.setText(selectUser.getPhoneNumber());
			txtRentalBook.setText(selectUser.getRentalBook());
			cmbEtc.setValue(selectUser.getEtc());

			btnFileSelect.setOnAction(eve1 -> {
				Image image = handleBtnImageFileAction(addPopup);
				imgV.setImage(image);
			});

			btnOk.setOnAction(eve -> {
				Connection con1 = null;
				PreparedStatement preparedStatement = null;
				try {

					con1 = DBUtil.getConnection(); //
					String query = "update memberTBL set name=?,pass=?,phoneNumber=?,birth=?,rentalBook=?,fileimg=?,etc=? where Id=?";
					preparedStatement = con1.prepareStatement(query);

					preparedStatement.setString(1, txtName.getText());
					preparedStatement.setString(2, txtPass.getText());
					preparedStatement.setString(3, txtPhoneNumber.getText());
					preparedStatement.setString(4, dpBirth.getValue().toString());
					preparedStatement.setString(5, txtRentalBook.getText());
					preparedStatement.setString(7, cmbEtc.getValue().toString());
					preparedStatement.setString(8, selectUser.getId());
					selectUser.setName(txtName.getText());
					selectUser.setPass(txtPass.getText());
					selectUser.setPhoneNumber(txtPhoneNumber.getText());
					selectUser.setBirth(dpBirth.getValue().toString());
					selectUser.setRentalBook(txtRentalBook.getText());
					selectUser.setEtc(cmbEtc.getValue().toString());

					if (selectFile == null)
						selectFile = new File(directoryMemberSave.getAbsolutePath() + "\\" + selectFileName);

					BufferedInputStream bis = null;// ������ ������ ����ϴ� Ŭ����
					BufferedOutputStream bos = null;// ������ ���� ����ϴ� Ŭ����
					String fileName = null;
					try {
						fileName = "Member_" + selectUser.getId() + "_" + selectUser.getName() + ".jpg";
						preparedStatement.setString(6, fileName);
						selectUser.setFileimg(fileName);
						bis = new BufferedInputStream(new FileInputStream(selectFile));
						bos = new BufferedOutputStream(
								new FileOutputStream(directoryMemberSave.getAbsolutePath() + "\\" + fileName));
						int data = -1;// -1���̻� �������� ���ٴ� �ǹ�
						while ((data = bis.read()) != -1) { // �̹������� ũ�⸸ŭ �ݺ�
							bos.write(data); // ���� ����
							bos.flush();// ���ۿ� �ִ� ���� �� �����ϱ����ؼ� ������.
						}
					} catch (Exception e1) {
						System.out.println("���� ���翡�� : " + e1.getMessage());
						return; // ���� �����ε� �ؿ� �����ϴ°����� �����ϸ�ȵǱ⶧���� �������� ����������
					} finally {
						try {
							selectUser.setFileimg(fileName);
							imageDelete(selectFileName, "member");
							if (bis != null)
								bis.close();
							if (bos != null)
								bos.close();
						} catch (IOException e1) {
						}
					}

					if (preparedStatement.executeUpdate() != 0) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("��� �Ϸ�");
						alert.showAndWait();
						addPopup.close();
						obLMember.set(userTableSelectIndex, selectUser);
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("��� ����");
						alert.showAndWait();
						throw new Exception();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("��� ���� : DB����");
					alert.setContentText(e1.getMessage());
					alert.showAndWait();
				} finally {
					// con,pstmt �ݳ�
					try {
						if (preparedStatement != null)
							preparedStatement.close();
						if (con1 != null)
							con1.close();
					} catch (SQLException e1) {
						System.out.println("RootController edit-save : " + e1.getMessage());
					}
				}

			});
		} catch (Exception e1) {
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
			Parent root = FXMLLoader.load(getClass().getResource("/view/adminAddBookPopup.fxml"));
			Stage addPopup = new Stage(StageStyle.UTILITY);
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			addPopup.setScene(new Scene(root));
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
			cmbCategory.setItems(dao.categoryList);

			Book book0 = obLBook.get(bookTableSelectIndex);
			selectFileName = book0.getFileimg().trim();
			localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
			localImage = new Image(localUrl);
			imgV.setImage(localImage);
			txtISBN.setText(book0.getIsbn());
			txtISBN.setDisable(true);
			txtTitle.setText(book0.getTitle());
			txtWriter.setText(book0.getWriter());
			cmbCategory.getSelectionModel().select(book0.getCategory());
			txtCompany.setText(book0.getCompany());
			txtDate.setText(book0.getDate());
			txaInformation.setText(book0.getInformation());

			btnFileSelect.setOnAction(eve1 -> {
				Image image = handleBtnImageFileAction(addPopup);
				imgV.setImage(image);
			});

			btnOk.setOnAction(eve -> {

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
					preparedStatement.setString(9, book0.getIsbn());
					book0.setIsbn(txtISBN.getText());
					book0.setTitle(txtTitle.getText());
					book0.setWriter(txtWriter.getText());
					book0.setCategory(cmbCategory.getValue().toString());
					book0.setCompany(txtCompany.getText());
					book0.setDate(txtDate.getText());
					book0.setInformation(txaInformation.getText());

					if (selectFile == null)
						selectFile = new File(directorySave.getAbsolutePath() + "\\" + selectFileName);

					BufferedInputStream bis = null;// ������ ������ ����ϴ� Ŭ����
					BufferedOutputStream bos = null;// ������ ���� ����ϴ� Ŭ����
					String fileName = null;
					try {
						fileName = "Book_" + book0.getIsbn() + "_" + book0.getTitle() + ".jpg";
						preparedStatement.setString(8, fileName);
						book0.setFileimg(fileName);
						bis = new BufferedInputStream(new FileInputStream(selectFile));
						bos = new BufferedOutputStream(
								new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
						int data = -1;// -1���̻� �������� ���ٴ� �ǹ�
						while ((data = bis.read()) != -1) { // �̹������� ũ�⸸ŭ �ݺ�
							bos.write(data); // ���� ����
							bos.flush();// ���ۿ� �ִ� ���� �� �����ϱ����ؼ� ������.
						}
					} catch (Exception e1) {
						System.out.println("���� ���翡�� : " + e1.getMessage());
						return; // ���� �����ε� �ؿ� �����ϴ°����� �����ϸ�ȵǱ⶧���� �������� ����������
					} finally {
						try {
							book0.setFileimg(fileName);
							imageDelete(selectFileName, "book");
							if (bis != null)
								bis.close();
							if (bos != null)
								bos.close();
						} catch (IOException e1) {
						}
					}

					if (preparedStatement.executeUpdate() != 0) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("��� �Ϸ�");
						alert.showAndWait();
						addPopup.close();
						obLBook.set(bookTableSelectIndex, book0);
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("��� ����");
						alert.showAndWait();
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
			Parent root = FXMLLoader.load(getClass().getResource("/view/adminAddBookPopup.fxml"));
			Stage addPopup = new Stage(StageStyle.UTILITY);
			addPopup.initModality(Modality.WINDOW_MODAL);
			addPopup.initOwner(btnBookAdd.getScene().getWindow());
			Scene s = new Scene(root);
			addPopup.setScene(s);
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
					BookDAO dao = new BookDAO();
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
							int data = -1;// -1���̻� �������� ���ٴ� �ǹ�
							while ((data = bis.read()) != -1) { // �̹������� ũ�⸸ŭ �ݺ�
								bos.write(data); // ���� ����
								bos.flush();// ���ۿ� �ִ� ���� �� �����ϱ����ؼ� ������.
							}
						} catch (Exception e1) {
							System.out.println("���� ���翡�� : " + e1.getMessage());
							return; // ���� �����ε� �ؿ� �����ϴ°����� �����ϸ�ȵǱ⶧���� �������� ����������
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
			// TODO: handle exception
		}
	}

	// ������ ���� ��ư �ڵ鷯�̺�Ʈ
	private void handleBtnBookDeleteAction(ActionEvent e) {
		Book selectBook = obLBook.get(bookTableSelectIndex);
		selectFileName = selectBook.getFileimg();
		localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
		localImage = new Image(localUrl, false);
		BookDAO dao = new BookDAO();
		int returnValue = dao.deleteBook(selectBook);
		if (returnValue != 0) {
			// �̹��� ���� ����
			imageDelete(selectFileName, "book");
			obLBook.remove(bookTableSelectIndex);
		} else {
			System.out.println("����");
		}
	}

	// ���� ���̺� �˻� ��ư �̺�Ʈ
	private void handleBtnBookSearchAction(ActionEvent e) {
		
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

		ArrayList<Book> bookTBL = dao.getBookTbl();
		for (Book b : bookTBL) {
			obLBook.add(b);
		}
		tblBook.setItems(obLBook);

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
		colContent.setMaxWidth(80);
		colContent.setCellValueFactory(new PropertyValueFactory("content"));

		TableColumn colTitle = new TableColumn("����");
		colTitle.setMaxWidth(80);
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
			root = FXMLLoader.load(getClass().getResource("/view/requestPopup.fxml"));
			Stage addPopup = new Stage(StageStyle.UTILITY);
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
			addPopup.setScene(new Scene(root));
			addPopup.show();

			btnAdd.setOnAction(eve -> {
				try {
					Parent root1 = FXMLLoader.load(getClass().getResource("/view/adminAddBookPopup.fxml"));
					Stage addPopup1 = new Stage(StageStyle.UTILITY);
					addPopup1.initModality(Modality.NONE);
					addPopup1.initOwner(btnAdd.getScene().getWindow());
					Scene s = new Scene(root1);
					addPopup1.setScene(s);
					addPopup1.show();
					Button btnOk = (Button) s.lookup("#btnOk");
					Button btnCancel = (Button) s.lookup("#btnCancel");
					Button btnFileSelect = (Button) s.lookup("#btnFileSelect");
					TextField txtISBN = (TextField) s.lookup("#txtISBN");
					TextField txtTitle1 = (TextField) s.lookup("#txtTitle");
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

					btnOk.setOnAction(eve1 -> {
						Book book1 = null;
						try {
							book1 = new Book(txtISBN.getText(), txtTitle.getText(), cmbCategory.getValue().toString(),
									txtWriter.getText(), txtCompany.getText(), txtDate.getText(), null,
									txaInformation.getText(), false);
						} catch (Exception e1) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("����");
							alert.setHeaderText("�帣�� �����ϼ���");
							alert.showAndWait();
							return;
						}
						if (selectFile == null) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("�����߻�");
							alert.setHeaderText("�̹��� ������ �����ϼ���");
							alert.setContentText("�������� �����ϼ���.");
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
							int data = -1;// -1���̻� �������� ���ٴ� �ǹ�
							while ((data = bis.read()) != -1) { // �̹������� ũ�⸸ŭ �ݺ�
								bos.write(data); // ���� ����
								bos.flush();// ���ۿ� �ִ� ���� �� �����ϱ����ؼ� ������.
							}
						} catch (Exception e1) {
							System.out.println("���� ���翡�� : " + e1.getMessage());
							return; // ���� �����ε� �ؿ� �����ϴ°����� �����ϸ�ȵǱ⶧���� �������� ����������
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
						BookDAO dao = new BookDAO();
						int returnValue = dao.addBook(book1);
						if (returnValue != 0) {
							obLBook.add(book1);
							addPopup.close();
						}
					});
					btnCancel.setOnAction(eve1 -> addPopup.close());
				} catch (Exception e1) {
					// TODO: handle exception
				}

			});

			btnBack.setOnAction(eve -> addPopup.close());

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/* ========================�����Լ��� ====================== */

	// �̹������� ���� �޼ҵ�
	public boolean imageDelete(String fileName, String type) {
		boolean result = false;
		File fileDelete = null;
		try {
			if (type.equals("book"))
				fileDelete = new File(directorySave.getAbsolutePath() + "\\" + fileName); // �����̹��� ����
			else if (type.equals("member"))
				fileDelete = new File(directoryMemberSave.getAbsolutePath() + "\\" + fileName); // �����̹��� ����

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
			alert.setContentText("�̹������ϸ� ��������ٶ���!!");
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
		// ��� ����
		directoryMemberSave = new File("C:/images/Library_MemberData");
		if (!directoryMemberSave.exists()) {
			directoryMemberSave.mkdir();
			System.out.println("���� ���丮 ����");
		}
	}

}
