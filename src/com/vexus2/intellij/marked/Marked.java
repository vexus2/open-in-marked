package com.vexus2.intellij.marked;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;

import java.io.IOException;

public class Marked extends AnAction {
  private static final String MARKED_APP_PATH = "/Applications/Marked.app";

  public void update(AnActionEvent e) {
    e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
  }

  public void actionPerformed(AnActionEvent e) {
    VirtualFile current_file = PlatformDataKeys.VIRTUAL_FILE.getData(e.getDataContext());

    VirtualFile app = VirtualFileManager.getInstance().findFileByUrl("file://" + MARKED_APP_PATH);

    if (app == null) {
      Notifications.Bus.notify(new Notification("Marked", "Information", "'Marked' is not installed.", NotificationType.INFORMATION));
      return;
    }
    if (current_file == null || !current_file.getPath().toString().matches(".*?\\.md$")) {
      Notifications.Bus.notify(new Notification("Marked", "Information", "This is not Markdown file.", NotificationType.INFORMATION));
      return;
    }

    Runtime rt = Runtime.getRuntime();

    try {
      String file_path = current_file.getPath().replace(" ", "\\ ");
      rt.exec("open -a " + MARKED_APP_PATH + " " + file_path);
    } catch (IOException e1) {
      Notifications.Bus.notify(new Notification("Marked", "Error", e1.getMessage(), NotificationType.ERROR));
    }

  }


}
