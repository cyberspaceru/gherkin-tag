package ru.sbtqa.plugins.cucumber.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PlatformPatterns;

/**
 * Created by cyberspace on 7/19/2017.
 */
public class ActionTitleCompletionContributor  extends CompletionContributor {

    public ActionTitleCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), new ActionTitleCompletionProvider());
    }

}
