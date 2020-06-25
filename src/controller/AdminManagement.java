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
	Image image=null;
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

		// 요청 테이블 책 더블클릭시 내용창
		tblRequest.setOnMouseClicked(e -> handelTblRequestDoubleClickAction(e));
		// 요청테이블 삭제 버튼 이벤트
		btnRequestDelete.setOnAction(e -> handleBtnRequestDeleteAction(e));
		// 도서 테이블 추가 버튼 이벤트
		btnBookAdd.setOnAction(e -> handleBtnBookAddAction(e));
		// 도서테이블 수정 버튼 이벤트
		btnBookEdit.setOnAction(e -> handleBtnBookEditAction(e));
		// 도서 테이블 책 더블클릭시 수정창
		// tblBook.setOnMouseClicked(e -> handelTblBookDoubleClickAction(e));
		// 도서 테이블 삭제 버튼 이벤트
		btnBookDelete.setOnAction(e -> handleBtnBookDeleteAction(e));
		// 도서 테이블 검색 버튼 이벤트
		btnBookSearch.setOnAction(e -> handleBtnBookSearchAction(e));

		// 유저 테이블 검색 버튼 이벤트
		btnUserSearch.setOnAction(e -> handleBtnUserSearchAction(e));
		// 유저테이블 수정 버튼 이벤트
		btnUserEdit.setOnAction(e -> handleBtnUserEditAction(e));
		// 유저 테이블 삭제 버튼 이벤트
		btnUserDelete.setOnAction(e -> handleBtnUserDeleteAction(e));
		// 관리페이지 뒤로가기
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
				alert.setHeaderText("삭제 완료");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("삭제 실패");
				alert.showAndWait();
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	// 뒤로가기 버튼 핸들러 이벤트
	private void handleBtnBackAction(ActionEvent e) {
		Stage adminMain = null;
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/adminMain.fxml"));
			Scene scene = new Scene(root);
			adminMain = new Stage();
			adminMain.setTitle("관리자 메인");
			adminMain.setScene(scene);
			adminMain.setResizable(true);
			((Stage) btnBack.getScene().getWindow()).close();
			adminMain.show();
		} catch (IOException e1) {
		}
	}

	/* ========================회원관리====================== */

	// 유저 테이블 검색 버튼 이벤트
	private void handleBtnUserSearchAction(ActionEvent e) {
		MemberDAO dao = new MemberDAO();
		ArrayList<Member> arrayList = dao.searchUser(txtUserSearch.getText());
		obLMember.clear();
		for (Member m : arrayList) {
			obLMember.add(m);

		}
	}

	// 유저 테이블 삭제 버튼 이벤트
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
				alert.setHeaderText("삭제 완료");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("삭제 실패");
				alert.showAndWait();
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// 유저테이블 수정 버튼 이벤트
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
			cmbEtc.setItems(FXCollections.observableArrayList("정상", "연체"));
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

					BufferedInputStream bis = null;// 파일을 읽을때 사용하는 클래스
					BufferedOutputStream bos = null;// 파일을 쓸때 사용하는 클래스
					String fileName = null;
					try {
						fileName = "Member_" + selectUser.getId() + "_" + selectUser.getName() + ".jpg";
						preparedStatement.setString(6, fileName);
						selectUser.setFileimg(fileName);
						bis = new BufferedInputStream(new FileInputStream(selectFile));
						bos = new BufferedOutputStream(
								new FileOutputStream(directoryMemberSave.getAbsolutePath() + "\\" + fileName));
						int data = -1;// -1더이상 읽을값이 없다는 의미
						while ((data = bis.read()) != -1) { // 이미지파일 크기만큼 반복
							bos.write(data); // 파일 복사
							bos.flush();// 버퍼에 있는 값을 다 저장하기위해서 보내라.
						}
					} catch (Exception e1) {
						System.out.println("파일 복사에러 : " + e1.getMessage());
						return; // 파일 에러인데 밑에 저장하는과정을 실행하면안되기때문에 리턴으로 끝내버린다
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
						alert.setHeaderText("등록 완료");
						alert.showAndWait();
						addPopup.close();
						obLMember.set(userTableSelectIndex, selectUser);
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("등록 실패");
						alert.showAndWait();
						throw new Exception();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("등록 실패 : DB에러");
					alert.setContentText(e1.getMessage());
					alert.showAndWait();
				} finally {
					// con,pstmt 반납
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

	// 유저데이터 테이블뷰셋팅 메소드
	private void tblUserColumnSetting() {

		TableColumn colName = new TableColumn("이름");
		colName.setMaxWidth(150);
		colName.setCellValueFactory(new PropertyValueFactory("name"));
		TableColumn colId = new TableColumn("ID");
		colId.setMaxWidth(100);
		colId.setCellValueFactory(new PropertyValueFactory("id"));
		TableColumn colPhoneNumber = new TableColumn("전화번호");
		colPhoneNumber.setMaxWidth(200);
		colPhoneNumber.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
		TableColumn colBirth = new TableColumn("생년월일");
		colBirth.setMaxWidth(150);
		colBirth.setCellValueFactory(new PropertyValueFactory("birth"));
		TableColumn colRentalBook = new TableColumn("대여중인 도서");
		colRentalBook.setMaxWidth(300);
		colRentalBook.setCellValueFactory(new PropertyValueFactory("rentalBook"));
		TableColumn colEtc = new TableColumn("비고");
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

	/* ========================도서관리====================== */

	// 도서탭 수정 버튼 핸들러이벤트
	private void handleBtnBookEditAction(ActionEvent e) {

		try {
			
			if (bookTableSelectIndex == -1)
				throw new Exception("수정할 데이터를 선택하세요.");
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

			btnFileSelect.setOnAction(eve1 -> {
				image = handleBtnImageFileAction(addPopup);
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

					if ((selectFile == null)) {
						//selectFile = new File(directorySave.getAbsolutePath() + "\\" + selectFileName);
					selectFile = new File(directorySave.getAbsolutePath() + "\\" + selectFileName);
					//System.out.println(selectFileName);
					//System.out.println(selectFile.toString());
					}
					BufferedInputStream bis = null;// 파일을 읽을때 사용하는 클래스
					BufferedOutputStream bos = null;// 파일을 쓸때 사용하는 클래스
					String fileName = null;
					try {
						fileName = "Book_" + book0.getIsbn() + "_" + book0.getTitle() + ".jpg";
						preparedStatement.setString(8, fileName);
						book0.setFileimg(fileName);
						bis = new BufferedInputStream(new FileInputStream(selectFile));
						bos = new BufferedOutputStream(
								new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
						int data = -1;// -1더이상 읽을값이 없다는 의미
						while ((data = bis.read()) != -1) { // 이미지파일 크기만큼 반복
							bos.write(data); // 파일 복사
							bos.flush();// 버퍼에 있는 값을 다 저장하기위해서 보내라.
						}
					} catch (Exception e1) {
						System.out.println("파일 복사에러 : " + e1.getMessage());
						return; // 파일 에러인데 밑에 저장하는과정을 실행하면안되기때문에 리턴으로 끝내버린다
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
						alert.setHeaderText("등록 완료");
						alert.showAndWait();
						addPopup.close();
						obLBook.set(bookTableSelectIndex, book0);
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("등록 실패");
						alert.showAndWait();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("등록 실패 : DB에러");
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
			alert.setHeaderText("데이터 미선택");
			alert.setContentText("수정할 데이터를 선택하세요.");
			alert.showAndWait();
		}

	}

	// 도서탭 추가 버튼 핸들러이벤트
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
						alert.setTitle("문제발생");
						alert.setHeaderText("이미지 파일을 선택하세요");
						alert.setContentText("다음에는 주의하세요.");
						alert.showAndWait();
						return;
					}
					int returnValue = dao.addBook(book1);
					if (returnValue != 0) {

						obLBook.add(book1);

						BufferedInputStream bis = null;// 파일을 읽을때 사용하는 클래스
						BufferedOutputStream bos = null;// 파일을 쓸때 사용하는 클래스
						try {

							bis = new BufferedInputStream(new FileInputStream(selectFile));
							bos = new BufferedOutputStream(
									new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
							int data = -1;// -1더이상 읽을값이 없다는 의미
							while ((data = bis.read()) != -1) { // 이미지파일 크기만큼 반복
								bos.write(data); // 파일 복사
								bos.flush();// 버퍼에 있는 값을 다 저장하기위해서 보내라.
							}
						} catch (Exception e1) {
							System.out.println("파일 복사에러 : " + e1.getMessage());
							return; // 파일 에러인데 밑에 저장하는과정을 실행하면안되기때문에 리턴으로 끝내버린다
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
					alert.setTitle("오류");
					alert.setHeaderText("모든 항목을 작성하세요");
					alert.showAndWait();
					return;
				}

			});
			btnCancel.setOnAction(eve -> addPopup.close());
		} catch (Exception e1) {
			// TODO: handle exception
		}
	}

	// 도서탭 삭제 버튼 핸들러이벤트
	private void handleBtnBookDeleteAction(ActionEvent e) {
		Book selectBook = obLBook.get(bookTableSelectIndex);
		selectFileName = selectBook.getFileimg();
		localUrl = "file:/C:/images/Library_BookData/" + selectFileName;
		localImage = new Image(localUrl, false);
		BookDAO dao = new BookDAO();
		int returnValue = dao.deleteBook(selectBook);
		if (returnValue != 0) {
			// 이미지 파일 삭제
			imageDelete(selectFileName, "book");
			obLBook.remove(bookTableSelectIndex);
		} else {
			System.out.println("실패");
		}
	}

	// 도서 테이블 검색 버튼 이벤트
	private void handleBtnBookSearchAction(ActionEvent e) {
		
		ArrayList<Book> arrayList = dao.searchBook(txtBookSearch.getText(), "title");
		obLBook.clear();
		for (Book b : arrayList) {
			obLBook.add(b);

		}

	}

	// 도서 테이블 셋팅 메소드
	private void tblBookColumnSetting() {
		TableColumn colISBN = new TableColumn("ISBN");
		colISBN.setMaxWidth(150);
		colISBN.setCellValueFactory(new PropertyValueFactory("isbn"));

		TableColumn colTitle = new TableColumn("제목");
		colTitle.setMaxWidth(200);
		colTitle.setCellValueFactory(new PropertyValueFactory("title"));

		TableColumn colCategory = new TableColumn("장르");
		colCategory.setMaxWidth(80);
		colCategory.setCellValueFactory(new PropertyValueFactory("category"));

		TableColumn colWriter = new TableColumn("저자");
		colWriter.setMaxWidth(200);
		colWriter.setCellValueFactory(new PropertyValueFactory("writer"));

		TableColumn colCompany = new TableColumn("출판사");
		colCompany.setMaxWidth(2000);
		colCompany.setCellValueFactory(new PropertyValueFactory("company"));

		TableColumn colDate = new TableColumn("출판일");
		colDate.setMaxWidth(150);
		colDate.setCellValueFactory(new PropertyValueFactory("date"));

		TableColumn colRental = new TableColumn("대여여부");
		colRental.setMaxWidth(90);
		colRental.setCellValueFactory(new PropertyValueFactory("rental"));

		TableColumn colInformation = new TableColumn("책 소개");
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

	/* ========================자료요청관리====================== */

	// 자료요청 테이블 셋팅 메소드
	private void tblRequestColumnSetting() {

		TableColumn colName = new TableColumn("작성자");
		colName.setMaxWidth(80);
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colNo = new TableColumn("No");
		colNo.setMaxWidth(80);
		colNo.setCellValueFactory(new PropertyValueFactory("no"));

		TableColumn colContent = new TableColumn("내용");
		colContent.setMaxWidth(80);
		colContent.setCellValueFactory(new PropertyValueFactory("content"));

		TableColumn colTitle = new TableColumn("제목");
		colTitle.setMaxWidth(80);
		colTitle.setCellValueFactory(new PropertyValueFactory("title"));
		TableColumn colDate = new TableColumn("날짜");
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

	// 요청 테이블 책 더블클릭시 내용창
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
							alert.setTitle("오류");
							alert.setHeaderText("장르를 선택하세요");
							alert.showAndWait();
							return;
						}
						if (selectFile == null) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("문제발생");
							alert.setHeaderText("이미지 파일을 선택하세요");
							alert.setContentText("다음에는 주의하세요.");
							alert.showAndWait();
							return;
						}
						BufferedInputStream bis = null;// 파일을 읽을때 사용하는 클래스
						BufferedOutputStream bos = null;// 파일을 쓸때 사용하는 클래스
						String fileName = null;
						try {
							fileName = "Book_" + book1.getIsbn() + "_" + book1.getTitle() + ".jpg";
							bis = new BufferedInputStream(new FileInputStream(selectFile));
							bos = new BufferedOutputStream(
									new FileOutputStream(directorySave.getAbsolutePath() + "\\" + fileName));
							int data = -1;// -1더이상 읽을값이 없다는 의미
							while ((data = bis.read()) != -1) { // 이미지파일 크기만큼 반복
								bos.write(data); // 파일 복사
								bos.flush();// 버퍼에 있는 값을 다 저장하기위해서 보내라.
							}
						} catch (Exception e1) {
							System.out.println("파일 복사에러 : " + e1.getMessage());
							return; // 파일 에러인데 밑에 저장하는과정을 실행하면안되기때문에 리턴으로 끝내버린다
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

	/* ========================서브함수들 ====================== */

	// 이미지파일 삭제 메소드
	public boolean imageDelete(String fileName, String type) {
		boolean result = false;
		File fileDelete = null;
		try {
			if (type.equals("book"))
				fileDelete = new File(directorySave.getAbsolutePath() + "\\" + fileName); // 삭제이미지 파일
			else if (type.equals("member"))
				fileDelete = new File(directoryMemberSave.getAbsolutePath() + "\\" + fileName); // 삭제이미지 파일

			if (fileDelete.exists() && fileDelete.isFile()) {
				result = fileDelete.delete();
			}
		} catch (Exception ie) {
			System.out.println(ie.getMessage());
			result = false;
		}
		return result;
	}

	// 이미지파일선택 버튼 이벤트 등록 핸들러 함수 처리
	private Image handleBtnImageFileAction(Stage stage) {
		Image image = null;
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		// 선택한 이미지파일을 파일형식으로 돌려준다.
		selectFile = fileChooser.showOpenDialog(stage);
		try {
			if (selectFile != null) {
				String localURL = selectFile.toURI().toURL().toString();// 파일의 실제 경로명!!알아두자
				image = new Image(localURL, false);
			
			} else {

			}
		} catch (MalformedURLException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("사진 가져오기");
			alert.setHeaderText("사진 가져오기 문제 발생");
			alert.setContentText("이미지파일만 가져오기바란다!!");
			alert.showAndWait();
		}
		return image;
	}

	// 이미지저장 디렉토리 생성 메소드
	private void setDirectorySaveImage() {
		// 도서 폴더
		directorySave = new File("C:/images/Library_BookData");
		if (!directorySave.exists()) {
			directorySave.mkdir();
			System.out.println("도서 디렉토리 생성");
		}
		// 멤버 폴더
		directoryMemberSave = new File("C:/images/Library_MemberData");
		if (!directoryMemberSave.exists()) {
			directoryMemberSave.mkdir();
			System.out.println("유저 디렉토리 생성");
		}
	}

}
