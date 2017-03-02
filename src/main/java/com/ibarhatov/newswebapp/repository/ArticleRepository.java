package com.ibarhatov.newswebapp.repository;


import com.ibarhatov.newswebapp.model.Article;
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
@Service("articleRepository")
@Transactional
public class ArticleRepository {

    protected static Logger logger = Logger.getLogger(ArticleRepository.class);

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    public List<Article> getAll() {
        logger.debug("Retrieving all articles");

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Article");
        return query.list();
    }

    public List<Article> findAllByCategoryId(Integer categoryId) {
        logger.debug("Retrieving all articles by selected category");

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Article where category_id = :categoryId");
        query.setParameter("categoryId", categoryId);
        return query.list();
    }

    public List<Article> findAllBySearchString(String searchString) {
        logger.debug("Retrieving all articles by selected category");

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Article where title like :searchString or text like :searchString");
        query.setParameter("searchString", '%' + searchString + '%');
        return query.list();
    }

    public List<Article> findAllByCategoryIdAndSearchString(Integer categoryId,String searchString) {
        logger.debug("Retrieving all articles by selected category");

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Article where (category_id = :categoryId) and (title like :searchString or text like :searchString)");
        query.setParameter("categoryId", categoryId);
        query.setParameter("searchString", '%' + searchString + '%');
        return query.list();
    }

    public Article findById(Integer articleId) {
        logger.debug("Retrieving article");

        Session session = sessionFactory.getCurrentSession();
        Article article = (Article) session.get(Article.class, articleId);
        return article;
    }

    public void save(Article article) {
        logger.debug("Adding article");

        Session session = sessionFactory.getCurrentSession();
        session.save(article);
    }

    public void delete(Integer articleId) {
        logger.debug("Deleting existing article");

        Session session = sessionFactory.getCurrentSession();
        Article article = (Article) session.get(Article.class, articleId);
        session.delete(article);
    }

    public void saveEditionArticle(Article article) {
        logger.debug("Editing existing article");

        Session session = sessionFactory.getCurrentSession();
        Article existingArticle = (Article) session.get(Article.class, article.getId());
        existingArticle.setTitle(article.getTitle());
        existingArticle.setText(article.getText());
        existingArticle.setCategoryId(article.getCategoryId());
        session.save(existingArticle);
    }
}
