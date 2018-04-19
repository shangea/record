package com.mays.record.view;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.mays.record.dao.ProduceDao;
import com.mays.record.model.ProduceInfo;
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
 * 生产记录窗口
 * 
 * @author mays
 * @date 2018年4月18日
 */
public class ProduceStage implements EventHandler<ActionEvent> {

	private GridPane mGrid;
	private Stage mStage;
	private Button mBtnQuery;
	private Button mBtnClean;
	private GridPane mTableGrid;
	private ScrollPane mScrollPane;
	private Label mLlNumSum;
	private Label mLlWeightSum;
	private List<String> workers;
	private ChoiceBox<String> mCbWorker;
	private ChoiceBox<String> mCbType;
	private DatePicker mDpStart;
	private DatePicker mDpEnd;
	private Button mBtnAdd;

	public ProduceStage() {
		mGrid = new GridPane();
//		mGrid.setGridLinesVisible(true);
		mGrid.setVgap(20);
		mGrid.setAlignment(Pos.CENTER);
		mStage = new Stage();
		mStage.setTitle("生产记录");
		mStage.setScene(new Scene(mGrid, 800, 700));
		initData();
		initView();
		initListener();
	}

	private void initData() {
		workers = ProduceDao.selectWorker();
		workers.add(0, "全部");
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

		paramGrid.addColumn(0, ViewBuilder.getLabelRight("员工："), ViewBuilder.getLabelRight("种类："));
		paramGrid.addColumn(2, ViewBuilder.getLabelRight("起始时间："), ViewBuilder.getLabelRight("结束时间："));
		mCbWorker = new ChoiceBox<>(FXCollections.observableArrayList(workers));
		mCbWorker.setValue("全部");
		ObservableList<String> typeList = FXCollections.observableArrayList(Constant.TYPES);
		typeList.add(0, "全部");
		mCbType = new ChoiceBox<>(typeList);
		mCbType.setValue("全部");
		paramGrid.addColumn(1, mCbWorker, mCbType);
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
		mLlNumSum = ViewBuilder.getLabel("总包数：\t\t");
		mLlWeightSum = ViewBuilder.getLabel("总重量：");
		mGrid.add(new HBox(50, mLlNumSum, mLlWeightSum), 0, 3);
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
		mTableGrid.addRow(0, ViewBuilder.getTableLabel("员工"), ViewBuilder.getTableLabel("种类"),
				ViewBuilder.getTableLabel("包数"), ViewBuilder.getTableLabel("时间"), ViewBuilder.getTableLabel("操作"));
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
			mCbWorker.setValue("全部");
			mCbType.setValue("全部");
			mDpStart.setValue(null);
			mDpEnd.setValue(null);
		} else if (mBtnAdd.equals(event.getSource())) {
			// 打开添加生产记录页面
			new AddProduceStage(this).show();
		}
	}

	private void query() {
		initTable();
		ProduceInfo info = new ProduceInfo();
		info.setWorker(mCbWorker.getValue());
		info.setType(mCbType.getValue());
		if (mDpStart.getValue() != null) {
			info.setStartTime(mDpStart.getValue().toString());
		}
		if (mDpEnd.getValue() != null) {
			info.setEndTime(mDpEnd.getValue().toString());
		}
		List<ProduceInfo> list = ProduceDao.selectProduceInfo(info);
		int numSum = ProduceDao.selectProduceInfoSum(info);
		BigDecimal weightSum = new BigDecimal(numSum).divide(new BigDecimal(40));
		mLlNumSum.setText("总包数：" + numSum + " \t");
		mLlWeightSum.setText("总重量：" + weightSum + " 吨");
		for (int i = 0; i < list.size(); i++) {
			ProduceInfo pi = list.get(i);
			Button btnDel = new Button("删除");
			GridPane.setHalignment(btnDel, HPos.CENTER);
			mTableGrid.addRow(i + 1, //
					ViewBuilder.getTableLabel(pi.getWorker()), //
					ViewBuilder.getTableLabel(pi.getType()), //
					ViewBuilder.getTableLabel(pi.getPackNum().toString()), //
					ViewBuilder.getTableLabel(pi.getProduceTime()), //
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
							if (1 == ProduceDao.delProduceInfo(pi.getId().toString())) {
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
	 * 添加数据成功后调用，1.重新获取员工下拉框数据，2.查询
	 * @author	mays
	 * @date	2018年4月19日
	 */
	public void addSuccess() {
		initData();
		mCbWorker.setItems(FXCollections.observableArrayList(workers));
		mCbWorker.setValue("全部");
		query();
	}

}
