package mryapper;

import mryapper.command.Command;
import mryapper.exception.InvalidFileDataException;
import mryapper.exception.InvalidSyntaxException;
import mryapper.parser.Parser;
import mryapper.storagemanager.StorageManager;
import mryapper.task.TaskList;

import java.io.IOException;

/**
 * Contains the main program of the ChatBot. Running the main program initializes
 * the ChatBot and runs the ChatBot.
 */
public class MrYapper {

    private static final String GREETING_MESSAGE = " Hello! I'm MrYapper\n"
            + " What can I do for you?";
    private static final String TASK_DATA_PATH = "src/data/tasks.txt";
    private final StorageManager storageManager;
    private TaskList tasks;

    /**
     * Initializes the ChatBot with the given data file path.
     * The file path is relative and if no data file exists, a new
     * data .txt file will be created automatically.
     *
     * @param filePath The relative file path of data file.
     */
    public MrYapper(String filePath) {
        this.storageManager = new StorageManager(filePath);
    }

    /**
     * Loads the data from the storage manager into the task list.
     *
     * @throws IOException Thrown if an error occurred while creating a new data file.
     * @throws InvalidFileDataException Thrown if the data file has an invalid format or is corrupted.
     */
    public void loadData() throws IOException, InvalidFileDataException {
        this.tasks = storageManager.loadData();
    }

    /**
     * Returns the greeating message.
     *
     * @return The greeting message.
     */
    public String greet() {
        return GREETING_MESSAGE;
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @return The response from the chatbot.
     */
    public String getResponse(String input) {
        try {
            assert input != null: "User input should not be null";

            Command c = Parser.parse(input);
            assert c != null: "Command should not be null";
            return c.execute(tasks, storageManager);
        } catch (InvalidSyntaxException | IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
