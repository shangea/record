package com.mays.record.view;

import java.util.List;

import com.mays.record.dao.UserDao;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 人员管理窗口
 * @author mays
 * @date 2018年4月12日
 */
public class UserStage implements EventHandler<ActionEvent> {

	private GridPane mGrid;
	private Stage mStage;
	private Label mLlBuyer;
	private Label mLlWorker;
	private Button mBtnRefresh;
	private Button mBtnAddUser;
	private Button mBtnDelUser;

	public UserStage() {
		mGrid = new GridPane();
//		mGrid.setGridLinesVisible(true);
		mGrid.setHgap(5);
		mGrid.setVgap(10);
		mGrid.setAlignment(Pos.CENTER);
		mStage = new Stage();
		mStage.setTitle("人员管理");
		mStage.setScene(new Scene(mGrid, 500, 500));

		initView();
		initData();
		initListener();
	}

	private void initView() {
		ColumnConstraints colNull = new ColumnConstraints(10);
		ColumnConstraints col = new ColumnConstraints(50, 100, 300);
		ColumnConstraints colBtn = new ColumnConstraints(100, 120, 300);
		mGrid.getColumnConstraints().addAll(col, colNull, col, colBtn);
		mGrid.add(ViewBuilder.getText("客户："), 0, 0);
		mGrid.add(ViewBuilder.getText("员工："), 2, 0);

		mLlBuyer = ViewBuilder.getLabel("");
		VBox vbb = new VBox();
		vbb.getChildren().add(mLlBuyer);
		mGrid.add(vbb, 0, 1, 1, 3);
		mLlWorker = ViewBuilder.getLabel("");
		VBox vbw = new VBox();
		vbw.getChildren().add(mLlWorker);
		mGrid.add(vbw, 2, 1, 1, 3);

		mBtnRefresh = ViewBuilder.getButton("刷新");
		mBtnAddUser = ViewBuilder.getButton("添加人员");
		mBtnDelUser = ViewBuilder.getButton("删除人员");
		GridPane.setHalignment(mBtnRefresh, HPos.RIGHT);
		GridPane.setHalignment(mBtnAddUser, HPos.RIGHT);
		GridPane.setHalignment(mBtnDelUser, HPos.RIGHT);
		mGrid.addColumn(3, mBtnRefresh, new Text(), mBtnAddUser, mBtnDelUser);

		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		mGrid.add(separator, 1, 0, 1, 3);
	}

	public void initData() {
		List<String> workerList = UserDao.selectUserByType(0);
		List<String> buyerList = UserDao.selectUserByType(1);
		mLlBuyer.setText(listToString(buyerList));
		mLlWorker.setText(listToString(workerList));
	}

	private void initListener() {
//		mStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//			@Override
//			public void handle(WindowEvent event) {
//				//窗口关闭监听
//				mUserDao.close();
//			}
//		});
		mBtnRefresh.setOnAction(this);
		mBtnAddUser.setOnAction(this);
		mBtnDelUser.setOnAction(this);
	}

	public String listToString(List<String> list) {
		String res = "";
		for (int i = 0; i < list.size(); i++) {
			res += list.get(i);
			if (i != list.size() - 1) {
				res += "\r\n";
			}
		}
		return res;
	}

	public void show() {
		mStage.show();
	}

	@Override
	public void handle(ActionEvent event) {
		if (mBtnRefresh.equals(event.getSource())) {
			// 刷新
			initData();
		} else if (mBtnAddUser.equals(event.getSource())) {
			// 添加人员
			new AddUserStage(this).show();
		} else if (mBtnDelUser.equals(event.getSource())) {
			// 删除人员
			new DelUserStage(this).show();
		}
	}

}
