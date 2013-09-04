package com.vexus2.intellij.marked;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Action extends AnAction {
  private static final String MARKED_APP_PATH = "/Applications/Marked.app";

  public void update(AnActionEvent e) {
    e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
  }

  public void actionPerformed(AnActionEvent e) {
    VirtualFile current_file = DataKeys.VIRTUAL_FILE.getData(e.getDataContext());

    Path path = Paths.get(MARKED_APP_PATH);
    if (!Files.exists(path)) {
      Notifications.Bus.notify(new Notification("Marked", "Information", "'Marked' is not installed.", NotificationType.INFORMATION));
      return;
    }
    if (current_file == null || !current_file.getPath().toString().matches(".*?\\.md$")) {
      Notifications.Bus.notify(new Notification("Marked", "Information", "This is not Markdown file.", NotificationType.INFORMATION));
      return;
    }

    Runtime rt = Runtime.getRuntime();

    try {
      rt.exec("open -a " + MARKED_APP_PATH + " " + current_file.getPath());
    } catch (IOException e1) {
      Notifications.Bus.notify(new Notification("Marked", "Error", e1.getMessage(), NotificationType.ERROR));
    }

  }


}
