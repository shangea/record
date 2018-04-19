package com.mays.record.view;

import java.util.List;
import java.util.TreeSet;

import com.mays.record.dao.UserDao;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * 删除人员
 * 
 * @author mays
 * @date 2018年4月16日
 */
public class DelUserStage {

	private GridPane mGrid;
	private Stage mStage;
	private UserStage mUserStage;
	private Button mBtnRemove;
	private GridPane mCbPane;
	private TreeSet<String> mSet;

	public DelUserStage(UserStage userStage) {
		mUserStage = userStage;
		mGrid = new GridPane();
		// mGrid.setGridLinesVisible(true);
		mGrid.setHgap(10);
		mGrid.setVgap(40);
		mGrid.setAlignment(Pos.CENTER);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPadding(new Insets(10));
		scrollPane.setContent(mGrid);
		mStage = new Stage();
		mStage.setTitle("删除人员");
		mStage.setScene(new Scene(scrollPane, 500, 400));

		initView();
		initData();
		initListener();
	}

	private void initView() {
		ColumnConstraints col = new ColumnConstraints(450);
		mGrid.getColumnConstraints().add(col);

		mCbPane = new GridPane();
		mCbPane.setHgap(15);
		mCbPane.setVgap(15);
		mCbPane.setAlignment(Pos.CENTER);
		// mCbPane.setGridLinesVisible(true);
		mGrid.add(mCbPane, 0, 0);

		mBtnRemove = ViewBuilder.getButton("删除");
		GridPane.setHalignment(mBtnRemove, HPos.CENTER);
		mGrid.add(mBtnRemove, 0, 1);
	}

	private void initData() {
		mSet = new TreeSet<>();
		List<String> nameList = UserDao.selectAllUserName();
		for (int i = 0; i < nameList.size(); i++) {
			String cbName = nameList.get(i);
			CheckBox cb = new CheckBox(cbName);
			cb.setFont(Font.font("System Regular", FontWeight.NORMAL, 16));
			cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						mSet.add(cbName);
					} else {
						mSet.remove(cbName);
					}
				}
			});
			mCbPane.add(cb, i % 3, i / 3);
		}
	}

	private void initListener() {
		mBtnRemove.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (mSet.size() == 0) {
					ViewBuilder.showAlert("请勾选要删除的名字");
					return;
				}
				try {
					for (String s : mSet) {
						if (1 != UserDao.delUserInfo(s)) {
							ViewBuilder.showAlert("删除失败");
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					ViewBuilder.showAlert("删除失败");
				}
				ViewBuilder.showAlert("删除成功");
				if (mUserStage != null) {
					mUserStage.initData();
				}
				mStage.close();
			}
		});
	}

	public void show() {
		mStage.show();
	}

}
