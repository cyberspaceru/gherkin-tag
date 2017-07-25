package org.jetbrains.plugins.cucumber.steps;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.cucumber.CucumberJvmExtensionPoint;
import org.jetbrains.plugins.cucumber.psi.GherkinFile;
import org.jetbrains.plugins.cucumber.psi.GherkinStep;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinFileImpl;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinStepImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class AbstractCucumberExtension implements CucumberJvmExtensionPoint {

  // При референсах со степа фичи к реализации в .java
  @Override
  public List<PsiElement> resolveStep(@NotNull final PsiElement element) {
    final Module module = ModuleUtilCore.findModuleForPsiElement(element);
    if (module == null) {
      return Collections.emptyList();
    }

    final Set<String> stepVariants = getAllPossibleStepVariants(element);

    final List<AbstractStepDefinition> stepDefinitions = loadStepsFor(element.getContainingFile(), module);
    final List<PsiElement> result = new ArrayList<>();
    GherkinStepImpl gherkinStep = (GherkinStepImpl) element;
    String language = gherkinStep.getContainingFile() instanceof GherkinFileImpl ? ((GherkinFileImpl) gherkinStep.getContainingFile()).getLocaleLanguage() : null;
    for (final AbstractStepDefinition stepDefinition : stepDefinitions) {
      for (final String s : stepVariants) {
        if (stepDefinition.matches(s, language) && stepDefinition.supportsStep(element)) {
          result.add(stepDefinition.getElement());
          break;
        }
      }
    }

    return result;
  }

  protected Set<String> getAllPossibleStepVariants(@NotNull final PsiElement element) {
    if (element instanceof GherkinStep) {
      return ((GherkinStep)element).getSubstitutedNameList();
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
