package ru.sbtqa.plugins.cucumber.util;

import com.intellij.psi.PsiElement;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinFileImpl;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinStepImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SBT-Tatciy-IO on 26.07.2017.
 */
public class TAGContext {

    private final GherkinFileImpl gherkinFile;
    private final GherkinStepImpl gherkinStep;
    private final static Pattern QUOTES_VALUE_EXTRACTOR_PATTERN = Pattern.compile("\"([^\"]*)\"");

    public TAGContext(GherkinStepImpl step) {
        this.gherkinStep = step;
        this.gherkinFile = (GherkinFileImpl) step.getContainingFile();
    }

    public String getCurrentPageName() {
        String result = "";
        PsiElement prevElement = gherkinStep;
        String openPageRegex = Features.find(getGherkinFile().getLocaleLanguage(), Features.OPEN_PAGE);
        assert openPageRegex != null;
        while ((prevElement = prevElement.getPrevSibling()) != null) {
            String prevStepName = (prevElement instanceof GherkinStepImpl) ? ((GherkinStepImpl) prevElement).getStepName() : null;
            if (prevStepName != null && prevStepName.matches(openPageRegex)) {
                Matcher m = QUOTES_VALUE_EXTRACTOR_PATTERN.matcher(prevStepName);
                if (m.find()) {
                    result = m.group().replaceAll("\"", "");
                    break;
                }
            }
        }
        return result;
    }

    public GherkinFileImpl getGherkinFile() {
        return gherkinFile;
    }

    public GherkinStepImpl getGherkinStep() {
        return gherkinStep;
    }
}
