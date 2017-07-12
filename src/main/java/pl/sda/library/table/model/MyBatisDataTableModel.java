package pl.sda.library.table.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import pl.sda.library.model.Book;

public class MyBatisDataTableModel extends CrudDataTableModel {

	private static final long serialVersionUID = 1L;

	private static final String NAMESPACE = "pl.sda.library.mybatis.LibraryMapper";

	private SqlSessionFactory sqlSessionFactory;

	public MyBatisDataTableModel() {

		try {
			InputStream inputStream = Resources
					.getResourceAsStream("mybatis-config.xml");

			sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(inputStream);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		filterByName("");
	}

	@Override
	public int getRowCount() {
		//TODO liczba książek
		SqlSession session = sqlSessionFactory.openSession();
		int count = session.selectOne(NAMESPACE + ".countBooks");
		session.close();
		return count;
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
		SqlSession session = sqlSessionFactory.openSession();
		Map<String,Object> mapa = new HashMap<>();
		mapa.put("id", id);
		Book book = session.selectOne(NAMESPACE+".getById",mapa);
		session.close();
		return book;
	}

	@Override
	public List<Book> getByName(String name) {
		//TODO książki na podstawie nazwy
		SqlSession session = sqlSessionFactory.openSession();
		List<Book> listOfBooks = session.selectList(NAMESPACE+".getBooks");
		session.close();
		return listOfBooks;
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
