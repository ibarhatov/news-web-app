package com.ibarhatov.newswebapp.repository;

import com.ibarhatov.newswebapp.model.Category;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ivan on 25.02.2017.
 */
@Service("categoryRepository")
@Transactional
public class CategoryRepository {

    protected static Logger logger = Logger.getLogger(CategoryRepository.class);

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    public List<Category> getAll() {
        logger.debug("Retrieving all categories");

        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("FROM Category");

        return query.list();
    }

    public Category findById(Integer categoryId) {
        logger.debug("Retrieving article");

        Session session = sessionFactory.getCurrentSession();
        Category category = (Category) session.get(Category.class, categoryId);
        return category;
    }
}
