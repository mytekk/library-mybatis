package pl.sda.library.table.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import pl.sda.library.model.Book;

public class MyBatisDataTableModel extends CrudDataTableModel {

	private static final long serialVersionUID = 1L;

	private static final String NAMESPACE = "pl.sda.library.mybatis.LibraryMapper";

	private SqlSessionFactory sqlSessionFactory; //obiekt obslugujacy polaczenie do DB

	public MyBatisDataTableModel() {

		try {
			InputStream inputStream = Resources
					.getResourceAsStream("mybatis-config.xml");

			sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(inputStream);  //definiujemy fabryke, ktora poslozy do otwierania poszczegolnych polaczen (session) do bazy
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		filterByName(""); //odpowiada za odsiwerzenie tabelki z ksiazkami, gdzies w srodku wywoluje getValueAt
	}

	@Override
	public int getRowCount() {
		//TODO liczba książek
		SqlSession session = sqlSessionFactory.openSession();
		int count = session.selectOne(NAMESPACE + ".countBooks");  //selectOne zwraca tylko jeden wiersz
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
		Map<String,Object> mapa = new HashMap<>(); //mapa parametrow do wstrzynkiecia w zapytanie
		mapa.put("id", id); //kolejnosc parametrow w mapie nie ma znaczenia, tylko nazwa musi licowac sie z tym co wpisalismy ( #{nazwa} ) w zapytaniu
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
		Map<String, Object> mapaParametrow = new HashMap<>();
		mapaParametrow.put("title", book.getTitle());
		mapaParametrow.put("authorFirstName", book.getAuthorFirstName());
		mapaParametrow.put("authorLastName", book.getAuthorLastName());

		SqlSession session = sqlSessionFactory.openSession();
		session.insert(NAMESPACE + ".insertAuthor", mapaParametrow);
		System.out.println(mapaParametrow.get("authorId"));
		session.insert(NAMESPACE + ".insertBook", mapaParametrow); //w tym miejsu mapa powinna zawierac wartosc authorId
		session.commit();												//bo zapytanie w libraryMapper jest tak skonstruowane
                                                                //ze po pomyslnym insercie dorzuci nam id wrzuconego wiersza do mapy parametrow
		refresh();                                          //w dodatku pod taka nazwa jaka tam zdefiniujemy
	}

	@Override
	public void update(Book book) {
		//TODO modyfikacja książki
		SqlSession session = sqlSessionFactory.openSession();

		Map<String, Object> map = new HashMap<>();
		map.put("authorFirstName", book.getAuthorFirstName());
		map.put("authorLastName", book.getAuthorLastName());
        map.put("title", book.getTitle());
        map.put("id", book.getId());
		//pomocniczym zapytaniem pobieram id autora ksiazki i dorzucam go do mapy, bedzie potrzebny przy updejcie
        Integer authorId = session.selectOne(NAMESPACE + ".getAuthorId", map);
        map.put("authorId", authorId);

        //teraz wlasciwe updejty. Jesli zwrocony author id jest null (czyli ksiazka niemiala autora), to dodajemy autora
        if (authorId != null) {
            System.out.println("aktualizuje autora");
            session.update(NAMESPACE + ".updateAuthor", map);
            session.update(NAMESPACE + ".updateBook", map);
        } else { //jesli byl autor, to
            System.out.println("wstawiam nowego autora");
            session.insert(NAMESPACE + ".insertAuthor", map);
            session.update(NAMESPACE + ".updateBookWithAuthor", map);
        }
        session.commit();

		refresh();
	}

	@Override
	public void delete(Book book) {
		//TODO usunięcie książki
        SqlSession session = sqlSessionFactory.openSession();

        Map<String, Object> map = new HashMap<>();
        map.put("id", book.getId());

        session.delete(NAMESPACE + ".deleteBookCategory", map);
        session.delete(NAMESPACE + ".deleteBook", map);
        session.commit();

        refresh();
	}

}
