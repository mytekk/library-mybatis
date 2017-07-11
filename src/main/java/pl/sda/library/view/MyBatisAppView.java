package pl.sda.library.view;

import pl.sda.library.table.model.CrudDataTableModel;
import pl.sda.library.table.model.MyBatisDataTableModel;

public class MyBatisAppView extends AppView {

	@Override
	protected CrudDataTableModel getDataTableModel() {
		return new MyBatisDataTableModel();
	}

}
