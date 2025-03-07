package com.commands;

import com.TaskList;
import com.exceptions.DukeException;
import com.util.Storage;
import com.tasks.*;
import com.util.ui.*;
import gui.GUIUi;
import com.util.stats.DukeStatistics;

import java.util.ArrayList;

public class ToDoCommand extends AddCommand {

    public ToDoCommand(String taskDescription) {
        super("todo", taskDescription, false);
    }

    /**
     * Adds ToDo task to list, saves new list to text file.
     * @param taskList
     * @param storage
     * @throws DukeException
     */
    public void execute(TaskList taskList, Storage storage, Ui ui) throws DukeException {
        Task newTask = new ToDo(description);
        taskList.addTask(newTask);
        ArrayList<Task> taskArr = taskList.getTaskArr();
        storage.save(taskArr);
        new DukeStatistics().addLog(this, newTask);
        ui.showAddTaskResponse(newTask, taskArr);
    }

}
