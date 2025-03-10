package com.example.BookNetwork.book;

import com.example.BookNetwork.book.exception.OperationNotPermittedException;
import com.example.BookNetwork.common.GenericResponse;
import com.example.BookNetwork.common.ResponseStatusEnum;
import com.example.BookNetwork.file.FileStorageService;
import com.example.BookNetwork.history.BookTransactionHistory;
import com.example.BookNetwork.history.BookTransactionHistoryRepository;
import com.example.BookNetwork.user.User;
import com.example.BookNetwork.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.BookNetwork.book.BookSpecification.withOwner;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private final FileStorageService fileStorageService;
    private final BookTransactionHistoryRepository historyRepository;
    public  GenericResponse<List<BigDecimal>> save(@Valid List<BookRequest> requests, Authentication connectedUser) {

        try {
            List<BigDecimal> savedBookIds=requests.stream().map(request -> {
                User user=(User) connectedUser.getPrincipal();
                Book book=bookMapper.toBook(request);
                book.setOwner(user);
                Book savedBook = bookRepository.save(book);
                log.info("Saved Book ID class: " + savedBook.getId().getClass().getName());

                return savedBook.getId();
            }).toList();
            return GenericResponse.<List<BigDecimal>>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Successfully added  books")
                    ._embedded(savedBookIds)
                    .build();

        }catch (Exception e){
            e.printStackTrace();
            return GenericResponse.<List<BigDecimal>>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Failed to add a Book "+e.getCause()+ e.getMessage())
                    ._embedded(null)
                    .build();
        }

       
    }


    public GenericResponse<BookResponse> findBookById(BigDecimal id) {
            Book entity=bookRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("Book not found for the given ID:: "+id));
            BookResponse bookResponse=bookMapper.toBookResponse(entity);
            return GenericResponse.<BookResponse>builder()
                    .message("Successfully fetched the book with the given ID:: "+id)
                    .status(ResponseStatusEnum.ERROR)
                    ._embedded(bookResponse)
                    .build();

    }

    public PageResponse<BookResponse> findAllBooks(Integer pageNo, Integer pageSize, Authentication connectedUser) {
        User user=(User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(pageNo,pageSize, Sort.by("createdDate").descending());
        Page<Book> books=bookRepository.findAllDisplayableBooks(pageable,user.getId());

        List<BookResponse> bookResponses=books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return  new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(Integer pageNo, Integer pageSize, Authentication connectedUser) {
        User user=(User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(pageNo,pageSize, Sort.by("createdDate").descending());

        //use Specification to filter
        Page<Book> books=bookRepository.findAll(withOwner(user.getId()),pageable);
        List<BookResponse> bookResponses=books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return  new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );

    }

    public PageResponse<BorrowedBooksResponse> findAllBorrowedBooks(Integer pageNo, Integer pageSize, Authentication connectedUser) {
        User user=(User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(pageNo,pageSize, Sort.by("createdDate").descending());
        Page<BookTransactionHistory>  allBorrowedBooks=historyRepository.findAllBorrowedBooks(pageable,user.getId());
        List<BorrowedBooksResponse> responseList=allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBooks)
                .toList();
        return  new PageResponse<>(
                responseList,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );



    }

    public PageResponse<BorrowedBooksResponse> findAllReturnedBooks(Integer pageNo, Integer pageSize, Authentication connectedUser) {
        User user=(User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(pageNo,pageSize, Sort.by("createdDate").descending());
        Page<BookTransactionHistory>  allBorrowedBooks=historyRepository.findAllReturnedBooks(pageable,user.getId());
        List<BorrowedBooksResponse> responseList=allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBooks)
                .toList();
        return  new PageResponse<>(
                responseList,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public BigDecimal updateSharableStatus(BigDecimal bookId, Authentication connectedUser) {
       Book book=bookRepository.findById(bookId)
               .orElseThrow(()->new EntityNotFoundException("Book not found for ID:: "+ bookId));

        User user=(User) connectedUser.getPrincipal();

        if (!Objects.equals(book.getOwner().getId(),user.getId())){
            throw new  OperationNotPermittedException("You cannot update other's books Sharable status");
        }
        book.setSharable(!book.isSharable());
        bookRepository.save(book);
        return bookId;

    }

    public BigDecimal updateArchivedStatus(BigDecimal bookId, Authentication connectedUser) {
        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("Book not found for ID:: "+ bookId));

        User user=(User) connectedUser.getPrincipal();

        if (!Objects.equals(book.getOwner().getId(),user.getId())){
            throw new  OperationNotPermittedException("You cannot update  other's books Archived status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public BigDecimal borrowBook(BigDecimal bookId, Authentication connectedUser) {
        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("Book not found for ID:: "+ bookId));

        if (book.isArchived() || !book.isSharable()){
            throw new  OperationNotPermittedException("Requested book cannot be borrowed since it is archived or Not Sharable");
        }
        User user=(User) connectedUser.getPrincipal();

        if (Objects.equals(book.getOwner().getId(),user.getId())){
            throw new  OperationNotPermittedException("You cannot borrow your own book");
        }
        final  boolean isAlreadyBorrowed=historyRepository.isAlreadyBorrowed(bookId,user.getId());
        if (isAlreadyBorrowed){
            throw new  OperationNotPermittedException("The requested book is Already Borrowed");
        }
        BookTransactionHistory bookTransactionHistory=BookTransactionHistory
                .builder()
                .book(book)
                .user(user)
                .returned(false)
                .returnedApproved(false)
                .build();
        return historyRepository.save(bookTransactionHistory).getId();
    }

    public BigDecimal returnBorrowedBook(BigDecimal bookId, Authentication connectedUser) {
        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("Book not found for ID:: "+ bookId));

        if (book.isArchived() || !book.isSharable()){
            throw new  OperationNotPermittedException("Requested book cannot be returned since it is archived or Not Sharable");
        }
        User user=(User) connectedUser.getPrincipal();

        if (Objects.equals(book.getOwner().getId(),user.getId())){
            throw new  OperationNotPermittedException("You cannot borrow or return your own book");
        }
       BookTransactionHistory returnedBook=historyRepository.findBookByBookIdAndUserId(bookId,user.getId())
               .orElseThrow(()-> new OperationNotPermittedException("You did not borrow this book"));

        returnedBook.setReturned(true);
        return historyRepository.save(returnedBook).getId();


    }

    public BigDecimal ApproveReturnedBorrowedBook(BigDecimal bookId, Authentication connectedUser) {
        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("Book not found for ID:: "+ bookId));

        if (book.isArchived() || !book.isSharable()){
            throw new  OperationNotPermittedException("Requested book cannot be returned since it is archived or Not Sharable");
        }
        User user=(User) connectedUser.getPrincipal();

        if (Objects.equals(book.getOwner().getId(),user.getId())){
            throw new  OperationNotPermittedException("You cannot borrow or return your own book");
        }
        BookTransactionHistory returnedBook=historyRepository.findBookByBookIdAndOwnerId(bookId,user.getId())
                .orElseThrow(()-> new OperationNotPermittedException("You did not return this book"));

        returnedBook.setReturnedApproved(true);
        return historyRepository.save(returnedBook).getId();
    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, BigDecimal bookId) {
        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("Book not found for ID:: "+ bookId));

        var profilePicture = fileStorageService.saveFile(file, connectedUser.getName());
        book.setBookCover(profilePicture);
        bookRepository.save(book);
    }

    public GenericResponse<?> addBooks(List<BookRequestDto> requests) {

        try {
            List<Book> books = requests.stream().map(request -> {
                Book book = new Book();
                book.setTitle(request.getTitle());
                book.setSynopsis(request.getSynopsis());
                book.setIsbn(request.getIsbn());
                book.setBookCover(request.getBookCover());
                book.setAuthorName(request.getAuthorName());
                book.setArchived(request.getArchived() != null ? request.getArchived() : false);
                book.setSharable(request.getSharable() != null ? request.getSharable() : false);

                // If ownerId is provided, set the owner
                if (request.getOwnerId() != null) {
                    User owner = userRepository.findById(request.getOwnerId())
                            .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + request.getOwnerId()));
                    book.setOwner(owner);
                }

                return book;
            }).toList();


            List<Book> savedBooks = bookRepository.saveAll(books);




        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponse<>("Failed to add book", ResponseStatusEnum.ERROR , null);
        }
        return null;
    }
}
