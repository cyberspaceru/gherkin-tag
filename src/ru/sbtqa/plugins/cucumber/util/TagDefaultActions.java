package ru.sbtqa.plugins.cucumber.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by SBT-Tatciy-IO on 01.08.2017.
 */
public class TagDefaultActions {
    private static Map<String, Map<String, String>> languageMap;

    public static final String FILL_FIELD = "ru.sbtqa.tag.pagefactory.fill.field";
    public static final String CLICK_LINK = "ru.sbtqa.tag.pagefactory.click.link";
    public static final String CLICK_BUTTON = "ru.sbtqa.tag.pagefactory.click.button";
    public static final String PRESS_KEY = "ru.sbtqa.tag.pagefactory.press.key";
    public static final String SELECT_CHECKBOX = "ru.sbtqa.tag.pagefactory.select.checkBox";
    public static final String SELECT = "ru.sbtqa.tag.pagefactory.select";
    public static final String ACCEPT_ALERT = "ru.sbtqa.tag.pagefactory.accept.alert";
    public static final String DISMISS_ALERT = "ru.sbtqa.tag.pagefactory.dismiss.alert";
    public static final String TEXT_APPEARS_ON_PAGE = "ru.sbtqa.tag.pagefactory.text.appears.on.page";
    public static final String TEXT_ABSENT_ON_PAGE = "ru.sbtqa.tag.pagefactory.text.absent.on.page";
    public static final String MODAL_WINDOW_WITH_TEXT_APPEARS = "ru.sbtqa.tag.pagefactory.modal.window.with.text.appears";
    public static final String CHECK_VALUE = "ru.sbtqa.tag.pagefactory.check.value";
    public static final String CHECK_FIELD_NOT_EMPTY = "ru.sbtqa.tag.pagefactory.check.field.not.empty";
    public static final String CHECK_VALUES_NOT_EQUAL = "ru.sbtqa.tag.pagefactory.check.values.not.equal";
    public static final String CHECK_ELEMENT_WITH_TEXT_PRESENT = "ru.sbtqa.tag.pagefactory.check.element.with.text.present";
    public static final String CHECK_TEXT_VISIBLE = "ru.sbtqa.tag.pagefactory.check.text.visible";

    static {
        languageMap = new HashMap<>();
        Map<String, String> en = new HashMap<>();
        Map<String, String> ru = new HashMap<>();
        en.put(FILL_FIELD, "fill the field");
        en.put(CLICK_LINK, "click the link");
        en.put(CLICK_BUTTON, "click the button");
        en.put(PRESS_KEY, "press the key");
        en.put(SELECT_CHECKBOX, "select CheckBox");
        en.put(SELECT, "select");
        en.put(ACCEPT_ALERT, "accepts alert");
        en.put(DISMISS_ALERT, "dismisses alert");
        en.put(TEXT_APPEARS_ON_PAGE, "text appears on the page");
        en.put(TEXT_ABSENT_ON_PAGE, "text is absent on the page");
        en.put(MODAL_WINDOW_WITH_TEXT_APPEARS, "modal window with text appears");
        en.put(CHECK_VALUE, "checks value");
        en.put(CHECK_FIELD_NOT_EMPTY, "checks that the field is not empty");
        en.put(CHECK_VALUES_NOT_EQUAL, "check that values are not equal");
        en.put(CHECK_ELEMENT_WITH_TEXT_PRESENT, "check that element with text is present");
        en.put(CHECK_TEXT_VISIBLE, "check the text is visible");

        ru.put(FILL_FIELD, "заполняет поле");
        ru.put(CLICK_LINK, "кликает по ссылке");
        ru.put(CLICK_BUTTON, "нажимает кнопку");
        ru.put(PRESS_KEY, "нажимает клавишу");
        ru.put(SELECT_CHECKBOX, "отмечает признак");
        ru.put(SELECT, "выбирает");
        ru.put(ACCEPT_ALERT, "принимает уведомление");
        ru.put(DISMISS_ALERT, "отклоняет уведомление");
        ru.put(TEXT_APPEARS_ON_PAGE, "текст появляется на странице");
        ru.put(TEXT_ABSENT_ON_PAGE, "текст отсутствует на странице");
        ru.put(MODAL_WINDOW_WITH_TEXT_APPEARS, "появляется модальное окно с текстом");
        ru.put(CHECK_VALUE, "проверяет значение");
        ru.put(CHECK_FIELD_NOT_EMPTY, "проверяет что поле непустое");
        ru.put(CHECK_VALUES_NOT_EQUAL, "проверяет несовпадение значения");
        ru.put(CHECK_ELEMENT_WITH_TEXT_PRESENT, "существует элемент с текстом");
        ru.put(CHECK_TEXT_VISIBLE, "отображается текст");

        languageMap.put("en", en);
        languageMap.put("ru", ru);
    }

    public static boolean isDefaultAction(String action, String language) {
        return languageMap.get(language).entrySet().stream()
                .filter(x -> action.equals(x.getKey()))
                .findFirst()
                .orElse(null) != null;
    }

    @Nullable
    public static String find(String language, String internationalKey) {
        return Optional.of(getParamsByInternationalKey(language::equals, internationalKey))
                .filter(x -> x.size() >= 1)
                .map(x -> x.get(0))
                .orElse(null);
    }

    @NotNull
    public static List<String> getParamsByInternationalKey(Predicate<String> languagePredicate, String internationalKey) {
        List<String> result = new ArrayList<>();
        languageMap.entrySet().stream()
                .filter(map -> languagePredicate.test(map.getKey()))
                .map(Map.Entry::getValue)
                .map(x -> x.get(internationalKey))
                .filter(Objects::nonNull)
                .forEach(result::add);
        return result;
    }
}
