package ru.sbtqa.plugins.cucumber.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

/**
 * Created by Kasyanenko Konstantin
 * 27.06.2017
 */
public class Features {
    private static Map<String, Map<String, String>> languageMap;

    static {
        languageMap = new HashMap<>();
        Map<String, String> en = new HashMap<>();
        Map<String, String> ru = new HashMap<>();
        en.put("ru.sbtqa.tag.pagefactory.userActionInBlockNoParams", "^user in block \\\"([^\\\"]*)\\\" \\((.*)\\)$");
        en.put("ru.sbtqa.tag.pagefactory.userActionInBlockTableParam", "^user in block \\\"([^\\\"]*)\\\" \\((.*)\\) with the parameters of table$");
        en.put("ru.sbtqa.tag.pagefactory.userActionInBlockOneParam", "^user in block \\\"([^\\\"]*)\\\" \\((.*)\\) with a parameter \\\"([^\\\"]*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.userActionInBlockTwoParams", "^user in block \\\"([^\\\"]*)\\\" \\((.*)\\) with the parameters \\\"([^\\\"]*)\\\"  \\\"([^\\\"]*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.findElementInBlock", "^user in block \\\"([^\\\"]*)\\\" finds (?:element|textinput|checkbox|radiobutton|table|header|button|link|image) \\\"([^\\\"]*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.findElementInList", "^user in list \\\"([^\\\"]*)\\\" finds the value element \\\"([^\\\"]*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.openPage", "^(?:user |he |)(?:is on the page|page is being opened|master tab is being opened) \\\"(.*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.userActionNoParams", "^user \\((.*)\\)$");
        en.put("ru.sbtqa.tag.pagefactory.userActionOneParam", "^user \\((.*)\\) (?:with param |)\\\"([^\\\"]*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.userActionTwoParams", "^user \\((.*)\\) (?:with the parameters |)\\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.userActionThreeParams", "^user \\((.*)\\) (?:with the parameters |)\\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.userActionTableParam", "^user \\((.*)\\) data$");
        en.put("ru.sbtqa.tag.pagefactory.userDoActionWithObject", "^user \\((.*)\\) \\\"([^\\\"]*) data$");
        en.put("ru.sbtqa.tag.pagefactory.userActionListParam", "^user \\((.*)\\) from the list$");
        en.put("ru.sbtqa.tag.pagefactory.openCopyPage", "^copy of the page is being opened in a new tab$");
        en.put("ru.sbtqa.tag.pagefactory.switchesToNextTab", "^user switches to the next tab$");
        en.put("ru.sbtqa.tag.pagefactory.urlMatches", "^URL matches \\\"(.*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.closingCurrentWin", "^user closes the current window and returns to \\\"(.*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.backPage", "^user push back in the browser$");
        en.put("ru.sbtqa.tag.pagefactory.goToPageByUrl", "^user navi gates to page \\\"(.*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.goToUrl", "^user navigates to url \\\"(.*)\\\"$");
        en.put("ru.sbtqa.tag.pagefactory.reInitPage", "^user refreshes the page$");
        en.put("ru.sbtqa.tag.pagefactory.swipeToText", "^user swipes \"(.*)\" to text \"(.*)\"$");

        ru.put("ru.sbtqa.tag.pagefactory.userActionInBlockNoParams", "^(?:пользователь |он | )в блоке \\\"([^\\\"]*)\\\" \\((.*)\\)$");
        ru.put("ru.sbtqa.tag.pagefactory.userActionInBlockTableParam", "^(?:пользователь |он |)в блоке \\\"([^\\\"]*)\\\" \\((.*)\\) с параметрами из таблицы$");
        ru.put("ru.sbtqa.tag.pagefactory.userActionInBlockOneParam", "^(?:пользователь |он |)в блоке \\\"([^\\\"]*)\\\" \\((.*)\\) с параметром \\\"([^\\\"]*)\\\"$");
        ru.put("ru.sbtqa.tag.pagefactory.userActionInBlockTwoParams", "^(?:пользователь |он |)в блоке \\\"([^\\\"]*)\\\" \\((.*)\\) с параметрами \\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\"$");
        ru.put("ru.sbtqa.tag.pagefactory.findElementInBlock", "^(?:пользователь |он |)в блоке \\\"([^\\\"]*)\\\" находит (?:элемент|текстовое поле|чекбокс|радиобатон|таблицу|заголовок|кнопку|ссылку|изображение) \\\"([^\\\"]*)\\\"$");
        ru.put("ru.sbtqa.tag.pagefactory.findElementInList", "^(?:пользователь |он |)в списке \\\"([^\\\"]*)\\\" находит элемент со значением \\\"([^\\\"]*)\\\"$");
        ru.put("ru.sbtqa.tag.pagefactory.openPage", "^(?:пользователь |он |)(?:находится на странице|открывается страница|открывается вкладка мастера) \\\"(.*)\\\"$");
        ru.put("ru.sbtqa.tag.pagefactory.userActionNoParams", "^(?:пользователь |он |)\\((.*)\\)$");
        ru.put("ru.sbtqa.tag.pagefactory.userActionOneParam", "^(?:пользователь |он |)\\((.*)\\) (?:с параметром |)\\\"([^\\\"]*)\\\"$");
        ru.put("ru.sbtqa.tag.pagefactory.userActionTwoParams", "^(?:пользователь |он |)\\((.*)\\) (?:с параметрами |)\\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\"$");
        ru.put("ru.sbtqa.tag.pagefactory.userActionThreeParams", "^(?:пользователь |он |)\\((.*)\\) (?:с параметрами |)\\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\"$");
        ru.put("ru.sbtqa.tag.pagefactory.userActionTableParam", "^(?:пользователь |он |)\\((.*)\\) данными$");
        ru.put("ru.sbtqa.tag.pagefactory.userDoActionWithObject", "^(?:пользователь |он |)\\((.*)\\) \\\"([^\\\"]*)\\\" данными $");
        ru.put("ru.sbtqa.tag.pagefactory.userActionListParam", "^(?:пользователь |он |)\\((.*)\\) из списка$");
        ru.put("ru.sbtqa.tag.pagefactory.openCopyPage", "^открывается копия страницы в новой вкладке$");
        ru.put("ru.sbtqa.tag.pagefactory.switchesToNextTab", "^(?:пользователь |он |)переключается на соседнюю вкладку$");
        ru.put("ru.sbtqa.tag.pagefactory.urlMatches", "^URL соответствует \\\"(.*)\\\"$");
        ru.put("ru.sbtqa.tag.pagefactory.closingCurrentWin", "^(?:пользователь |он |)закрывает текущее окно и возвращается на \\\"(.*)\\\"$");
        ru.put("ru.sbtqa.tag.pagefactory.backPage", "^(?:пользователь |он |)нажимает назад в браузере$");
        ru.put("ru.sbtqa.tag.pagefactory.goToPageByUrl", "^(?:пользователь |он |)переходит на страницу \\\"(.*)\\\" по ссылке$");
        ru.put("ru.sbtqa.tag.pagefactory.goToUrl", "^(?:пользователь |он |)(?:переходит на|открывает) url \"(.*)\"$");
        ru.put("ru.sbtqa.tag.pagefactory.reInitPage", "^обновляем страницу$");
        ru.put("ru.sbtqa.tag.pagefactory.swipeToText", "^пользователь свайпает экран \\\"([^\\\"]*)\\\" до текста \\\"([^\\\"]*)\\\"$");
        languageMap.put("en", en);
        languageMap.put("ru", ru);
    }

    @Nullable
    public static String find(String language, String simpleName) {
        return Optional.of(getParam(language::equals, simpleName))
                .filter(x -> x.size() >= 1)
                .map(x -> x.get(0).replaceAll("\\\\\\\\", "\\\\"))
                .orElse(null);
    }

    /**
     * Возвращает новое значнение или если нет в мапе то то что ему пришло
     *
     * @param oldName
     * @return
     */
    @NotNull
    public static List<String> getParam(Predicate<String> languagePredicate, String oldName) {
        List<String> result = new ArrayList<>();
        languageMap.entrySet().stream()
                .filter(map -> languagePredicate.test(map.getKey()))
                .map(Map.Entry::getValue)
                .map(x -> x.get(oldName))
                .filter(Objects::nonNull)
                .forEach(result::add);
        return result;
    }
}
