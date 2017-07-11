package pl.sda.library.table.model;

import java.util.LinkedList;
import java.util.List;

import pl.sda.library.model.Book;

public class MyBatisDataTableModel extends CrudDataTableModel {

	private static final long serialVersionUID = 1L;

	public MyBatisDataTableModel() {
		filterByName("");
	}

	@Override
	public int getRowCount() {
		//TODO liczba książek
		return getByName(filter).size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Book book = getByName(filter).get(rowIndex);
		switch (columnIndex) {
			case 0:
				return book.getId();
			case 1:
				return book.getTitle();
			case 2:
				return book.getAuthorFirstName();
			case 3:
				return book.getAuthorLastName();
			case 4:
				return book.getCategories();
			default:
				return null;
		}
	}

	@Override
	public Book getById(int id) {
		//TODO książka na podstawie id
		return null;
	}

	@Override
	public List<Book> getByName(String name) {
		//TODO książki na podstawie nazwy
		return new LinkedList<>();
	}

	@Override
	public void create(Book book) {
		//TODO dodanie książki
		refresh();
	}

	@Override
	public void update(Book book) {
		//TODO modyfikacja książki
		refresh();
	}

	@Override
	public void delete(Book book) {
		//TODO usunięcie książki
		refresh();
	}

}
