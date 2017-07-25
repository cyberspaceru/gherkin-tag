package ru.sbtqa.plugins.cucumber.util;

import com.intellij.codeInsight.completion.AllClassesGetter;
import com.intellij.codeInsight.completion.PlainPrefixMatcher;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import kotlin.Pair;
import org.jetbrains.plugins.cucumber.referencesearch.CucumberJavaUtil;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by cyberspace on 7/19/2017.
 */
public class TAGProject {

    private static final String ACTION_TITLE_ANNOTATION_QUALIFIED_NAME = "ru.sbtqa.tag.pagefactory.annotations.ActionTitle";
    private static final String PAGE_ENTRY_ANNOTATION_QUALIFIED_NAME = "ru.sbtqa.tag.pagefactory.annotations.PageEntry";
    private static final String TAG_PAGE_QUALIFIED_NAME = "ru.sbtqa.tag.pagefactory.Page";

    /**
     * Найти все аннотации ActionTitle класса наследуемого от ru.sbtqa.tag.pagefactory.Page.
     *
     * @param page
     * @return Возвращает поток пар значений (Аннотация; Количество параметров в сигнатуре метода c данной аннотацией).
     */
    public static Stream<Pair<PsiAnnotation, Integer>> actionTitle(PsiClass page) {
        List<Pair<PsiAnnotation, PsiMethod>> allAnnotations = new ArrayList<>();
        Arrays.stream(page.getAllMethods())
                .filter(x -> x.getContainingClass() != null &&
                        x.getContainingClass().getQualifiedName() != null &&
                        !TAG_PAGE_QUALIFIED_NAME.equals(x.getContainingClass().getQualifiedName())) // игнорируем экшины из родителя
                .forEach(x -> Arrays.stream(x.getModifierList().getAnnotations())
                            .forEach(y -> allAnnotations.add(new Pair<>(y, x))));

        return allAnnotations.stream()
                .filter(x -> x.getFirst() != null && ACTION_TITLE_ANNOTATION_QUALIFIED_NAME.equals(x.getFirst().getQualifiedName()))
                .map(x -> new Pair<>(x.getFirst(), x.getSecond().getParameterList().getParameters().length))
                .unordered();
    }

    /**
     * Найти значаение title() для аннотации PageEntry для класса наследованного от ru.sbtqa.tag.pagefactory.Page.
     *
     * @param page
     * @param project
     * @return
     */
    public static String findPageName(PsiClass page, Project project) {
        Stream<PsiAnnotation> allAnnotations = Optional.ofNullable(page)
                .filter(x -> isTAGPage(x) && x.getModifierList() != null)
                .map(PsiModifierListOwner::getModifierList)
                .map(PsiAnnotationOwner::getAnnotations)
                .map(Arrays::stream)
                .orElse(null);
        if (allAnnotations == null)
            return null;
        final PsiConstantEvaluationHelper evaluationHelper = JavaPsiFacade.getInstance(project).getConstantEvaluationHelper();
        return allAnnotations.filter(x -> PAGE_ENTRY_ANNOTATION_QUALIFIED_NAME.equals(x.getQualifiedName()))
                .findFirst()
                .map(CucumberJavaUtil::getAnnotationTitle)
                .filter(Objects::nonNull)
                .map(x -> evaluationHelper.computeConstantExpression(x, false))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .filter(x -> x.length() > 1)
                .orElse(null);
    }

    /**
     * Поиск всех имеющих предка ru.sbtqa.tag.pagefactory.Page.
     *
     * @param project
     * @return
     */
    public static Stream<PsiClass> pages(Project project) {
        final List<PsiClass> classes = new ArrayList<>();
        AllClassesGetter.processJavaClasses(
                new PlainPrefixMatcher(""),
                project,
                GlobalSearchScope.projectScope(project),
                classes::add
        );
        return classes.stream()
                .filter(TAGProject::isTAGPage)
                .unordered();
    }

    /**
     * Имеет ли класс в наследниках ru.sbtqa.tag.pagefactory.Page.
     *
     * @param psiClass
     * @return
     */
    private static boolean isTAGPage(PsiClass psiClass) {
        while (psiClass != null) {
            if (TAG_PAGE_QUALIFIED_NAME.equals(psiClass.getQualifiedName()))
                return true;
            psiClass = psiClass.getSuperClass();
        }
        return false;
    }

}
