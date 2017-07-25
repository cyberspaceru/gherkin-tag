package ru.sbtqa.plugins.cucumber.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.cucumber.completion.CucumberCompletionContributor;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinFileImpl;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinStepImpl;
import ru.sbtqa.plugins.cucumber.util.TAGProject;

/**
 * Created by SBT-Tatciy-IO on 19.07.2017.
 */
public class PageEntryCompletionProvider extends CompletionProvider<CompletionParameters> {

    private static final String ENGLISH_PATTERN = "^(?:user |he |)(?:is on the page|page is being opened|master tab is being opened) \"";
    private static final String RUSSIAN_PATTERN = "^(?:пользователь |он |)(?:находится на странице|открывается страница|открывается вкладка мастера) \"";

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {
        PsiElement element = parameters.getPosition().getContext();
        PsiFile file = parameters.getPosition().getContainingFile();
        String stepName = element instanceof GherkinStepImpl ? ((GherkinStepImpl) element).getStepName() : null;
        String localLanguage = file instanceof GherkinFileImpl ? ((GherkinFileImpl) file).getLocaleLanguage() : null;

        String pattern = null;
        if (localLanguage != null && ("en".equals(localLanguage) || "ru".equals(localLanguage)))
            pattern = "en".equals(localLanguage) ? ENGLISH_PATTERN : RUSSIAN_PATTERN;

        final String startWith = stepName != null && stepName.contains("\"") ? stepName.substring(0, stepName.indexOf("\"") + 1) : null;

        if (startWith != null && pattern != null && startWith.matches(pattern)) {
            final Project project = element.getProject();
            TAGProject.pages(project)
                    .forEach(x -> resultSet.addElement(LookupElementBuilder.create(startWith + TAGProject.findPageName(x, project) + "\"")));
            resultSet.stopHere();
        }
    }

}
