package be.jeroendruwe;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Created by jdruwe on 28/03/2017.
 */
public class AutoExcludeRegistration implements ApplicationComponent {

    @NotNull
    @Override
    public String getComponentName() {
        return "AutoExcludePlugin";
    }

    @Override
    public void initComponent() {
        ActionManager actionManager = ActionManager.getInstance();
        ExcludeAction excludeAction = new ExcludeAction();

        actionManager.registerAction("AutoExcludePluginExclude", excludeAction);
        DefaultActionGroup windowMenu = (DefaultActionGroup) actionManager.getAction("ToolsMenu");

        windowMenu.addSeparator();
        windowMenu.add(excludeAction);
    }

    @Override
    public void disposeComponent() {

    }

}
