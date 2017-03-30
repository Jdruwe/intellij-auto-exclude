package be.jeroendruwe;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.impl.ModifiableModelCommitter;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileFilter;
import org.jetbrains.annotations.NotNull;

/**
 * Created by jdruwe on 28/03/2017.
 */
public class ExcludeAction extends AnAction {

    public ExcludeAction() {
        super("_Auto Exclude");
    }

    @Override
    public void actionPerformed(AnActionEvent event) {

        if (event.getProject() != null) {

            final ModifiableModuleModel modulesModel = ModuleManager.getInstance(event.getProject()).getModifiableModel();
            final Module[] modules = modulesModel.getModules();
            final ModifiableRootModel[] models = new ModifiableRootModel[modules.length];

            for (int i = 0; i < modules.length; i++) {
                models[i] = ModuleRootManager.getInstance(modules[i]).getModifiableModel();
                final ContentEntry[] contentEntries = models[i].getContentEntries();

                for (final ContentEntry contentEntry : contentEntries) {
                    final VirtualFile contentRoot = contentEntry.getFile();
                    if (contentRoot == null) continue;
                    VfsUtilCore.iterateChildrenRecursively(contentRoot, VirtualFileFilter.ALL, getIterator(contentEntry));
                }
            }

            ApplicationManager.getApplication().runWriteAction(() -> ModifiableModelCommitter.multiCommit(models, modulesModel));
        }
    }

    @NotNull
    private ContentIterator getIterator(ContentEntry contentEntry) {
        return virtualFile -> {

            if (isValidVirtualFile(virtualFile)) {
                contentEntry.addExcludeFolder(virtualFile);
            }
            return true;
        };
    }

    private boolean isValidVirtualFile(VirtualFile virtualFile) {
        return virtualFile != null && virtualFile.isDirectory() && virtualFile.getName().equals("dist");
    }
}