package pl.jstk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
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

	/**
	 * Returns a list of all books. 
	 * This method always returns immediately.
	 * This method does not take any params.
	 * @return List of booksTO.
	 */
	
	public List<BookTo> findAllBooks() {
		return BookMapper.map2To(bookRepository.findAll());
	}
	
	/**
	 * Returns a list of all books fin by title. 
	 * This method always returns immediately.
	 * @param title of book.
	 * @return      Books found by title.
	 */
	@Override
	public List<BookTo> findBooksByTitle(String title) {
		return BookMapper.map2To(bookRepository.findBookByTitle(title));
	}
	
	/**
	 * Returns a list of all books fin by author. 
	 * This method always returns immediately.
	 * @param author of book.
	 * @return Books found by author.
	 */
	@Override
	public List<BookTo> findBooksByAuthor(String author) {
		return BookMapper.map2To(bookRepository.findBookByAuthor(author));
	}
	
	/**
	 * Returns a book and save it. 
	 * This method always returns immediately.
	 * @param Book.
	 * @return      Book entity.
	 */
	@Override
	@Transactional
	public BookTo saveBook(BookTo book) {
		BookEntity entity = BookMapper.map(book);
		entity = bookRepository.save(entity);
		return BookMapper.map(entity);
	}
	/**
	 * Method to remove book from repository.
	 * This method always do immediately.
	 * @param Id of book.
	 */
	@Override
	@Transactional
	public void deleteBook(Long id) {
		bookRepository.deleteById(id);

	}
	/**
	 * Returns a book by its id. 
	 * This method always returns immediately.
	 * @param Id of book.
	 * @return      Book.
	 */
	@Override
	public BookTo getBookById(Long id) {
		return BookMapper.map(bookRepository.getOne(id));
	}
	/**
	 * Returns list of books filtered by author or tile. 
	 * This method always returns immediately.
	 * @param Found book.
	 * @return      List of books.
	 */
	@Override
	public List<BookTo> findBookByTitleOrAuthor(BookTo foundBook) {

		List<BookTo> books = findAllBooks();
		if (foundBook.getTitle().length() != 0) {
			books = books.stream().filter(b -> b.getTitle().equals(foundBook.getTitle())).collect(Collectors.toList());
		}
		if (foundBook.getAuthors().length() != 0) {
			books = books.stream().filter(b -> b.getAuthors().equals(foundBook.getAuthors()))
					.collect(Collectors.toList());
		}
	
		return books;
	}

}
