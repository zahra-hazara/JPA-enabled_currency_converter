package dao;

import entity.Currency;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CurrencyDAO {
    private EntityManagerFactory emf;
    private EntityManager em;

    public CurrencyDAO() {
        emf = Persistence.createEntityManagerFactory("currencyPU");
        em = emf.createEntityManager();
    }

    public List<Currency> getAllCurrencies() {
        TypedQuery<Currency> query = em.createQuery("SELECT c FROM Currency c", Currency.class);
        return query.getResultList();
    }

    public void addCurrency(Currency currency) {
        em.getTransaction().begin();
        em.persist(currency);
        em.getTransaction().commit();
    }

    public void close() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
}