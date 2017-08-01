package ru.sbtqa.plugins.cucumber.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiConstantEvaluationHelper;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.cucumber.completion.CucumberCompletionContributor;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinFileImpl;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinStepImpl;
import org.jetbrains.plugins.cucumber.referencesearch.CucumberJavaUtil;
import ru.sbtqa.plugins.cucumber.util.TagContext;
import ru.sbtqa.plugins.cucumber.util.TagDefaultActions;
import ru.sbtqa.plugins.cucumber.util.TagProject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by cyberspace on 7/19/2017.
 */
public class ActionTitleCompletionProvider extends CompletionProvider<CompletionParameters> {


    private static final Pattern PARAMETERS_PATTERN = Pattern.compile("<string>|<number>|<param>");
    private static final String[] ENGLISH_KEYWORDS = new String[]{"user \\(", "he \\(", "\\("};
    private static final String[] RUSSIAN_KEYWORDS = new String[]{"пользователь \\(", "он \\(", "\\("};
    private static final String PATTERN_FOR_INSERTION = "\"<string>\"";

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {
        if (!(parameters.getPosition().getContext() instanceof GherkinStepImpl))
            return;
        GherkinStepImpl element = (GherkinStepImpl) parameters.getPosition().getContext();
        GherkinFileImpl file = (GherkinFileImpl) parameters.getPosition().getContainingFile();
        String stepName = element.getStepName();
        String localLanguage = file.getLocaleLanguage();

        TagContext tagContext = new TagContext(element);
        String[] starts = null;
        if (localLanguage != null && (TagProject.EN_LANGUAGE.equals(localLanguage) || TagProject.RU_LANGUAGE.equals(localLanguage)))
            starts = "en".equals(localLanguage) ? ENGLISH_KEYWORDS : RUSSIAN_KEYWORDS;

        if (stepName != null && starts != null && stepName.matches("(" + StringUtils.join(starts, "|") + ").*")) {
            getVariations(element.getProject(), localLanguage, starts, tagContext.getCurrentPageName())
                    .filter(Objects::nonNull)
                    .forEach(x -> addActionTitle(x.replace("\\(", "("), resultSet));
            resultSet.stopHere();
        }
    }

    private void addActionTitle(String step, CompletionResultSet resultSet) {
        final List<TextRange> ranges = new ArrayList<>();
        Matcher m = PARAMETERS_PATTERN.matcher(step);
        while (m.find())
            ranges.add(new TextRange(m.start(), m.end()));
        resultSet.addElement(LookupElementBuilder.create(step).withInsertHandler(new CucumberCompletionContributor.StepInsertHandler(ranges)));
    }

    private Stream<String> getVariations(Project project, String language, String[] starts, String pageName) {
        List<String> result = new ArrayList<>();
        final PsiConstantEvaluationHelper evaluationHelper = JavaPsiFacade.getInstance(project).getConstantEvaluationHelper();
        TagProject.pages(project)
                .filter(x -> pageName.equals(TagProject.findPageName(x, project)))
                .map(TagProject::actionTitle)
                .forEach(x -> x.forEach(pair -> // Каждая аннотация ActionTitle
                                Optional.ofNullable(CucumberJavaUtil.getAnnotationValue(pair.getFirst()))
                                        .map(y -> evaluationHelper.computeConstantExpression(y, false))
                                        .map(Object::toString)
                                        .filter(y -> y.length() > 1)
                                        .map(y -> TagDefaultActions.isDefaultAction(y, language) ? TagDefaultActions.find(language, y) : y)
                                        .ifPresent(y -> Arrays.stream(starts).forEach(z -> result.add(z + y + ")" + StringUtils.repeat(" " + PATTERN_FOR_INSERTION, pair.getSecond()))))
                        )
                );
        return result.stream();
    }
}
