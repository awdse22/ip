package mryapper.command;

import mryapper.parser.DateTimeParser;
import mryapper.task.Task;
import mryapper.task.Deadline;
import mryapper.task.Todo;
import mryapper.task.Event;

import java.util.ArrayList;

/**
 * Contains class methods to create all the commands of the Chatbot.
 */
public class CommandList {

    /**
     * Returns a command which creates a "to do" task.
     *
     * @param description The description of the task.
     * @return A command which creates the task to do when executed.
     */
    public static Command addTodoTask(String description) {
        return (tasks, ui, storage) -> {
            Task newTask = new Todo(description);
            tasks.add(newTask);
            ui.send(String.format(" Task added successfully!\n   %s\n Now you have %d tasks in the list",
                    newTask, tasks.count()));
            tasks.saveToStorage(storage);
            return false;
        };
    }

    /**
     * Returns a command which creates a task with deadline.
     *
     * @param description The description of the task.
     * @param deadline The deadline of the task.
     * @return A command which creates the deadline task when executed.
     */
    public static Command addDeadlineTask(String description, String deadline) {
        return (tasks, ui, storage) -> {
            Task newTask = new Deadline(description, DateTimeParser.parseDateTime(deadline));
            tasks.add(newTask);
            ui.send(String.format(" Task added successfully!\n   %s\n Now you have %d tasks in the list",
                    newTask, tasks.count()));
            tasks.saveToStorage(storage);
            return false;
        };
    }

    /**
     * Returns a command which creates an event task.
     *
     * @param description The description of the event.
     * @param start The starting time or/and date of the event.
     * @param end The ending time or/and date of the event.
     * @return A command which creates the event task when executed.
     */
    public static Command addEventTask(String description, String start, String end) {
        return (tasks, ui, storage) -> {
            Task newTask = new Event(description,
                    DateTimeParser.parseDateTime(start),
                    DateTimeParser.parseDateTime(end));
            tasks.add(newTask);
            ui.send(String.format(" Task added successfully!\n   %s\n Now you have %d tasks in the list",
                    newTask, tasks.count()));
            tasks.saveToStorage(storage);
            return false;
        };
    }

    /**
     * Returns a command which deletes the task from the task list.
     *
     * @param taskNumber The index of the task to remove, counting from 1.
     * @return A command which deletes a task from the given task list.
     */
    public static Command deleteTask(int taskNumber) {
        return (tasks, ui, storage) -> {
            try {
                Task deletedTask = tasks.remove(taskNumber);
                ui.send(String.format(" Task deleted successfully!\n   %s\n"
                        + "Now you have %d tasks in the list", deletedTask, tasks.count()));
            } catch (IndexOutOfBoundsException e) {
                String errorMessage = String.format(" There is no such task!\n "
                        + "You currently have %d tasks in your list", tasks.count());
                ui.send(errorMessage);
            }
            return false;
        };
    }

    /**
     * Returns the command which displays all the task.
     *
     * @return A command which displays the tasks in the given TaskList when executed.
     */
    public static Command listTasks() {
        return (tasks, ui, storage) -> {
            ui.send(tasks.toString());
            return false;
        };
    }

    /**
     * Returns a command which searches all tasks matching the keywords in the input.
     *
     * @param searchInput The search input from the user.
     * @return A command which displays all the task containing keywords in the search input.
     */
    public static Command findTask(String searchInput) {
        return (tasks, ui, storage) -> {
            ArrayList<Task> searchResult = tasks.searchTasks(searchInput);
            if (searchResult.isEmpty()) {
                ui.send(" None of the tasks match your search results!");
                return false;
            }

            ui.send(" Here are the matching tasks in your list:");
            for (int i = 0; i < searchResult.size(); i++ ) {
                int taskNumber = i + 1;
                ui.send(" " + taskNumber + "." + searchResult.get(i).toString());
            }
            return false;
        };
    }

    /**
     * Returns the command which marks the task as done.
     *
     * @param taskNumber The index of the task in the TaskList counting from 1.
     * @return A command which marks the task under the given index of the given TaskList.
     */
    public static Command markTask(int taskNumber) {
        return (tasks, ui, storage) -> {
            try {
                ui.send("Nice! I have marked this task as done:\n   "
                        + tasks.mark(taskNumber));
            } catch (IndexOutOfBoundsException e) {
                String errorMessage = String.format(" There is no such task!\n "
                        + "You currently have %d tasks in your list", tasks.count());
                ui.send(errorMessage);
            }
            return false;
        };
    }

    /**
     * Returns the command which marks the task as not done.
     *
     * @param taskNumber The index of the task in the TaskList counting from 1.
     * @return A command which unmarks the task under the given index of the given TaskList.
     */
    public static Command unmarkTask(int taskNumber) {
        return (tasks, ui, storage) -> {
            try {
                ui.send("OK, I've marked this task as not done yet:\n   "
                        + tasks.unmark(taskNumber));
            } catch (IndexOutOfBoundsException e) {
                String errorMessage = String.format(" There is no such task!\n "
                        + "You currently have %d tasks in your list", tasks.count());
                ui.send(errorMessage);
            }
            return false;
        };
    }

    /**
     * Returns a command which makes the Chatbot say the goodbye message/
     * @return A command which displays the goodbye message and tells the Chatbot to exit.
     */
    public static Command bye() {
        return (tasks, ui, storage) -> {
            ui.showGoodbye();
            return true;
        };
    }
}
