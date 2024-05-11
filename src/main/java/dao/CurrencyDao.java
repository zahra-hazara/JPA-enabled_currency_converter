package dao;

import entity.Currency;

import javax.persistence.EntityManager;


public class CurrencyDao {
        private EntityManager entityManager;

        public CurrencyDao(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

    public CurrencyDao() {

    }

    public Currency findCurrencyByAbbreviation(String abbreviation) {
            return entityManager.find(Currency.class, abbreviation);
        }

        public void saveCurrency(Currency currency) {
            entityManager.getTransaction().begin();
            entityManager.persist(currency);
            entityManager.getTransaction().commit();
        }
    }