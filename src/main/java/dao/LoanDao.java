package dao;

import entity.Loan;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;
import java.util.List;

public class LoanDao {

    public void save(Loan loan) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(loan);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void update(Loan loan) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(loan);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<Loan> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Loan", Loan.class).list();
        }
    }

    public Loan getActiveLoanByBookId(Long bookId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                    "from Loan where book.id = :bookId and returnDate is null", Loan.class);
            query.setParameter("bookId", bookId);
            return query.uniqueResult();
        }
    }
}