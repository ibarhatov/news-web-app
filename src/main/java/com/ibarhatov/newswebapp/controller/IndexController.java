package com.ibarhatov.newswebapp.controller;

import com.ibarhatov.newswebapp.model.Article;
import com.ibarhatov.newswebapp.repository.ArticleRepository;
import com.ibarhatov.newswebapp.repository.CategoryRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Ivan on 24.02.2017.
 */
@Controller
@RequestMapping("/")
public class IndexController {

    protected static Logger logger = Logger.getLogger(IndexController.class);

    @Resource(name = "articleRepository")
    private ArticleRepository articleRepository;

    @Resource(name = "categoryRepository")
    private CategoryRepository categoryRepository;

    //    главная страница со списком новостей
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model, RedirectAttributes redirectAttributes,
                       @RequestParam(value = "categoryId", required = false) String category,
                       @RequestParam(value = "search", required = false) String searchString) {
        logger.debug("Received request to show main page");

        if (isValide(searchString)) {
            redirectAttributes.addAttribute("search", searchString);
            redirectAttributes.addAttribute("categoryId", category);
            return "redirect:search";
        }
        String headerText;
        List<Article> articles = null;
        if (isValide(category)) {
            Integer categoryId = Integer.valueOf(category);
            articles = articleRepository.findAllByCategoryId(categoryId);
            headerText = categoryRepository.findById(categoryId).getName();
        } else {
            articles = articleRepository.getAll();
            headerText = "Новости";
        }
        for (Article article : articles) {
            String text = article.getText();
            if (text.contains("\n")) {
                int newLineIndex = text.indexOf("\n");
                text = text.substring(0, newLineIndex) + "...";
            }
            if (text.length() > 150) {
                text = text.substring(0, 150) + "...";
            }
            article.setText(text);
        }
        model.addAttribute("headerText", headerText);
        model.addAttribute("categoryId", category);
        model.addAttribute("categories", categoryRepository.getAll());
        model.addAttribute("news", articles);
        return "index";
    }

    //    страница со списком новостей
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model, RedirectAttributes redirectAttributes,
                         @RequestParam(value = "categoryId", required = false) String category,
                         @RequestParam(value = "search", required = false) String searchString) {
        logger.debug("Received request to show main page");

        model.addAttribute("categories", categoryRepository.getAll());
        if (!isValide(searchString)) {
            redirectAttributes.addAttribute("categoryId", category);
            return "redirect:";
        }
        List<Article> result;
        if (isValide(category)) {
            Integer categoryId = Integer.valueOf(category);
            result = articleRepository.findAllByCategoryIdAndSearchString(categoryId, searchString);
        } else {
            result = articleRepository.findAllBySearchString(searchString);
        }
        for (Article article : result) {
            String text = article.getText();
            if (text.contains("\n")) {
                int newLineIndex = text.indexOf("\n");
                text = text.substring(0, newLineIndex) + "...";
            }
            if (text.length() > 150) {
                text = text.substring(0, 150) + "...";
            }
            article.setText(text);
        }
        String headerText;
        if (result.isEmpty()) {
            headerText = "Найдено: 0";
        } else {
            headerText = "Найдено: " + result.size();
        }
        model.addAttribute("headerText", headerText);
        model.addAttribute("categoryId", category);
        model.addAttribute("news", result);
        return "search";
    }

    //    страницы добавления новой статьи
    @RequestMapping(value = "/addArticle")
    public String addArticle(Model model, RedirectAttributes redirectAttributes,
                             @RequestParam(value = "categoryId", required = false) String categoryId,
                             @RequestParam(value = "search", required = false) String searchString,
                             @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "text", required = false) String text,
                             @RequestParam(value = "category", required = false) String category) {
        logger.debug("Received request to show save article page");

        if (isValide(searchString)) {
            redirectAttributes.addAttribute("search", searchString);
            redirectAttributes.addAttribute("categoryId", categoryId);
            return "redirect:search";
        }

        model.addAttribute("categories", categoryRepository.getAll());
        if (isValide(title)) {
            Article article = new Article();
            article.setTitle(title);
            article.setText(text);
            article.setDate(new Date());
            article.setCategoryId(Integer.valueOf(category));
            articleRepository.save(article);
            return "redirect:addArticleResult";
        }
        return "addArticle";
    }

    @RequestMapping("/addArticleResult")
    public String articleWasAdded(Model model, RedirectAttributes redirectAttributes,
                                  @RequestParam(value = "search", required = false) String searchString) {
        if (isValide(searchString)) {
            redirectAttributes.addAttribute("search", searchString);
            return "redirect:search";
        }
        model.addAttribute("categories", categoryRepository.getAll());
        return "/addArticleResult";
    }

    //    переход на страницу редактирования статьи
    @RequestMapping(value = "/editArticle", method = RequestMethod.GET)
    public String editArticle(Model model, @RequestParam(value = "id") String articleId) {
        logger.debug("Received request to show save article page");

        model.addAttribute("categories", categoryRepository.getAll());
        Integer id = Integer.valueOf(articleId);
        Article article = articleRepository.findById(id);
        model.addAttribute("article", article);
        model.addAttribute("category", categoryRepository.getAll());
        return "/editArticle";
    }

    //    редактирования статьи
    @RequestMapping(value = "/editArticle", method = RequestMethod.POST)
    public String saveEditionArticle(RedirectAttributes redirectAttributes,
                                     @RequestParam(value = "articleId") String articleId,
                                     @RequestParam(value = "title") String title,
                                     @RequestParam(value = "text") String text,
                                     @RequestParam(value = "category") String category) {
        logger.debug("Received request to show save article page");

        Article article = new Article();
        article.setId(Integer.valueOf(articleId));
        article.setTitle(title);
        article.setText(text);
        article.setDate(new Date());
        article.setCategoryId(Integer.valueOf(category));
        articleRepository.saveEditionArticle(article);
        redirectAttributes.addAttribute("articleId", article.getId());
        return "redirect:article";
    }

    //    страница с полным описанием статьи
    @RequestMapping(value = "/article", method = RequestMethod.GET)
    public String getArticle(Model model, RedirectAttributes redirectAttributes,
                             @RequestParam(value = "articleId", required = false) String articleId,
                             @RequestParam(value = "search", required = false) String searchString) {
        logger.debug("Received request to show full article page");

        if (isValide(searchString)) {
            redirectAttributes.addAttribute("search", searchString);
            return "redirect:search";
        }
        Integer id = Integer.valueOf(articleId);
        Article article = articleRepository.findById(id);
        model.addAttribute("article", article);
        Integer categoryId = article.getCategoryId();
        model.addAttribute("category", categoryRepository.findById(categoryId));
        model.addAttribute("categories", categoryRepository.getAll());
        return "article";
    }

    //    удаление статьи
    @RequestMapping(value = "/article", method = RequestMethod.POST)
    public String getArticle(Model model, RedirectAttributes redirectAttributes,
                             @RequestParam(value = "removeArticle") String removableArticleId) {
        logger.debug("Received request to delete article");

        if (isValide(removableArticleId)) {
            articleRepository.delete(Integer.valueOf(removableArticleId));
        }
        return "redirect:";
    }

    //    проверка параметра на валидность
    private boolean isValide(String parameter) {
        return (parameter != null) && !parameter.isEmpty() ? true : false;
    }
}
