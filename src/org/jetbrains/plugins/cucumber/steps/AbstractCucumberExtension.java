package org.jetbrains.plugins.cucumber.steps;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.cucumber.CucumberJvmExtensionPoint;
import org.jetbrains.plugins.cucumber.psi.GherkinStep;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinFileImpl;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinStepImpl;
import org.jetbrains.plugins.cucumber.referencesearch.CucumberJavaUtil;
import ru.sbtqa.plugins.cucumber.util.TagContext;
import ru.sbtqa.plugins.cucumber.util.TagProject;

import java.util.*;

public abstract class AbstractCucumberExtension implements CucumberJvmExtensionPoint {

    // При референсах со степа фичи к реализации в .java
    @Override
    public List<PsiElement> resolveStep(@NotNull final PsiElement element) {
        final Module module = ModuleUtilCore.findModuleForPsiElement(element);
        if (module == null)
            return Collections.emptyList();
        final List<AbstractStepDefinition> stepDefinitions = loadStepsFor(element.getContainingFile(), module);
        final List<PsiElement> result = new ArrayList<>();
        GherkinStepImpl gherkinStep = (GherkinStepImpl) element;
        if (gherkinStep.isAction())
            result.addAll(resolveAction(gherkinStep, stepDefinitions, module));
        else if (gherkinStep.isPage())
            result.addAll(resolvePage(gherkinStep, stepDefinitions, module));
        else result.addAll(resolveStepDef(gherkinStep, stepDefinitions, module));
        return result;
    }

    private List<PsiElement> resolveAction(GherkinStepImpl gherkinStep, List<AbstractStepDefinition> stepDefinitions, Module module) {
        final List<PsiElement> result = new ArrayList<>();
        TagContext tagContext = new TagContext(gherkinStep);
        String pageName = tagContext.getCurrentPageName();
        String actionTitle = tagContext.getCurrentActionTitle();
        if (pageName != null && actionTitle != null) {
            for (final AbstractStepDefinition definition : stepDefinitions) {
                PsiClass clazz = definition.getElement() instanceof PsiClass ? (PsiClass) definition.getElement() : null;
                if (clazz != null && pageName.equals(TagProject.findPageName(clazz, module.getProject()))) {
                    PsiMethod psiMethod = TagProject.containsActionTitle(clazz, actionTitle, module.getProject());
                    if (psiMethod != null) {
                        result.add(psiMethod);
                        break;
                    }
                }
            }
        }
        return result;
    }

    private List<PsiElement> resolvePage(GherkinStepImpl gherkinStep, List<AbstractStepDefinition> stepDefinitions, Module module) {
        final List<PsiElement> result = new ArrayList<>();
        String pageName = new TagContext(gherkinStep).getCurrentPageName();
        if (pageName != null) {
            for (final AbstractStepDefinition definition : stepDefinitions) {
                if (definition.getElement() instanceof PsiClass && pageName.equals(TagProject.findPageName((PsiClass) definition.getElement(), module.getProject()))) {
                    result.add(definition.getElement());
                    break;
                }
            }
        }
        return result;
    }
    private List<PsiElement> resolveStepDef(GherkinStepImpl gherkinStep, List<AbstractStepDefinition> stepDefinitions, Module module) {
        final List<PsiElement> result = new ArrayList<>();
        final Set<String> stepVariants = getAllPossibleStepVariants(gherkinStep);
        String language = gherkinStep.getContainingFile() instanceof GherkinFileImpl ? ((GherkinFileImpl) gherkinStep.getContainingFile()).getLocaleLanguage() : null;
        for (final AbstractStepDefinition stepDefinition : stepDefinitions) {
            for (final String s : stepVariants) {
                if (stepDefinition.matches(s, language) && stepDefinition.supportsStep(gherkinStep)) {
                    result.add(stepDefinition.getElement());
                    break;
                }
            }
        }

        return result;
    }

    protected Set<String> getAllPossibleStepVariants(@NotNull final PsiElement element) {
        if (element instanceof GherkinStep) {
            return ((GherkinStep) element).getSubstitutedNameList();
        }
        return Collections.emptySet();
    }

    @Override
    public void flush(@NotNull final Project project) {
    }

    @Override
    public void reset(@NotNull final Project project) {
    }

    @Override
    public Object getDataObject(@NotNull Project project) {
        return null;
    }
}
