package ru.sbtqa.plugins.cucumber.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinFileImpl;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinStepImpl;
import ru.sbtqa.plugins.cucumber.util.TagProject;
import ru.sbtqa.plugins.cucumber.util.TagSteps;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by SBT-Tatciy-IO on 19.07.2017.
 */
public class PageEntryCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {
        PsiElement element = parameters.getPosition().getContext();
        PsiFile file = parameters.getPosition().getContainingFile();
        String stepName = element instanceof GherkinStepImpl ? ((GherkinStepImpl) element).getStepName() : null;
        String localLanguage = file instanceof GherkinFileImpl ? ((GherkinFileImpl) file).getLocaleLanguage() : null;

        String pattern = null;
        if (localLanguage != null && (TagProject.EN_LANGUAGE.equals(localLanguage) || TagProject.RU_LANGUAGE.equals(localLanguage))) {
            String openPageRegex = TagSteps.find(localLanguage, TagSteps.OPEN_PAGE);
            pattern = Optional.ofNullable(openPageRegex).map(x -> x.substring(0, x.indexOf('"') + 1)).orElse(null);
        }

        final String startWith = stepName != null && stepName.contains("\"") ? stepName.substring(0, stepName.indexOf('"') + 1) : null;

        if (startWith != null && pattern != null && startWith.matches(pattern)) {
            final Project project = element.getProject();
            TagProject.pages(project)
                    .filter(Objects::nonNull)
                    .map(x -> TagProject.findPageName(x, project))
                    .filter(Objects::nonNull)
                    .forEach(x -> resultSet.addElement(LookupElementBuilder.create(startWith + x + "\"")));
            resultSet.stopHere();
        }
    }

}
