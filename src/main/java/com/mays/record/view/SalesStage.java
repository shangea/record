package com.mays.record.view;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.mays.record.dao.SalesDao;
import com.mays.record.model.SalesInfo;
import com.mays.record.util.Constant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * 销售记录窗口
 * @author	mays
 * @date	2018年4月19日
 */
public class SalesStage implements EventHandler<ActionEvent> {

	private GridPane mGrid;
	private Stage mStage;
	private Button mBtnQuery;
	private Button mBtnClean;
	private GridPane mTableGrid;
	private ScrollPane mScrollPane;
	private Label mLlWeightSum;
	private List<String> buyers;
	private ChoiceBox<String> mCbBuyer;
	private ChoiceBox<String> mCbType;
	private DatePicker mDpStart;
	private DatePicker mDpEnd;
	private Button mBtnAdd;

	public SalesStage() {
		mGrid = new GridPane();
//		mGrid.setGridLinesVisible(true);
		mGrid.setVgap(20);
		mGrid.setAlignment(Pos.CENTER);
		mStage = new Stage();
		mStage.setTitle("销售记录");
		mStage.setScene(new Scene(mGrid, 800, 700));
		initData();
		initView();
		initListener();
	}

	private void initData() {
		buyers = SalesDao.selectBuyer();
		buyers.add(0, "全部");
	}

	private void initView() {
		ColumnConstraints col = new ColumnConstraints(200, 640, 800);
		mGrid.getColumnConstraints().add(col);

		// 查询条件
		GridPane paramGrid = new GridPane();
		ColumnConstraints c1 = new ColumnConstraints(100);
		ColumnConstraints c2 = new ColumnConstraints(150);
		paramGrid.getColumnConstraints().addAll(c1, c2, c1, c2);
		paramGrid.setAlignment(Pos.CENTER);
		paramGrid.setHgap(10);
		paramGrid.setVgap(20);

		paramGrid.addColumn(0, ViewBuilder.getLabelRight("客户："), ViewBuilder.getLabelRight("种类："));
		paramGrid.addColumn(2, ViewBuilder.getLabelRight("起始时间："), ViewBuilder.getLabelRight("结束时间："));
		mCbBuyer = new ChoiceBox<>(FXCollections.observableArrayList(buyers));
		mCbBuyer.setValue("全部");
		ObservableList<String> typeList = FXCollections.observableArrayList(Constant.TYPES);
		typeList.add(0, "全部");
		mCbType = new ChoiceBox<>(typeList);
		mCbType.setValue("全部");
		paramGrid.addColumn(1, mCbBuyer, mCbType);
		mDpStart = new DatePicker();
		mDpEnd = new DatePicker();
		paramGrid.addColumn(3, mDpStart, mDpEnd);
		mGrid.add(paramGrid, 0, 0);

		// 查询按钮
		mBtnQuery = ViewBuilder.getButton("查询");
		mBtnClean = ViewBuilder.getButton("清空");
		mBtnAdd = ViewBuilder.getButton("添加");
		HBox hBox = new HBox(40, mBtnQuery, mBtnClean, mBtnAdd);
		hBox.setAlignment(Pos.CENTER);
		GridPane.setHalignment(hBox, HPos.CENTER);
		mGrid.add(hBox, 0, 1);

		// 查询列表
		mScrollPane = new ScrollPane();
		mScrollPane.setPrefHeight(480);
		mGrid.add(mScrollPane, 0, 2);
		initTable();

		// 统计
		mLlWeightSum = ViewBuilder.getLabel("总重量：");
		mGrid.add(new HBox(50, mLlWeightSum), 0, 3);
	}

	private void initTable() {
		mTableGrid = new GridPane();
		mScrollPane.setContent(mTableGrid);
		mTableGrid.setGridLinesVisible(true);
		mTableGrid.setPadding(new Insets(10));
		for (int i = 0; i < 5; i++) {
			mTableGrid.getColumnConstraints().add(i, new ColumnConstraints(50, 120, 150));
		}
		// 添加表头
		mTableGrid.addRow(0, ViewBuilder.getTableLabel("客户"), ViewBuilder.getTableLabel("种类"),
				ViewBuilder.getTableLabel("重量(吨)"), ViewBuilder.getTableLabel("时间"), ViewBuilder.getTableLabel("操作"));
	}

	private void initListener() {
		mBtnQuery.setOnAction(this);
		mBtnClean.setOnAction(this);
		mBtnAdd.setOnAction(this);
	}

	public void show() {
		mStage.show();
	}

	@Override
	public void handle(ActionEvent event) {
		if (mBtnQuery.equals(event.getSource())) {
			// 查询
			query();
		} else if (mBtnClean.equals(event.getSource())) {
			// 清空
			mCbBuyer.setValue("全部");
			mCbType.setValue("全部");
			mDpStart.setValue(null);
			mDpEnd.setValue(null);
		} else if (mBtnAdd.equals(event.getSource())) {
			// 打开添加销售记录页面
			new AddSalesStage(this).show();
		}
	}

	private void query() {
		initTable();
		SalesInfo info = new SalesInfo();
		info.setBuyer(mCbBuyer.getValue());
		info.setType(mCbType.getValue());
		if (mDpStart.getValue() != null) {
			info.setStartTime(mDpStart.getValue().toString());
		}
		if (mDpEnd.getValue() != null) {
			info.setEndTime(mDpEnd.getValue().toString());
		}
		List<SalesInfo> list = SalesDao.selectSalesInfo(info);
		int weightSum = SalesDao.selectSalesInfoSum(info);
		mLlWeightSum.setText("总重量：" + new BigDecimal(weightSum).divide(new BigDecimal(1000)) + " 吨");
		for (int i = 0; i < list.size(); i++) {
			SalesInfo si = list.get(i);
			Button btnDel = new Button("删除");
			GridPane.setHalignment(btnDel, HPos.CENTER);
			mTableGrid.addRow(i + 1, //
					ViewBuilder.getTableLabel(si.getBuyer()), //
					ViewBuilder.getTableLabel(si.getType()), //
					ViewBuilder.getTableLabel(new BigDecimal(si.getWeight()).divide(new BigDecimal(1000)).toString()), //
					ViewBuilder.getTableLabel(si.getSalesTime()), //
					btnDel);
			btnDel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// 删除单条记录
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"确定删除?");
					alert.setTitle("重要操作");
					alert.setHeaderText("");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.OK) {
						try {
							if (1 == SalesDao.delSalesInfo(si.getId().toString())) {
								ViewBuilder.showAlert("删除成功");
								query();
							} else {
								ViewBuilder.showAlert("删除失败");
							}
						} catch (Exception e) {
							ViewBuilder.showAlert("程序异常");
							e.printStackTrace();
						}
					}
				}
			});
		}
	}

	/**
	 * 添加数据成功后调用，1.重新获取客户下拉框数据，2.查询
	 * @author	mays
	 * @date	2018年4月19日
	 */
	public void addSuccess() {
		initData();
		mCbBuyer.setItems(FXCollections.observableArrayList(buyers));
		mCbBuyer.setValue("全部");
		query();
	}

}
