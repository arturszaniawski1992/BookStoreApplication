package pl.jstk.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.jstk.entity.BookEntity;
import pl.jstk.mapper.BookMapper;
import pl.jstk.repository.BookRepository;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

	private BookRepository bookRepository;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public List<BookTo> findAllBooks() {
		return BookMapper.map2To(bookRepository.findAll());
	}

	@Override
	public List<BookTo> findBooksByTitle(String title) {
		return BookMapper.map2To(bookRepository.findBookByTitle(title));
	}

	@Override
	public List<BookTo> findBooksByAuthor(String author) {
		return BookMapper.map2To(bookRepository.findBookByAuthor(author));
	}

	@Override
	@Transactional
	public BookTo saveBook(BookTo book) {
		BookEntity entity = BookMapper.map(book);
		entity = bookRepository.save(entity);
		return BookMapper.map(entity);
	}

	@Override
	@Transactional
	public void deleteBook(Long id) {
		bookRepository.deleteById(id);

	}

	@Override
	public BookTo getBookById(Long id) {
		return BookMapper.map(bookRepository.getOne(id));
	}

	public List<BookTo> findBookByTitleOrAuthor(BookTo foundBook) {
		String title = foundBook.getTitle();
		String author = foundBook.getAuthors();
		List<BookTo> listOfFoundBooks = null;

		if (title != "" && author == "") {
			List<BookTo> listByTitle = findBooksByTitle(foundBook.getTitle());
			return listByTitle;
		} else if (title == "" && author != "") {
			List<BookTo> listByAuthor = findBooksByAuthor(foundBook.getTitle());
			return listByAuthor;
		} else if (title != "" && author != "") {
			List<BookTo> listOfAllBooks = findAllBooks();
			listOfAllBooks = listOfAllBooks.stream().filter(b -> b.getAuthors().equals(author))
					.filter(t -> t.getTitle().equals(title)).collect(Collectors.toList());
		} else if (title != "" && author == "") {
			return listOfFoundBooks;

		}
		return listOfFoundBooks;
	}
	/*
	 * public List<BookTo> findBookByTitleOrAuthor(BookTo foundBook) { String
	 * title = foundBook.getTitle().toLowerCase(); String authors =
	 * foundBook.getAuthors().toLowerCase(); List<BookTo> searchedBooksList =
	 * null; if (title.equals("") && authors.equals("")) { searchedBooksList =
	 * findAllBooks(); } if (title.equals("") && !authors.equals("")) {
	 * searchedBooksList = findBooksByTitle(authors); } if (!title.equals("") &&
	 * authors.equals("")) { searchedBooksList = findBooksByTitle(title); }
	 * 
	 * if (!title.equals("") && !authors.equals("")) { searchedBooksList =
	 * findBooksByTitle(title); searchedBooksList = searchedBooksList.stream()
	 * .filter(book ->
	 * book.getAuthors().toLowerCase().contains(authors)).collect( Collectors.
	 * toList());
	 * 
	 * } return searchedBooksList;
	 * 
	 * }
	 */

}
