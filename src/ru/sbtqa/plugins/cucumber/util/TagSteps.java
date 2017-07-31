package ru.sbtqa.plugins.cucumber.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by Kasyanenko Konstantin
 * 27.06.2017
 */
public class TagSteps {

    public static final String USER_ACTION_IN_BLOCK_NO_PARAMS = "ru.sbtqa.tag.pagefactory.userActionInBlockNoParams";
    public static final String USER_ACTION_IN_BLOCK_TABLE_PARAM = "ru.sbtqa.tag.pagefactory.userActionInBlockTableParam";
    public static final String USER_ACTION_IN_BLOCK_ONE_PARAM= "ru.sbtqa.tag.pagefactory.userActionInBlockOneParam";
    public static final String USER_ACTION_IN_BLOCK_TWO_PARAMS = "ru.sbtqa.tag.pagefactory.userActionInBlockTwoParams";
    public static final String FIND_ELEMENTS_IN_BLOCK = "ru.sbtqa.tag.pagefactory.findElementInBlock";
    public static final String FIND_ELEMENTS_IN_LIST = "ru.sbtqa.tag.pagefactory.findElementInList";
    public static final String OPEN_PAGE = "ru.sbtqa.tag.pagefactory.openPage";
    public static final String USER_ACTION_NO_PARAMS = "ru.sbtqa.tag.pagefactory.userActionNoParams";
    public static final String USER_ACTION_ONE_PARAM = "ru.sbtqa.tag.pagefactory.userActionOneParam";
    public static final String USER_ACTION_TWO_PARAMS = "ru.sbtqa.tag.pagefactory.userActionTwoParams";
    public static final String USER_ACTION_THREE_PARAMS = "ru.sbtqa.tag.pagefactory.userActionThreeParams";
    public static final String USER_ACTION_TABLE_PARAM = "ru.sbtqa.tag.pagefactory.userActionTableParam";
    public static final String USER_DO_ACTION_WITH_OBJECT = "ru.sbtqa.tag.pagefactory.userDoActionWithObject";
    public static final String USER_ACTION_LIST_PARAM = "ru.sbtqa.tag.pagefactory.userActionListParam";
    public static final String OPEN_COPY_PAGE = "ru.sbtqa.tag.pagefactory.openCopyPage";
    public static final String SWITCHES_TO_NEXT_TAB = "ru.sbtqa.tag.pagefactory.switchesToNextTab";
    public static final String URL_MATCHES = "ru.sbtqa.tag.pagefactory.urlMatches";
    public static final String CLOSING_CURRENT_WIN = "ru.sbtqa.tag.pagefactory.closingCurrentWin";
    public static final String BACK_PAGE = "ru.sbtqa.tag.pagefactory.backPage";
    public static final String GO_TO_PAGE_BY_URL = "ru.sbtqa.tag.pagefactory.goToPageByUrl";
    public static final String GO_TO_URL = "ru.sbtqa.tag.pagefactory.goToUrl";
    public static final String RE_INIT_PAGE = "ru.sbtqa.tag.pagefactory.reInitPage";
    public static final String SWIPE_TO_TEXT = "ru.sbtqa.tag.pagefactory.swipeToText";

    private static Map<String, Map<String, String>> languageMap;

    public static final List<String> ACTIONS = new ArrayList<>();

    static {
        languageMap = new HashMap<>();
        Map<String, String> en = new HashMap<>();
        Map<String, String> ru = new HashMap<>();
        en.put(USER_ACTION_IN_BLOCK_NO_PARAMS, "^user in block \\\"([^\\\"]*)\\\" \\((.*)\\)$");
        en.put(USER_ACTION_IN_BLOCK_TABLE_PARAM, "^user in block \\\"([^\\\"]*)\\\" \\((.*)\\) with the parameters of table$");
        en.put(USER_ACTION_IN_BLOCK_ONE_PARAM, "^user in block \\\"([^\\\"]*)\\\" \\((.*)\\) with a parameter \\\"([^\\\"]*)\\\"$");
        en.put(USER_ACTION_IN_BLOCK_TWO_PARAMS, "^user in block \\\"([^\\\"]*)\\\" \\((.*)\\) with the parameters \\\"([^\\\"]*)\\\"  \\\"([^\\\"]*)\\\"$");
        en.put(FIND_ELEMENTS_IN_BLOCK, "^user in block \\\"([^\\\"]*)\\\" finds (?:element|textinput|checkbox|radiobutton|table|header|button|link|image) \\\"([^\\\"]*)\\\"$");
        en.put(FIND_ELEMENTS_IN_LIST, "^user in list \\\"([^\\\"]*)\\\" finds the value element \\\"([^\\\"]*)\\\"$");
        en.put(OPEN_PAGE, "^(?:user |he |)(?:is on the page|page is being opened|master tab is being opened) \\\"(.*)\\\"$");
        en.put(USER_ACTION_NO_PARAMS, "^user \\((.*)\\)$");
        en.put(USER_ACTION_ONE_PARAM, "^user \\((.*)\\) (?:with param |)\\\"([^\\\"]*)\\\"$");
        en.put(USER_ACTION_TWO_PARAMS, "^user \\((.*)\\) (?:with the parameters |)\\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\"$");
        en.put(USER_ACTION_THREE_PARAMS, "^user \\((.*)\\) (?:with the parameters |)\\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\"$");
        en.put(USER_ACTION_TABLE_PARAM, "^user \\((.*)\\) data$");
        en.put(USER_DO_ACTION_WITH_OBJECT, "^user \\((.*)\\) \\\"([^\\\"]*) data$");
        en.put(USER_ACTION_LIST_PARAM, "^user \\((.*)\\) from the list$");
        en.put(OPEN_COPY_PAGE, "^copy of the page is being opened in a new tab$");
        en.put(SWITCHES_TO_NEXT_TAB, "^user switches to the next tab$");
        en.put(URL_MATCHES, "^URL matches \\\"(.*)\\\"$");
        en.put(CLOSING_CURRENT_WIN, "^user closes the current window and returns to \\\"(.*)\\\"$");
        en.put(BACK_PAGE, "^user push back in the browser$");
        en.put(GO_TO_PAGE_BY_URL, "^user navi gates to page \\\"(.*)\\\"$");
        en.put(GO_TO_URL, "^user navigates to url \\\"(.*)\\\"$");
        en.put(RE_INIT_PAGE, "^user refreshes the page$");
        en.put(SWIPE_TO_TEXT, "^user swipes \"(.*)\" to text \"(.*)\"$");

        ru.put(USER_ACTION_IN_BLOCK_NO_PARAMS, "^(?:пользователь |он | )в блоке \\\"([^\\\"]*)\\\" \\((.*)\\)$");
        ru.put(USER_ACTION_IN_BLOCK_TABLE_PARAM, "^(?:пользователь |он |)в блоке \\\"([^\\\"]*)\\\" \\((.*)\\) с параметрами из таблицы$");
        ru.put(USER_ACTION_IN_BLOCK_ONE_PARAM, "^(?:пользователь |он |)в блоке \\\"([^\\\"]*)\\\" \\((.*)\\) с параметром \\\"([^\\\"]*)\\\"$");
        ru.put(USER_ACTION_IN_BLOCK_TWO_PARAMS, "^(?:пользователь |он |)в блоке \\\"([^\\\"]*)\\\" \\((.*)\\) с параметрами \\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\"$");
        ru.put(FIND_ELEMENTS_IN_BLOCK, "^(?:пользователь |он |)в блоке \\\"([^\\\"]*)\\\" находит (?:элемент|текстовое поле|чекбокс|радиобатон|таблицу|заголовок|кнопку|ссылку|изображение) \\\"([^\\\"]*)\\\"$");
        ru.put(FIND_ELEMENTS_IN_LIST, "^(?:пользователь |он |)в списке \\\"([^\\\"]*)\\\" находит элемент со значением \\\"([^\\\"]*)\\\"$");
        ru.put(OPEN_PAGE, "^(?:пользователь |он |)(?:находится на странице|открывается страница|открывается вкладка мастера) \\\"(.*)\\\"$");
        ru.put(USER_ACTION_NO_PARAMS, "^(?:пользователь |он |)\\((.*)\\)$");
        ru.put(USER_ACTION_ONE_PARAM, "^(?:пользователь |он |)\\((.*)\\) (?:с параметром |)\\\"([^\\\"]*)\\\"$");
        ru.put(USER_ACTION_TWO_PARAMS, "^(?:пользователь |он |)\\((.*)\\) (?:с параметрами |)\\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\"$");
        ru.put(USER_ACTION_THREE_PARAMS, "^(?:пользователь |он |)\\((.*)\\) (?:с параметрами |)\\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\" \\\"([^\\\"]*)\\\"$");
        ru.put(USER_ACTION_TABLE_PARAM, "^(?:пользователь |он |)\\((.*)\\) данными$");
        ru.put(USER_DO_ACTION_WITH_OBJECT, "^(?:пользователь |он |)\\((.*)\\) \\\"([^\\\"]*)\\\" данными $");
        ru.put(USER_ACTION_LIST_PARAM, "^(?:пользователь |он |)\\((.*)\\) из списка$");
        ru.put(OPEN_COPY_PAGE, "^открывается копия страницы в новой вкладке$");
        ru.put(SWITCHES_TO_NEXT_TAB, "^(?:пользователь |он |)переключается на соседнюю вкладку$");
        ru.put(URL_MATCHES, "^URL соответствует \\\"(.*)\\\"$");
        ru.put(CLOSING_CURRENT_WIN, "^(?:пользователь |он |)закрывает текущее окно и возвращается на \\\"(.*)\\\"$");
        ru.put(BACK_PAGE, "^(?:пользователь |он |)нажимает назад в браузере$");
        ru.put(GO_TO_PAGE_BY_URL, "^(?:пользователь |он |)переходит на страницу \\\"(.*)\\\" по ссылке$");
        ru.put(GO_TO_URL, "^(?:пользователь |он |)(?:переходит на|открывает) url \"(.*)\"$");
        ru.put(RE_INIT_PAGE, "^обновляем страницу$");
        ru.put(SWIPE_TO_TEXT, "^пользователь свайпает экран \\\"([^\\\"]*)\\\" до текста \\\"([^\\\"]*)\\\"$");
        languageMap.put("en", en);
        languageMap.put("ru", ru);

        ACTIONS.addAll(Arrays.asList(USER_ACTION_NO_PARAMS,
                USER_ACTION_ONE_PARAM,
                USER_ACTION_TWO_PARAMS,
                USER_ACTION_THREE_PARAMS,
                USER_ACTION_TABLE_PARAM,
                USER_DO_ACTION_WITH_OBJECT,
                USER_ACTION_LIST_PARAM));
    }

    private TagSteps() {}

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
