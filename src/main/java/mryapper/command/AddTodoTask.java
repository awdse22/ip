package mryapper.command;

import mryapper.storagemanager.StorageManager;
import mryapper.task.Task;
import mryapper.task.TaskList;
import mryapper.task.Todo;

/**
 * A command which creates a "to do" task.
 */
public class AddTodoTask extends Command {

    private final String description;

    public AddTodoTask(String description) {
        this.description = description;
    }

    @Override
    public String execute(TaskList tasks, StorageManager storage) {
        assert !description.isEmpty() : "description should not be empty";

        Task newTask = new Todo(description);
        tasks.add(newTask);
        tasks.saveToStorage(storage);
        String response = String.format("Task added successfully!\n  %s\n"
                + "Now you have %d tasks in the list",
                newTask, tasks.count());
        return response;
    }

    /**
     * Gets the syntax of the command.
     *
     * @return The syntax of the to do command.
     */
    public static String getSyntax() {
        return "e.g. todo CS2103T weekly quiz";
    }
}
