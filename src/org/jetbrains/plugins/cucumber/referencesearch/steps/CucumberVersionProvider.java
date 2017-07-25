package org.jetbrains.plugins.cucumber.referencesearch.steps;

import org.jetbrains.plugins.cucumber.psi.GherkinStep;
import org.jetbrains.plugins.cucumber.referencesearch.config.CucumberConfigUtil;

public class CucumberVersionProvider {
  public String getVersion(GherkinStep step) {
    return CucumberConfigUtil.getCucumberCoreVersion(step);
  }
}