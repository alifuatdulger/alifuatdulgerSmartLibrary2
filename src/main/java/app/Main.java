package app;

import dao.BookDao;
import dao.LoanDao;
import dao.StudentDao;
import entity.Book;
import entity.BookStatus;
import entity.Loan;
import entity.Student;
import util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BookDao bookDao = new BookDao();
    private static final StudentDao studentDao = new StudentDao();
    private static final LoanDao loanDao = new LoanDao();

    public static void main(String[] args) {

        Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        while (true) {
            System.out.println("   SMART LIBRARY PLUS MENÜ     \n");
            System.out.println("1 - Kitap Ekle");
            System.out.println("2 - Kitapları Listele");
            System.out.println("3 - Öğrenci Ekle");
            System.out.println("4 - Öğrencileri Listele");
            System.out.println("5 - Kitap Ödünç Ver");
            System.out.println("6 - Ödünç Listesini Görüntüle");
            System.out.println("7 - Kitap Geri Teslim Al");
            System.out.println("0 - Çıkış");
            System.out.print("Seçiminiz: ");

            String choiceStr = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (Exception e) {
                System.out.println("Lütfen sadece sayı giriniz!");
                continue;
            }

            switch (choice) {
                case 1 -> addBook();
                case 2 -> listBooks();
                case 3 -> addStudent();
                case 4 -> listStudents();
                case 5 -> borrowBook();
                case 6 -> listLoans();
                case 7 -> returnBook();
                case 0 -> {
                    System.out.println("Sistem kapatılıyor. İyi günler!");
                    HibernateUtil.getSessionFactory().close();
                    System.exit(0);
                }
                default -> System.out.println("Geçersiz seçenek!");
            }
        }
    }

    private static void addBook() {
        System.out.print("Kitap Başlığı: ");
        String title = scanner.nextLine();
        System.out.print("Yazar: ");
        String author = scanner.nextLine();
        System.out.print("Basım Yılı: ");
        int year = Integer.parseInt(scanner.nextLine());

        Book book = new Book(title, author, year);
        bookDao.save(book);
        System.out.println("Kitap başarıyla eklendi.");
    }

    private static void listBooks() {
        List<Book> books = bookDao.getAll();
        if (books.isEmpty()) {
            System.out.println("Kayıtlı kitap yok.");
        } else {
            System.out.println("\n--- KİTAPLAR ---");
            books.forEach(System.out::println);
        }
    }

    private static void addStudent() {
        System.out.print("Öğrenci Adı: ");
        String name = scanner.nextLine();
        System.out.print("Bölüm: ");
        String dept = scanner.nextLine();

        Student student = new Student(name, dept);
        studentDao.save(student);
        System.out.println(" Öğrenci kaydedildi.");
    }

    private static void listStudents() {
        List<Student> students = studentDao.getAll();
        if (students.isEmpty()) {
            System.out.println(" Kayıtlı öğrenci yok.");
        } else {
            System.out.println("\n--- ÖĞRENCİLER ---");
            students.forEach(System.out::println);
        }
    }

    private static void borrowBook() {
        System.out.print("Öğrenci ID: ");
        Long studId = Long.parseLong(scanner.nextLine());
        System.out.print("Kitap ID: ");
        Long bookId = Long.parseLong(scanner.nextLine());

        Book book = bookDao.getById(bookId);
        Student student = studentDao.getById(studId);

        if (book != null && student != null) {
            if (book.getStatus() == BookStatus.BORROWED) {
                System.out.println("HATA: Bu kitap zaten bir başkasında!");
            } else {
                book.setStatus(BookStatus.BORROWED);
                bookDao.update(book);

                Loan loan = new Loan(student, book, LocalDate.now());
                loanDao.save(loan);
                System.out.println("Kitap " + student.getName() + " isimli öğrenciye verildi.");
            }
        } else {
            System.out.println("HATA: Geçersiz ID!");
        }
    }

    private static void listLoans() {
        List<Loan> loans = loanDao.getAll();
        if (loans.isEmpty()) {
            System.out.println(">> Ödünç kaydı yok.");
        } else {
            System.out.println("\n--- ÖDÜNÇ LİSTESİ ---");
            for (Loan l : loans) {
                String returnDate = (l.getReturnDate() == null) ? "HENÜZ GELMEDİ" : l.getReturnDate().toString();
                System.out.println("Öğrenci: " + l.getStudent().getName() +
                        " | Kitap: " + l.getBook().getTitle() +
                        " | Tarih: " + l.getBorrowDate() +
                        " | İade: " + returnDate);
            }
        }
    }

    private static void returnBook() {
        System.out.print("İade edilecek Kitap ID: ");
        Long bookId = Long.parseLong(scanner.nextLine());

        Loan loan = loanDao.getActiveLoanByBookId(bookId);

        if (loan != null) {
            loan.setReturnDate(LocalDate.now());
            loanDao.update(loan);

            Book book = loan.getBook();
            book.setStatus(BookStatus.AVAILABLE);
            bookDao.update(book);

            System.out.println(">> Kitap teslim alındı, kütüphaneye geri döndü.");
        } else {
            System.out.println(">> HATA: Bu kitap için aktif bir ödünç kaydı bulunamadı.");
        }
    }
}